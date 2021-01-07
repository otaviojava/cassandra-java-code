package otaviojava.github.io.cassandra;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.PagingIterable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Hello world!
 */
public class App5 {

    private static final String KEYSPACE = "library";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (CqlSession session = CqlSession.builder().build()) {

            InventoryMapper inventoryMapper = new InventoryMapperBuilder(session).build();
            BookDao mapper = inventoryMapper.productDao(CqlIdentifier.fromCql(KEYSPACE));

            Book cleanCode = getBook(1L, "Clean Code", "Robert Cecil Martin", Set.of("Java", "OO"));
            Book cleanArchitecture = getBook(2L, "Clean Architecture", "Robert Cecil Martin", Set.of("Good practice"));
            Book effectiveJava = getBook(3L, "Effective Java", "Joshua Bloch", Set.of("Java", "Good practice"));
            Book nosql = getBook(4L, "Nosql Distilled", "Martin Fowler", Set.of("NoSQL", "Good practice"));

            mapper.saveAsync(cleanCode);
            mapper.saveAsync(cleanArchitecture);
            mapper.saveAsync(effectiveJava);
            mapper.saveAsync(nosql);

            CompletableFuture<Book> future = mapper.findByIdAsync(1L);
            Book book = future.get();
            System.out.println("Book found" + book);
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
