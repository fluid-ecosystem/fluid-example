# example-postgres-hibernate

Database read/write via [Hibernate](https://hibernate.org/orm/), an ORM:
entities and HQL instead of tables and SQL. Same database and same shape of
work as [example-postgres-jdbc](../example-postgres-jdbc) — create, write
5, read back, log both — mapped by `@Entity` annotations here instead of by
hand.

Look for this line in `docker compose logs example-postgres-hibernate`:

```
Postgres Hibernate example completed successfully: 5 entities read back
```

## Why the dependency list looks the way it does

`DependencyDownloader` fetches exactly the jars a `pom.xml` lists, with no
transitive resolution (see `fluid-builder`'s `DEVELOPMENT.md`). Hibernate is
not self-contained the way the JDBC/Mongo drivers are, so `pom.xml` lists
`hibernate-core`'s entire runtime dependency set explicitly, each pinned to
the exact version `hibernate-core` itself depends on.

One exclusion is deliberate: `jakarta.xml.bind-api` is included (Hibernate's
`MetadataSources` touches a JAXB class during construction regardless of
whether XML mapping is used), but the full `jaxb-runtime` implementation
and *its* transitive closure (`jaxb-core`, `istack-commons-runtime`,
`txw2`, ...) are not. This example bootstraps Hibernate natively
(`StandardServiceRegistry` + `MetadataSources`) with annotation-only
mapping, never through JPA's `persistence.xml`, so the code path that would
actually need a working `JAXBContext` is never reached — only the
`JAXBException` class needs to resolve. Verified by running it: no
`ClassNotFoundException`, data round-trips correctly.

## Run it

```bash
docker compose --profile postgres-hibernate up --build
```

Shares the same `postgres` service as
[example-postgres-jdbc](../example-postgres-jdbc) (`localhost:5433`), in a
separate table (`hibernate_messages` vs. `messages`).
