package otaviojava.github.io.cassandra;

/**
 * Hello world!
 */
public class App4 {
//
//    private static final String KEYSPACE = "library";
//    private static final String TYPE = "book";
//    private static final String COLUMN_FAMILY = "category";
//    private static final String[] NAMES = new String[]{"name", "books"};
//
//    public static void main(String[] args) {
//        try (CqlSession session = CqlSession.builder().build()) {
//
//
//            UserType userType = session.getCluster().getMetadata().getKeyspace(KEYSPACE).getUserType(TYPE);
//            UDTValue cleanCode = getValue(userType, 1, "Clean Code", "Robert Cecil Martin", Sets.newHashSet("Java", "OO", "Good practice", "Design"));
//            UDTValue cleanArchitecture = getValue(userType, 2, "Clean Architecture", "Robert Cecil Martin", Sets.newHashSet("OO", "Good practice"));
//            UDTValue effectiveJava = getValue(userType, 3, "Effective Java", "Joshua Bloch", Sets.newHashSet("Java", "OO", "Good practice"));
//            UDTValue nosql = getValue(userType, 4, "Nosql Distilled", "Martin Fowler", Sets.newHashSet("NoSQL", "Good practice"));
//
//            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(NAMES, new Object[]{"Java", Sets.newHashSet(cleanCode, effectiveJava)}));
//            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(NAMES, new Object[]{"OO", Sets.newHashSet(cleanCode, effectiveJava, cleanArchitecture)}));
//            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(NAMES, new Object[]{"Good practice", Sets.newHashSet(cleanCode, effectiveJava, cleanArchitecture, nosql)}));
//            session.execute(QueryBuilder.insertInto(KEYSPACE, COLUMN_FAMILY).values(NAMES, new Object[]{"NoSQL", Sets.newHashSet(nosql)}));
//
//            Consumer<Row> log = row -> {
//                String name = row.getString("name");
//                Set<UDTValue> books = row.getSet("books", UDTValue.class);
//                Set<String> logBooks = new HashSet<>();
//                for (UDTValue book : books) {
//                    long isbn = book.getLong("isbn");
//                    String bookName = book.getString("name");
//                    String author = book.getString("author");
//                    logBooks.add(String.format(" %d %s %s", isbn, bookName, author));
//                }
//                System.out.println(String.format("The result %s %s", name, logBooks));
//            };
//
//            findById(session, "OO", log);
//            findById(session, "Good practice", log);
//            deleteById(session, "OO");
//
//            PreparedStatement prepare = session.prepare("select * from library.category where name = ?");
//            BoundStatement statement = prepare.bind("Java");
//            ResultSet resultSet = session.execute(statement);
//            resultSet.forEach(log);
//        }
//
//    }
//
//    private static void findById(Session session, String name, Consumer<Row> log) {
//        ResultSet resultSet = session.execute(QueryBuilder.select().from(KEYSPACE, COLUMN_FAMILY).where(QueryBuilder.eq("name", name)));
//        resultSet.forEach(log);
//    }
//
//    private static void deleteById(Session session, String name) {
//        session.execute(QueryBuilder.delete().from(KEYSPACE, COLUMN_FAMILY).where(QueryBuilder.eq("name", name)));
//
//    }
//
//    private static UDTValue getValue(UserType userType, long isbn, String name, String author, Set<String> categories) {
//        UDTValue udtValue = userType.newValue();
//        TypeCodec<Object> textCodec = CodecRegistry.DEFAULT_INSTANCE.codecFor(DataType.text());
//        TypeCodec<Object> setCodec = CodecRegistry.DEFAULT_INSTANCE.codecFor(DataType.set(DataType.text()));
//        TypeCodec<Object> bigIntCodec = CodecRegistry.DEFAULT_INSTANCE.codecFor(DataType.bigint());
//        udtValue.set("isbn", isbn, bigIntCodec);
//        udtValue.set("name", name, textCodec);
//        udtValue.set("author", author, textCodec);
//        udtValue.set("categories", categories, setCodec);
//        return udtValue;
//
//    }

}
