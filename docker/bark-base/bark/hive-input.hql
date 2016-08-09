CREATE TABLE users_info_src (
  user_id bigint,
  first_name string,
  last_name string,
  address string,
  email string,
  phone string,
  post_code string)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '|'
STORED AS TEXTFILE;

LOAD DATA LOCAL INPATH '/bark/dataFile/users_info_src.dat' OVERWRITE INTO TABLE users_info_src;

CREATE TABLE users_info_target (
  user_id bigint,
  first_name string,
  last_name string,
  address string,
  email string,
  phone string,
  post_code string)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '|'
STORED AS TEXTFILE;

LOAD DATA LOCAL INPATH '/bark/dataFile/users_info_target.dat' OVERWRITE INTO TABLE users_info_target;

exit;
