#!/bin/bash
set -e

COUCHBASE_HOST=${COUCHBASE_HOST}
COUCHBASE_USERNAME=${COUCHBASE_USERNAME}
COUCHBASE_PASSWORD=${COUCHBASE_PASSWORD}
COUCHBASE_BUCKET=${COUCHBASE_BUCKET}

INDEXES=(
  "CREATE PRIMARY INDEX \`primary_index\` ON \`${COUCHBASE_BUCKET}\`"
)

echo "Starting Couchbase index setup process..."

echo "Waiting for Couchbase Query Service..."
until curl -s http://$COUCHBASE_HOST:8093/admin/ping > /dev/null; do
  echo "Query Service is not ready yet. Retrying in 5 seconds..."
  sleep 5
done

for INDEX_QUERY in "${INDEXES[@]}"; do
  echo "Creating index 'INDEX_QUERY'..."
      cbq -e http://$COUCHBASE_HOST:8093 \
        -u "$COUCHBASE_USERNAME" \
        -p "$COUCHBASE_PASSWORD" \
        --script="$INDEX_QUERY"
      echo "Index '$INDEX_QUERY' created successfully!"
done

echo "Couchbase index setup complete!"
