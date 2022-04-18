package cvut.alice.ottrmanager.model;
import java.util.ArrayList;
import java.util.Objects;

public class Template {
    private String iri;
    private ArrayList<Argument> arguments;
    private ArrayList<Template> templates;

    public Template(String iri, ArrayList<Argument> arguments, ArrayList<Template> templates) {
        this.iri = iri;
        this.arguments = arguments;
        this.templates = templates;
    }

    @Override
    public String toString() {
        return "Template{" +
                "name='" + iri +
//                ", templates=" + templates +
                "}\n";
    }

    public String getIri() {
        return iri;
    }

    public void setIri(String iri) {
        this.iri = iri;
    }

    public ArrayList<Argument> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<Argument> arguments) {
        this.arguments = arguments;
    }

    public ArrayList<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(ArrayList<Template> templates) {
        this.templates = templates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Template)) return false;
        Template template = (Template) o;
        return getIri().equals(template.getIri());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIri(), getArguments(), getTemplates());
    }
}
