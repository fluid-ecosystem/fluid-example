# example-advanced-send / example-advanced-receive

The fuller surface of Fluid's producer and consumer API, moved here from
`fluid-builder/examples/` (where it was excluded from the shipped artifact
and only ever compile-checked, never run).

Unlike `example-send`/`example-receive`, this is **not** a matched pair —
`example-advanced-send` publishes to `demo-*` topics to demonstrate keyed
partitioning, explicit partitions, batching, headers and error handling;
`example-advanced-receive` subscribes to a different set of topics
(`orders`, `batch-orders`, `priority-orders`, `customer-events`) to
demonstrate `@KafkaSubscription`'s full tuning surface: batch consumption,
dead-letter routing, retries, sticky partition assignment and custom key
extraction. Run it to see the options exercised, not to see messages flow
end to end.

## Run it

```bash
docker compose --profile advanced up --build
```

It is opt-in (the `advanced` profile) so the default `docker compose up`
used by CI and the quickstart stays limited to the simple send/receive pair.

See the root [README](../README.md) for the full list of examples and the
shared Kafka broker they all run against.
