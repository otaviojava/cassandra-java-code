package otaviojava.github.io.cassandra;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends CassandraRepository<Book, Long> {

    @Query("select * from book")
    List<Book> findAll();
}
