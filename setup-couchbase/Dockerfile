FROM python:3.9-slim

# Set environment variables to ensure the container runs without buffering and uses UTF-8
ENV PYTHONUNBUFFERED=1
ENV PYTHONDONTWRITEBYTECODE=1

# Set environment variables for Couchbase
ENV COUCHBASE_ADMINISTRATOR_USERNAME=Administrator
ENV COUCHBASE_ADMINISTRATOR_PASSWORD=123456
ENV COUCHBASE_BUCKET=todo_list
ENV COUCHBASE_HOST=couchbase

# Set the working directory inside the container
WORKDIR /app

# Copy the Python script into the container
COPY main.py /app/main.py

# Install necessary Python packages
RUN pip install --no-cache-dir requests

# Command to run the Python script
CMD ["python", "/app/main.py"]
