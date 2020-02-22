package otaviojava.github.io.cassandra;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface BookAccessor {

    @Query("SELECT * FROM library.book")
    Result<Book> getAll();


    @Query("SELECT * FROM library.book where isbn = ?")
    Book findById(long isbn);

    @Query("SELECT * FROM library.book where isbn = :isbn")
    Book findById2(@Param("isbn") long isbn);

}
