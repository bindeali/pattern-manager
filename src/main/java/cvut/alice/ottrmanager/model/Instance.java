package cvut.alice.ottrmanager.model;

import java.util.Collection;

public class Instance {
    private String name;
    private Collection<String> arguments;

    public Instance(String name, Collection<String> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<String> getArguments() {
        return arguments;
    }

    public void setArguments(Collection<String> arguments) {
        this.arguments = arguments;
    }
}
