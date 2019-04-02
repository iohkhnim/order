FROM java:8
VOLUME /tmp
COPY target/*.jar order-0.0.1-SNAPSHOT.jar
COPY key key
ENTRYPOINT ["java","-jar","/order-0.0.1-SNAPSHOT.jar"]