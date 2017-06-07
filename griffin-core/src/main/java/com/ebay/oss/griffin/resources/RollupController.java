package com.ebay.oss.griffin.resources;
import com.ebay.oss.griffin.domain.CrawlerValue;
import com.ebay.oss.griffin.domain.ListMetricsValue;
import com.ebay.oss.griffin.domain.MetricsDetails;
import com.ebay.oss.griffin.domain.MetricValue;
import com.ebay.oss.griffin.service.RollupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
//@Scope("request")
@Path("/crawler")
public class RollupController {
	@Autowired
    RollupService metricService;

//	@GET
//	@Path("/metrics")
//	@Produces(MediaType.APPLICATION_JSON)
//    public List<MetricValue> findAll(){
//        return metricService.getAll();
//    }

//	@GET
//	@Path("/metrics/dailyMetrics/{days}")
//	@Produces(MediaType.APPLICATION_JSON)
//    public Map<String,List<MetricValue>> findDailyMetricsMap(@PathParam("days") int days){
//        List<MetricValue> dailyMetricsList=metricService.getDailyMetrics(days);
//        Map<String,List<MetricValue>> dailyMetricsMap=new HashMap<String,List<MetricValue>>();
//        for (MetricValue me:dailyMetricsList){
//            List<MetricValue> meList=new ArrayList<MetricValue>();
//            if(!dailyMetricsMap.containsKey(me.getMetricName())){
//                meList.add(me);
//                dailyMetricsMap.put(me.getMetricName(),meList);
//            }else{
//                meList=dailyMetricsMap.get(me.getMetricName());
//                meList.add(me);
//                dailyMetricsMap.put(me.getMetricName(),meList);
//            }
//        }
//        return dailyMetricsMap;
//    }

    /**
     *
     * @param days
     * @return
     */
	@GET
    @Path("/metrics/{days}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CrawlerValue> findDailyCrawler(@PathParam("days") int days){
        List<MetricValue> dailyMetricsList=metricService.getDailyMetrics(days);
        Map<String,List<MetricValue>> dailyMetricsMap=new HashMap<String,List<MetricValue>>();
        for (MetricValue me:dailyMetricsList){
            List<MetricValue> meList=new ArrayList<MetricValue>();
            if(!dailyMetricsMap.containsKey(me.getMetricName())){
                meList.add(me);
                dailyMetricsMap.put(me.getMetricName(),meList);
            }else{
                meList=dailyMetricsMap.get(me.getMetricName());
                meList.add(me);
                dailyMetricsMap.put(me.getMetricName(),meList);
            }
        }
        CrawlerValue crawlerValue=new CrawlerValue();
        crawlerValue.setName("Crawler");
        crawlerValue.setDq(0.0);
        List<ListMetricsValue> metrics=new ArrayList<ListMetricsValue>();
        for (String metricName:dailyMetricsMap.keySet()){
            List<MetricValue> eachNamedailyMetricsList=dailyMetricsMap.get(metricName);
            ListMetricsValue listMetricsValue=new ListMetricsValue();
            List<MetricsDetails> details=new ArrayList<MetricsDetails>();
            listMetricsValue.setName(metricName);
            long countSum=0;
            double valueSum=0.0;
            long lastestTimestamp=0l;
            for (MetricValue me:eachNamedailyMetricsList){
                if(me.getCount()!=0)
                    valueSum=valueSum+me.getValue()*(long)me.getCount()/100;
                countSum=countSum+me.getCount();
                lastestTimestamp=lastestTimestamp>me.getTimestamp()?lastestTimestamp:me.getTimestamp();
                details.add(new MetricsDetails(me.getTimestamp(),me.getValue(),me.getCount()));
            }
            double dq_value = 0;
            int dq_fail = 1;
            if(countSum > 0) dq_value = valueSum/countSum*100;
            if (dq_value > 95) dq_fail = 0;
            
            listMetricsValue.setDq(dq_value);
            listMetricsValue.setCount(countSum);
            listMetricsValue.setDqfail(dq_fail);
            listMetricsValue.setTimestamp(lastestTimestamp);
            listMetricsValue.setMetricType("");
            listMetricsValue.setAssetName(null);
            listMetricsValue.setDetails(details);
            metrics.add(listMetricsValue);
        }
        crawlerValue.setMetrics(metrics);
        List<CrawlerValue> res=new ArrayList<CrawlerValue>();
        res.add(crawlerValue);
        return res;
    }

	@GET
    @Path("/metrics/dailyMetricsList/{days}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MetricValue> findDailyMetricsList(@PathParam("days") int days){
        return metricService.getDailyMetrics(days);
    }

    @POST
    @Path("/metrics/delDailyMetricsDB")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean delDailyMetricsDB(){
        return metricService.deleAllDailyMetrics();
    }

}
