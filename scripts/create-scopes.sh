#!/bin/bash
set -e

COUCHBASE_HOST=${COUCHBASE_HOST}
COUCHBASE_USERNAME=${COUCHBASE_USERNAME}
COUCHBASE_PASSWORD=${COUCHBASE_PASSWORD}
COUCHBASE_BUCKET=${COUCHBASE_BUCKET}
COUCHBASE_SCOPES=${COUCHBASE_SCOPES}

echo "Starting Couchbase scope creation process..."

IFS=',' read -ra SCOPES_ARRAY <<< "$COUCHBASE_SCOPES"
for SCOPE in "${SCOPES_ARRAY[@]}"; do
  echo "Checking if scope '$SCOPE' exists..."
  if cbq -e $COUCHBASE_HOST:8093 \
    -u "$COUCHBASE_USERNAME" \
    -p "$COUCHBASE_PASSWORD" \
    --script="SELECT RAW scope_name FROM system:scopes WHERE bucket_id='$COUCHBASE_BUCKET' AND scope_name='$SCOPE';" | grep -q "\"$SCOPE\""; then
    echo "Scope '$SCOPE' already exists. Skipping creation."
  else
    echo "Creating scope '$SCOPE'..."
    cbq -e $COUCHBASE_HOST:8093 \
      -u "$COUCHBASE_USERNAME" \
      -p "$COUCHBASE_PASSWORD" \
      --script="CREATE SCOPE \`$COUCHBASE_BUCKET\`.\`$SCOPE\`;"
    echo "Scope '$SCOPE' created successfully!"
  fi
done

echo "Couchbase scope creation process complete!"
