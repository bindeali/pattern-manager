package cvut.alice.ottrmanager.controller;

import cvut.alice.ottrmanager.tool.common.Manager;
import org.apache.jena.query.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@RestController
public class Basic2Controller {

    @PostMapping("/filter")
    public @ResponseBody String filterODPs(@RequestBody String queryString) {
        Query query = QueryFactory.create(queryString);
        try (QueryExecution queryExecution = QueryExecutionFactory.create(query, Manager.getManager().getModel())) {
            ResultSet resultSet = queryExecution.execSelect();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ResultSetFormatter.outputAsJSON(outputStream, resultSet);
            return outputStream.toString();
        }
    }
}
