FROM bark-base-env:latest
MAINTAINER com.ebay.oss

#input files
ADD script/*.sh /bark/
ADD ./*.sh /bark/
RUN mkdir /bark/tmp /bark/temp /bark/log
RUN chmod -R 755 /bark

#add db
RUN mkdir /db
ADD db /db

#remove tomcat webapps
ENV APACHE_HOME /apache/apache-tomcat-7.0.70
RUN rm -rf $APACHE_HOME/webapps/*

#prepare data
WORKDIR /bark
RUN ./hadoop-start.sh && ./pre-start.sh && ./hadoop-end.sh && rm hadoop-start.sh hadoop-end.sh pre-start.sh

#download resources
WORKDIR /bark
RUN ./download-resources.sh && rm download-resources.sh

#workdir
WORKDIR /bark

#entrypoint
ENTRYPOINT ["./start-up.sh"]
