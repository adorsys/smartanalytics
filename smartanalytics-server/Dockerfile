FROM adorsys/openjdk-jre-base:8-minideb
MAINTAINER https://git.adorsys.de/adorsys/smartanalytics
ENV JAVA_OPTS -Xmx1024m
WORKDIR /opt/multibanking-service
COPY smartanalytics-server/target/smartanalytics-server.jar /opt/smartanalytics/smartanalytics-server.jar
EXPOSE 8080
CMD exec $JAVA_HOME/bin/java $JAVA_OPTS -jar /opt/smartanalytics/smartanalytics-server.jar
