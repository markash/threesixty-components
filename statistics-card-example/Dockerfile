FROM fabric8/java-jboss-openjdk8-jdk:1.3.1

ENV JAVA_APP_JAR statistics-card-example-0.3.0.jar
ENV AB_ENABLED off
ENV AB_JOLOKIA_AUTH_OPENSHIFT true
ENV JAVA_OPTIONS -Xmx256m -Djava.security.egd=file:///dev/./urandom

EXPOSE 8080

ADD target/statistics-card-example-0.3.0.jar /deployments/
