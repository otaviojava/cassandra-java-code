package otaviojava.github.io.cassandra;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Query;
import com.datastax.oss.driver.api.mapper.annotations.Select;

import java.util.List;

@Dao
public interface BookDao {

    @Select
    Book findById(Long id);

    @Insert
    void save(Book book);

    @Delete
    void delete(Book book);

    @Query("SELECT * FROM library.book")
    PagingIterable<Book> map();
}
