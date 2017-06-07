package com.ebay.oss.griffin.domain;

public class MetricsDetails {
	long timestamp;
    double value;
    String bolling;
    double comparisionValue;
    long count;

    String mad;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getBolling() {
        return bolling;
    }

    public void setBolling(String bolling) {
        this.bolling = bolling;
    }

    public double getComparisionValue() {
        return comparisionValue;
    }

    public void setComparisionValue(double comparisionValue) {
        this.comparisionValue = comparisionValue;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getMad() {
        return mad;
    }

    public void setMad(String mad) {
        this.mad = mad;
    }

    public MetricsDetails(long timestamp, double value, long count) {
        this.timestamp = timestamp;
        this.value = value;
        this.bolling = null;
        this.comparisionValue = 0.0;
        this.count = count;
        this.mad = null;
    }

}
