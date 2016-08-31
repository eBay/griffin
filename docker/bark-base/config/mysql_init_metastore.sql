CREATE DATABASE metastore;
USE metastore;
SOURCE /apache/apache-hive-1.2.1-bin/scripts/metastore/upgrade/mysql/hive-schema-1.2.0.mysql.sql;


CREATE USER 'hive'@'localhost' IDENTIFIED BY '123456';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'hive'@'localhost';
GRANT SELECT,INSERT,UPDATE,DELETE,LOCK TABLES,EXECUTE ON metastore.* TO 'hive'@'localhost';
FLUSH PRIVILEGES;
