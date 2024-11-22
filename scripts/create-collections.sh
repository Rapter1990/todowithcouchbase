#!/bin/bash
set -e

COUCHBASE_HOST=${COUCHBASE_HOST}
COUCHBASE_USERNAME=${COUCHBASE_USERNAME}
COUCHBASE_PASSWORD=${COUCHBASE_PASSWORD}
COUCHBASE_BUCKET=${COUCHBASE_BUCKET}
COUCHBASE_COLLECTIONS=${COUCHBASE_COLLECTIONS}

echo "Starting collection creation process..."

IFS=',' read -ra COLLECTIONS_ARRAY <<< "$COUCHBASE_COLLECTIONS"
for COLLECTION in "${COLLECTIONS_ARRAY[@]}"; do
  SCOPE_NAME=$(echo "$COLLECTION" | cut -d '.' -f 1)
  COLLECTION_NAME=$(echo "$COLLECTION" | cut -d '.' -f 2)

  echo "Creating collection: $COLLECTION_NAME under scope: $SCOPE_NAME"
  cbq -e http://$COUCHBASE_HOST:8093 \
    -u "$COUCHBASE_USERNAME" \
    -p "$COUCHBASE_PASSWORD" \
    --script="CREATE COLLECTION \`$COUCHBASE_BUCKET\`.\`$SCOPE_NAME\`.\`$COLLECTION_NAME\`;" || echo "Collection '$COLLECTION_NAME' already exists in scope '$SCOPE_NAME'. Skipping."
done

echo "Collection creation process complete!"
