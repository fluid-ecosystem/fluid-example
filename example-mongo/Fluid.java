import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;

/**
 * Database read/write against MongoDB, a document store rather than a
 * relational one: no table DDL, documents go in as-is. Same shape as the
 * JDBC examples otherwise — write 5, read back, log both.
 */
public class Fluid {

    public static void main(String[] args) throws Exception {
        String uri = System.getenv().getOrDefault("MONGO_URI", "mongodb://mongo:27017");

        try (MongoClient client = MongoClients.create(uri)) {
            MongoDatabase db = client.getDatabase("fluid");
            MongoCollection<Document> messages = db.getCollection("messages");

            for (int i = 1; i <= 5; i++) {
                messages.insertOne(new Document()
                        .append("content", "hello from MongoDB #" + i)
                        .append("createdAt", new Date()));
            }
            System.out.println("Inserted 5 documents into MongoDB");

            int rowsRead = 0;
            for (Document doc : messages.find()) {
                rowsRead++;
                System.out.println("doc " + doc.getObjectId("_id") + ": " + doc.getString("content")
                        + " @ " + doc.getDate("createdAt"));
            }

            System.out.println("MongoDB example completed successfully: "
                    + rowsRead + " documents read back");
        }

        Thread.sleep(10000);
    }
}
