#!/usr/bin/env sh
docker --log-level "info" compose down --remove-orphans
#docker --log-level "info" system prune --all --force
docker images ls
docker ps --all
#./gradlew clean
./gradlew bootBuildImage --info
./build-docker-ssh-keygen.sh
#docker --log-level "info" compose build --no-cache --pull
docker --log-level "info" compose build
docker images ls
docker --log-level "info" compose up --force-recreate --detach
docker ps --all
