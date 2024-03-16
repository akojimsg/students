### Notes

Postgres Commands

a. Start/Stop postgres:
```
sudo pg_ctlcluster 12 main start/stop
```

b. Create a user:
```
CREATE USER user WITH PASSWORD password;
```

c. Create a "students" database and grant necessary permissions
```
CREATE DATABASE students;
GRANT ALL PRIVILEGES ON DATABASE students TO user;
\c students
GRANT ALL ON SCHEMA public TO user;
```

d. View docker container address:
```
docker inspect \
  -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' container_name_or_id
  
docker inspect \
  --format '{{ .NetworkSettings.IPAddress }}' container_name_or_id
```

e. Read .env to terminal
```
$ export $(grep -v '^#' .env | xargs) && env
... other environment variables ...
VAR1=hello
VAR2=world
VAR3=thisIsMyEnvFile
... other environment variables ...
```

### Reference

1. [Medplum - Install on Ubuntu](https://www.medplum.com/docs/self-hosting/install-on-ubuntu)
2. [Mockito - Test cases](https://medium.com/javarevisited/implementing-unit-tests-for-a-spring-boot-api-that-uses-spring-data-jpa-and-postgresql-6e2e0880e5db)
3. [https://tldp.org/LDP/abs/html/index.html](https://tldp.org/LDP/abs/html/index.html)
4. [Postgres Environment variables](https://www.postgresql.org/docs/current/libpq-envars.html)