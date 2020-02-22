package otaviojava.github.io.cassandra;

import com.google.common.collect.Sets;
import jakarta.nosql.mapping.PreparedStatement;
import jakarta.nosql.mapping.column.ColumnTemplate;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import java.util.Optional;
import java.util.Set;

/**
 * Hello world!
 */
public class App2 {


    public static void main(String[] args) {

        try(SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            ColumnTemplate template =  container.select(ColumnTemplate.class).get();

            Book cleanCode = getBook(1L, "Clean Code", "Robert Cecil Martin", Sets.newHashSet("Java", "OO"));
            Book cleanArchitecture = getBook(2L, "Clean Architecture", "Robert Cecil Martin", Sets.newHashSet("Good practice"));
            Book effectiveJava = getBook(3L, "Effective Java", "Joshua Bloch", Sets.newHashSet("Java", "Good practice"));
            Book nosql = getBook(4L, "Nosql Distilled", "Martin Fowler", Sets.newHashSet("NoSQL", "Good practice"));

            template.insert(cleanCode);
            template.insert(cleanArchitecture);
            template.insert(effectiveJava);
            template.insert(nosql);


            Optional<Book> book = template.find(Book.class, 1L);
            System.out.println("Book found: " + book);

            template.delete(Book.class, 1L);

            System.out.println("Book found: " + template.find(Book.class, 1L));


            PreparedStatement prepare = template.prepare("select * from Book where isbn = @isbn");
            prepare.bind("isbn",2L);
            Optional<Book> result = prepare.getSingleResult();
            System.out.println("prepare: " + result);
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
