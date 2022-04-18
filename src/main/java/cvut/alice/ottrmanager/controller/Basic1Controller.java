package cvut.alice.ottrmanager.controller;

import cvut.alice.ottrmanager.tool.common.Manager;
import jdk.dynalink.linker.support.SimpleLinkRequest;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
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
        Model outputModel = ModelFactory.createDefaultModel();
        Resource template = Manager.getManager().getModel().getResource(IRI);
        Selector selector = new SimpleSelector(template, null, (RDFNode) null);
        StmtIterator stmtIterator = Manager.getManager().getModel().listStatements(selector);
        while (stmtIterator.hasNext()) {
            Statement statement = stmtIterator.nextStatement();
            RDFNode object = statement.getObject();
            if (object.isAnon()) {
                Selector blankSelector = new SimpleSelector(object.asResource(), null, (RDFNode) null);
                StmtIterator blankStmtIterator = Manager.getManager().getModel().listStatements(blankSelector);
                while (blankStmtIterator.hasNext()) {
                    Statement blankStmt = blankStmtIterator.nextStatement();
                    outputModel.add(blankStmt);
                }
            }
            outputModel.add(statement);
        }
        StringWriter stringWriter = new StringWriter();
        outputModel.write(stringWriter, "TURTLE");
        return stringWriter.toString();
    }
}