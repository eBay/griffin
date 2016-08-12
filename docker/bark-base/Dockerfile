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
RUN cd /apache && wget http://ftp.wayne.edu/apache/hive/hive-1.2.1/apache-hive-1.2.1-bin.tar.gz && tar -xvf apache-hive-1.2.1-bin.tar.gz
ENV HIVE_HOME /apache/apache-hive-1.2.1-bin
ENV PATH $HIVE_HOME/bin:$PATH

#running HiveServer2 and Beeline
ENV HADOOP_USER_CLASSPATH_FIRST true
RUN rm /usr/local/hadoop-2.6.0/share/hadoop/yarn/lib/jline-0.9.94.jar

#install tomcat 7
RUN cd /apache && wget http://apache.spinellicreations.com/tomcat/tomcat-7/v7.0.70/bin/apache-tomcat-7.0.70.tar.gz && tar -xvf apache-tomcat-7.0.70.tar.gz
ADD docker/bark-base/config/tomcat /etc/init.d/
RUN chmod 775 /etc/init.d/tomcat

#install mongodb
ADD docker/bark-base/config/mongodb-org-3.2.repo /etc/yum.repos.d/
RUN yum install -y mongodb-org mongodb-org-server mongodb-org-shell mongodb-org-mongos mongodb-org-tools && yum clean all

#expose ports
EXPOSE 8080
EXPOSE 27017

#install maven
RUN cd /apache && wget http://mirror.stjschools.org/public/apache/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz && tar -xzvf apache-maven-3.3.9-bin.tar.gz
ENV M2_HOME /apache/apache-maven-3.3.9
ENV PATH $PATH:$M2_HOME/bin

#add working directory
RUN mkdir /bark
ADD docker/bark-base/bark /bark
RUN chmod -R 755 /bark

#env
ENV HADOOP_HOME /usr/local/hadoop
ENV PATH $PATH:$HADOOP_HOME/bin

#prepare env data
WORKDIR /bark
RUN ./hadoop-start.sh && ./pre-start.sh && hive -f hive-input.hql && ./hd-after-hive.sh && ./hd-test-json.sh && ./hadoop-end.sh && rm hadoop-start.sh pre-start.sh hive-input.hql hd-after-hive.sh hd-test-json.sh hadoop-end.sh


