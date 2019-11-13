pragma solidity ^0.5.2;

/**
 * @title A simple digests container
 * @author Andrea Lamparelli
 * @notice This contract can be used to store digests of external operations, digests can be gropued by correlation identifier
 **/
contract Digest {
    
    /**
     * client_addres => (correlation_id => digest[])
     **/
    mapping (address => mapping (bytes => bytes32[])) private digests;
    
    /**
     * @notice Events that notifies that a specific client has added a new digest on a specific correlation identifier
     * @param client: identifies which is the client application that added the new digest
     * @param corrId: this is the identifier to which the new digest is correlated
     * @param digest: this is the exact digest of an operation performed by the client
     **/
    event Add(address client, bytes corrId, bytes32 digest);
    
    /**
     * @notice Function that adds a new digest strictly correlated to an identifier, on behalf of a client application
     * @param corrId: identifier used to keep correlated some digests 
     * @param digest: new digest to add
     **/
    function addDigest(bytes memory corrId, bytes32 digest) public {
        digests[msg.sender][corrId].push(digest);
        emit Add(msg.sender, corrId, digest);
    }
    
    /**
     * @notice Function used to retrieve a list of digest for a given client which are associated to a given corrId
     * @param client is the address from which retrieve the digests
     * @param corrId is used to retrieve only those digest that are correltaed to this id
     **/
    function getCorrelatedDigests(address client, bytes memory corrId) public view returns (bytes32[] memory) {
        return digests[client][corrId];
    }
}
