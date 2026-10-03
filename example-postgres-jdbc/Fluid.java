import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Database read/write with no ORM: java.sql directly against the JDBC
 * driver. Compare with example-postgres-hibernate, same database, same
 * shape of work, mapped by hand instead of by annotations.
 */
public class Fluid {

    public static void main(String[] args) throws Exception {
        String url = System.getenv().getOrDefault("POSTGRES_URL", "jdbc:postgresql://postgres:5432/fluid");
        String user = System.getenv().getOrDefault("POSTGRES_USER", "fluid");
        String password = System.getenv().getOrDefault("POSTGRES_PASSWORD", "fluid");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            try (Statement ddl = conn.createStatement()) {
                ddl.execute("""
                    CREATE TABLE IF NOT EXISTS messages (
                        id SERIAL PRIMARY KEY,
                        content TEXT NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT now()
                    )
                    """);
            }

            try (PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO messages (content) VALUES (?)")) {
                for (int i = 1; i <= 5; i++) {
                    insert.setString(1, "hello from raw JDBC #" + i);
                    insert.executeUpdate();
                }
            }
            System.out.println("Inserted 5 rows via JDBC");

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

            System.out.println("Postgres JDBC example completed successfully: "
                    + rowsRead + " rows read back");
        }

        // Keep the container up long enough for `docker compose logs` to
        // capture the above before compose tears it down.
        Thread.sleep(10000);
    }
}
