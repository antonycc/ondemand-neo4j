#!/usr/bin/env bash
# On-demand Neo4j is an exploration of Neo4j with deployment to AWS
# Copyright (C) 2022  Antony Cartwright, Polycode Limited
#
# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at https://mozilla.org/MPL/2.0/.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# Mozilla Public License, v. 2.0 for more details.
#
# Purpose: Run neo4j locally with a browser: http://localhost:7474/browser/
# Usage:
#    ./run-neo4j-in-docker.sh

#docker run --interactive --tty --mount type=bind,source="$(pwd)",target=/workspace eclipse-temurin:18-jdk bash -c 'cd /workspace && ./gradlew clean build -x test'
docker compose up
