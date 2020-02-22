package otaviojava.github.io.cassandra;

import com.google.common.collect.Sets;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Hello world!
 */
public class App5 {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class)) {

            BookRepository repository = ctx.getBean(BookRepository.class);
            Book cleanCode = getBook(1L, "Clean Code", "Robert Cecil Martin", Sets.newHashSet("Java", "OO"));
            Book cleanArchitecture = getBook(2L, "Clean Architecture", "Robert Cecil Martin", Sets.newHashSet("Good practice"));
            Book effectiveJava = getBook(3L, "Effective Java", "Joshua Bloch", Sets.newHashSet("Java", "Good practice"));
            Book nosql = getBook(4L, "Nosql Distilled", "Martin Fowler", Sets.newHashSet("NoSQL", "Good practice"));

            repository.insert(cleanCode);
            repository.insert(cleanArchitecture);
            repository.insert(effectiveJava);
            repository.insert(nosql);

            List<Book> books = repository.findAll();
            System.out.println(books);

            Optional<Book> book = repository.findById(1L);
            System.out.println(book);


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
