FROM bark-base-env:latest
MAINTAINER com.ebay.oss

#input files
ADD script/*.sh $BARK_HOME/
ADD ./*.sh $BARK_HOME/
RUN mkdir $BARK_HOME/tmp $BARK_HOME/temp $BARK_HOME/log
RUN chmod -R 755 $BARK_HOME

#add db
RUN mkdir /db
ADD db /db

#remove tomcat webapps
ENV APACHE_HOME /apache/apache-tomcat-7.0.70
RUN rm -rf $APACHE_HOME/webapps/*

#prepare data
WORKDIR $BARK_HOME
RUN ./hadoop-start.sh && ./pre-start.sh && ./hadoop-end.sh && rm hadoop-start.sh hadoop-end.sh pre-start.sh

#download resources
RUN ./download-resources.sh && rm download-resources.sh

#entrypoint
ENTRYPOINT ["./start-up.sh"]
