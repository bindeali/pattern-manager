package cvut.alice.ottrmanager.tool.a3;

import cvut.alice.ottrmanager.model.Argument;
import cvut.alice.ottrmanager.model.Template;
import cvut.alice.ottrmanager.tool.common.Manager;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Comparator {

    public HashMap<Template, HashMap<Argument, ArrayList<String>>> compareModelAndTemplates() {
        HashMap<Template, HashMap<Argument, ArrayList<String>>> ret = new HashMap<>();
        HashMap<String, ArrayList<String>> subjectsAndTypes = new HashMap<>();
        ResIterator resIterator = Manager.getManager().getModel().listSubjectsWithProperty(RDF.type);
        System.out.println();
        while (resIterator.hasNext()) {
            ArrayList<String> types = new ArrayList<>();
            Resource resource = resIterator.nextResource();
            if (resource.getURI() == null) continue;
            Selector selector = new SimpleSelector(resource, RDF.type, (Object) null);
            StmtIterator stmtIterator = Manager.getManager().getModel().listStatements(selector);
            while (stmtIterator.hasNext()) {
                Statement statement = stmtIterator.nextStatement();
                types.add(statement.getObject().asResource().getURI());
            }
            subjectsAndTypes.put(resource.getURI(), types);
        }
        for (Template template : Manager.getManager().getTemplates()) {
            HashMap<Argument, ArrayList<String>> value = new HashMap<>();
            for (Argument argument : template.getArguments()) {
                ArrayList<String> resources = new ArrayList<>();
                for (Map.Entry<String, ArrayList<String>> set : subjectsAndTypes.entrySet()) {
                    if (set.getValue().contains(argument.getType()) || argument.getType() == null){
                        resources.add(set.getKey());
                    }
                }
                value.put(argument, resources);
            }
            if (!value.values().isEmpty())
                ret.put(template, value);
        }
        return ret;
    }
}
