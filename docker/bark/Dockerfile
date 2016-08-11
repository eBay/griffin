FROM bark-base-env:latest
MAINTAINER com.ebay.oss

#input files
ADD script/env.sh /bark/
ADD script/bark_regular_run.sh /bark/
ADD script/bark_jobs.sh /bark/
ADD models/bark-models-0.0.1-SNAPSHOT.jar /bark/
ADD start-up.sh pre-start.sh hadoop-start.sh hadoop-end.sh /bark/
RUN mkdir /bark/tmp && mkdir /bark/temp && mkdir /bark/log
RUN chmod -R 775 /bark

#add db
RUN mkdir /db
ADD db /db

#add war
ENV APACHE_HOME /apache/apache-tomcat-7.0.70
RUN rm -rf $APACHE_HOME/webapps/*
ADD webapps/ROOT.war $APACHE_HOME/webapps/

#prepare data
WORKDIR /bark
RUN ./hadoop-start.sh && ./pre-start.sh && ./hadoop-end.sh && rm hadoop-start.sh hadoop-end.sh pre-start.sh

#entrypoint
ENTRYPOINT ["./start-up.sh"]
