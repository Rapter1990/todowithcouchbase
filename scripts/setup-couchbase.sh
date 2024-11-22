#!/bin/bash
set -e

COUCHBASE_HOST=${COUCHBASE_HOST}
COUCHBASE_USERNAME=${COUCHBASE_USERNAME}
COUCHBASE_PASSWORD=${COUCHBASE_PASSWORD}
COUCHBASE_BUCKET=${COUCHBASE_BUCKET}

echo "Starting Couchbase setup process..."

echo "Waiting for Couchbase to start..."
until curl -s http://$COUCHBASE_HOST:8091/ui/index.html > /dev/null; do
  echo "Couchbase is not ready yet. Retrying in 5 seconds..."
  sleep 5
done

echo "Initializing Couchbase Cluster..."
couchbase-cli cluster-init -c $COUCHBASE_HOST:8091 \
  --cluster-username="$COUCHBASE_USERNAME" \
  --cluster-password="$COUCHBASE_PASSWORD" \
  --services=data,index,query,fts \
  --cluster-ramsize=512 \
  --cluster-index-ramsize=256 || echo "Cluster already initialized, skipping."

echo "Checking if bucket '$COUCHBASE_BUCKET' exists..."
if couchbase-cli bucket-list -c $COUCHBASE_HOST:8091 \
  --username="$COUCHBASE_USERNAME" \
  --password="$COUCHBASE_PASSWORD" | grep -q "$COUCHBASE_BUCKET"; then
  echo "Bucket '$COUCHBASE_BUCKET' already exists. Skipping creation."
else
  echo "Creating bucket '$COUCHBASE_BUCKET'..."
  couchbase-cli bucket-create -c $COUCHBASE_HOST:8091 \
    --username="$COUCHBASE_USERNAME" \
    --password="$COUCHBASE_PASSWORD" \
    --bucket="$COUCHBASE_BUCKET" \
    --bucket-type=couchbase \
    --bucket-ramsize=256 \
    --wait
  echo "Bucket '$COUCHBASE_BUCKET' created successfully!"
fi

echo "Couchbase setup complete!"
