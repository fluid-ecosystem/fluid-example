# example-h2

Database read/write against [H2](https://www.h2database.com/), embedded
in-memory — no separate container, no network hop, the whole database lives
inside this one JVM for the process's lifetime. Same raw-JDBC shape as
[example-postgres-jdbc](../example-postgres-jdbc), to make the contrast
(file/network database vs. embedded) easy to see: the only things that
differ are the JDBC URL and the SQL dialect's identity-column syntax.

Look for this line in `docker compose logs example-h2`:

```
H2 JDBC example completed successfully: 5 rows read back
```

## Run it

```bash
docker compose --profile h2 up --build
```

No database container to wait on — `example-h2` has no `depends_on`.
