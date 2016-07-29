## Bark

Bark is a Data Quality solution for distributed data systems at any scale in both streaming or batch data context. It provides a framework process for defining data quality model, executing data quality measurement, automating data profiling and validation, as well as a unified data quality visualization across multiple data systems. You can access our home page [here](https://ebay.github.io/DQSolution/).


### Contact us
[DL-eBay-bark-dev@ebay.com](mailto://DL-eBay-bark-dev@ebay.com)

[Slack](https://ebay-eng.slack.com/messages/ebaysf-bark/)

### How to build
1. git clone the repository of https://github.com/eBay/DQSolution
2. run "mvn install"

### How to run
1. Install jdk (1.7 or later versions)
2. Install Tomcat (7.0 or later versions)
3. Install MongoDB and import the collections
	```
	mongorestore /db:unitdb0 /dir:<dir of bark-doc>/db/unitdb0
	```

4. Install [Hadoop](http://mirror.stjschools.org/public/apache/hadoop/common/hadoop-2.7.2/hadoop-2.7.2.tar.gz) (2.7 or later versions), you can get some help [here](https://hadoop.apache.org/docs/r2.7.2/hadoop-project-dist/hadoop-common/SingleCluster.html). Make sure you have the permission to use command "hadoop"
5. Install [Spark](http://www.webhostingjams.com/mirror/apache/spark/spark-2.0.0/spark-2.0.0-bin-hadoop2.7.tgz) (version 2.0.0), if you want to install Pseudo Distributed/Single Node Cluster, you can get some help [here](http://why-not-learn-something.blogspot.com/2015/06/spark-installation-pseudo.html)
6. Install [Hive](http://mirrors.koehn.com/apache/hive/hive-2.1.0/apache-hive-2.1.0-bin.tar.gz) (version 2.1.0), you can get some help [here](https://cwiki.apache.org/confluence/display/Hive/GettingStarted#GettingStarted-RunningHive)
7. Put your data into Hive. You can get sample data [here](https://github.com/eBay/DQSolution/tree/master/bark-doc/hive), then put them into hive as following

    ```
    CREATE TABLE movie_source (
      movieid STRING,
      title STRING,
      genres STRING)
    ROW FORMAT DELIMITED
    FIELDS TERMINATED BY '\;'
    STORED AS TEXTFILE;

    LOAD DATA LOCAL INPATH '<your hdfs table path>/movie_source/MovieLensSample_Source.dat' OVERWRITE INTO TABLE movie_source;

    CREATE TABLE movie_target (
      movieid STRING,
      title STRING,
      genres STRING)
    ROW FORMAT DELIMITED
    FIELDS TERMINATED BY '\;'
    STORED AS TEXTFILE;

    LOAD DATA LOCAL INPATH '<your hdfs table path>/movie_target/MovieLensSample_Target.dat' OVERWRITE INTO TABLE movie_target;
    ```
    Make sure you can get the data with hive command in your working path. 
    If you use hive cmdline to input data, remember to create _SUCCESS file in the hdfs path
    ```
    hadoop fs -touchz <your hdfs table path>/movie_source/_SUCCESS
    hadoop fs -touchz <your hdfs table path>/movie_target/_SUCCESS
    ```
8. If you want to use our default models, please skip this step.
You can create your own model, build your jar file, and put it in your local path
9. Edit your own script files to run the jobs automatically, you can edit the lines of demo ones as below for your environment

    [bark_jobs.sh](https://github.com/eBay/DQSolution/tree/master/bark-doc/hive/script/bark_jobs.sh)
    ```
    runningdir=<your hdfs empty path>/running
    lv1tempfile=<your local path>/temp.txt
    lv2tempfile=<your local path>/temp2.txt
    logfile=<your local path>/log.txt
    ```
    If you set "runningdir" to your own hdfs path, you should keep it the same with "job.hdfs.folder" in [application.properties](https://github.com/eBay/DQSolution/tree/master/bark-core/src/main/resources/application.properties) (the modification of this file needs your rebuild of bark-core and redeploy)

    ```
    spark-submit --class com.ebay.bark.Accu33 --master yarn --queue default --executor-memory 512m --num-executors 10 bark-models-0.0.1-SNAPSHOT.jar  $lv1dir/cmd.txt $lv1dir/
    spark-submit --class com.ebay.bark.Vali3 --master yarn --queue default --executor-memory 512m --num-executors 10 bark-models-0.0.1-SNAPSHOT.jar  $lv1dir/cmd.txt $lv1dir/
    ```

    These commands submit the jobs to spark, if you want to try your own model or modify some parameters, you can edit it. 
    If you try to use your own model, change "bark-models-0.0.1-SNAPSHOT.jar" to "your path/your model.jar"

    [bark_regular_run.sh](https://github.com/eBay/DQSolution/tree/master/bark-doc/hive/script/bark_regular_run.sh)

    ```
    <your local path contain this file bark_jobs.sh>/bark_jobs.sh 2>&1

    <your local path>/nohup.out
    ```
    then run the script file bark_regular_run.sh
    ```
    nohup ./bark_regular_run.sh
    ```

10. Open [application.properties](https://github.com/eBay/DQSolution/tree/master/bark-core/src/main/resources/application.properties) file, read the comments and specify the properties correctly.
11. Build the whole project and deploy bark-core/target/ROOT.war to tomcat
12. Then you can review the RESTful APIs through http://localhost:8080/api/v1/application.wadl

### How to develop
In dev environment, you can run backend REST service and frontend UI seperately. The majority of the backend code logics are in the [bark-core](https://github.com/eBay/DQSolution/tree/master/bark-core) project. So, to start backend, please import maven project Bark into eclipse, right click ***bark-core->Run As->Run On Server***

To start frontend, please follow up the below steps.

1. Open **bark-ui/js/services/services.js** file

2. Specify **BACKEND_SERVER** to your real backend server address, below is an example

    ```
    var BACKEND_SERVER = 'http://localhost:8080'; //dev env
    //var BACKEND_SERVER = 'http://localhost:8080/ROOT'; //dev env
    ```

3. Open a command line, run the below commands in root directory of **bark-ui**

   - npm install
   - bower install
   - npm start

4. Then the UI will be opened in browser automatically, please follow the [userGuide](https://github.com/eBay/DQSolution/tree/master/bark-doc/userguide.md), enjoy your journey!

**Note**: The front-end UI is still under development, you can only access some basic features currently.


### Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for details on how to contribute code, documentation, etc.
