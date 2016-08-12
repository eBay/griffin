#!/bin/bash

hadoop fs -mkdir /user/test
hadoop fs -put /bark/jsonFile/accu_config.json /user/test/
hadoop fs -put /bark/jsonFile/vali_config.json /user/test/
hadoop fs -mkdir /user/test/output
