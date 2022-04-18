package cvut.alice.ottrmanager.tool.a12;

import cvut.alice.ottrmanager.model.Pair;
import cvut.alice.ottrmanager.model.Template;
import cvut.alice.ottrmanager.tool.common.Manager;

import java.util.ArrayList;
import java.util.Optional;

public class PairFinder {

    public ArrayList<ArrayList<Template>> getCombinations(Template instance) {
        Optional<Template> templateOptional = Manager.getManager().getTemplates().stream().filter(temp -> temp.getIri().equals(instance.getIri())).findFirst();
        if (templateOptional.isPresent()) {
            Template template = templateOptional.get();
            ArrayList<ArrayList<Template>> ret = new ArrayList<>();
            ret.add(new ArrayList<>(template.getTemplates()));
            return ret;
        }
        return new ArrayList<>();
    }

    public void findPairs() {
        for (Template instance : Manager.getManager().getTemplates()) {
            for (ArrayList<Template> iT : getCombinations(instance)) {
                boolean found = false;
                for (Pair pair : Manager.getManager().getPairs()) {
                    if (pair.getI().containsAll(iT)) {
                        pair.getT().add(instance);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Pair pair = new Pair();
                    pair.getI().addAll(iT);
                    pair.getT().add(instance);
                    Manager.getManager().getPairs().add(pair);
                }
            }
        }
    }
}
