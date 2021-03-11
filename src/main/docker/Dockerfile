FROM java:8
MAINTAINER DS.wd.J
VOLUME /tmp
ADD daishu-cloud-gateway-0.1.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
ENV APP_OPTS ""
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar", "--spring.cloud.consul.host=consul-ack-consul-server.dsxb-microunit.svc.cluster.local"]
