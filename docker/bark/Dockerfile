FROM bark-base-env:latest
MAINTAINER com.ebay.oss

#input files
ADD docker/bark/script/*.sh /bark/
ADD docker/bark/*.sh /bark/
RUN mkdir /bark/tmp && mkdir /bark/temp && mkdir /bark/log
RUN chmod -R 755 /bark

#add db
ADD bark-doc/db /db

#remove tomcat webapps
ENV APACHE_HOME /apache/apache-tomcat-7.0.70
RUN rm -rf $APACHE_HOME/webapps/*

#prepare data
WORKDIR /bark
RUN ./hadoop-start.sh && ./pre-start.sh && ./hadoop-end.sh && rm hadoop-start.sh hadoop-end.sh pre-start.sh

#include source code
RUN mkdir /DQSolution
ADD bark-models /DQSolution/bark-models
ADD bark-core /DQSolution/bark-core
ADD bark-ui /DQSolution/bark-ui
ADD pom.xml /DQSolution/
RUN chmod -R 755 /DQSolution

#mvn install source code
RUN cd /DQSolution/bark-models/ && mvn clean install -DskipTests && cp /DQSolution/bark-models/target/bark-models-*-SNAPSHOT.jar /bark/bark-models.jar && chmod 755 /bark/bark-models.jar

RUN cd /DQSolution/ && mvn clean install -DskipTests && cp /DQSolution/bark-core/target/ROOT.war $APACHE_HOME/webapps/ && chmod 755 $APACHE_HOME/webapps/ROOT.war

#remove source code
RUN rm -rf /DQSolution

#workdir
WORKDIR /bark

#entrypoint
ENTRYPOINT ["./start-up.sh"]
