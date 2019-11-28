# Smart Contract Invocation Protocol (SCIP) Case Study

## Introduction

A case-study that shows how the [SCIP protocol](https://github.com/lampajr/scip) can be used in a heterogeneous multi-blockchain setup.

The following picture highlights the architecture of the distributed system that is invloved in the case study:
![Arch.png](Arch.png)

We will create the various components of this system, bring it up and then run a sample the client application against it.
The sample client application is described by the following figure:

![CaseStudy](CaseStudy.png)

## Prerequisits

- Active internet connection :)
- Docker with Docker-Compose
- Git bash (for Windows users)

## Instructions

- The following instructions assume you are using Windows. Otherwise, ignore the requirement that you need to run the commands using git bash, since bash will be readily available for you.
- Using git bash, navigate to the `AutomateSetup` directory, and run the shell script `Start.sh`, i.e., 
  ```
  > cd AutomateSetup
  > ./Start.sh
  ```
   This will pull/create the necessary docker images, execute the docker-compose file to bring up the various docker containers needed, and deploy the Ethereum and Fabric smart contracts.
- At the end, you can bring down the network with the command 

  ```
  > ./Stop.sh
  ```

  You can also remove BAL and ganache-cli images if you use the command 
  
  ```
  > ./Stop.sh rmi
  ``` 
  
  instead

## Notes

- The crpyto artifacts for Fabric are already generated. If you need to generate them again use the script [generate.sh](./AutomateSetup/fabric/_defaults-generation/generate.sh). Then you need to update the keyfile name mentioned inside the [docker-compose file](./AutomateSetup/docker-compose.yml) (in the `ca` service specification).
