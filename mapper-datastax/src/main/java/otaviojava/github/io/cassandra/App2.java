package otaviojava.github.io.cassandra;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.google.common.collect.Sets;

import java.util.Set;
import java.util.stream.StreamSupport;

/**
 * Hello world!
 */
public class App2 {


    public static void main(String[] args) {
        try (Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build()) {

            Session session = cluster.connect();
            MappingManager manager = new MappingManager(session);
            Mapper<Book> mapper = manager.mapper(Book.class);

            Book cleanCode = getBook(1L, "Clean Code", "Robert Cecil Martin", Sets.newHashSet("Java", "OO"));
            Book cleanArchitecture = getBook(2L, "Clean Architecture", "Robert Cecil Martin", Sets.newHashSet("Good practice"));
            Book effectiveJava = getBook(3L, "Effective Java", "Joshua Bloch", Sets.newHashSet("Java", "Good practice"));
            Book nosql = getBook(4L, "Nosql Distilled", "Martin Fowler", Sets.newHashSet("NoSQL", "Good practice"));

            mapper.save(cleanCode);
            mapper.save(cleanArchitecture);
            mapper.save(effectiveJava);
            mapper.save(nosql);


            Book book = mapper.get(1L);
            System.out.println("Book found: " + book);

            mapper.delete(book);

            System.out.println("Book found: " + mapper.get(1L));

            PreparedStatement prepare = session.prepare("select * from library.book where isbn = ?");
            BoundStatement statement = prepare.bind(2L);
            Result<Book> books = mapper.map(session.execute(statement));
            StreamSupport.stream(books.spliterator(), false).forEach(System.out::println);
        }

    }

    private static Book getBook(long isbn, String name, String author, Set<String> categories) {
        Book book = new Book();
        book.setIsbn(isbn);
        book.setName(name);
        book.setAuthor(author);
        book.setCategories(categories);
        return book;
    }

}
