FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/wanchcoach-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} wanchcoach.jar

EXPOSE 8081

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/wanchcoach.jar"]


# FROM openjdk:17-jdk
# ARG JAR_FILE=build/libs/test-0.0.1-SNAPSHOT.jar
# ADD ${JAR_FILE} test.jar
# ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/test.jar"]
