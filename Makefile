.PHONY: docker-image-file
.PHONY: deploy

MODULE = github.com/akojimsg/java/students
VERSION = v1.0.0
TAG = students-0.0.1-SNAPSHOT
EBS_ENVIRONMENT = students-api-e1
EBS_DOCKER = students-docker-e1

ifneq (,$(wildcard ./.env))
    include .env
    export
endif

create-ebs-env:
	eb create ${EBS_ENVIRONMENT} --source target/${TAG}.jar --version ${TAG}

create-docker-env:
	eb create ${EBS_DOCKER} --version ${TAG} --single

deploy-ebs-ec2:
	eb use ${EBS_ENVIRONMENT}
	mvn clean package spring-boot:repackage
	eb setenv POSTGRES_HOST=${AWS_PG_ENDPOINT} DB=${DB} DB_USER=${DB_USER} DB_SECRET=${DB_SECRET} DB_PORT=5432 JWT_SECRET=${JWT_SECRET} SERVER_PORT=5000
	eb deploy --version ${TAG}

deploy-ebs-docker:
	eb use ${EBS_DOCKER}
	eb setenv POSTGRES_HOST=${AWS_PG_ENDPOINT} DB=${DB} DB_USER=${DB_USER} DB_SECRET=${DB_SECRET} DB_PORT=5432 JWT_SECRET=${JWT_SECRET}
	eb deploy --version ${TAG}

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

init-postgres:
	# docker run -d --name pgrunner2 --network app-network -e POSTGRES_PASSWORD="${POSTGRES_PASSWORD}" postgres
	docker run --name pgrunner2 -e POSTGRES_PASSWORD="${POSTGRES_SECRET}" postgres

config-postgres-aws:
	export PGPASSWORD=${AWS_PG_SECRET} && ./scripts/init-db-aws.sh

eb-docker-local:
	eb local run --port 5000

eb-version:
	eb version