package otaviojava.github.io.cassandra;

import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.cassandra.core.CassandraTemplate;

import java.util.List;
import java.util.Set;

/**
 * Hello world!
 */
public class App3 {

    private static final String KEYSPACE = "library";
    private static final String COLUMN_FAMILY = "category";

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(CassandraConfig.class)) {

            CassandraTemplate template = ctx.getBean(CassandraTemplate.class);

            BookType cleanCode = getBook(1L, "Clean Code", "Robert Cecil Martin", Set.of("Java", "OO"));
            BookType cleanArchitecture = getBook(2L, "Clean Architecture", "Robert Cecil Martin", Set.of("Good practice"));
            BookType effectiveJava = getBook(3L, "Effective Java", "Joshua Bloch", Set.of("Java", "Good practice"));
            BookType nosqlDistilled = getBook(4L, "Nosql Distilled", "Martin Fowler", Set.of("NoSQL", "Good practice"));

            Category java = getCategory("Java", Set.of(cleanCode, effectiveJava));
            Category oo = getCategory("OO", Set.of(cleanCode, effectiveJava, cleanArchitecture));
            Category goodPractice = getCategory("Good practice", Set.of(cleanCode, effectiveJava, cleanArchitecture, nosqlDistilled));
            Category nosql = getCategory("NoSQL", Set.of(nosqlDistilled));

            template.insert(java);
            template.insert(oo);
            template.insert(goodPractice);
            template.insert(nosql);

            List<Category> categories = template.select(QueryBuilder.selectFrom(KEYSPACE, COLUMN_FAMILY).all().build(), Category.class);
            System.out.println(categories);
        }

    }

    private static Category getCategory(String name, Set<BookType> books) {
        Category category = new Category();
        category.setName(name);
        category.setBooks(books);
        return category;
    }

    private static BookType getBook(long isbn, String name, String author, Set<String> categories) {
        BookType book = new BookType();
        book.setIsbn(isbn);
        book.setName(name);
        book.setAuthor(author);
        book.setCategories(categories);
        return book;
    }

}
