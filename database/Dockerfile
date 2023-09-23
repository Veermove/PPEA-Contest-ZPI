# Use the official PostgreSQL image as the base image
FROM postgres:latest

# Environment variables for PostgreSQL
ENV POSTGRES_USER myuser
ENV POSTGRES_PASSWORD mypassword
ENV POSTGRES_DB mydb

# Copy SQL scripts into the container
COPY init.sql /docker-entrypoint-initdb.d/
