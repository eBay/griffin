#!/bin/bash


sed -i.bak s/^.*"hive-txn-schema-0.13.0.mysql.sql".*/"SOURCE \/apache\/apache-hive-1.2.1-bin\/scripts\/metastore\/upgrade\/mysql\/hive-txn-schema-0.13.0.mysql.sql;"/ /apache/apache-hive-1.2.1-bin/scripts/metastore/upgrade/mysql/hive-schema-1.2.0.mysql.sql

mysql -u root -p123456 < $BARK_HOME/mysql_init_metastore.sql

rm $BARK_HOME/mysql_init_metastore.sql
