#!/bin/bash

: ${HADOOP_PREFIX:=/usr/local/hadoop}i
: ${TOMCAT_HOME:=/apache/apache-tomcat-7.0.70}

$HADOOP_PREFIX/etc/hadoop/hadoop-env.sh

# installing libraries if any - (resource urls added comma separated to the ACP system variable)
cd $HADOOP_PREFIX/share/hadoop/common ; for cp in ${ACP//,/ }; do  echo == $cp; curl -LO $cp ; done; cd -

# altering the core-site configuration
sed s/HOSTNAME/$HOSTNAME/ /usr/local/hadoop/etc/hadoop/core-site.xml.template > /usr/local/hadoop/etc/hadoop/core-site.xml

# setting spark defaults
echo spark.yarn.jar hdfs:///spark/spark-assembly-1.6.0-hadoop2.6.0.jar > $SPARK_HOME/conf/spark-defaults.conf
cp $SPARK_HOME/conf/metrics.properties.template $SPARK_HOME/conf/metrics.properties

service sshd start
$HADOOP_PREFIX/sbin/start-dfs.sh
$HADOOP_PREFIX/sbin/start-yarn.sh

#spark start
$SPARK_HOME/sbin/start-all.sh

#start mongodb
mongod -f /etc/mongod.conf

#mysql service
service mysqld start

/bark/mysql_secure.sh 123456 && rm /bark/mysql_secure.sh
/bark/mysql_init.sh && rm /bark/mysql_init.sh

#hive metastore service
hive --service metastore &

/bin/bash -c "bash"
