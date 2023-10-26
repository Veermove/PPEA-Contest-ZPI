# Use an official Go runtime as a parent image
FROM golang:1.21

# Set the working directory in the container
WORKDIR /app

# Copy the local source code to the container
COPY . .

ENV PG_STORE_USER=postgres
ENV PG_STORE_PASSWORD=postgres
ENV PG_STORE_HOST=localhost
ENV PG_STORE_PORT=5432
ENV PG_STORE_DATABASE=datastore

# Build the Go application inside the container
RUN make build

# Expose a port (if your Go application listens on a specific port)
EXPOSE 8080

# Define the command to run your application
CMD ["./data_store"]
