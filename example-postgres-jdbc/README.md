# example-postgres-jdbc

Database read/write with no ORM: `java.sql` directly against the
[PostgreSQL JDBC driver](https://jdbc.postgresql.org/), which has no runtime
dependencies of its own — a deliberate pick, since `DependencyDownloader`
fetches exactly the jars a `pom.xml` lists, with no transitive resolution.

`Fluid.java` creates a `messages` table if missing, inserts 5 rows, reads
them back, and logs both — the thing to look for in
`docker compose logs example-postgres-jdbc` is:

```
Postgres JDBC example completed successfully: 5 rows read back
```

Compare with [example-postgres-hibernate](../example-postgres-hibernate),
same database and same shape of work, mapped by hand here instead of by
annotations.

## Run it

```bash
docker compose --profile postgres-jdbc up --build
```

Postgres is reachable from the host at `localhost:5433` (not 5432, to avoid
colliding with a locally installed Postgres) with user/password/db all
`fluid`.
