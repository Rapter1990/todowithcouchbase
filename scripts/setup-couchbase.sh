#!/bin/bash

# Function to check Couchbase readiness
wait_for_couchbase() {
  echo "Waiting for Couchbase to start on port 8091..."
  until curl -s -o /dev/null -w "%{http_code}" http://127.0.0.1:8091/ui/index.html | grep -q "200"; do
    sleep 5
    echo "Still waiting for Couchbase..."
  done
  echo "Couchbase is ready!"
}

# Wait for Couchbase to start
wait_for_couchbase
