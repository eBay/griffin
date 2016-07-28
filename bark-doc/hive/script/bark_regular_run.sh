#!/bin/bash

set +e
while true
do
  echo "start"
  /home/hduser/bark/bark_jobs.sh 2>&1
  rcode=$?
  echo "end $rcode"
  rm -rf /home/hduser/bark/nohup.out
  sleep 60
done
set -e