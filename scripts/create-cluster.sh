#!/bin/bash

# Set up the Couchbase cluster
echo "Setting up Couchbase cluster..."
curl -v -X POST http://127.0.0.1:8091/pools/default \
  -d memoryQuota=256 \
  -d indexMemoryQuota=256



