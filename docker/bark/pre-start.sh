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
