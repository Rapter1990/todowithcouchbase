import os
import time
import requests

class CouchbaseSetup:
    def __init__(self):
        self.host = os.getenv('COUCHBASE_HOST', 'localhost')
        self.admin_username = os.getenv('COUCHBASE_ADMINISTRATOR_USERNAME', 'Administrator')
        self.admin_password = os.getenv('COUCHBASE_ADMINISTRATOR_PASSWORD', '123456')
        self.bucket_name = os.getenv('COUCHBASE_BUCKET', 'todo_list')
        self.base_url = f"http://{self.host}:8091"
        self.query_url = f"http://{self.host}:8093/query/service"
        self.headers = {'Content-Type': 'application/x-www-form-urlencoded'}

    def wait_for_couchbase(self):
        print(f"Waiting for Couchbase to start on {self.host}:8091...")
        while True:
            try:
                response = requests.get(f"{self.base_url}/ui/index.html", timeout=5)
                if response.status_code == 200:
                    print("Couchbase is ready!")
                    break
            except requests.exceptions.RequestException:
                print("Couchbase is not ready yet. Retrying in 5 seconds...")
                time.sleep(5)

    def create_cluster(self):
        print("Initializing Couchbase Cluster...")
        url = f"{self.base_url}/pools/default"
        data = {
            'memoryQuota': 256,
            'indexMemoryQuota': 256
        }
        response = requests.post(url, auth=(self.admin_username, self.admin_password), data=data)
        if response.status_code == 200:
            print("Cluster setup completed successfully.")
        else:
            print(f"Failed to create cluster: {response.json()}")

    def create_bucket(self):
        print("Creating Couchbase Bucket...")
        url = f"{self.base_url}/pools/default/buckets"
        data = {
            'name': self.bucket_name,
            'bucketType': 'couchbase',
            'ramQuotaMB': 256
        }
        response = requests.post(url, auth=(self.admin_username, self.admin_password), data=data)
        if response.status_code == 202:
            print(f"Bucket '{self.bucket_name}' created successfully.")
        else:
            print(f"Failed to create bucket: {response.json()}")

    def create_scopes(self):
        print("Creating Scopes...")
        scopes = ['user-scope', 'task-scope', 'invalid-token-scope', 'log-scope']
        for scope in scopes:
            query = f"CREATE SCOPE `{self.bucket_name}`.`{scope}`"
            response = requests.post(self.query_url, auth=(self.admin_username, self.admin_password), data={'statement': query})
            if response.status_code == 200:
                print(f"Scope '{scope}' created successfully.")
            else:
                print(f"Failed to create scope '{scope}': {response.json()}")

    def create_collections(self):
        print("Creating Collections...")
        collections = {
            'user-scope': 'user-collection',
            'task-scope': 'task-collection',
            'invalid-token-scope': 'invalid-token-collection',
            'log-scope': 'log-collection'
        }
        for scope, collection in collections.items():
            query = f"CREATE COLLECTION `{self.bucket_name}`.`{scope}`.`{collection}`"
            response = requests.post(self.query_url, auth=(self.admin_username, self.admin_password), data={'statement': query})
            if response.status_code == 200:
                print(f"Collection '{collection}' in scope '{scope}' created successfully.")
            else:
                print(f"Failed to create collection '{collection}' in scope '{scope}': {response.json()}")

    def create_indexes(self):
        print("Creating Indexes...")
        indexes = [
            f"CREATE PRIMARY INDEX `primary_index` ON `{self.bucket_name}`"
        ]
        for index_query in indexes:
            response = requests.post(self.query_url, auth=(self.admin_username, self.admin_password), data={'statement': index_query})
            if response.status_code == 200:
                print(f"Index created successfully: {index_query}")
            else:
                print(f"Failed to create index: {response.json()}")

    def run(self):
        self.wait_for_couchbase()
        self.create_cluster()
        self.create_bucket()
        self.create_scopes()
        self.create_collections()
        self.create_indexes()
        print("Couchbase setup completed successfully.")


if __name__ == "__main__":
    setup = CouchbaseSetup()
    setup.run()
