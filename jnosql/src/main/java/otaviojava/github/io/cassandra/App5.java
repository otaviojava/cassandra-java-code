package otaviojava.github.io.cassandra;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import java.util.Optional;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class App5
{
    public static void main( String[] args )
    {
        try(SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            BookRepository repository =  container.select(BookRepository.class).get();

            Book cleanCode = getBook(1L, "Clean Code", "Robert Cecil Martin", Set.of("Java", "OO"));
            Book cleanArchitecture = getBook(2L, "Clean Architecture", "Robert Cecil Martin", Set.of("Good practice"));
            Book effectiveJava = getBook(3L, "Effective Java", "Joshua Bloch", Set.of("Java", "Good practice"));
            Book nosql = getBook(4L, "Nosql Distilled", "Martin Fowler", Set.of("NoSQL", "Good practice"));

            repository.save(cleanCode);
            repository.save(cleanArchitecture);
            repository.save(effectiveJava);
            repository.save(nosql);

            Optional<Book> book = repository.findById(1L);
            System.out.println(book);

            repository.deleteById(1L);

            System.out.println("Using method query");
            repository.findAll().forEach(System.out::println);
            System.out.println("Using CQL");
            repository.findAll1().forEach(System.out::println);
            System.out.println("Using query JNoSQL");
            repository.findAll2().forEach(System.out::println);

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
