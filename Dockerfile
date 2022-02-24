FROM openjdk:8-jdk-alpine
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} app.jar
RUN apt-get install python
RUN apt-get -y update
RUN apt-get install -y chromium chromium-driver

RUN pip install --upgrade pip
RUN pip install cryptography==3.3.2
RUN pip install selenium==4.0.0
ENTRYPOINT ["java","-jar","/app.jar"]
