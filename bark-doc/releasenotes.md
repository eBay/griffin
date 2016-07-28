# Bark 0.0.1 Release Notes

## Summary
Bark is a Data Quality solution for distributed data systems at any scale in both streaming or batch data context. The below features are released in version 0.0.1

- Register/delete data asset
- Create/edit/delete Model(Accuracy, Validity, Anomaly Detection, Publish Metrics)
- Metrics calculation on Spark
- Metrics visualization

For a complete list of the features, please refer to [Functional Specification Document](FSD.md).

## Known Issues
- [#109](https://github.corp.ebay.com/bark/barkweb/issues/109) User can see all metrics on "My Dashboard" the 1st time
- [#110](https://github.corp.ebay.com/bark/barkweb/issues/110) Replace Highcharts with open source JS chart library
- [#113](https://github.corp.ebay.com/bark/barkweb/issues/113) "%" symbol on the chart when the is actually a "count" value
- [#115](https://github.corp.ebay.com/bark/barkweb/issues/115) Page isn't navigated properly when registering data asset if same name exists
