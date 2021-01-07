package otaviojava.github.io.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.codec.registry.CodecRegistry;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * Hello world!
 */
public class App3 {

    private static final String KEYSPACE = "library";
    private static final String TYPE = "book";
    private static final String COLUMN_FAMILY = "category";
    private static final String[] NAMES = new String[]{"name", "books"};

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

//            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(NAMES, new Object[]{"Java", Set.of(cleanCode, effectiveJava)}));
//            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(NAMES, new Object[]{"OO", Set.of(cleanCode, effectiveJava, cleanArchitecture)}));
//            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(NAMES, new Object[]{"Good practice", Set.of(cleanCode, effectiveJava, cleanArchitecture, nosql)}));
//            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(NAMES, new Object[]{"NoSQL", Set.of(nosql)}));

            ResultSet resultSet = session.execute(QueryBuilder.selectFrom(KEYSPACE, COLUMN_FAMILY).all().build());
            for (Row row : resultSet) {
                String name = row.getString("name");
                UdtValue books = row.getUdtValue("books");
                Set<String> logBooks = new HashSet<>();
//                for (UserDefinedType book : books) {
//                    long isbn = book.getLong("isbn");
//                    String bookName = book.getString("name");
//                    String author = book.getString("author");
//                    logBooks.add(String.format(" %d %s %s", isbn, bookName, author));
//                }
                System.out.println(String.format("The result %s %s", name, logBooks));

            }
        }

    }

    private static UdtValue getValue(UserDefinedType userType, long isbn, String name, String author, Set<String> categories) {
        UdtValue udtValue = userType.newValue();

        TypeCodec<Object> textCodec = CodecRegistry.DEFAULT.codecFor(userType.getFieldTypes().get(1));
        TypeCodec<Object> setCodec = CodecRegistry.DEFAULT.codecFor(userType.getFieldTypes().get(2));
        TypeCodec<Object> bigIntCodec = CodecRegistry.DEFAULT.codecFor(userType.getFieldTypes().get(3));
        udtValue.set("isbn", isbn, bigIntCodec);
        udtValue.set("name", name, textCodec);
        udtValue.set("author", author, textCodec);
        udtValue.set("categories", categories, setCodec);
        return udtValue;

    }

}
