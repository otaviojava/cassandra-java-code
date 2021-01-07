package otaviojava.github.io.cassandra;


import jakarta.nosql.mapping.Query;
import org.eclipse.jnosql.mapping.cassandra.column.CQL;
import org.eclipse.jnosql.mapping.cassandra.column.CassandraRepository;

import java.util.stream.Stream;

public interface BookRepository extends CassandraRepository<Book, Long> {

    Stream<Book> findAll();

    @CQL("select * from book")
    Stream<Book> findAll1();

    @Query("select * from Book")
    Stream<Book> findAll2();
}
