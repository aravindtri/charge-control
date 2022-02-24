### 1. Get Linux
FROM python:3.10-buster

### 2. Get Java via the package manager
RUN apk update \
&& apk upgrade \
&& apk add --no-cache bash \
&& apk add --no-cache --virtual=build-dependencies unzip \
&& apk add --no-cache curl \
&& apk add --no-cache openjdk8-jre

ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} app.jar

RUN apt-get -y update
RUN apt-get install -y chromium chromium-driver

RUN pip install --upgrade pip
RUN pip install cryptography==3.3.2
RUN pip install selenium==4.0.0
ENTRYPOINT ["java","-jar","/app.jar"]
