package cvut.alice.ottrmanager.tool.common;

import cvut.alice.ottrmanager.model.Argument;
import cvut.alice.ottrmanager.model.Instance;
import cvut.alice.ottrmanager.model.Template;
import org.apache.commons.io.FileUtils;
import org.apache.jena.rdf.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

    public String loadFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists() && file.canRead()) {
            return FileUtils.readFileToString(file);
        }
        return path;
    }

    public void loadSource(String path) {
        Manager.getManager().setModel(ModelFactory.createDefaultModel());
        Manager.getManager().getModel().read(path);
        StmtIterator iter = Manager.getManager().getModel().listStatements();
        // Process predicates as templates, with subject and object as arguments
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            ArrayList<Argument> arguments = new ArrayList<>();
            arguments.add(new Argument("subject"));
            arguments.add(new Argument("object"));
            Template template = new Template(stmt.getPredicate().toString(), arguments, new ArrayList<>());
            if (!Manager.getManager().getTemplates().contains(template))
                Manager.getManager().getTemplates().add(new Template(stmt.getPredicate().toString(), arguments, new ArrayList<>()));
        }
        ResIterator iter2 = Manager.getManager().getModel().listSubjects();
        while (iter2.hasNext()) {
            Resource resource = iter2.nextResource();
            if (resource.getURI() == null) continue;
            Selector selector = new SimpleSelector(resource, null, (Object) null);
            ArrayList<Argument> arguments = new ArrayList<>();
            ArrayList<Template> templates = new ArrayList<>();
            StmtIterator iter3 = Manager.getManager().getModel().listStatements(selector);
            while (iter3.hasNext()) {
                Statement statement = iter3.nextStatement();
                ArrayList<Argument> args = new ArrayList<>();
                args.add(new Argument("subject", null, statement.getSubject().getURI()));
                args.add(new Argument("object"));
                arguments.add(new Argument("object-" + statement.getSubject().getURI()));
                templates.add(new Template(statement.getPredicate().toString(), args, new ArrayList<>()));
            }
            Manager.getManager().getTemplates().add(new Template(resource.getURI(), arguments, templates));
        }
    }

    public void loadLibrary(String source) {
        String[] statements = source.split("\\.");
        for (String template : statements) {
            ArrayList<Argument> newArguments = new ArrayList<>();
            ArrayList<Template> newTemplates = new ArrayList<>();
            int argStart = template.indexOf("[");
            int argEnd = template.indexOf("]");
            int templateStart = template.indexOf("{");
            int templateEnd = template.indexOf("}");
            String name = template.substring(0, argStart);
            String[] arguments = template.substring(argStart, argEnd).split(",");
            String[] templates = template.substring(templateStart, templateEnd).split("\\),");
            for (String arg : arguments) {
                String[] parts = arg.split(" ");
                if (parts.length > 2) continue;
                newArguments.add(parts.length == 2 ? new Argument(parts[1], parts[0]) : new Argument(parts[0]));
            }
            for (String temp : templates) {
                int tempStart = temp.indexOf("(");
                int tempEnd = temp.indexOf(")");
                String[] args = temp.substring(tempStart, tempEnd).split(",");
                String nameTemp = temp.substring(0, tempStart);
                ArrayList<Argument> newArgs = new ArrayList<>();
                for (String arg : args) {
                    newArgs.add(new Argument(arg));
                }
                newTemplates.add(new Template(nameTemp, newArgs, new ArrayList<>()));
            }
            Manager.getManager().getTemplates().add(new Template(name, newArguments, newTemplates));
        }
    }

    public void loadInstances(String source) {
        String[] statements = source.split("\\.");
        for (String instance : statements) {
            int argStart = instance.indexOf("(");
            int argEnd = instance.indexOf(")");
            List<String> arguments = Arrays.asList(instance.substring(argStart, argEnd).split(","));
            String name = instance.substring(0, argStart);
            Instance i = new Instance(name, arguments);
            Manager.getManager().getInstances().add(i);
        }
    }
}
