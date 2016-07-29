#!/bin/bash

runningdir=/user/test/bark/running
lv1tempfile=/home/hduser/bark/temp.txt
lv2tempfile=/home/hduser/bark/temp2.txt
logfile=/home/hduser/bark/log.txt

set +e

hadoop fs -ls $runningdir > $lv1tempfile

rm -rf $logfile
touch $logfile

while read line
do
  lv1dir=${line##* }
  echo $lv1dir
  hadoop fs -ls $lv1dir/_START
  rc=$?
  if [ $rc -ne 0 ] && [ "${lv1dir:0:1}" == "/" ]
  then
    hadoop fs -cat $lv1dir/_watchfile > $lv2tempfile

    watchfiledone=1
    while read watchline
    do
      echo $watchline >> $logfile
      hadoop fs -ls $watchline/_SUCCESS
      rcode=$?
      echo $rcode
      if [ $rcode -ne 0 ]
      then
        watchfiledone=0
      fi
    done < $lv2tempfile

    if [ $watchfiledone -eq 1 ]
    then
      hadoop fs -touchz $lv1dir/_START
      hadoop fs -ls $lv1dir/_type_0.done
      rc1=$?
      hadoop fs -ls $lv1dir/_type_1.done
      rc2=$?
      if [ $rc1 -eq 0 ]
      then
        echo "spark-submit --class com.ebay.bark.Accu33 --master yarn --queue default --executor-memory 512m --num-executors 10 accuracy-1.0-SNAPSHOT.jar  $lv1dir/cmd.txt $lv1dir/ "
        spark-submit --class com.ebay.bark.Accu33 --master yarn --queue default --executor-memory 512m --num-executors 10 bark-models-0.0.1-SNAPSHOT.jar  $lv1dir/cmd.txt $lv1dir/
      elif [ $rc2 -eq 0 ]
      then
        echo "spark-submit --class com.ebay.bark.Vali3 --master yarn --queue default --executor-memory 512m --num-executors 10 accuracy-1.0-SNAPSHOT.jar  $lv1dir/cmd.txt $lv1dir/ "
        spark-submit --class com.ebay.bark.Vali3 --master yarn --queue default --executor-memory 512m --num-executors 10 bark-models-0.0.1-SNAPSHOT.jar  $lv1dir/cmd.txt $lv1dir/
      fi

      echo "watch file ready" >> $logfile
      exit
    fi
  fi

done < $lv1tempfile

set +e