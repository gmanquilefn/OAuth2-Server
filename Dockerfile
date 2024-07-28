FROM eclipse-temurin:21.0.4_7-jre-alpine
RUN mkdir keys/
ADD target/oauth2-auth-server-1.0.0.jar oauth2-auth-server.jar
EXPOSE 9000
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} ${SPRING_PROFILE} -jar oauth2-auth-server.jar"]