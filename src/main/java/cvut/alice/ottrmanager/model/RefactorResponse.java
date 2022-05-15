package cvut.alice.ottrmanager.model;

import java.util.Collection;

public class RefactorResponse {
    private final String pattern;
    private final Collection<String> instantiations;

    public RefactorResponse(String pattern, Collection<String> instantiations) {
        this.pattern = pattern;
        this.instantiations = instantiations;
    }

    public String getPattern() {
        return pattern;
    }

    public Collection<String> getInstantiations() {
        return instantiations;
    }
}
