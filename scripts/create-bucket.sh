#!/bin/bash
set -e

COUCHBASE_HOST=${COUCHBASE_HOST:-127.0.0.1}
COUCHBASE_ADMINISTRATOR_USERNAME=${COUCHBASE_ADMINISTRATOR_USERNAME:-Administrator}
COUCHBASE_ADMINISTRATOR_PASSWORD=${COUCHBASE_ADMINISTRATOR_PASSWORD:-123456}
COUCHBASE_BUCKET=${COUCHBASE_BUCKET:-todo_list}
RAM_QUOTA_MB=${RAM_QUOTA_MB:-100}
BUCKET_TYPE=${BUCKET_TYPE:-couchbase}

echo "Starting Couchbase bucket creation process..."

# Wait for Couchbase Server to be ready
echo "Waiting for Couchbase Server..."
until curl -s -o /dev/null -w "%{http_code}" http://$COUCHBASE_HOST:8091 > /dev/null; do
  echo "Couchbase Server is not ready yet. Retrying in 5 seconds..."
  sleep 5
done
echo "Couchbase Server is ready."

# Create the bucket
echo "Creating bucket '$COUCHBASE_BUCKET'..."
curl -s -u $COUCHBASE_ADMINISTRATOR_USERNAME:$COUCHBASE_ADMINISTRATOR_PASSWORD \
  -X POST http://$COUCHBASE_HOST:8091/pools/default/buckets \
  -d name=$COUCHBASE_BUCKET \
  -d bucketType=$BUCKET_TYPE \
  -d ramQuotaMB=$RAM_QUOTA_MB \
  && echo "Bucket '$COUCHBASE_BUCKET' created successfully."

echo "Couchbase bucket creation process complete!"

