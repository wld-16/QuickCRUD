#!/bin/bash
docker exec -it integration-db bash -c "sh /docker-entrypoint-initdb.d/dropTables.sh"
