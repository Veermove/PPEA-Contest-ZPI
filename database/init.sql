-- Create a new database
CREATE DATABASE store;

-- Connect to the new database
\c store;

-- Create a new user with a password
CREATE USER keeper WITH ENCRYPTED PASSWORD 'admin';

-- Grant privileges to the user for the database
GRANT ALL PRIVILEGES ON DATABASE store TO keeper;
