package com.ebay.oss.griffin.domain;

import java.util.List;
public class ListMetricsValue {
	String name;
    double dq;
    long count;
    long dqfail;
    long timestamp;
    String metricType;
    String assetName;
    List<MetricsDetails> details;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDq() {
        return dq;
    }

    public void setDq(double dq) {
        this.dq = dq;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getDqfail() {
        return dqfail;
    }

    public void setDqfail(long dqfail) {
        this.dqfail = dqfail;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public List<MetricsDetails> getDetails() {
        return details;
    }

    public void setDetails(List<MetricsDetails> details) {
        this.details = details;
    }

}
