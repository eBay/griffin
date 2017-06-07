package com.ebay.oss.griffin.domain;


//import com.google.code.morphia.annotations.Entity;
import java.io.Serializable;

//@Entity
public class MetricValue implements Serializable {
//    @Id
//    @GeneratedValue(strategy= GenerationType.AUTO)
//    private long _id;
    private String metricName;
    private long timestamp;
    private double value;
    long count;

//    public long get_id() {
//        return _id;
//    }
//
//    public void set_id(long _id) {
//        this._id = _id;
//    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

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

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
