package cvut.alice.ottrmanager.controller;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
public class Basic2Controller {
    @Autowired
    Environment env;

    @PostMapping("/query")
    public @ResponseBody String filterODPs(@RequestParam(value = "query") String queryString) {
        RDFConnectionRemoteBuilder builder = RDFConnectionFuseki.create().destination(env.getProperty("FUSEKI"));
        try (RDFConnectionFuseki connection = (RDFConnectionFuseki) builder.build()) {
            Model inputModel = connection.fetchDataset().getDefaultModel();
            String decodedQuery = URLDecoder.decode(queryString, StandardCharsets.UTF_8);
            Query query = QueryFactory.create(decodedQuery);
            try (QueryExecution queryExecution = QueryExecutionFactory.create(query, inputModel)) {
                ResultSet resultSet = queryExecution.execSelect();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ResultSetFormatter.outputAsJSON(outputStream, resultSet);
                return outputStream.toString();
            }
        }
    }
}
