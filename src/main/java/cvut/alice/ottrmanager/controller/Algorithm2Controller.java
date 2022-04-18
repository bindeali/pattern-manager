package cvut.alice.ottrmanager.controller;

import cvut.alice.ottrmanager.model.Argument;
import cvut.alice.ottrmanager.model.Pair;
import cvut.alice.ottrmanager.model.Template;
import cvut.alice.ottrmanager.tool.a12.Interpreter;
import cvut.alice.ottrmanager.tool.a12.PairFinder;
import cvut.alice.ottrmanager.tool.common.Constructor;
import cvut.alice.ottrmanager.tool.common.Manager;
import org.apache.jena.rdf.model.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;

@RestController
public class Algorithm2Controller {
    @PostMapping(path = "/create")
    public @ResponseBody String createNewODPsFromModel(@RequestBody String body, HttpServletResponse httpServletResponse){
        Model model = ModelFactory.createDefaultModel();
        model.read(new ByteArrayInputStream(body.getBytes()), null);
        if (model.isEmpty()) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        PairFinder pairFinder = new PairFinder();
        Interpreter interpreter = new Interpreter();
        Constructor constructor = new Constructor();
        Manager.getManager().clear();
        Property parameters = Manager.getManager().getModel().createProperty("http://ns.ottr.xyz/0.4/parameters");
        Selector selector = new SimpleSelector(null, parameters, (RDFNode) null);
        StmtIterator stmtIterator = Manager.getManager().getModel().listStatements(selector);
        while (stmtIterator.hasNext()){
            Statement statement = stmtIterator.nextStatement();
            Template template = new Template(statement.getSubject().getURI(), new ArrayList<>(), new ArrayList<>());
            RDFNode object = statement.getObject();
            if (object.isAnon()) {
                Selector blankSelector = new SimpleSelector(object.asResource(), null, (RDFNode) null);
                StmtIterator blankStmtIterator = Manager.getManager().getModel().listStatements(blankSelector);
                Argument argument = new Argument(null,null);
                while (blankStmtIterator.hasNext()) {
                    Statement blankStmt = blankStmtIterator.nextStatement();
                    if (blankStmt.getPredicate().hasURI("http://ns.ottr.xyz/0.4/variable")){
                        argument.setObject(blankStmt.getObject());
                    } else if (blankStmt.getPredicate().hasURI("http://ns.ottr.xyz/0.4/type")){
                        argument.setType(blankSelector.getObject());
                    }
                }
                if (argument.getType() != null && argument.getObject() != null)
                    template.getArguments().add(argument);
            }
            Manager.getManager().getTemplates().add(template);
        }
        pairFinder.findPairs();
        interpreter.interpretPairs();
        for (Pair pair : Manager.getManager().getPairs()){

        }
        StringWriter stringWriter = new StringWriter();
        model.write(stringWriter, "TURTLE");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        return stringWriter.toString();
    }
}
