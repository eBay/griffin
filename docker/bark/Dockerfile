FROM bark-base-env:latest
MAINTAINER com.ebay.oss

#input files
ADD script/*.sh /bark/
ADD ./*.sh /bark/
RUN mkdir /bark/tmp /bark/temp /bark/log
RUN chmod -R 755 /bark

#git clone source code
RUN mkdir /bark/git && cd /bark/git && git clone https://github.com/eBay/DQSolution.git

#add db
RUN cp -r /bark/git/DQSolution/bark-doc/db /db

#remove tomcat webapps
ENV APACHE_HOME /apache/apache-tomcat-7.0.70
RUN rm -rf $APACHE_HOME/webapps/*

#prepare data
WORKDIR /bark
RUN ./hadoop-start.sh && ./pre-start.sh && ./hadoop-end.sh && rm hadoop-start.sh hadoop-end.sh pre-start.sh

#mvn install source code
RUN cd /bark/git/DQSolution/bark-models/ && mvn clean install -DskipTests && mv /bark/git/DQSolution/bark-models/target/bark-models-*-SNAPSHOT.jar /bark/bark-models.jar && chmod 755 /bark/bark-models.jar

RUN cd /bark/git/DQSolution/ && mvn clean install -DskipTests && mv /bark/git/DQSolution/bark-core/target/ROOT.war $APACHE_HOME/webapps/ && chmod 755 $APACHE_HOME/webapps/ROOT.war

#remove source code
RUN rm -rf /bark/git

#workdir
WORKDIR /bark

#entrypoint
ENTRYPOINT ["./start-up.sh"]
