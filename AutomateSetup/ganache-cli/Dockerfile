FROM trufflesuite/ganache-cli as ganache
RUN mkdir /truffle
WORKDIR /truffle
RUN npm install -g truffle@5.0.9
COPY ./Digest /smart-contracts/Digest