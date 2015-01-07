FROM ubuntu:14.04

RUN \
    apt-get update && \
    apt-get -y install wget tar && \
    wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u25-b17/server-jre-8u25-linux-x64.tar.gz && \
    mkdir -p /env/jre && \
    tar -xzf server-jre-8u25-linux-x64.tar.gz -C /env/jre jdk1.8.0_25 --strip 1 && \
    rm server-jre-8u25-linux-x64.tar.gz

ENV JAVA_HOME /env/jre
ENV PATH $JAVA_HOME/bin:$PATH