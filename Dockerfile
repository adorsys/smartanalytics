FROM adoptopenjdk/openjdk11:alpine-slim

ARG BUILD_DATE
ARG BUILD_VERSION

LABEL maintainer="kan@adorsys.com" \
      org.label-schema.version=$BUILD_VERSION \
      org.label-schema.build-date=$BUILD_DATE \
      org.label-schema.vendor="adorsys GmbH & Co. KG" \
      org.label-schema.name="smartanalytics"

ENV JAVA_OPTS -Xmx1024m

WORKDIR /opt/smartanalytics

COPY ./smartanalytics-server/target/smartanalytics-server.jar /opt/smartanalytics/smartanalytics-server.jar

EXPOSE 8082

RUN  addgroup -S app_group && adduser -S -G app_group smartanalytics_user && chown -R smartanalytics_user:app_group /opt/smartanalytics

USER smartanalytics_user

CMD exec $JAVA_HOME/bin/java $JAVA_OPTS -jar /opt/smartanalytics/smartanalytics-server.jar
