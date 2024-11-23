#!/bin/bash

# Create scopes
echo "Creating scopes..."

# User Scope
curl -v -u ${COUCHBASE_ADMINISTRATOR_USERNAME:-Administrator}:${COUCHBASE_ADMINISTRATOR_PASSWORD:-123456} \
  -X POST http://127.0.0.1:8091/query/service \
  -d "statement=CREATE SCOPE ${COUCHBASE_BUCKET:-todo_list}.user-scope"

# Task Scope
curl -v -u ${COUCHBASE_ADMINISTRATOR_USERNAME:-Administrator}:${COUCHBASE_ADMINISTRATOR_PASSWORD:-123456} \
  -X POST http://127.0.0.1:8091/query/service \
  -d "statement=CREATE SCOPE ${COUCHBASE_BUCKET:-todo_list}.task-scope"

# Invalid Token Scope
curl -v -u ${COUCHBASE_ADMINISTRATOR_USERNAME:-Administrator}:${COUCHBASE_ADMINISTRATOR_PASSWORD:-123456} \
  -X POST http://127.0.0.1:8091/query/service \
  -d "statement=CREATE SCOPE ${COUCHBASE_BUCKET:-todo_list}.invalid-token-scope"

# Log Scope
curl -v -u ${COUCHBASE_ADMINISTRATOR_USERNAME:-Administrator}:${COUCHBASE_ADMINISTRATOR_PASSWORD:-123456} \
  -X POST http://127.0.0.1:8091/query/service \
  -d "statement=CREATE SCOPE ${COUCHBASE_BUCKET:-todo_list}.log-scope"