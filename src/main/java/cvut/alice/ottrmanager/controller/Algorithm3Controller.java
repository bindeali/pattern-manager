package cvut.alice.ottrmanager.controller;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.apache.jena.vocabulary.RDF;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class Algorithm3Controller {
    @PostMapping(path = "/suggest")
    public @ResponseBody HashMap<String, HashMap<String, ArrayList<String>>> suggestODPsFromModel(@RequestBody String body, HttpServletResponse httpServletResponse) {

        RDFConnectionRemoteBuilder builder = RDFConnectionFuseki.create().destination("http://localhost:3030/default");
        try (RDFConnectionFuseki connection = (RDFConnectionFuseki) builder.build()) {
            Model patternModel = connection.fetchDataset().getDefaultModel();
            // < pattern , < parameter type , parameter candidates > >
            HashMap<String, HashMap<String, ArrayList<String>>> ret = new HashMap<>();
            HashMap<String, ArrayList<String>> inputModelSubjectsAndTypes = new HashMap<>();
            // < pattern , < parameter name , parameter type > >
            HashMap<String, HashMap<String, String>> patternParameterTypes = new HashMap<>();
            Model inputModel = ModelFactory.createDefaultModel();
            inputModel.read(new ByteArrayInputStream(body.getBytes()), null);
            if (inputModel.isEmpty()) {
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            //get types of subjects from input
            StmtIterator stmtIterator = inputModel.listStatements(null, RDF.type, (RDFNode) null);
            while (stmtIterator.hasNext()) {
                Statement statement = stmtIterator.nextStatement();
                Resource resource = statement.getResource();
                RDFNode rdfNode = statement.getObject();
                inputModelSubjectsAndTypes.computeIfAbsent(resource.getURI(), k -> new ArrayList<>());
                if (rdfNode.isURIResource()) {
                    inputModelSubjectsAndTypes.computeIfAbsent(resource.getURI(), k -> new ArrayList<>()).add(rdfNode.asResource().getURI());
                }
            }

            //get types of parameters from patterns
            String queryString = "PREFIX og: <http://onto.fel.cvut.cz/ontologies/application/ontoGrapher/>\n" +
                    "PREFIX ottr: <http://ns.ottr.xyz/0.4/>\n" +
                    "\n" +
                    "select ?template ?param ?paramTitle ?paramListTitle ?type ?listType where {\n" +
                    "    ?template a ottr:Template;\n" +
                    "              ottr:parameters (?param).\n" +
                    "    optional{\n" +
                    "        ?param ottr:type ?type.\n" +
                    "        ?param dc:title ?paramTitle.\n" +
                    "        filter(!isBlank(?type))\n" +
                    "    }\n" +
                    "    optional{\n" +
                    "        ?param ottr:type ?blank.\n" +
                    "        ?param dc:title ?paramListTitle.\n" +
                    "        ?blank ottr:NEList ?listType.\n" +
                    "        filter(isBlank(?blank))\n" +
                    "    }\n" +
                    "}";
            Query query = QueryFactory.create(queryString);
            try (QueryExecution queryExecution = QueryExecutionFactory.create(query, patternModel)) {
                ResultSet resultSet = queryExecution.execSelect();
                while (resultSet.hasNext()) {
                    QuerySolution querySolution = resultSet.nextSolution();
                    Resource template = querySolution.getResource("template");
                    Literal paramTitle = querySolution.getLiteral("paramTitle");
                    Literal paramListTitle = querySolution.getLiteral("paramListTitle");
                    Resource type = querySolution.getResource("type");
                    Resource listType = querySolution.getResource("listType");
                    Resource actualType = null;
                    if (type != null) actualType = type;
                    if (listType != null) actualType = listType;
                    if (actualType == null) {
                        httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        return ret;
                    }
                    Literal actualTitle = null;
                    if (type != null) actualTitle = paramTitle;
                    if (listType != null) actualTitle = paramListTitle;
                    if (actualTitle == null) {
                        httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        return ret;
                    }
                    patternParameterTypes.computeIfAbsent(template.getURI(), k -> new HashMap<>()).put(actualTitle.getLexicalForm(), actualType.getURI());
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            //compare!
            for (Map.Entry<String, HashMap<String, String>> pattern : patternParameterTypes.entrySet()) {
                // parameter - candidates
                HashMap<String, ArrayList<String>> value = new HashMap<>();
                for (Map.Entry<String, String> argument : pattern.getValue().entrySet()) {
                    ArrayList<String> resources = new ArrayList<>();
                    for (Map.Entry<String, ArrayList<String>> set : inputModelSubjectsAndTypes.entrySet()) {
                        if (set.getValue().contains(argument.getValue()) || Objects.equals(argument.getValue(), "http://ns.ottr.xyz/0.4/IRI")) {
                            resources.add(set.getKey());
                        }
                    }
                    value.put(argument.getKey(), resources);
                }
                if (!value.values().isEmpty())
                    ret.put(pattern.getKey(), value);
            }
            return ret;
        }
    }
}
