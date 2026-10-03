# example-mongo

Database read/write against MongoDB — a document store rather than a
relational one, so there's no table DDL: documents go in as-is via the
[official sync driver](https://www.mongodb.com/docs/drivers/java/sync/current/).
Otherwise the same shape as the JDBC examples: write 5, read back, log both.

Look for this line in `docker compose logs example-mongo`:

```
MongoDB example completed successfully: 5 documents read back
```

The driver ships as three artifacts (`mongodb-driver-sync` depends on
`mongodb-driver-core` and `bson`); all three are listed in `pom.xml` since
`DependencyDownloader` does no transitive resolution.

## Run it

```bash
docker compose --profile mongo up --build
```

Mongo is reachable from the host at `localhost:27017`.
