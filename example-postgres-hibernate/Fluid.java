import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

/**
 * Database read/write via Hibernate, an ORM: entities and HQL instead of
 * tables and SQL. Same database and same shape of work as
 * example-postgres-jdbc, mapped by annotations instead of by hand.
 *
 * <p>Bootstrapped natively (StandardServiceRegistry + MetadataSources)
 * rather than through JPA's persistence.xml, which keeps this module's
 * dependency list to hibernate-core's own runtime deps -- no JAXB, which
 * persistence.xml parsing would otherwise pull in.
 */
public class Fluid {

    public static void main(String[] args) throws Exception {
        String url = System.getenv().getOrDefault("POSTGRES_URL", "jdbc:postgresql://postgres:5432/fluid");
        String user = System.getenv().getOrDefault("POSTGRES_USER", "fluid");
        String password = System.getenv().getOrDefault("POSTGRES_PASSWORD", "fluid");

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.connection.driver_class", "org.postgresql.Driver")
                .applySetting("hibernate.connection.url", url)
                .applySetting("hibernate.connection.username", user)
                .applySetting("hibernate.connection.password", password)
                .applySetting("hibernate.hbm2ddl.auto", "update")
                .build();

        try {
            SessionFactory sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(Message.class)
                    .buildMetadata()
                    .buildSessionFactory();

            try {
                try (Session session = sessionFactory.openSession()) {
                    Transaction tx = session.beginTransaction();
                    for (int i = 1; i <= 5; i++) {
                        session.persist(new Message("hello from Hibernate #" + i));
                    }
                    tx.commit();
                }
                System.out.println("Inserted 5 entities via Hibernate");

                List<Message> messages;
                try (Session session = sessionFactory.openSession()) {
                    messages = session.createQuery("from Message", Message.class).list();
                }

                for (Message m : messages) {
                    System.out.println("entity " + m.getId() + ": " + m.getContent()
                            + " @ " + m.getCreatedAt());
                }

                System.out.println("Postgres Hibernate example completed successfully: "
                        + messages.size() + " entities read back");
            } finally {
                sessionFactory.close();
            }
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }

        Thread.sleep(10000);
    }
}
