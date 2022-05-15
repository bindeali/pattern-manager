package cvut.alice.ottrmanager.tool.a12;

import cvut.alice.ottrmanager.model.Pair;
import cvut.alice.ottrmanager.model.Template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class PairFinder {

    public ArrayList<ArrayList<Template>> getCombinations(Template template, Collection<Template> templates) {
        Optional<Template> templateOptional = templates.stream().filter(temp -> temp.getIri().equals(template.getIri())).findFirst();
        if (templateOptional.isPresent()) {
            Template t = templateOptional.get();
            ArrayList<ArrayList<Template>> ret = new ArrayList<>();
            ret.add(new ArrayList<>(t.getTemplates()));
            return ret;
        }
        return new ArrayList<>();
    }

    public void findPairs(Collection<Template> templates, Collection<Pair> pairs) {
        for (Template template : templates) {
            for (ArrayList<Template> iT : getCombinations(template, templates)) {
                boolean found = false;
                for (Pair pair : pairs) {
                    if (pair.getI().containsAll(iT)) {
                        pair.getT().add(template);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Pair pair = new Pair();
                    pair.getI().addAll(iT);
                    pair.getT().add(template);
                    pairs.add(pair);
                }
            }
        }
    }
}
