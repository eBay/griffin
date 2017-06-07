package com.ebay.oss.griffin.service;

import com.ebay.oss.griffin.domain.MetricValue;
import com.google.gson.Gson;
import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
public class RollupService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RollupService.class);
    private DB db;
    private String dailyColllectionName;
//    @Autowired
//    private SchedulerFactoryBean factory;

    public DBCollection dbCollection=null;

    public RollupService(){
        Properties env = new Properties();
        try {
            env.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("application.properties"));
            String mongoServer = env.getProperty("spring.data.mongodb.host");
            int mongoPort = Integer.parseInt(env
                    .getProperty("spring.data.mongodb.port"));
            String dbName=env.getProperty("spring.data.mongodb.database");
            db = new MongoClient(mongoServer, mongoPort).getDB(dbName);
            dbCollection = db.getCollection("dq_metrics_values");
            dailyColllectionName=env.getProperty("dailyCollection.name");
            System.out.println(dbCollection.getName()+":"+dbCollection.find().count());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//	public List<MetricValue> getAll(){
//        List<MetricValue> result=new ArrayList<MetricValue>();
//        //.limit(3)
//        for (DBObject dbo:dbCollection.find(new BasicDBObject("metricName","nrt_accu_5814_AmazonPriceRefresh")).sort(new BasicDBObject("timestamp",-1))){
//            result.add(toEntity(dbo));
//        }
//        return result;
//    }

    public List<MetricValue> getAllBetweenTime(DBCollection collection,long startTime,long endTime){
        BasicDBObject query = new BasicDBObject();
        query.put("timestamp", BasicDBObjectBuilder.start("$gte", startTime).add("$lte", endTime).get());
        List<MetricValue> result=new ArrayList<MetricValue>();
        for (DBObject dbo:collection.find(query).sort(new BasicDBObject("timestamp", -1))){
            result.add(toEntity(dbo));
        }
        return result;
    }

	public List<MetricValue> getDailyMetrics(int days){
        DBCollection dailyCollection=db.getCollection(dailyColllectionName);
        DBCursor cur=dailyCollection.find().sort(new BasicDBObject("timestamp", -1)).limit(1);
        
        if (cur.hasNext()) {
        	DBObject obj = cur.next();
        	MetricValue lastestMetric=toEntity(obj);
            long latestTimestamp=lastestMetric.getTimestamp();
            long daysAgoTimestamp=latestTimestamp-(long)days*24*3600*1000;
            return getAllBetweenTime(dailyCollection,daysAgoTimestamp,latestTimestamp);
        } else {
        	return new ArrayList<MetricValue>();
        }
    }

    public boolean deleAllDailyMetrics(){
        DBCollection dailyCollection=db.getCollection(dailyColllectionName);
        if (dailyCollection==null || dailyCollection.find().count()==0){
            return false;
        }
        dailyCollection.drop();
        return true;
    }

   

    public MetricValue toEntity(DBObject dbo) {
        Gson gson = new Gson();
        return gson.fromJson(dbo.toString(), MetricValue.class);
    }

}
