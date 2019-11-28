#!/bin/bash
#
# Copyright Ghareeb Falazi Rights Reserved
#
# SPDX-License-Identifier: Apache-2.0
#
# Exit on first error
# Obtain CONTAINER_IDS and remove them
# TODO Might want to make this optional - could clear other containers

set -e

echo "---Copying Ethereum smart contract to working directory of ganache-cli docker images...---"
cp -r ../SmartContracts/Ethereum/Digest ./ganache-cli/Digest

echo "---Bringing up docker images...---"
docker-compose up -d

sleep 1
echo "---Sleeping 10s to allow blockchains to complete booting...---"
sleep 9

set -x
echo "---Deploying the Digest smart contract on the ganache Ethereum simulator...---"
docker exec \
  ganache-with-truffle \
  truffle migrate

echo "---Installing the Energy Management System Fabric smart contract on peer0.org1.example.com---"
docker exec \
    cli \
    scripts/script.sh
    
set -e
echo -e "\n---Cleaning up...---\n"

rm -rf ./ganache-cli/Digest

echo -e "\n-----------"
echo -e "\n---DONE!---"
echo -e "\n-----------"