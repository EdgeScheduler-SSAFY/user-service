FROM openjdk:17-alpine

#build된 jar 파일
ARG JAR_FILE_PATH=build/libs

# 호스트의 JAR 파일을 컨테이너로 복사
COPY ${JAR_FILE_PATH}/user-service-0.0.1-SNAPSHOT.jar user-service.jar


ENTRYPOINT ["java", "-jar", "./user-service.jar"]