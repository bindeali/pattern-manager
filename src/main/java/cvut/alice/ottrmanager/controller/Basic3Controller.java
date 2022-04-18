package cvut.alice.ottrmanager.controller;

import cvut.alice.ottrmanager.tool.common.Manager;
import org.apache.jena.rdf.model.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.net.http.HttpHeaders;

@RestController
public class Basic3Controller {
    @PostMapping("/template")
    public void postODP(@RequestBody String body, @RequestHeader HttpHeaders headers, HttpServletResponse response) {
        Model model = ModelFactory.createDefaultModel();
        model.read(new ByteArrayInputStream(body.getBytes()), null);
        if (model.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        Manager.getManager().getModel().add(model);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}
