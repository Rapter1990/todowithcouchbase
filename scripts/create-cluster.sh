#!/bin/bash
set -e

COUCHBASE_HOST=${COUCHBASE_HOST:-127.0.0.1}
COUCHBASE_ADMINISTRATOR_USERNAME=${COUCHBASE_ADMINISTRATOR_USERNAME:-Administrator}
COUCHBASE_ADMINISTRATOR_PASSWORD=${COUCHBASE_ADMINISTRATOR_PASSWORD:-123456}
MEMORY_QUOTA_MB=${MEMORY_QUOTA_MB:-256}
INDEX_MEMORY_QUOTA_MB=${INDEX_MEMORY_QUOTA_MB:-256}

echo "Starting Couchbase cluster setup process..."

# Wait for Couchbase Server to be ready
echo "Waiting for Couchbase Server..."
until curl -s -o /dev/null -w "%{http_code}" http://$COUCHBASE_HOST:8091 > /dev/null; do
  echo "Couchbase Server is not ready yet. Retrying in 5 seconds..."
  sleep 5
done
echo "Couchbase Server is ready."

# Configure the cluster
echo "Setting up Couchbase cluster with memory quota..."
curl -s -u $COUCHBASE_ADMINISTRATOR_USERNAME:$COUCHBASE_ADMINISTRATOR_PASSWORD \
  -X POST http://$COUCHBASE_HOST:8091/pools/default \
  -d memoryQuota=$MEMORY_QUOTA_MB \
  -d indexMemoryQuota=$INDEX_MEMORY_QUOTA_MB \
  && echo "Cluster setup completed successfully."

echo "Couchbase cluster setup process complete!"



