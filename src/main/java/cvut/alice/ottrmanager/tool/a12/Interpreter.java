package cvut.alice.ottrmanager.tool.a12;



import cvut.alice.ottrmanager.tool.common.Manager;
import cvut.alice.ottrmanager.model.Argument;
import cvut.alice.ottrmanager.model.Pair;
import cvut.alice.ottrmanager.model.Template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public class Interpreter {

    private List<Pair> filterPairs() {
        return Manager.getManager().getPairs().stream().filter(pair -> (pair.getI().size() > 1 && pair.getT().size() > 1)).collect(toList());
    }

    private Template constructTemplate(Collection<Template> I) {
        ArrayList<Argument> arguments = new ArrayList<>();
        ArrayList<Template> templates = new ArrayList<>();
        for (Template t : I) {
            arguments.addAll(t.getArguments());
            templates.add(t);
        }
        return new Template(UUID.randomUUID().toString(), arguments, templates);
    }

    private boolean compareTemplate(Template t1, Template t2) {
        return t2.getTemplates().containsAll(t1.getTemplates()) && t1.getArguments().size() == t2.getArguments().size();
    }

    private void alterTemplate(Template template, Template newTemplate, Collection<Template> oldTemplates) {
        template.getTemplates().removeIf(oldTemplates::contains);
        template.getTemplates().add(newTemplate);
    }

    public void interpretPairs() {
        List<Pair> pairs = filterPairs();
        Manager.getManager().setPairs(pairs);
        for (Pair pair : pairs) {
            Template template = constructTemplate(pair.getI());
            for (Template instance : pair.getT()) alterTemplate(instance, template, pair.getI());
        }
    }
}
