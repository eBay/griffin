package com.ebay.oss.griffin.domain;

import java.util.List;
public class CrawlerValue {
	
	String name;
    double dq;
    List<ListMetricsValue> metrics;

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

    public List<ListMetricsValue> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<ListMetricsValue> metrics) {
        this.metrics = metrics;
    }

}
