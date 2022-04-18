package cvut.alice.ottrmanager.model;

import java.util.ArrayList;
import java.util.Collection;

public class Pair {
    private ArrayList<Template> I;
    private ArrayList<Template> T;

    public Pair() {
        this.I = new ArrayList<>();
        this.T = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        for (Template template : I) {
            stringBuilder.append(template.getIri());
            stringBuilder.append(" ?param").append(count).append(",\n");
            count++;
        }
        return "Pair{" +
                "Pattern=" + stringBuilder.toString() +
                " Instances=" + T +
                '}';
    }

    public Collection<Template> getI() {
        return I;
    }

    public void setI(ArrayList<Template> i) {
        I = i;
    }

    public ArrayList<Template> getT() {
        return T;
    }

    public void setT(ArrayList<Template> t) {
        T = t;
    }
}
