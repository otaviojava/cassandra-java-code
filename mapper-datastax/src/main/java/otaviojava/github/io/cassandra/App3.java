package otaviojava.github.io.cassandra;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.shaded.guava.common.collect.Sets;

import java.util.Set;
import java.util.stream.StreamSupport;

/**
 * Hello world!
 */
public class App3 {

    private static final String KEYSPACE = "library";

    public static void main(String[] args) {
        try (CqlSession session = CqlSession.builder().build()) {

            InventoryMapper inventoryMapper = new InventoryMapperBuilder(session).build();
            CategoryDao mapper = inventoryMapper.getCategoryDao(CqlIdentifier.fromCql(KEYSPACE));

            BookType cleanCode = getBook(1L, "Clean Code", "Robert Cecil Martin", Set.of("Java", "OO"));
            BookType cleanArchitecture = getBook(2L, "Clean Architecture", "Robert Cecil Martin", Set.of("Good practice"));
            BookType effectiveJava = getBook(3L, "Effective Java", "Joshua Bloch", Set.of("Java", "Good practice"));
            BookType nosqlDistilled = getBook(4L, "Nosql Distilled", "Martin Fowler", Set.of("NoSQL", "Good practice"));

            Category java = getCategory("Java", Sets.newHashSet(cleanCode, effectiveJava));
            Category oo = getCategory("OO", Sets.newHashSet(cleanCode, effectiveJava, cleanArchitecture));
            Category goodPractice = getCategory("Good practice", Sets.newHashSet(cleanCode, effectiveJava, cleanArchitecture, nosqlDistilled));
            Category nosql = getCategory("NoSQL", Sets.newHashSet(nosqlDistilled));

            mapper.save(java);
            mapper.save(oo);
            mapper.save(goodPractice);
            mapper.save(nosql);

            PagingIterable<Category> categories = mapper.map();
            StreamSupport.stream(categories.spliterator(), false).forEach(System.out::println);
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
