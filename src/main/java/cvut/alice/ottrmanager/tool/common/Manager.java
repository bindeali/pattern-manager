package cvut.alice.ottrmanager.tool.common;


import org.apache.jena.query.Dataset;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;

public class Manager {
    private static Manager manager = new Manager();
    private Dataset dataset;
    private RDFConnectionRemoteBuilder builder = RDFConnectionFuseki.create().destination("");

    public static Manager getManager() {
        return manager;
    }


    public Manager() {
        try (RDFConnectionFuseki connection = (RDFConnectionFuseki) builder.build()) {
            this.dataset = connection.fetchDataset();
        }
    }

    public Dataset getDataset() {
        return dataset;
    }

    public RDFConnectionRemoteBuilder getBuilder() {
        return builder;
    }
}
