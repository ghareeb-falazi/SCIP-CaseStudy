pragma solidity >=0.4.21 <0.6.0;

/**
 * @title A simple digests container
 * @author Andrea Lamparelli
 * @notice This contract can be used to store digests of external operations, each digest is associated to a specific correlation_id
 **/
contract Digest {
    
    /**
     * client_addres => (correlation_id => digest)
     **/
    mapping (address => mapping (string => bytes)) private digestsMap;
    
    /**
     * @notice Events that notifies that a specific client has added a new digest on a specific correlation identifier
     * @param client identifies which is the client application that added the new digest
     * @param corrId is the identifier to which the digest is correlated to
     * @param digest is a digest of a specific operation performed by the client
     **/
    event DigestAdded(address client, string corrId, bytes digest);
    
    /**
     * @notice Function that adds a new digest strictly correlated to an identifier, on behalf of a client application
     * @param corrId is the identifier to which the new digest has to be associated with
     * @param digest is the new operation digest that has to be added
     **/
    function addDigest(string memory corrId, bytes memory digest) public {
        digestsMap[msg.sender][corrId] = digest;
        emit DigestAdded(msg.sender, corrId, digest);
    }
    
    /**
     * @notice Function used to retrieve a digest for a given client which is associated to a given corrId
     * @param client is the address from which retrieve the digest
     * @param corrId is used to retrieve only those digest that are correlated to this id
     **/
    function getDigest(address client, string memory corrId) public view returns (bytes memory) {
        return digestsMap[client][corrId];
    }
}