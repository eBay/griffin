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
package org.apache.bark.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.apache.bark.model.AssetLevelMetrics;
import org.apache.bark.model.AssetLevelMetricsDetail;
import org.apache.bark.service.DQMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
//@Scope("request")
@Path("/report")
public class ReportController {

	@Autowired
	private DQMetricsService dqMetricsService;


	@GET
	@Path("/be")
	@Produces(MediaType.APPLICATION_JSON)
	public String getBEReportByName(@QueryParam("name") String name) {

		return "{\"title\": \"Bullseye e2e Visitor Data Quality Validation Report\", \"details\":[{\"name\":\"search\", \"value\":99.38}, {\"name\":\"viewitem\", \"value\":64.54}]}";

	}

	@GET
	@Path("/24h")
	@Produces(MediaType.APPLICATION_JSON)
	public AssetLevelMetrics getBrief(@QueryParam("name") String name) {
		return dqMetricsService.oneDataBriefDashboard(name);
	}

	@GET
	@Path("/bar")
	@Produces(MediaType.APPLICATION_JSON)
	public BarReport getBarReport(@QueryParam("org") String org, @QueryParam("asset") String asset) {

		AssetLevelMetrics metrics = null;
		BarReport report = new BarReport();
		report.details = new ArrayList<BarPoint>();
		if("viewitem".equals(asset)){
			report.setTitle("Bullseye viewitem daily DQ");

			metrics = dqMetricsService.metricsForReport("accuracy_viewitem_queue");
			report.details.add(new BarPoint("Tier2_LVS_Recognized_User", calAvg(metrics)));

			metrics = dqMetricsService.metricsForReport("accuracy_viewitem_e2e_user");
			report.details.add(new BarPoint("e2e_user", calAvg(metrics)));

			metrics = dqMetricsService.metricsForReport("accuracy_viewitem_e2e_visitor");
			report.details.add(new BarPoint("e2e_visitor", calAvg(metrics)));
		}else if("search".equals(asset)){
			report.setTitle("Bullseye search daily DQ");

			metrics = dqMetricsService.metricsForReport("accuracy_search_queue");
			report.details.add(new BarPoint("Tier2_LVS_Recognized_User", calAvg(metrics)));

			metrics = dqMetricsService.metricsForReport("accuracy_search_e2e_user");
			report.details.add(new BarPoint("e2e_user", calAvg(metrics)));

			metrics = dqMetricsService.metricsForReport("accuracy_search_e2e_visitor");
			report.details.add(new BarPoint("e2e_visitor", calAvg(metrics)));
		}else if("bid_new".equals(asset)){
			report.setTitle("Bullseye bid_new daily DQ");

			metrics = dqMetricsService.metricsForReport("accuracy_bid_new_queue");
			report.details.add(new BarPoint("bid_new", metrics.getDq()));

		}else if("item_watch".equals(asset)){
			report.setTitle("Bullseye item_watch daily DQ");

			metrics = dqMetricsService.metricsForReport("accuracy_item_watch_queue");
			report.details.add(new BarPoint("item_watch", metrics.getDq()));

		}else if("transaction_new".equals(asset)){
			report.setTitle("Bullseye transaction_new daily DQ");

			metrics = dqMetricsService.metricsForReport("accuracy_viewitem_queue");
			report.details.add(new BarPoint("transation_new", metrics.getDq()));

		}

		return report;
		//    	return "{\"title\": \"Bullseye viewitem daily DQ\", \"details\":[{\"name\":\"Tier2_LVS_Recognized_User\", \"value\":93.01}, {\"name\":\"e2e_Vistor\", \"value\":99.38}, {\"name\":\"e2e_User\", \"value\":64.54}]}";

	}


	@GET
	@Path("/line")
	@Produces(MediaType.APPLICATION_JSON)
	public LineReport getLineReport(@QueryParam("org") String org, @QueryParam("asset") String asset) {
		AssetLevelMetrics metrics = null;
		LineReport report = new LineReport();
		report.items = new ArrayList<LineItem>();
		LineItem item = null;
		if("viewitem".equals(asset)){
			report.setTitle("Bullseye viewitem hourly DQ");

			metrics = dqMetricsService.metricsForReport("accuracy_viewitem_queue");

			item = new LineItem();
			item.name = "Tier2_LVS_Recognized_User";
			item.details = new ArrayList<LinePoint>();

			List<AssetLevelMetricsDetail> list = metrics.getDetails();

			for(AssetLevelMetricsDetail detail:list){
				item.details.add(new LinePoint(detail.getTimestamp(), detail.getValue()));
			}

			report.items.add(item);
			/////////////////////////////////////////////////////////////////////////


			metrics = dqMetricsService.metricsForReport("accuracy_viewitem_e2e_user");
			item = new LineItem();
			item.name = "e2e_user";
			item.details = new ArrayList<LinePoint>();

			list = metrics.getDetails();

			for(AssetLevelMetricsDetail detail:list){
				item.details.add(new LinePoint(detail.getTimestamp(), detail.getValue()));
			}

			report.items.add(item);
			///////////////////////////////////////////////////////////////////////////
			metrics = dqMetricsService.metricsForReport("accuracy_viewitem_e2e_visitor");
			item = new LineItem();
			item.name = "e2e_visitor";
			item.details = new ArrayList<LinePoint>();

			list = metrics.getDetails();

			for(AssetLevelMetricsDetail detail:list){
				item.details.add(new LinePoint(detail.getTimestamp(), detail.getValue()));
			}

			report.items.add(item);
		}else if("search".equals(asset)){
			report.setTitle("Bullseye search hourly DQ");

			metrics = dqMetricsService.metricsForReport("accuracy_search_queue");
			item = new LineItem();
			item.name = "Tier2_LVS_Recognized_User";
			item.details = new ArrayList<LinePoint>();

			List<AssetLevelMetricsDetail> list = metrics.getDetails();

			for(AssetLevelMetricsDetail detail:list){
				item.details.add(new LinePoint(detail.getTimestamp(), detail.getValue()));
			}

			report.items.add(item);
			/////////////////////////////////////////////////////////////////////////


			metrics = dqMetricsService.metricsForReport("accuracy_search_e2e_user");
			item = new LineItem();
			item.name = "e2e_user";
			item.details = new ArrayList<LinePoint>();

			list = metrics.getDetails();

			for(AssetLevelMetricsDetail detail:list){
				item.details.add(new LinePoint(detail.getTimestamp(), detail.getValue()));
			}

			report.items.add(item);
			///////////////////////////////////////////////////////////////////////////
			metrics = dqMetricsService.metricsForReport("accuracy_search_e2e_visitor");
			item = new LineItem();
			item.name = "e2e_visitor";
			item.details = new ArrayList<LinePoint>();

			list = metrics.getDetails();

			for(AssetLevelMetricsDetail detail:list){
				item.details.add(new LinePoint(detail.getTimestamp(), detail.getValue()));
			}

			report.items.add(item);
		}

		return report;

		//		return createMockData();

		//    	return "{\"title\": \"Bullseye viewitem daily DQ\", \"details\":[{\"name\":\"Tier2_LVS_Recognized_User\", \"value\":93.01}, {\"name\":\"e2e_Vistor\", \"value\":99.38}, {\"name\":\"e2e_User\", \"value\":64.54}]}";

	}

	private float calAvg(AssetLevelMetrics metrics){
		List<AssetLevelMetricsDetail> list = metrics.getDetails();
		float result = 0f;
		if(list != null && list.size() > 0){
			for(AssetLevelMetricsDetail detail:list){
				result += detail.getValue();
			}
			result = result / list.size();
		}
		return result;
	}

	private LineReport createMockData(){
		LineReport report = new LineReport();
		report.title = "Bullseye viewitem hourly DQ";
		report.items = new ArrayList<LineItem>();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-7:00"));
		long seed = 0;
		try {
			seed = dateFormat.parse("2016-06-13T00:00:00").getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Random rdm = new Random(new Date().getTime());

		LineItem item = new LineItem();
		item.name = "Tier2_LVS_Recognized_User";
		item.details = new ArrayList<LinePoint>();
		for(int i = 0; i < 24; i ++){
			item.details.add(new LinePoint(seed + i*3600*1000, 95 + rdm.nextFloat()*5));
		}
		report.items.add(item);

		item = new LineItem();
		item.name = "e2e_Vistor";
		item.details = new ArrayList<LinePoint>();
		for(int i = 0; i < 24; i ++){
			item.details.add(new LinePoint(seed + i*3600*1000, 95 + rdm.nextFloat()*5));
		}
		report.items.add(item);

		item = new LineItem();
		item.name = "e2e_User";
		item.details = new ArrayList<LinePoint>();
		for(int i = 0; i < 24; i ++){
			item.details.add(new LinePoint(seed + i*3600*1000, 95 + rdm.nextFloat()*5));
		}
		report.items.add(item);


		return report;
	}

}

@XmlRootElement
class LineReport{
	String title;
	List<LineItem> items;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<LineItem> getItems() {
		return items;
	}
	public void setItems(List<LineItem> items) {
		this.items = items;
	}
}

@XmlRootElement
class LineItem{
	String name;
	List<LinePoint> details;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<LinePoint> getDetails() {
		return details;
	}
	public void setDetails(List<LinePoint> details) {
		this.details = details;
	}
}

@XmlRootElement
class LinePoint{
	long timestamp;
	float value;
	private LinePoint() {}
	public LinePoint(long timestamp, float value) {
		super();
		this.timestamp = timestamp;
		this.value = value;
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

}

@XmlRootElement
class BarReport{
	String title;
	List<BarPoint> details;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<BarPoint> getDetails() {
		return details;
	}
	public void setDetails(List<BarPoint> details) {
		this.details = details;
	}

}

@XmlRootElement
class BarPoint{
	String name;
	float value;

	private BarPoint() {}

	public BarPoint(String name, float value) {
		super();
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}

}
