#!/bin/bash
set -e

COUCHBASE_HOST=${COUCHBASE_HOST:-127.0.0.1}
COUCHBASE_ADMINISTRATOR_USERNAME=${COUCHBASE_ADMINISTRATOR_USERNAME:-Administrator}
COUCHBASE_ADMINISTRATOR_PASSWORD=${COUCHBASE_ADMINISTRATOR_PASSWORD:-123456}
COUCHBASE_BUCKET=${COUCHBASE_BUCKET:-todo_list}

# Define scopes to be created
SCOPES=(
  "user-scope"
  "task-scope"
  "invalid-token-scope"
  "log-scope"
)

echo "Starting Couchbase scopes setup process..."

# Wait for Couchbase Query Service to be ready
echo "Waiting for Couchbase Query Service..."
until curl -s http://$COUCHBASE_HOST:8093/admin/ping > /dev/null; do
  echo "Query Service is not ready yet. Retrying in 5 seconds..."
  sleep 5
done
echo "Query Service is ready."

# Iterate over the scopes
for SCOPE in "${SCOPES[@]}"; do
  echo "Creating scope '$SCOPE'..."
  curl -s -u $COUCHBASE_ADMINISTRATOR_USERNAME:$COUCHBASE_ADMINISTRATOR_PASSWORD \
    -X POST http://$COUCHBASE_HOST:8093/query/service \
    -d "statement=CREATE SCOPE \`${COUCHBASE_BUCKET}\`.\`${SCOPE}\`" \
    && echo "Scope '$SCOPE' created successfully."
done

echo "Couchbase scopes setup process complete!"