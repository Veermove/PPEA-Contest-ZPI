# ZPI Project

## Run with docker compose
* Make sure that you have docker and docker compose installed.
* Make sure that you created .env file in the root directory of the project, which contains all the environment variables. Follow the .env.example file.
* Set up following environemnt variables:
     * POSTGRES_USER= value same as in .env file
     * POSTGRES_PASSWORD= value same as in .env file
     * POSTGRES_DB= value same as in .env file

This is due to fact when `db service` is starting it is creating database with name from `POSTGRES_DB` variable and user with name from `POSTGRES_USER` variable and password from `POSTGRES_PASSWORD` variable. And it does not read it from .env file.

* Make sure that `frontend` will be resolved to your localhost address by the operating system

Run the following command in the root directory of the project:
```bash
$ docker compose up -d
```

If you need to modify specific dockerfiles remember to rebuild the images:
```bash
$ docker compose up -d --build
```
or:
```bash
$ docker compose build
$ docker compose up -d
```

To stop the containers use:
```bash
$ docker compose down
```

If you need to completely remove the containers and reset database use:
```bash
$ docker compose down --volumes
```

### Connect to database
```bash
docker exec -ti pgstore psql -d datastore -U postgres
```
Remember to use the user and password from .env file.

### Basic psql commands
* To quit the psql:
```bash
\q
```

* List all databases in the PostgreSQL database server

```bash
\l
```

* List all schemas:

```bash
\dn
```

* List all stored procedures and functions:

```bash
\df
```

List all views:

```bash
\dv
```

Lists all tables in a current database for schema
```bash
\dt schema_name.
```

Or to get more information on tables in the current database:

```bash
\dt+ schema_name.
```

Get detailed information on a table.

```bash
\d+ schema_name.table_name
```

Show query output in the pretty-format:

```bash
\x
```

List all users:

```bash
\du
```

Create a new role:
```sql
CREATE ROLE role_name;
```

Create a new role with a username and password:
```sql
CREATE ROLE username NOINHERIT LOGIN PASSWORD password;
```

Change role for the current session to the new_role:
```sql
SET ROLE new_role;
```

Allow role_1 to set its role as role_2:
```sql
GRANT role_2 TO role_1;
```
