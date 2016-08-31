#!/bin/bash

sleep 5
hdfs dfsadmin -safemode leave

#mysql service
service mysqld start

$BARK_HOME/mysql_secure.sh 123456 && rm $BARK_HOME/mysql_secure.sh
$BARK_HOME/mysql_init.sh && rm $BARK_HOME/mysql_init.sh

#hive metastore service
hive --service metastore &

#insert hive table
hive -f $BARK_HOME/hive-input.hql

rm $BARK_HOME/hive-input.hql
