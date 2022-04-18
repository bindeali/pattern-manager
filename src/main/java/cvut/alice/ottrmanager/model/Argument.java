package cvut.alice.ottrmanager.model;

import org.apache.jena.rdf.model.RDFNode;

import java.util.Objects;

public class Argument {
    private RDFNode object;
    private RDFNode type;
    private String value;

    public Argument(RDFNode object) {
        this.object = object;
    }

    public Argument(RDFNode object, RDFNode type) {
        this.object = object;
        this.type = type;
    }

    public Argument(RDFNode object, RDFNode type, String value) {
        this.object = object;
        this.type = type;
        this.value = value;
    }

    public RDFNode getObject() {
        return object;
    }

    public void setObject(RDFNode object) {
        this.object = object;
    }

    public RDFNode getType() {
        return type;
    }

    public void setType(RDFNode type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Argument)) return false;
        Argument argument = (Argument) o;
        return getObject().equals(argument.getObject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getObject(), getType(), getValue());
    }
}
