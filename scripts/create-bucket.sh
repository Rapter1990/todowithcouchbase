#!/bin/bash

# Create the bucket
echo "Creating bucket ${COUCHBASE_BUCKET:-todo_list}..."
curl -v -u ${COUCHBASE_ADMINISTRATOR_USERNAME:-Administrator}:${COUCHBASE_ADMINISTRATOR_PASSWORD:-123456} \
  -X POST http://127.0.0.1:8091/pools/default/buckets \
  -d name=${COUCHBASE_BUCKET:-todo_list} \
  -d bucketType=couchbase \
  -d ramQuotaMB=100
