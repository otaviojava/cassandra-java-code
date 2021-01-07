package otaviojava.github.io.cassandra;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

@Mapper
public interface InventoryMapper {
    @DaoFactory
    BookDao productDao(@DaoKeyspace CqlIdentifier keyspace);
}