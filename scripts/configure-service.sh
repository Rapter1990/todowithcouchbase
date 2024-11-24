#!/bin/bash
set -e

COUCHBASE_HOST=${COUCHBASE_HOST:-127.0.0.1}
COUCHBASE_ADMINISTRATOR_USERNAME=${COUCHBASE_ADMINISTRATOR_USERNAME:-Administrator}
COUCHBASE_ADMINISTRATOR_PASSWORD=${COUCHBASE_ADMINISTRATOR_PASSWORD:-123456}

echo "Starting Couchbase service configuration..."

# Wait for Couchbase Server to be ready
echo "Waiting for Couchbase Server..."
until curl -s -o /dev/null -w "%{http_code}" http://$COUCHBASE_HOST:8091 > /dev/null; do
  echo "Couchbase Server is not ready yet. Retrying in 5 seconds..."
  sleep 5
done
echo "Couchbase Server is ready."

# Configure services
echo "Configuring Couchbase services..."
curl -s -X POST http://$COUCHBASE_HOST:8091/node/controller/setupServices \
  -d "services=kv%2Cn1ql%2Cindex" \
  && echo "Services configured successfully."

# Configure administrator settings
echo "Configuring Couchbase administrator settings..."
curl -s -X POST http://$COUCHBASE_HOST:8091/settings/web \
  -d "port=8091" \
  -d "username=$COUCHBASE_ADMINISTRATOR_USERNAME" \
  -d "password=$COUCHBASE_ADMINISTRATOR_PASSWORD" \
  && echo "Administrator settings configured successfully."

echo "Couchbase service configuration complete!"
