FROM java:8u111-jre
VOLUME /tmp
COPY target/*.jar order-0.0.1-SNAPSHOT.jar
COPY key key
ENTRYPOINT ["java","-jar","/order-0.0.1-SNAPSHOT.jar"]