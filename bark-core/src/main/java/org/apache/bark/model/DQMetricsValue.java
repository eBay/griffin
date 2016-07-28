/*
	Copyright (c) 2016 eBay Software Foundation.
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package org.apache.bark.model;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
//import com.google.code.morphia.annotations.Id;
import javax.xml.bind.annotation.XmlRootElement;



//import com.google.code.morphia.annotations.Entity;
//import com.google.code.morphia.annotations.Property;
@XmlRootElement
//@Entity("dq_metrics_values")
public class DQMetricsValue extends BaseObj implements Comparable<DQMetricsValue> {

	private Long _id;

	@NotNull
	@Pattern(regexp="\\A([0-9a-zA-Z\\_\\-])+$")
	private String metricName;

	//@Property("metricType")
	//private String metricType;

	//@Property("assetId")
	private String assetId;

	//@Property("timestamp")
	@Min(0)
	private long timestamp;

	@Min(0)
	private float value;

	public DQMetricsValue()
	{
		;
	}

	public DQMetricsValue(String name, long timestamp, float value)
	{
		this.metricName = name;
		//this.metricType = type;
		//this.assetId = id;
		this.timestamp = timestamp;
		this.value = value;
	}

	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public String getMetricName() {
		return metricName;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	//	public String getMetricType() {
	//		return metricType;
	//	}
	//
	//	public void setMetricType(String metricType) {
	//		this.metricType = metricType;
	//	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	@Override
	public int compareTo(DQMetricsValue o) {
		return o.getTimestamp() == this.getValue() ? 0 :
			(o.getTimestamp() > this.getTimestamp() ? 1 : -1);
	}

}