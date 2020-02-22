package otaviojava.github.io.cassandra;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.google.common.collect.Sets;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Hello world!
 */
public class App2 {

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

            Consumer<Row> log = row -> {
                Long isbn = row.getLong("isbn");
                String name = row.getString("name");
                String author = row.getString("author");
                Set<String> categories = row.getSet("categories", String.class);
                System.out.println(String.format(" the result is %s %s %s %s", isbn, name, author, categories));
            };

            findById(session,1L, log);

            deleteById(session, 1L);

            PreparedStatement prepare = session.prepare("select * from library.book where isbn = ?");
            BoundStatement statement = prepare.bind(2L);
            ResultSet resultSet = session.execute(statement);
            resultSet.forEach(log);


        }


    }

    private static void deleteById(Session session, Long isbn) {
        session.execute(QueryBuilder.delete().from(KEYSPACE, COLUMN_FAMILY).where(QueryBuilder.eq("isbn", isbn)));

    }

    private static void findById(Session session, long isbn, Consumer<Row> log) {
        ResultSet resultSet = session.execute(QueryBuilder.select().from(KEYSPACE, COLUMN_FAMILY).where(QueryBuilder.eq("isbn", isbn)));
        resultSet.forEach(log);
    }

}
