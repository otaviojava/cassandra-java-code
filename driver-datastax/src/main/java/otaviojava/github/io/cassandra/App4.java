package otaviojava.github.io.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.codec.registry.CodecRegistry;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.term.Term;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Hello world!
 */
public class App4 {

    private static final String KEYSPACE = "library";
    private static final String TYPE = "book";
    private static final String COLUMN_FAMILY = "category";

    public static void main(String[] args) {
        try (CqlSession session = CqlSession.builder().build()) {

            UserDefinedType userType =
                    session.getMetadata()
                            .getKeyspace(KEYSPACE)
                            .flatMap(ks -> ks.getUserDefinedType(TYPE))
                            .orElseThrow(() -> new IllegalArgumentException("Missing UDT definition"));

            UdtValue cleanCode = getValue(userType, 1, "Clean Code", "Robert Cecil Martin", Set.of("Java", "OO", "Good practice", "Design"));
            UdtValue cleanArchitecture = getValue(userType, 2, "Clean Architecture", "Robert Cecil Martin", Set.of("OO", "Good practice"));
            UdtValue effectiveJava = getValue(userType, 3, "Effective Java", "Joshua Bloch", Set.of("Java", "OO", "Good practice"));
            UdtValue nosql = getValue(userType, 4, "Nosql Distilled", "Martin Fowler", Set.of("NoSQL", "Good practice"));

            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY)
                    .values(createCondition("Java", Set.of(cleanCode, effectiveJava))).build());
            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY)
                    .values(createCondition("OO", Set.of(cleanCode, effectiveJava, cleanArchitecture))).build());
            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY)
                    .values(createCondition("Good practice",
                            Set.of(cleanCode, effectiveJava, cleanArchitecture, nosql))).build());
            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY)
                    .values(createCondition("NoSQL", Set.of(nosql))).build());

            Consumer<Row> log = row -> {
                String name = row.getString("name");
                Set<UdtValue> books = row.getSet("books", UdtValue.class);
                Set<String> logBooks = new HashSet<>();
                for (UdtValue book : books) {
                    long isbn = book.getLong("isbn");
                    String bookName = book.getString("name");
                    String author = book.getString("author");
                    logBooks.add(String.format(" %d %s %s", isbn, bookName, author));
                }
                System.out.println(String.format("The result %s %s", name, logBooks));
            };

            findById(session, "OO", log);
            findById(session, "Good practice", log);
            deleteById(session, "OO");

            PreparedStatement prepare = session.prepare("select * from library.category where name = ?");
            BoundStatement statement = prepare.bind("Java");
            ResultSet resultSet = session.execute(statement);
            resultSet.forEach(log);
        }

    }

    private static void findById(CqlSession session, String name, Consumer<Row> log) {
        ResultSet resultSet = session.execute(QueryBuilder.selectFrom(KEYSPACE, COLUMN_FAMILY)
                .all().where(Relation.column("name").isEqualTo(QueryBuilder.literal(name))).build());
        resultSet.forEach(log);
    }

    private static void deleteById(CqlSession session, String name) {
        session.execute(QueryBuilder.deleteFrom(KEYSPACE, COLUMN_FAMILY)
                .where(Relation.column("name").isEqualTo(QueryBuilder.literal(name))).build());

    }

    private static UdtValue getValue(UserDefinedType userType, long isbn, String name, String author, Set<String> categories) {
        UdtValue udtValue = userType.newValue();
        TypeCodec<Object> bigIntCodec = CodecRegistry.DEFAULT.codecFor(userType.getFieldTypes().get(0));
        TypeCodec<Object> textCodec = CodecRegistry.DEFAULT.codecFor(userType.getFieldTypes().get(1));
        TypeCodec<Object> setCodec = CodecRegistry.DEFAULT.codecFor(userType.getFieldTypes().get(3));
        udtValue.set("isbn", isbn, bigIntCodec);
        udtValue.set("name", name, textCodec);
        udtValue.set("author", author, textCodec);
        udtValue.set("categories", categories, setCodec);
        return udtValue;
    }

    private static Map<String, Term> createCondition(String name, Set<UdtValue> books) {
        return Map.of("name", QueryBuilder.literal(name), "books", QueryBuilder.literal(books));
    }
}
