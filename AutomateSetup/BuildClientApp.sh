#! /bin/bash
#
# Copyright Ghareeb Falazi Rights Reserved
#
# SPDX-License-Identifier: Apache-2.0
#
# Exit on first error

set -e

printf "\n---Building Maven...---\n"

cd ../ClientApplication/scip-client-application
mvn package -DskipTests=true

printf "\n---Copying executables...---\n"

cd ..
cp -r frontend/dist ../../AutomateSetup/client-app/frontend/dist
cp -r backend/target ../../AutomateSetup/client-app/backend/target

printf "\n---DONE!---\n"