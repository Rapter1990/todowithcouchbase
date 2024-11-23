#!/bin/bash

# Configure services
echo "Configure services..."
curl -v http://127.0.0.1:8091/node/controller/setupServices \
  -d services=kv%2Cn1ql%2Cindex

echo "Configuring administrator settings..."
curl -v http://127.0.0.1:8091/settings/web \
  -d port=8091 \
  -d username=${COUCHBASE_ADMINISTRATOR_USERNAME:-Administrator} \
  -d password=${COUCHBASE_ADMINISTRATOR_PASSWORD:-123456}