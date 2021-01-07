package otaviojava.github.io.cassandra;

import jakarta.nosql.column.ColumnQuery;
import org.eclipse.jnosql.artemis.cassandra.column.CassandraTemplate;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import java.util.Set;
import java.util.stream.Stream;

import static jakarta.nosql.column.ColumnQuery.select;

/**
 * Hello world!
 */
public class App3 {


    public static void main(String[] args) {

        try(SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            CassandraTemplate template =  container.select(CassandraTemplate.class).get();

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

            ColumnQuery query = select().from("category").build();
            Stream<Category> books = template.select(query);
            books.forEach(System.out::println);
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
