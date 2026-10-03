import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Database read/write against H2, embedded in-memory — no separate
 * container, no network hop, the whole database lives inside this JVM for
 * the process's lifetime. Same raw-JDBC shape as example-postgres-jdbc, to
 * make the contrast (file/network database vs. embedded) easy to see.
 */
public class Fluid {

    public static void main(String[] args) throws Exception {
        // DB_CLOSE_DELAY=-1 keeps the in-memory database alive for the whole
        // JVM lifetime; without it, H2 drops the database the instant the
        // one connection that created it closes.
        String url = "jdbc:h2:mem:fluid;DB_CLOSE_DELAY=-1";

        try (Connection conn = DriverManager.getConnection(url, "sa", "")) {
            try (Statement ddl = conn.createStatement()) {
                ddl.execute("""
                    CREATE TABLE IF NOT EXISTS messages (
                        id IDENTITY PRIMARY KEY,
                        content VARCHAR(255) NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                    )
                    """);
            }

            try (PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO messages (content) VALUES (?)")) {
                for (int i = 1; i <= 5; i++) {
                    insert.setString(1, "hello from embedded H2 #" + i);
                    insert.executeUpdate();
                }
            }
            System.out.println("Inserted 5 rows via JDBC into embedded H2");

            int rowsRead = 0;
            try (Statement query = conn.createStatement();
                 ResultSet rs = query.executeQuery(
                         "SELECT id, content, created_at FROM messages ORDER BY id")) {
                while (rs.next()) {
                    rowsRead++;
                    System.out.println("row " + rs.getInt("id") + ": " + rs.getString("content")
                            + " @ " + rs.getTimestamp("created_at"));
                }
            }

            System.out.println("H2 JDBC example completed successfully: "
                    + rowsRead + " rows read back");
        }

        Thread.sleep(10000);
    }
}
