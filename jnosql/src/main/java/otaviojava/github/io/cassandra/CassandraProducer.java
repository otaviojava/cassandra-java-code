package otaviojava.github.io.cassandra;

import jakarta.nosql.Settings;
import org.eclipse.jnosql.diana.cassandra.column.CassandraColumnFamilyManager;
import org.eclipse.jnosql.diana.cassandra.column.CassandraColumnFamilyManagerFactory;
import org.eclipse.jnosql.diana.cassandra.column.CassandraConfiguration;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import java.util.Collections;

@ApplicationScoped
public class CassandraProducer {

    private static final String KEYSPACE = "library";

    private CassandraConfiguration cassandraConfiguration;

    private CassandraColumnFamilyManagerFactory managerFactory;

    @PostConstruct
    public void init() {
        cassandraConfiguration = new CassandraConfiguration();
        Settings settings = Settings.of(Collections.singletonMap("cassandra-host-1", "localhost"));
        managerFactory = cassandraConfiguration.get(settings);
    }


    @Produces
    @ApplicationScoped
    public CassandraColumnFamilyManager getManagerCassandra() {
        return managerFactory.get(KEYSPACE);
    }

    public void dispose(@Disposes CassandraColumnFamilyManager manager) {
        manager.close();
        managerFactory.close();
    }

}