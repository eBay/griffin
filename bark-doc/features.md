# Feature List v0.1.1

## 1. DQ Assessment
### Data Profiling
- Responsive Profiling, end user can select time period to do the profiling on full data set
- *Cumulative Profiling, same as what we do in v 0.1.0*
- *Anomaly detection, support machine learning algorithms*

## 2. DQ Measurement
### Model management
- Model CRUD, support 6 dimensions: Completeness, Uniqueness, Timeliness, Validity, Accuracy, Consistency

KPI document

| item   | Desc |
|-------------- | --------------- |
| Name | A meaningful name of the KPI that express what is being measured.  |
| Objective | Why do you measure this? What business processes are impacted if there data is not ok?  |
| Dimensions | What data quality dimensions (integrity, validity, etc.) are this KPI related to?  |
| Frequency of measure |How often do you wish to report on this KPI? Daily, weekly or monthly?  |
|Unit of measure|What is the unit of the KPI? Number of records, pct of records, number of bad values, etc.?|
|Lowest acceptable measure| Threshold that indicates if the data quality aspect the KPI represents is at a minimal acceptable level. The value here must be in the unit of measure of the KPI. |
|Target value|At what value is the KPI considered to represent data quality at a high level? |
|Responsible|The person responsible for the particular KPI. |
|Formula|The tables and fields that are used to analyze and calculate the KPI. This is the functional design formula that forms the basis for the technical implementation|
|Hierarchies|When reporting on a KPI it is very useful to be able to slice and dice the measure according to different dimensions or hierarchies. For a customer data KPI for instance, good hierarchies would be regions, country, company code and account group.<br/> Being able to view the KPI through a hierarchy also makes it easier to follow up with specific groups of business users. |
|Notes and assumptions|If certain assumptions are made about the KPI make sure to document it. |



### Model Engine
- Job Scheduler
- Model Execution, support both real-time data and batch data

## 3. DQ Scorecards
### Model Consumption
- Model consumer could be different from creator, consumer defines his own threshold and notification email
- Customize dashboard

### Heatmap
- Different heatmaps for different users

### Email Reporting
- Subscribe the metrics for email reporting

### Notification
- A notification service supports different types of notifications, such as email, information bar on web UI, twitter, etc.

## 4. Issue Resolution
- Download sample data

## 5. Metadata Management
- Data Asset Registration, support HDFS, HiveTable, RDBMS, etc.
- Schema Registration
- Organization management
- Otherss

## 6. Security
