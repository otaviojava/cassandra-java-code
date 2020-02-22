package otaviojava.github.io.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.StreamSupport;

/**
 * Hello world!
 */
public class App5 {

    private static final String KEYSPACE = "library";
    private static final String COLUMN_FAMILY = "book";

    public static void main(String[] args)  {
        try (Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build()) {

            Session session = cluster.connect();
            MappingManager manager = new MappingManager(session);
            Mapper<Book> mapper = manager.mapper(Book.class);


            Book cleanCode = getBook(1L, "Clean Code", "Robert Cecil Martin", Sets.newHashSet("Java", "OO"));
            Book cleanArchitecture = getBook(2L, "Clean Architecture", "Robert Cecil Martin", Sets.newHashSet("Good practice"));
            Book effectiveJava = getBook(3L, "Effective Java", "Joshua Bloch", Sets.newHashSet("Java", "Good practice"));
            Book nosql = getBook(4L, "Nosql Distilled", "Martin Fowler", Sets.newHashSet("NoSQL", "Good practice"));

            mapper.saveAsync(cleanCode);
            mapper.saveAsync(cleanArchitecture);
            mapper.saveAsync(effectiveJava);
            mapper.saveAsync(nosql);

            ListenableFuture<Result<Book>> books = mapper.mapAsync(session.executeAsync(QueryBuilder.select().from(KEYSPACE, COLUMN_FAMILY)));
            Consumer consumer = new Consumer(books);
            books.addListener(consumer, Executors.newSingleThreadExecutor());
        }

    }

    private static class Consumer implements Runnable {

        private final ListenableFuture<Result<Book>> books;

        private Consumer(ListenableFuture<Result<Book>> books) {
            this.books = books;
        }

        @Override
        public void run() {
            if (books.isDone()) {
                Result<Book> books = null;
                try {
                    books = this.books.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                System.out.println("Executed async: ");
                StreamSupport.stream(books.spliterator(), false).forEach(System.out::println);
            }
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
