FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY /arthas-bin.zip /opts/arthas-bin.zip
ADD live-user-payment-provider-docker.jar app.jar
ENV JAVA_OPTS="\
-server \
-Xmx128m \
-Xms64m \
-Xmn64m"
ENTRYPOINT java  ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.util.concurrent=ALL-UNNAMED --add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.math=ALL-UNNAMED -jar app.jar