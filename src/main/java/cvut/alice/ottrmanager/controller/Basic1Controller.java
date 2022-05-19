package cvut.alice.ottrmanager.controller;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.GraphExtract;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.TripleBoundary;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringWriter;

@RestController
public class Basic1Controller {

    @GetMapping("/template")
    public @ResponseBody
    String retrieveODP(@RequestParam(value = "iri") String IRI) {
        RDFConnectionRemoteBuilder builder = RDFConnectionFuseki.create().destination("http://localhost:3030/default");
        try (RDFConnectionFuseki connection = (RDFConnectionFuseki) builder.build()) {
            Model inputModel = connection.fetchDataset().getDefaultModel();
            Node template = inputModel.getResource(IRI).asNode();
            GraphExtract graphExtract = new GraphExtract(TripleBoundary.stopNowhere);
            Graph result = graphExtract.extract(template, inputModel.getGraph());
            StringWriter stringWriter = new StringWriter();
            RDFDataMgr.write(stringWriter, result, RDFFormat.TURTLE);
            return stringWriter.toString();
        }
    }
}