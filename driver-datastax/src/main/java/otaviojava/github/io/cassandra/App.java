package otaviojava.github.io.cassandra;


import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.term.Term;

import java.util.Map;
import java.util.Set;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

/**
 * Hello world!
 */
public class App {

    private static final String KEYSPACE = "library";
    private static final String COLUMN_FAMILY = "book";

    public static void main(String[] args) {
        try (CqlSession session = CqlSession.builder().build()) {

            Map<String, Term> cleanCode = createInsertQuery(new Object[]{1, "Clean Code", "Robert Cecil Martin",
                    Set.of("Java", "OO")});
            Map<String, Term> cleanArchitecture = createInsertQuery(new Object[]{2, "Clean Architecture",
                    "Robert Cecil Martin",
                    Set.of("Good practice")});
            Map<String, Term> effectiveJava = createInsertQuery(new Object[]{3, "Effective Java", "Joshua Bloch",
                    Set.of("Java", "Good practice")});
            Map<String, Term> nosql = createInsertQuery(new Object[]{4, "Nosql Distilled", "Martin Fowler",
                    Set.of("NoSQL", "Good practice")});

            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(cleanCode).build());
            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(cleanArchitecture).build());
            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(effectiveJava).build());
            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(nosql).build());
            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(cleanCode).build());

            ResultSet resultSet = session.execute(QueryBuilder.selectFrom(KEYSPACE, COLUMN_FAMILY).all().build());
            for (Row row : resultSet) {
                Long isbn = row.getLong("isbn");
                String name = row.getString("name");
                String author = row.getString("author");
                Set<String> categories = row.getSet("categories", String.class);
                System.out.println(String.format(" the result is %s %s %s %s", isbn, name, author, categories));
            }
        }

    }

    private static Map<String, Term>  createInsertQuery(Object[] parameters) {
        return Map.of("isbn", literal(parameters[0]), "name", literal(parameters[1]),
                "author", literal(parameters[2]),
                "categories", literal(parameters[3]));

    }

}
