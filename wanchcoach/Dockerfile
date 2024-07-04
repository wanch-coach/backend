FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/wanchcoach-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} wanchcoach.jar

COPY /home/ubuntu/sql/ src/main/resources/

EXPOSE 8081

# 환경변수 설정
ARG ENCRYPT_KEY
ENV ENCRYPT-KEY $ENCRYPT_KEY

ENTRYPOINT ["java","-Dspring.cloud.config.profile=dev","-jar","/wanchcoach.jar"]


# FROM openjdk:17-jdk
# ARG JAR_FILE=build/libs/test-0.0.1-SNAPSHOT.jar
# ADD ${JAR_FILE} test.jar
# ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/test.jar"]
