# Use an official Go runtime as a parent image
FROM golang:1.21

# Set the working directory in the container
WORKDIR /app

# Copy the local source code to the container
COPY . .

# Build the Go application inside the container
RUN make build

# Expose a port (if your Go application listens on a specific port)
EXPOSE 8080

# Define the command to run your application
CMD ["./data_store", "-debug", "-initdict"]
