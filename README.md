# ZPI Project
## Polish Project Excellence Award
Implementation of basic functions.

## Instruction for database

### Prerequisites
- docker
- docker-compose
- create `.env` file with akin to `.env.schema`
### Datastore
Start postgresql database using docker compose plugin
```bash
$ docker compose up
```
or using docker-compose
```bash
$ docker-compose up
```

### Connect to database
```bash
docker exec -ti pgstore /usr/local/bin/psql -d datastore -U postgres
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
