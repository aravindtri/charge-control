FROM openjdk:8-jdk-slim-buster

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        ca-certificates \
        curl \
        python3.7 \
        python3-pip \
        python3.7-dev \
        python3-setuptools \
        python3-wheel 

ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} app.jar

RUN apt-get -y update
RUN apt-get install -y chromium chromium-driver

RUN pip install --upgrade pip
RUN pip install cryptography==3.3.2
RUN pip install selenium==4.0.0
ENTRYPOINT ["java","-jar","/app.jar"]
