#Bark Introduction

![bark logo](https://ebay.github.io/DQSolution/img/bark-sm.gif)

Bark is a Data Quality Service Platform on the eBay cloud which provides:
* Automatic quality validation of your data
* Health monitoring, Profiling and detection
* Unified Visualization
* A set of tools to build data quality pipelines

## Background
At eBay, different teams (DSS, Search Science, Tracking, Personalization, Experimentation etc) have built customized data quality tools to detect and analyze data quality issues within their own domain. We want to take a platform approach to provide shared infrastructure and generic features to solve common data quality check pain points. This would enable us to build trusted data assets.

## Rationale
At eBay, the key challenges on data quality includes:

1. it takes a long time to identify and fix poor data quality
2. some data quality issues do have business impact on user experiences, revenue, efficiency & compliance
3. Communication overhead of data quality metrics

The idea of  Bark is to provide data quality validation as a service, to allow data engineers and data consumers to have:
* Near real-time understanding of the data quality health of your data pipelines with end-to-end monitoring, all in one place.
* Unified Visualization of data quality status
* Profiling, detecting and correlating issues and providing recommendations that drive rapid and focused troubleshooting
* A set of tools to build data quality pipelines across all eBay data platforms.
* Native code generation to run everywhere, including Hadoop, Kafka, Spark, etc.

### Who is this for?
The targeted user of this tool includes anyone who creates/processes/uses data, including:
* Data Engineers who process the data ETL
* Analysts who build dataset and create metrics
* Data Scientists who build model and ETL process to generate dataset.
* Business users who use any data report or application which use the data. they need know the data quality status.
* Managers who want to know the DQ metrics to know the system data health.

### What problems does this address?
The following are the main problems in big data flow across multi platforms

**Problem 1:** Lack of end2end unified view of data quality MEASUREMENT from data source to applications, takes a long time to identify and fix poor data quality.

**Problem 2:** How to get data quality measured in Streaming mode, develop DQ rule to detect and analyze data issue.

**Problem 3:** No Shared platform and Service, have to apply and manage own hardware and software infrastructure

### How will the developer, customer experience change as a result?
Bark will provide a set of tool and service to help user build a data quality control pipeline as the diagram below. It will change the traditional way how we control data quality and user can be focus on developing business logic on validating the data quality and doing root cause analysis.

![data quality flow](https://ebay.github.io/DQSolution/img/data%20quality%20Flow.png)

## Initial Goals
MVP Use Cases:
* Real time data processing data quality on Spark: able to collect the data from source (Pulsar/Rhoes) to Bullseye to Application, build DQ metrics repository, create basic statistics rule engine to detect DQ issue;
* Build End2end data quality unified dashboard for Bullseye,  provide basic FE tool to help people analyzing metrics insights and sample data.
* Complete data quality management flow including registering data asset, creating DQ model, and metrics visualization

## Resources

1. https://wiki.vip.corp.ebay.com/display/CCOE/Data+Quality+Service+BARK
2. http://bark.vip.ebay.com
3. CI: https://ebayci.qa.ebay.com/bark4dq-8582/
4. Sonar: http://sonar.vip.qa.ebay.com/dashboard/index/1287584
