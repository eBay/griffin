#!/bin/bash

sleep 5
hdfs dfsadmin -safemode leave

#mysql service
service mysqld start

/bark/mysql_secure.sh 123456 && rm /bark/mysql_secure.sh
/bark/mysql_init.sh && rm /bark/mysql_init.sh

#hive metastore service
hive --service metastore &

#insert hive table
hive -f hive-input.hql

rm hive-input.hql
