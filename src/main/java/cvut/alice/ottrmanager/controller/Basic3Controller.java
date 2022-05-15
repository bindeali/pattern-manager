package cvut.alice.ottrmanager.controller;

import cvut.alice.ottrmanager.tool.common.Manager;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
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
        Manager.getManager().getDataset().getDefaultModel().add(model);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}
