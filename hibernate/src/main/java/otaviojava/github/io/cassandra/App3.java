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
public class App3 {


    public static void main(String[] args) {
        EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("hibernate");
        EntityManager manager = managerFactory.createEntityManager();
        manager.getTransaction().begin();
        manager.merge(getBook(1L, "Clean Code", "Robert Cecil Martin"));
        manager.merge(getBook(2L, "Clean Architecture", "Robert Cecil Martin"));
        manager.merge(getBook(3L, "Agile Principles, Patterns, and Practices in C#", "Robert Cecil Martin"));
        manager.merge(getBook(4L, "Effective Java", "Joshua Bloch"));
        manager.merge(getBook(5L, "Java Concurrency", "Robert Cecil Martin"));
        manager.getTransaction().commit();
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(manager);

        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Book.class).get();
        org.apache.lucene.search.Query query = qb
                .keyword()
                .onFields("name", "author")
                .matching("Robert")
                .createQuery();

        Query persistenceQuery =  fullTextEntityManager.createFullTextQuery(query, Book.class);
        List<Book> result = persistenceQuery.getResultList();
        System.out.println(result);

        managerFactory.close();
    }


    private static Book getBook(long isbn, String name, String author) {
        Book book = new Book();
        book.setIsbn(isbn);
        book.setName(name);
        book.setAuthor(author);
        return book;
    }

}
