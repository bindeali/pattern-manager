package cvut.alice.ottrmanager.controller;

import cvut.alice.ottrmanager.tool.common.Manager;
import org.apache.jena.rdf.model.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

@RestController
public class Algorithm2Controller {
    @PostMapping(path = "/create")
    public @ResponseBody String createNewODPsFromModel(@RequestBody String body, HttpServletResponse httpServletResponse) {
        Model inputModel = ModelFactory.createDefaultModel();
        Model templateModel = Manager.getManager().getDataset().getDefaultModel();
        inputModel.read(new ByteArrayInputStream(body.getBytes()), null);
        if (inputModel.isEmpty()) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        HashMap<ArrayList<Property>, ArrayList<Resource>> pairs = new HashMap<>();
        ResIterator resIterator = inputModel.listSubjects();
        while (resIterator.hasNext()) {
            Resource subject = resIterator.nextResource();
            StmtIterator stmtIterator = inputModel.listStatements(subject, null, (RDFNode) null);
            ArrayList<Property> predicates = new ArrayList<>();
            while (stmtIterator.hasNext()) {
                Statement statement = stmtIterator.nextStatement();
                predicates.add(statement.getPredicate());
            }
            predicates.sort(Comparator.comparing(Resource::getURI));
            pairs.computeIfAbsent(predicates, k -> new ArrayList<>()).add(subject);
        }
        
    }
}
