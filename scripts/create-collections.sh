#!/bin/bash
set -e

COUCHBASE_HOST=${COUCHBASE_HOST:-127.0.0.1}
COUCHBASE_ADMINISTRATOR_USERNAME=${COUCHBASE_ADMINISTRATOR_USERNAME:-Administrator}
COUCHBASE_ADMINISTRATOR_PASSWORD=${COUCHBASE_ADMINISTRATOR_PASSWORD:-123456}
COUCHBASE_BUCKET=${COUCHBASE_BUCKET:-todo_list}

# Define collections to be created
declare -A COLLECTIONS=(
  ["user-scope"]="user-collection"
  ["task-scope"]="task-collection"
  ["invalid-token-scope"]="invalid-token-collection"
  ["log-scope"]="log-collection"
)

echo "Starting Couchbase collections setup process..."

# Wait for Couchbase Query Service to be ready
echo "Waiting for Couchbase Query Service..."
until curl -s http://$COUCHBASE_HOST:8093/admin/ping > /dev/null; do
  echo "Query Service is not ready yet. Retrying in 5 seconds..."
  sleep 5
done
echo "Query Service is ready."

# Iterate over the scopes and collections
for SCOPE in "${!COLLECTIONS[@]}"; do
  COLLECTION=${COLLECTIONS[$SCOPE]}
  echo "Creating collection '$COLLECTION' in scope '$SCOPE'..."
  curl -s -u $COUCHBASE_ADMINISTRATOR_USERNAME:$COUCHBASE_ADMINISTRATOR_PASSWORD \
    -X POST http://$COUCHBASE_HOST:8093/query/service \
    -d "statement=CREATE COLLECTION \`${COUCHBASE_BUCKET}\`.\`${SCOPE}\`.\`${COLLECTION}\`" \
    && echo "Collection '$COLLECTION' in scope '$SCOPE' created successfully."
done

echo "Couchbase collections setup process complete!"
