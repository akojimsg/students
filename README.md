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

d. Repeat the same "students_test" database

c. View docker container address:


### Reference

1. [Medplum - Install on Ubuntu](https://www.medplum.com/docs/self-hosting/install-on-ubuntu)
2. [Mockito - Test cases](https://medium.com/javarevisited/implementing-unit-tests-for-a-spring-boot-api-that-uses-spring-data-jpa-and-postgresql-6e2e0880e5db)