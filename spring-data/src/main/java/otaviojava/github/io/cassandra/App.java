package otaviojava.github.io.cassandra;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.google.common.collect.Sets;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.cassandra.core.CassandraTemplate;

import java.util.List;
import java.util.Set;

/**
 * Hello world!
 */
public class App {
    private static final String KEYSPACE = "library";
    private static final String COLUMN_FAMILY = "book";

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class)) {

            CassandraTemplate template = ctx.getBean(CassandraTemplate.class);

            Book cleanCode = getBook(1L, "Clean Code", "Robert Cecil Martin", Sets.newHashSet("Java", "OO"));
            Book cleanArchitecture = getBook(2L, "Clean Architecture", "Robert Cecil Martin", Sets.newHashSet("Good practice"));
            Book effectiveJava = getBook(3L, "Effective Java", "Joshua Bloch", Sets.newHashSet("Java", "Good practice"));
            Book nosql = getBook(4L, "Nosql Distilled", "Martin Fowler", Sets.newHashSet("NoSQL", "Good practice"));

            template.insert(cleanCode);
            template.insert(cleanArchitecture);
            template.insert(effectiveJava);
            template.insert(nosql);

            List<Book> books = template.select(QueryBuilder.select().from(KEYSPACE, COLUMN_FAMILY), Book.class);
            System.out.println(books);


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
