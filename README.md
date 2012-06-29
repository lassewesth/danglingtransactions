Dangling Transactions
====================

This little project has two tools: one to create a corrupted database, and one to test what happens when you restart the database and let it recover. See danglingtransactions.DatabaseCorruptor and danglingtransactions.DatabaseInspector, respectively. Run one after the other, and observe e.g.:

```$ java danglingtransactions.DatabaseCorruptor
Database is corrupt!
#valid nodes: 178
#invalid nodes: 26

$ java danglingtransactions.DatabaseInspector
Database is healthy
#valid nodes: 178
#invalid nodes: 0```