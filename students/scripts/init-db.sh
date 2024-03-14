#!/bin/bash
set -eux

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  \set dbsecret $DB_SECRET
  CREATE USER student_admin WITH PASSWORD ':dbsecret';
  CREATE DATABASE studentsdb;
  GRANT ALL PRIVILEGES ON DATABASE studentsdb TO student_admin;
  \c studentsdb
  GRANT ALL ON SCHEMA public TO student_admin;
EOSQL