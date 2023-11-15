## On the subject of migrations

Tool responsible for migrations:
```bash
$ go install -tags 'postgres' github.com/golang-migrate/migrate/v4/cmd/migrate@latest
```

Display version of last applied migration to databse:
```bash
$ migrate \
    -path ./db-client/migrations/ \
    -database postgres://${PG_STORE_USER}:${PG_STORE_PASSWORD}@${PG_STORE_HOST}:${PG_STORE_PORT}/${PG_STORE_DATABASE}?sslmode=disable
    version

$ migrate \
    -path ./db-client/migrations/ \
    -database postgres://postgres_user_env:postgres_password_env@localhost:5431/datastore?sslmode=disable version
```

Apply all "up" migrations:
```bash
$ migrate \
    -path ./db-client/migrations/ \
    -database ...
    up
```

Apply all "down" migrations:
```bash
$ migrate \
    -path ./db-client/migrations/ \
    -database ...
    down
```

### On the subject of adding new migrations
As per documentation:
```bash
$ create [-ext E] [-dir D] [-seq] [-digits N] [-format] [-tz] NAME
           Create a set of timestamped up/down migrations titled NAME, in directory D with extension E.
           Use -seq option to generate sequential up/down migrations with N digits.
           Use -format option to specify a Go time format string. Note: migrations with the same time cause "duplicate migration version" error.
           Use -tz option to specify the timezone that will be used when generating non-sequential migrations (defaults: UTC).
```



## Dictionary Data & Mock Data
### Assessors and submissions
 | submission_id | year |     name     | duration_days | assessor_id | ipma_expert_id | person_id |             email             |
 |-------------- |----- | -----| ----- | ----- | ----- |----- | -----|
 |             1 | 2023 | EcoGlobe     |            45 |           1 |              1 |        21 | orlando.palladino@email.com|
 |             1 | 2023 | EcoGlobe     |            45 |           2 |              2 |        22 | palmina.perugino@email.com|
 |             2 | 2023 | AquaBotics   |            60 |           3 |              3 |        23 | pompeo.pieroni@email.com|
 |             2 | 2023 | AquaBotics   |            60 |           4 |              4 |        24 | roberto.romani@email.com|
 |             3 | 2023 | SolarScape   |            30 |           5 |              5 |        25 | romualdo.rossellini@email.com|
 |             3 | 2023 | SolarScape   |            30 |           6 |              6 |        26 | rufino.santoro@email.com|
 |             4 | 2022 | UrbanHarmony |            55 |           7 |              7 |        27 | silvano.santucci@email.com|
 |             4 | 2022 | UrbanHarmony |            55 |           8 |              8 |        28 | teodora.sartore@email.com|
 |             5 | 2022 | BioWonders   |            40 |           9 |              9 |        29 | tommaso.savonarola@email.com|
 |             5 | 2022 | BioWonders   |            40 |          10 |             10 |        30 | umberto.sforza@email.com|
 |             6 | 2022 | SpaceVoyage  |            25 |           1 |              1 |        21 | orlando.palladino@email.com|
 |             6 | 2022 | SpaceVoyage  |            25 |           2 |              2 |        22 | palmina.perugino@email.com|
 |             7 | 2021 | FutureFarms  |            35 |           3 |              3 |        23 | pompeo.pieroni@email.com|
 |             7 | 2021 | FutureFarms  |            35 |           4 |              4 |        24 | roberto.romani@email.com|
 |             8 | 2021 | TechNest     |            50 |           5 |              5 |        25 | romualdo.rossellini@email.com|
 |             8 | 2021 | TechNest     |            50 |           6 |              6 |        26 | rufino.santoro@email.com|
 |             9 | 2021 | ArtisanCraft |            65 |           7 |              7 |        27 | silvano.santucci@email.com|
 |             9 | 2021 | ArtisanCraft |            65 |           8 |              8 |        28 | teodora.sartore@email.com|
 |             9 | 2021 | ArtisanCraft |            65 |           9 |              9 |        29 | tommaso.savonarola@email.com|
