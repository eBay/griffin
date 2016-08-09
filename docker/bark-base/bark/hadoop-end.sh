#!/bin/bash

: ${HADOOP_PREFIX:=/usr/local/hadoop}

/bin/bash -c "$*"
$HADOOP_PREFIX/sbin/stop-dfs.sh
$HADOOP_PREFIX/sbin/stop-yarn.sh
