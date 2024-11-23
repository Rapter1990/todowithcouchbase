#!/bin/bash
set -e

COUCHBASE_HOST=${COUCHBASE_HOST:-127.0.0.1}
COUCHBASE_USERNAME=${COUCHBASE_USERNAME:-Administrator}
COUCHBASE_PASSWORD=${COUCHBASE_PASSWORD:-123456}
COUCHBASE_BUCKET=${COUCHBASE_BUCKET:-todo_list}
INDEXER_STORAGE_MODE=${INDEXER_STORAGE_MODE:-plasma}

INDEXES=(
  "CREATE PRIMARY INDEX \`primary_index\` ON \`${COUCHBASE_BUCKET}\`"
)

echo "Starting Couchbase index setup process..."

# Wait for Couchbase Query Service to be ready
echo "Waiting for Couchbase Query Service..."
until curl -s http://$COUCHBASE_HOST:8093/admin/ping > /dev/null; do
  echo "Query Service is not ready yet. Retrying in 5 seconds..."
  sleep 5
done

# Set Indexer Storage Mode
echo "Setting indexer storage mode to $INDEXER_STORAGE_MODE..."
curl -s -u "$COUCHBASE_USERNAME:$COUCHBASE_PASSWORD" \
  -X POST http://$COUCHBASE_HOST:8091/settings/indexes \
  -d "storageMode=$INDEXER_STORAGE_MODE" \
  && echo "Indexer storage mode set successfully."

# Create indexes
for INDEX_QUERY in "${INDEXES[@]}"; do
  echo "Creating index: $INDEX_QUERY"
  cbq -e http://$COUCHBASE_HOST:8093 \
    -u "$COUCHBASE_USERNAME" \
    -p "$COUCHBASE_PASSWORD" \
    --script="$INDEX_QUERY"
  echo "Index created successfully: $INDEX_QUERY"
done

echo "Couchbase index setup complete!"
