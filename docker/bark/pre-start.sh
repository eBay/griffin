#!/bin/bash

#sleep 10 seconds
sleep 5
hdfs dfsadmin -safemode leave

#restore mongodb data
mongod -f /etc/mongod.conf
mongorestore --db unitdb0 /db/unitdb0

#create bark env
hadoop fs -mkdir /user/bark
hadoop fs -chmod g+w /user/bark
hadoop fs -mkdir /user/bark/running
hadoop fs -mkdir /user/bark/history
hadoop fs -mkdir /user/bark/failure
hadoop fs -chmod g+w /user/bark/running
hadoop fs -chmod g+w /user/bark/history
hadoop fs -chmod g+w /user/bark/failure

#mysql service
service mysqld start

/bark/mysql_secure.sh 123456 && rm /bark/mysql_secure.sh
/bark/mysql_init.sh && rm /bark/mysql_init.sh

#hive metastore service
hive --service metastore &

#insert hive table
hive -f hive-input.hql
