package otaviojava.github.io.cassandra;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

/**
 * Hello world!
 */
public class App4 {


    public static void main(String[] args) {
        EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("hibernate");
        EntityManager manager = managerFactory.createEntityManager();
        manager.getTransaction().begin();
        manager.merge(getBook(1L, "Clean Code", "Robert Cecil Martin", "Java,OO"));
        manager.merge(getBook(2L, "Clean Architecture", "Robert Cecil Martin", "Good practice"));
        manager.merge(getBook(3L, "Agile Principles, Patterns, and Practices in C#", "Robert Cecil Martin", "Good practice"));
        manager.merge(getBook(4L, "Effective Java", "Joshua Bloch", "Java, Good practice"));
        manager.merge(getBook(5L, "Java Concurrency", "Robert Cecil Martin", "Java,OO"));
        manager.merge(getBook(6L, "Nosql Distilled", "Martin Fowler", "Java,OO"));
        manager.getTransaction().commit();

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(manager);

        QueryBuilder builder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();
        org.apache.lucene.search.Query luceneQuery = builder.keyword().onFields("category").matching("Java").createQuery();

        Query query = fullTextEntityManager.createFullTextQuery(luceneQuery, Book.class);
        List<Book> result = query.getResultList();
        System.out.println(result);
        managerFactory.close();

    }


    private static Book getBook(long isbn, String name, String author, String category) {
        Book book = new Book();
        book.setIsbn(isbn);
        book.setName(name);
        book.setAuthor(author);
        book.setCategory(category);
        return book;
    }

}
