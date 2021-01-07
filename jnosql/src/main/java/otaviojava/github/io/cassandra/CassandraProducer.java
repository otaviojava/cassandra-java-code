package otaviojava.github.io.cassandra;

import jakarta.nosql.column.ColumnFamilyManager;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class CassandraProducer {

    @Inject
    @ConfigProperty(defaultValue = "column")
    private ColumnFamilyManager manager;

    @Produces
    @ApplicationScoped
    public ColumnFamilyManager getManagerCassandra() {
        return manager;
    }

    public void dispose(@Disposes ColumnFamilyManager manager) {
        manager.close();
    }

}