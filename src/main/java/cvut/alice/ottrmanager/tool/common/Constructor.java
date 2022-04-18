package cvut.alice.ottrmanager.tool.common;

import cvut.alice.ottrmanager.model.Template;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.StringWriter;
import java.util.ArrayList;

public class Constructor {

    public String constructTemplate(Template template) {
        Model model = ModelFactory.createDefaultModel();

        StringWriter stringWriter = new StringWriter();
        model.write(stringWriter, "TURTLE");
        return stringWriter.toString();
    }
}
