FROM sequenceiq/spark:1.6.0
MAINTAINER com.ebay.oss

#set java environment variables
ENV JAVA_HOME /usr/java/jdk1.7.0_51
ENV PATH $JAVA_HOME/bin:$PATH

#install wget
RUN yum install -y wget

#enter /apache
RUN mkdir /apache

#install hive 1.2.1 and set environment variables
RUN cd /apache && wget https://www.apache.org/dist/hive/hive-1.2.1/apache-hive-1.2.1-bin.tar.gz && tar -xvf apache-hive-1.2.1-bin.tar.gz
ENV HIVE_HOME /apache/apache-hive-1.2.1-bin
ENV PATH $HIVE_HOME/bin:$PATH

#running HiveServer2 and Beeline
ENV HADOOP_USER_CLASSPATH_FIRST true
RUN rm /usr/local/hadoop-2.6.0/share/hadoop/yarn/lib/jline-0.9.94.jar

#install tomcat 7
RUN cd /apache && wget https://www.apache.org/dist/tomcat/tomcat-7/v7.0.70/bin/apache-tomcat-7.0.70.tar.gz && tar -xvf apache-tomcat-7.0.70.tar.gz
ADD config/tomcat /etc/init.d/
RUN chmod 775 /etc/init.d/tomcat
ENV TOMCAT_HOME /apache/apache-tomcat-7.0.70

#install mongodb
ADD config/mongodb-org-3.2.repo /etc/yum.repos.d/
RUN yum install -y mongodb-org mongodb-org-server mongodb-org-shell mongodb-org-mongos mongodb-org-tools && yum clean all

#expose ports
EXPOSE 8080
EXPOSE 27017

#add working directory
RUN mkdir /bark
ADD bark /bark
RUN chmod -R 755 /bark

#env
ENV HADOOP_HOME /usr/local/hadoop
ENV PATH $PATH:$HADOOP_HOME/bin

#prepare env data
WORKDIR /bark
RUN ./hadoop-start.sh && ./pre-start.sh && ./hd-before-hive.sh && hive -f hive-input.hql && ./hd-after-hive.sh && ./hd-test-json.sh && ./hadoop-end.sh && rm hadoop-start.sh pre-start.sh hd-before-hive.sh hive-input.hql hd-after-hive.sh hd-test-json.sh hadoop-end.sh

#modify spark webui port
ADD config/spark-conf-mod.sh /bark
RUN ./spark-conf-mod.sh && rm spark-conf-mod.sh
