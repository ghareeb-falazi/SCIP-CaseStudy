# SCIP-CaseStudy
A case-study that shows how the SCIP protocol can be used in a heterogeneous multi-blockchain setup.

## Notes
- The crpyto artifacts for Fabric are already generated. If you need to generate them again use the script [generate.sh](./AutomateSetup/fabric/_defaults-generation/generate.sh). Then you need to update the keyfile name mentioned inside the [docker-compose file](./AutomateSetup/docker-compose.yml) (in the `ca` service specification).
