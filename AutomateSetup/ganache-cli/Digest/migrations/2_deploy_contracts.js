const Digest = artifacts.require ("./Digest.sol");

module.exports = function(deployer) {
      deployer.deploy(Digest);
}