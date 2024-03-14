.PHONY: build-docker-image
.PHONY: dev

MODULE = github.com/akojimsg/java/students
VERSION = v1.0.0

ifneq (,$(wildcard ./.env))
    include .env
    export
endif

docker-image-file:
	docker build -f Dockerfile -t registry.${MODULE}:${VERSION}-openjdk .;

docker-image-maven:
	mvn spring-boot:build-image -DskipTests -Dspring-boot.build-image.imageName=registry.${MODULE}:${VERSION}-mvn -DPOSTGRES_HOST=${POSTGRES_HOST}

start-local:
	mvn clean install
	mvn spring-boot:run

start-dev:
	docker-compose up --build
stop-dev:
	docker-compose stop

run-tests:
	mvn clean test

clean-docker-all:
	docker container prune -f
	docker image prune -a -f

start-postgres-service:
	sudo pg_ctlcluster 12 main start

stop-postgres-service:
	sudo pg_ctlcluster 12 main stop

run-postgres:
	docker run -d --name postgresdb --network app-network -e POSTGRES_PASSWORD="${POSTGRES_PASSWORD}" postgres