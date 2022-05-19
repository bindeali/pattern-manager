package cvut.alice.ottrmanager.controller;

import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
public class Basic3Controller {

    @PostMapping("/update")
    public void postODP(@RequestBody String body, HttpServletResponse response) {
        RDFConnectionRemoteBuilder builder = RDFConnectionFuseki.create().destination("http://localhost:3030/default");
        String decodedQuery = URLDecoder.decode(body, StandardCharsets.UTF_8);
        System.out.println(decodedQuery);
        try (RDFConnectionFuseki connection = (RDFConnectionFuseki) builder.build()) {
            connection.update(decodedQuery);
        }
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}
