package otaviojava.github.io.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Hello world!
 */
public class App {

    private static final String KEYSPACE = "library";
    private static final String COLUMN_FAMILY = "book";
    private static final String[] NAMES = new String[]{"isbn", "name", "author", "categories"};

    public static void main(String[] args) {
        try (Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build()) {

            Session session = cluster.connect();

            Object[] cleanCode = new Object[]{1, "Clean Code", "Robert Cecil Martin", Sets.newHashSet("Java", "OO")};
            Object[] cleanArchitecture = new Object[]{2, "Clean Architecture", "Robert Cecil Martin", Sets.newHashSet("Good practice")};
            Object[] effectiveJava = new Object[]{3, "Effective Java", "Joshua Bloch", Sets.newHashSet("Java", "Good practice")};
            Object[] nosql = new Object[]{4, "Nosql Distilled", "Martin Fowler", Sets.newHashSet("NoSQL", "Good practice")};

            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(NAMES, cleanCode));
            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(NAMES, cleanArchitecture));
            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(NAMES, effectiveJava));
            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(NAMES, nosql));
            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(NAMES, cleanCode));

            ResultSet resultSet = session.execute(QueryBuilder.select().from(KEYSPACE, COLUMN_FAMILY));
            for (Row row : resultSet) {
                Long isbn = row.getLong("isbn");
                String name = row.getString("name");
                String author = row.getString("author");
                Set<String> categories = row.getSet("categories", String.class);
                System.out.println(String.format(" the result is %s %s %s %s", isbn, name, author, categories));
            }
        }

    }

}
