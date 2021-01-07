package otaviojava.github.io.cassandra;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Query;
import com.datastax.oss.driver.api.mapper.annotations.Select;

@Dao
public interface CategoryDao {

    @Select
    Category findById(String id);

    @Insert
    void save(Category book);

    @Delete
    void delete(Category book);

    @Query("SELECT * FROM library.category")
    PagingIterable<Category> map();

}
