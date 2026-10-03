# fluid-example

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

More examples — Postgres with Hibernate (ORM), MongoDB, H2 — are tracked in
[#14](https://github.com/fluid-ecosystem/fluid-example/issues/14) and land
incrementally as their own modules.

## Quickstart

```bash
docker compose up --build
```

Brings up a single-node Kafka broker ([docker-compose.kafka.yaml](docker-compose.kafka.yaml))
and the default send/receive pair. See each module's README for what it
demonstrates and, where relevant, Kubernetes manifests.

## CI

[`.github/workflows/run-the-echosystem.yml`](.github/workflows/run-the-echosystem.yml)
builds `fluid-builder` from source (not the published image, so a PR tests
its own changes), brings up the default compose stack, and asserts the
expected message counts arrive.
