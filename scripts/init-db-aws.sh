#!/bin/bash
set -eux

psql -v ON_ERROR_STOP=1 -U "$AWS_PG_MASTER_USER" -h "$AWS_PG_ENDPOINT" <<-EOSQL
  \set dbsecret $DB_SECRET
  DROP DATABASE IF EXISTS studentsdb;
  DROP USER IF EXISTS student_admin;
  CREATE USER student_admin WITH PASSWORD :'dbsecret';
  CREATE DATABASE studentsdb;
  GRANT ALL PRIVILEGES ON DATABASE studentsdb TO student_admin;
  \c studentsdb
  GRANT ALL ON SCHEMA public TO student_admin;
EOSQL