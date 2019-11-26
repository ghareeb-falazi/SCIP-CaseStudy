FROM alpine/git as clone
WORKDIR /app
RUN git clone --single-branch --branch scip https://github.com/ghareeb-falazi/BlockchainAccessLayer

FROM maven:3.5-jdk-8-alpine as build
WORKDIR /app
COPY --from=clone /app/BlockchainAccessLayer /app
RUN mvn package -DskipTests

FROM tomcat:8.0.43-jre8 as bal-ethereum
# deploy to tomcat and overwrite configs
COPY --from=build /app/target/blockchain-access-layer.war /usr/local/tomcat/webapps/
COPY /tomcat/server.xml /usr/local/tomcat/conf/
COPY /tomcat/tomcat-users.xml /usr/local/tomcat/conf/
# configure BAL
COPY connectionProfiles-eth.json $HOME/root/.bal/connectionProfiles.json
# configure ganache-cli
COPY /eth/UTC--2019-05-30T11-21-08.970000000Z--90645dc507225d61cb81cf83e7470f5a6aa1215a.json /eth/UTC--2019-05-30T11-21-08.970000000Z--90645dc507225d61cb81cf83e7470f5a6aa1215a.json
EXPOSE 8080
CMD chmod +x /usr/local/tomcat/bin/catalina.sh
CMD ["catalina.sh", "run"]

FROM tomcat:8.0.43-jre8 as bal-fabric
# deploy to tomcat and overwrite configs
COPY --from=build /app/target/blockchain-access-layer.war /usr/local/tomcat/webapps/
COPY /tomcat/server.xml /usr/local/tomcat/conf/
COPY /tomcat/tomcat-users.xml /usr/local/tomcat/conf/
# configure BAL
COPY connectionProfiles-fab.json $HOME/root/.bal/connectionProfiles.json
# configure fabric-network
COPY /fab/connection-org1.json /fab/connection-org1.json
EXPOSE 8080
CMD chmod +x /usr/local/tomcat/bin/catalina.sh
CMD ["catalina.sh", "run"]



