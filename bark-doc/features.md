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

Measurement types

|No.	|quality dimension|	measurement type|	desc	|object of measurement	assessment| category|
|-----|-----------------|-----------------|-------|---------------------------------|---------|
|1	|completeness	|sufficiency of meta and reference data|	assess sufficiency of metadata and reference data|	overall database content	|Initial one-time assessment|
|2	|Consistency	|Consistent formating within a field|	Assess column properties and data for consistent formatting of data within a field	|Data Model|	Initial one-time assessment|
|3	|Integrity/Consistency|	Consistent formatting cross-table|	Assess column properties and data for consistent formatting of data within fields of the same type across a data base|	Data Model|	Initial one-time assessment|
|4	|Consistency|	Consistent use of default values for a field|	Assess column properties and data for default value(s) assigned for each field that can be defaulted|	Data Model|	Initial one-time assessment|
|5	|Integrity/Consistency|	Consistent defaults cross-table|	Assess column properties and data for default value(s) assigned for each field that can be defaulted|	Data Model|	Initial one-time assessment|
|6	|Timeliness|	Timely delivery of data for processing|	Compare actual time of data delivery to scheduled data delivery|	Process/Adherence to schedule|	In-line measurement|
|7	|Completeness|	Dataset completeness - availability for processing|	For files, confirm all files are available for processing (with version check if possible).	|Receipt of data|	Process control|
|8	|Completeness|	Dataset completeness - record counts|	For files, compare record counts in a file to record counts documented in a control record|	Receipt of data	|Process control|
|9	|Completeness|	Dataset completeness - summarized amount field data|	For files, compare summarized data in amount fields to summarized amount provided in a control record|	Receipt of data|	Process control|
|10	|Completeness|	Dataset completeness - size compared to past sizes|	Reasonability check, compare size of input to size of past input for previous runs of the same process; record count for files, number or rate of messages, summarized data, etc.|	Receipt of data|	In-line measurement|
|11	|Completeness|	Record completeness - length|	Ensure length of records matches a defined expectation|	Condition of data upon receipt|	Process control|
|12	|Completeness	|Field completeness - non-nullable|	Ensure all non-nullable fields are populated|	Condition of data upon receipt|	Process control|
|13	|Integrity/Completeness|	Dataset integrity - deduping|	Identity and remove duplicate records	|Condition of data upon receipt|	Process control|
|14|	Integrity/Completeness|	Dataset integrity - duplicate record reasonability check	|Reasonability check, compare ratio of duplicate records to total records in a dataset to the ratio in previous instances of dataset|	Condition of data upon receipt|	Process control|
|15|	Completeness|	Field content completeness - defaults from source	|Reasonability check, compare the number and percentage of records defaulted for source-provided critical fields to a defined threshold or historical number and percentage|	Condition of data upon receipt|	In-line measurement|
|16|	Completeness|	Dataset completeness based on date criteria	|Ensure minimum and maximum dates on critical dates fields conform to a defined range identified parameters for loading data	|Condition of data upon receipt|	Process control|
|17|	Completeness|	Dataset reasonability based on date criteria|	Ensure minimum and maximum dates on critical dates fields conform to a reasonability rule|	Condition of data upon receipt|	In-line measurement|
|18|	Completeness|	Field content completeness - received data is missing fields critical to processing|	Inspect population of critical fields before processing records	|Condition of data upon receipt|	Process control|
|19|	Completeness|	Dataset completeness - balance record counts through a process|	Balance record counts throughout data processing, account for rejected records, including duplicates; for exact balance situations|	Data processing|	Process control|
|20|	Completeness|	Dataset completeness - reasons for rejecting records|	Reasonability check, compare number and percentage of records dropped for specific reasons with a defined threshold or historical number and percentage|	Data processing	|Process control|
|21|	Completeness|	Dataset completeness through a process - ratio of input to output	|Reasonability check, compare the ratio of process input/output to the ratio in previous instances situations|	Data processing	|In-line measurement|
|22|	Completeness|	Dataset completeness through a process - balance amount fields|	Amount field reasonability check, compare ratio of summed amount field input and output to ratio of previous instances of a dataset, for nonexact balance situations|	Content/amount fields	|In-line measurement|
|23|	Completeness|	Field content completeness - ratio of summed amount fields|	Amount field reasonability check, compare ratio of summed amount field input and output to ratio of previous instances of a dataset, for nonexact balance situations	|Content/amount fields|	In-line measurement|
|24|	Completeness|	Field content completeness - defaults from derivation (subtype of #33 multicolumn profile)|	Reasonability check, compare the number and number and percentage of records defaulted for derived fields to a defined threshold or historical number and percentage|	Data processing	|In-line measurement|
|25|	Timeliness|	Data Processing duration|	Reasonability check, compare process duration to historical process duration or to a defined time limit|	Data processing|	In-line measurement|
|26|	Timeliness|	Timely availability of data for access|	Compare actual time data is available for data consumers access to scheduled time of data availability|	Process/ Adherence to schedule|	In-line measurement|
|27|	Validity|	Validity check, single field, detailed results|	Compare values on incoming data to valid values in a defined domain (reference table, range, or mathematical rule)|	Content/row counts	|In-line measurement|
|28|	Validity|	Validity check, roll-up	|Summarize results of detailed validity check; compare roll-up counts and percentage of valid/invalid values to historical levels|	Content summaray	|In-line measurement|
|29|	Integrity/Validity|	Validity check, multiple columns within a table, detailed results|	Compare values in related columns on the same table to values in a mapped relation or business rule	|Content/row counts	|In-line measurement|
|30|	Consistency|	Consistent column profile|	Reasonability check, compare record count distribution of values (column profile) to past instances of data populating the same field	|Content/row counts|	In-line measurement|
|31|	Consistency|	Consistent dataset content, distinct count of represented entity, with ratios to record counts|	Reasonability check, compare distinct counts of entities represented within a dataset (e.g., the distinct number of customers represented in sales data) to threshold, historical counts, or total records|	Content summary|	In-line measurement|
|32|	Consistency	|Consistent dataset content, ratio of distinct counts of two represented entities|	Reasonability check, compare ratio between distinct counts of important fields/entities (e.g., customers/sales office, claims/insured person) to threshold or historical ratio	|Content summary|	In-line measurement|
|33|	Consistency	|Consistent multicolumn profile|	Reasonability check, compare record count distribution of values across multiple fields to historical percentages, in order to test business rules (multicolumn profile with qualifiers)|	Content/row counts|	In-line measurement|
|34|	Consistency|	Chronology consistentwith business rules within a table (subtype of #33 multicolumn profile)|	Reasonability check, compare date values to business rule for chronology|	Content/date content	|In-line measurement|
|35|	Consistency	|Consistent time elapsed (hours, days, months, etc.)|	Reasonability check, compare consistency of time elapsed to past instances of data populating the same fields|	Content/date content	|In-line measurement|
|36|	Consistency|	Consistent amount field calculations across secondary fields|	Reasonability check, compare amount column calculations, sum(total) amount, percentage of total amount, and average amount across a secondary field or fields to historical counts and percentages, with qualifiers to narrow results.|	Content/amount fields	|In-line measurement|
|37	|Consistency|	Consistent record counts by aggregated date|	Reasonability check, compare record counts and percentage of record counts associated an aggregated date, such as a month, quarter, or year, to historical counts and percentages|	Content/aggregated date|	Periodic measurement|
|38|	Consistency	|Consistent amount field data by aggregated date|	Reasonability check, compare amount field data (total amount, percentage of total amount) aggregated by date (month, quarter, or year) to historical total and percentage|	Content/aggregated date	|Periodic measurement|
|39|	Integrity/completeness|	Parent/child referential integrity|	Confirm referential integrity between parent/child tables to identify parentless child("orphan") records and values	|Cross-table content|	Periodic measurement
|40|	Integrity/Completeness|	Child/Parent referential integrity|	Confirm referential integrity between child/parent tables to identify childless parent records and values|	Cross-table content	|Periodic measurement|
|41	|Integrity/Validity|	Validity check, cross table, detailed results	|Compare values in a mapped or business rule relationship across tables to ensure data is associated consistently|	Cross-table content|	Periodic measurement|
|42|	Integrity/Consistency|	Consistent cross-table multicolumn profile|	Cross-table reasonability check, compare record count distribution of values across fields on related tables to historical percentage, in order to test adherence to business rules (multicolumn profile with qualifiers)	|Cross-table content|	Periodic measurement|
|43	|Integrity/Consistency	|Chronology consistent with business rules across-tables	|Cross-table reasonability, compare date values to business rule for chronology/cross-table|Content/chronology/cross-table|	Periodic measurement	|
|44|	Integrity/Consistency|	Consistent cross-table amount column calculations|	Cross-table reasonability check, compare summed amount calculations (total, percentage of total, average or ratios between these) on related tables|	Cross-table content/ amount fields|	Periodic measurement|
|45|	Integrity/Consistency|	Consistent cross-table amounts columns by aggregated dates|	Cross-table reasonability check, compare amount field data (total amount, percentage of total amount) associated date (month, quarter, or year) on related tables|	Cross-table content/aggregated date	|Periodic measurement|
|46	|Consistency|	Consistency compared to external benchmarks|	Compare data quality measurement results to a set of benchmarks, such external industry or nationally established measurements for similar data|	Overal database content|	Periodic measurement|
|47|	Completeness|	Dataset completeness - overall sufficiency for defined purposes|	Compare macro database content (e.g., data domains, sources, number of records, historical breadth of data, represented entities) to requirements for specific uses of the data	|Overall database content	|Periodic measurement|
|48|	Completeness|	Dataset completeness - overall sufficiency of measures and controls	|Assess effectiveness of measurements and controls|	Overall database database content|	Periodic assessment|


### Model Engine
- Job Scheduler
- Model Execution, support both real-time data and batch data

## 3. DQ Scorecards
### Model Consumption
- Model consumer could be different from creator, consumer defines his own threshold and notification email
- Customize dashboard

### Heatmap
- Different heatmaps for different users

### Notification
- A notification service supports different types of notifications, such as email, information bar on web UI, twitter, etc.
- Support Email Reporting first: Subscribe the metrics for email reporting

## 4. Issue Resolution
- Download sample data

## 5. Metadata Management
- Data Asset Registration, support HDFS, HiveTable, RDBMS, etc.
- Dataset definition: how to retrieve the datasets for daily/weekly/hourly calculations
- Schema Registration
- Organization management
- Others

## 6. Security
