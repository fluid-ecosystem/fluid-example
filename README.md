# fluid-example

[![Test the Echosystem](https://github.com/fluid-ecosystem/fluid-example/actions/workflows/run-the-echosystem.yml/badge.svg)](https://github.com/fluid-ecosystem/fluid-example/actions/workflows/run-the-echosystem.yml)
[![Test Database Examples](https://github.com/fluid-ecosystem/fluid-example/actions/workflows/test-database-examples.yml/badge.svg)](https://github.com/fluid-ecosystem/fluid-example/actions/workflows/test-database-examples.yml)
[![GitHub release](https://img.shields.io/github/v/release/fluid-ecosystem/fluid-example?label=release)](https://github.com/fluid-ecosystem/fluid-example/releases/latest)
[![Built on fluid-builder](https://img.shields.io/docker/v/maifeeulasad/fluid-builder?sort=semver&label=fluid-builder&logo=docker&logoColor=white)](https://github.com/fluid-ecosystem/fluid-builder)
[![Dependabot](https://img.shields.io/badge/dependabot-enabled-brightgreen?logo=dependabot&logoColor=white)](https://github.com/fluid-ecosystem/fluid-example/security/dependabot)

Runnable examples built against
[fluid-ecosystem/fluid-builder](https://github.com/fluid-ecosystem/fluid-builder),
each one a `Dockerfile` + `pom.xml` + plain `.java` files on top of
`maifeeulasad/fluid-builder:latest`, composed together with Docker Compose.

## Examples

| Module | Shows | Run |
|---|---|---|
| [example-send](example-send) / [example-receive](example-receive) | Minimal matched Kafka producer/consumer pair | `docker compose up --build` (default) |
| [example-advanced-send](example-advanced-send) / [example-advanced-receive](example-advanced-receive) | Full producer/consumer API: keys, batches, headers, dead letters, retries, custom partitioning | `docker compose --profile advanced up --build` |
| [example-postgres-jdbc](example-postgres-jdbc) | Database read/write, no ORM — raw `java.sql` against Postgres | `docker compose --profile postgres-jdbc up --build` |
| [example-h2](example-h2) | Database read/write, no ORM — embedded in-memory H2, no container | `docker compose --profile h2 up --build` |
| [example-mongo](example-mongo) | Database read/write against a document store (MongoDB), no DDL | `docker compose --profile mongo up --build` |
| [example-postgres-hibernate](example-postgres-hibernate) | Database read/write with an ORM — Hibernate entities and HQL against Postgres | `docker compose --profile postgres-hibernate up --build` |

All items tracked in [#14](https://github.com/fluid-ecosystem/fluid-example/issues/14) are now in.

## Libraries demonstrated

Each example adds exactly what it needs to its own `pom.xml`; nothing here
is bundled by `fluid-builder` itself — see its own README for what is.

| Library | Version | Used by |
|---|---|---|
| [`kafka-clients`](https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients) | 3.9.2 | every Kafka example |
| [`postgresql`](https://jdbc.postgresql.org/) (JDBC driver) | 42.7.13 | example-postgres-jdbc, example-postgres-hibernate |
| [`hibernate-core`](https://hibernate.org/orm/) | 6.6.4.Final | example-postgres-hibernate |
| [`h2`](https://www.h2database.com/) | 2.3.232 | example-h2 |
| [`mongodb-driver-sync`](https://www.mongodb.com/docs/drivers/java/sync/current/) | 4.11.1 | example-mongo |

## Quickstart

```bash
docker compose up --build
```

Brings up a single-node Kafka broker ([docker-compose.kafka.yaml](docker-compose.kafka.yaml))
and the default send/receive pair. See each module's README for what it
demonstrates and, where relevant, Kubernetes manifests.

## CI

Both workflows build `fluid-builder` from source rather than pulling the
published image, so a PR against either repo is tested against its own
changes:

- [`run-the-echosystem.yml`](.github/workflows/run-the-echosystem.yml) —
  brings up the default compose stack, asserts the expected Kafka message
  counts arrive.
- [`test-database-examples.yml`](.github/workflows/test-database-examples.yml) —
  one job per database example, each bringing up its own Compose profile
  and asserting its success line appears.
