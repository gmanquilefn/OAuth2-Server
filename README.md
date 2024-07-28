
## Authorization-Authentication Server implementation of Spring OAuth2 Authorization Server

---

This project implements the default configuration of Spring OAuth2 Authorization Server with:
1. JDBC PostgreSQL configuration for persistence
2. PostgreSQL database schema proved by Spring OAuth2 Authorization Server in documentation
3. RSA256 Public and Private keys loading from .pem files (X.509, PKCS8 respectively)


Spring OAuth2 Authorization Server project: https://spring.io/projects/spring-authorization-server

Spring OAuth2 Authorization Server documentation: https://docs.spring.io/spring-authorization-server/reference/overview.html


### Installation

---

For this installation guide you will need Docker and docker-compose, any other method will not be documented (boring, no explanations)

#### 1. Check application.yml

Check the application.yml file in src/main/java/resources folder of the project if the configuration meet you needs, specifically:

```yaml
    datasource: #host.docker.internal for container to container connection
      url: jdbc:postgresql://172.17.0.1:5432/authserver
      username: postgres
      password: postgres

    jwt:
      key: #RSA Keys from docker volume bind mount
        public: file:/keys/app.pub
        private: file:/keys/app.key
        id: auth-server
```

for the database ip, you can use the following command to see the ip address of the host.docker.internal for container to container connection:

```bash
ip addr show docker0 | grep -Po 'inet \K[\d.]+'
```

The file route of the public and private keys of the auth server are configured to be used in a bind mount docker volume configuration


#### 2. Generate the docker image

To generate the docker image we will need the .jar file of the project as specified in Dockerfile:

```dockerfile
FROM eclipse-temurin:21.0.4_7-jre-alpine
RUN mkdir keys/
ADD target/oauth2-auth-server-1.0.0.jar oauth2-auth-server.jar
EXPOSE 9000
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} ${SPRING_PROFILE} -jar oauth2-auth-server.jar"]
```

To generate the .jar file we will use maven, you can use maven wrapper, a maven runner from the IDE, etc...

```bash
mvn clean install
```

This command will generate the .jar file in the /target directory of the project, if there is a .jar file previously generated, the "clean" command will deleted and create a new one

Then we will execute the docker build command:

```bash
docker build -t oauth2-auth-server:1.0.0 .
```

This will generate the docker image that you can see using "docker images" command with the specified name and tag previously executed

#### 3. Configure docker-compose

In the folder that you will install the application, we need to create the following docker-compose.yml file:

```yaml
version: '3'

services:
  oauth2server:
    container_name: oauth2-auth-server
    image: oauth2-auth-server:1.0.0
    ports:
      - "9000:9000"
    environment:
      - "JAVA_OPTS=-Xmx2048m -Duser.timezone=America/Santiago"
      - "SPRING_PROFILE=-Dspring.profiles.active=dev"
    restart: always
    volumes:
      - ${KEYS_PATH}:/keys
      - ${LOG_PATH}:/log
    logging:
      options:
        max-size: 10m
        max-file: "10"
```

Where you can change (or not):
* Duser.timezone to the preferred timezone
* Dspring.profiles.active to specify another profile
* bind mounts to keys and log paths
* any other configuration you need...

#### 3.1. Configure .env file

This step is optional but recommended to use Docker "variable interpolation", https://docs.docker.com/compose/compose-file/12-interpolation/

In the folder that you will install the application, create a .env file

```
vim .env
```

In this file, we are going to put all the variables we need, specifically, KEYS_PATH and LOG_PATH to be used by docker-compose

```bash
KEYS_PATH="/your/path/here/keys"
LOG_PATH="your/path/here/log"
```

#### 4. Run docker-compose

To run the docker container, run the following command:

```bash
docker compose up -d
```

This will create the docker container.

To see if it is running:
```bash
docker ps

CONTAINER ID   IMAGE                      COMMAND                  CREATED          STATUS         PORTS                                       NAMES
a2012e4d9df6   oauth2-auth-server:1.0.0   "sh -c 'java ${JAVA_…"   48 seconds ago   Up 2 seconds   0.0.0.0:9000->9000/tcp, :::9000->9000/tcp   oauth2-auth-server
ba52ed4a4ca0   postgres:latest            "docker-entrypoint.s…"   4 hours ago      Up 4 hours     0.0.0.0:5432->5432/tcp, :::5432->5432/tcp   postgres-db
```