#!/bin/bash

# Create collections
echo "Creating collections..."

# User Collection
curl -v -u ${COUCHBASE_ADMINISTRATOR_USERNAME:-Administrator}:${COUCHBASE_ADMINISTRATOR_PASSWORD:-123456} \
  -X POST http://127.0.0.1:8091/query/service \
  -d "statement=CREATE COLLECTION ${COUCHBASE_BUCKET:-todo_list}.user-scope.user-collection"

# Task Collection
curl -v -u ${COUCHBASE_ADMINISTRATOR_USERNAME:-Administrator}:${COUCHBASE_ADMINISTRATOR_PASSWORD:-123456} \
  -X POST http://127.0.0.1:8091/query/service \
  -d "statement=CREATE COLLECTION ${COUCHBASE_BUCKET:-todo_list}.task-scope.task-collection"

# Invalid Token Collection
curl -v -u ${COUCHBASE_ADMINISTRATOR_USERNAME:-Administrator}:${COUCHBASE_ADMINISTRATOR_PASSWORD:-123456} \
  -X POST http://127.0.0.1:8091/query/service \
  -d "statement=CREATE COLLECTION ${COUCHBASE_BUCKET:-todo_list}.invalid-token-scope.invalid-token-collection"

# Log Collection
curl -v -u ${COUCHBASE_ADMINISTRATOR_USERNAME:-Administrator}:${COUCHBASE_ADMINISTRATOR_PASSWORD:-123456} \
  -X POST http://127.0.0.1:8091/query/service \
  -d "statement=CREATE COLLECTION ${COUCHBASE_BUCKET:-todo_list}.log-scope.log-collection"

echo "Couchbase cluster, scopes, and collections setup complete."