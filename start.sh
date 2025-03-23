#!/bin/bash
set -x

mkdir -p /Users/alinstefanescu/workspaces/jenkins_config
#mkdir -p /workspaces/jenkins_config
docker compose --profile mongo --profile hello-service up -d 