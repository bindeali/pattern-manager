package cvut.alice.ottrmanager.controller;

import cvut.alice.ottrmanager.tool.common.Manager;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.GraphExtract;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.TripleBoundary;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringWriter;

@RestController
public class Basic4Controller {
    @GetMapping("/statistics")
    public @ResponseBody
    String retrieveODPStatistics(@RequestParam(value = "iri") String IRI) {
        Model inputModel = Manager.getManager().getDataset().getDefaultModel();
        Model outputModel = ModelFactory.createDefaultModel();
        Resource template = inputModel.getResource(IRI);
        Property property = inputModel.createProperty("http://ottr-manager.xyz/isInstantiated");
        Selector selector = new SimpleSelector(template, property, (RDFNode) null);
        StmtIterator stmtIterator = inputModel.listStatements(selector);
        while (stmtIterator.hasNext()) {
            Statement statement = stmtIterator.nextStatement();
            RDFNode rdfNode = statement.getObject();
            if (rdfNode.isURIResource()) {
                Node node = rdfNode.asNode();
                GraphExtract graphExtract = new GraphExtract(TripleBoundary.stopNowhere);
                Graph result = graphExtract.extract(node, Manager.getManager().getDataset().getDefaultModel().getGraph());
                Model resultModel = ModelFactory.createModelForGraph(result);
                outputModel.add(resultModel);
            }
        }
        StringWriter stringWriter = new StringWriter();
        RDFDataMgr.write(stringWriter, outputModel, RDFFormat.TURTLE);
        return stringWriter.toString();
    }
}
