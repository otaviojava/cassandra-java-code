package otaviojava.github.io.cassandra;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

/**
 * Hello world!
 */
public class App2 {


    public static void main(String[] args) {
        EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("hibernate");
        EntityManager manager = managerFactory.createEntityManager();
        manager.getTransaction().begin();
        String name = "Clean Code";
        Book cleanCode = getBook(1L, "Clean Code", "Robert Cecil Martin");
        Book cleanArchitecture = getBook(2L, "Clean Architecture", "Robert Cecil Martin");
        Book agile = getBook(3L, "Agile Principles, Patterns, and Practices in C#", "Robert Cecil Martin");
        Book effectiveJava = getBook(4L, "Effective Java", "Joshua Bloch");
        Book javaConcurrency = getBook(5L, "Java Concurrency", "Robert Cecil Martin");

        manager.merge(cleanCode);
        manager.merge(cleanArchitecture);
        manager.merge(agile);
        manager.merge(effectiveJava);
        manager.merge(javaConcurrency);
        manager.getTransaction().commit();

        Query query = manager.createQuery("select b from book b where name = :name");
        query.setParameter("name", name);
        List<Book> books = query.getResultList();
        System.out.println("books: " + books);
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
