FROM  openjdk:14.0.2-slim

RUN apt update && apt upgrade -y && apt install curl unzip -y

WORKDIR /opt/gradle

RUN \
    curl -L https://services.gradle.org/distributions/gradle-6.7.1-bin.zip -o ./gradle-6.7.1-bin.zip && \
    unzip gradle-6.7.1-bin.zip && \
    rm gradle-6.7.1-bin.zip

RUN ls /opt/gradle

ENV GRADLE_HOME=/opt/gradle/gradle-6.7.1


ENV PATH=$PATH:$GRADLE_HOME/bin

COPY . /usr/kalah/api
WORKDIR /usr/kalah/api
RUN rm -rf client/

EXPOSE 8080

ENTRYPOINT ["gradle", "bootRun"]
