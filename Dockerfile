FROM maven:alpine
MAINTAINER https://github.com/adorsys/smartanalytics

ENV JAVA_OPTS -Xmx1024m

WORKDIR /opt/smartanalytics

ADD . /opt/smartanalytics

RUN mvn clean install -DskipTests

EXPOSE 8080
CMD exec $JAVA_HOME/bin/java $JAVA_OPTS -jar /opt/smartanalytics/smartanalytics-server/target/smartanalytics-server.jar
