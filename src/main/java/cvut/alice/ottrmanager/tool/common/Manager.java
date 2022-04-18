package cvut.alice.ottrmanager.tool.common;


import cvut.alice.ottrmanager.model.Instance;
import cvut.alice.ottrmanager.model.Pair;
import cvut.alice.ottrmanager.model.Template;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.tdb.TDBFactory;

import java.util.ArrayList;
import java.util.Collection;

public class Manager {
    private static Manager manager = new Manager();

    public static Manager getManager() {return manager;}

    private Collection<Instance> instances;
    private Collection<Template> templates;
    private Collection<Pair> pairs;
    private Model model;

    public Manager() {
        this.instances = new ArrayList<>();
        this.templates = new ArrayList<>();
        this.pairs = new ArrayList<>();
        Dataset dataset = TDBFactory.createDataset(".");
        dataset.begin(ReadWrite.READ);
        this.model = dataset.getDefaultModel();
        dataset.end();
    }

    public void clear() {
        this.instances = new ArrayList<>();
        this.templates = new ArrayList<>();
        this.pairs = new ArrayList<>();
    }

    public void insertInstance(Instance instance) {
        if (!this.getInstances().contains(instance))
            this.getInstances().add(instance);
    }

    public void insertInstance(Template template) {
        if (!this.getTemplates().contains(template))
            this.getTemplates().add(template);
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Collection<Instance> getInstances() {
        return instances;
    }

    public void setInstances(Collection<Instance> instances) {
        this.instances = instances;
    }

    public Collection<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(Collection<Template> templates) {
        this.templates = templates;
    }

    public Collection<Pair> getPairs() {
        return pairs;
    }

    public void setPairs(Collection<Pair> pairs) {
        this.pairs = pairs;
    }
}
