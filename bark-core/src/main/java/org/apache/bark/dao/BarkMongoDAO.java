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
package org.apache.bark.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.bark.common.KeyValue;
import org.apache.bark.common.ModelTypeConstants;
import org.apache.bark.common.ValidityTypeConstants;
import org.apache.bark.model.DQJob;
import org.apache.bark.model.DQMetricsValue;
import org.apache.bark.model.DQModelEntity;
import org.apache.bark.model.DQSchedule;
import org.apache.bark.model.DataAsset;
import org.apache.bark.model.MappingItemInput;
import org.apache.bark.model.ModelInput;
import org.apache.bark.model.SampleFilePathLKP;
import org.apache.bark.model.UserSubscribeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class BarkMongoDAO {
	private static Logger logger = LoggerFactory.getLogger(BarkMongoDAO.class);

	private static BarkMongoDAO dao;

	public static MongoClient mongo;
	public static DB db;
	public static DBCollection dq_metrics_values;
	public static DBCollection dq_model;
	public static DBCollection data_assets;
	public static DBCollection dq_job;
	public static DBCollection dq_schedule;
	public static DBCollection user_subscribe;
	public static DBCollection dq_missed_file_path_lkp;
	public static DBCollection SEQUENCES;

	public static String model_seq = "DQ_MODEL_NO";
	public static String metric_seq = "DQ_METRICS_SEQ_NO";
	public static String asset_seq = "DQ_DATA_ASSET_SEQ_NO";
	public static String job_seq = "DQ_JOB_SEQ_NO";
	public static String missing_data_file_seq = "DQ_MISSED_FILE_SEQ_NO";

	private BarkMongoDAO() {
		try {
			Properties env = new Properties();
			env.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("application.properties"));
			String mongoServer = env.getProperty("spring.data.mongodb.host");
			int mongoPort = Integer.parseInt(env
					.getProperty("spring.data.mongodb.port"));

			mongo = new MongoClient(mongoServer, mongoPort);

			db = mongo.getDB("unitdb0");
			dq_metrics_values = db.getCollection("dq_metrics_values");
			dq_model = db.getCollection("dq_model");
			dq_job = db.getCollection("dq_job");
			dq_schedule = db.getCollection("dq_schedule");
			data_assets = db.getCollection("data_assets");
			user_subscribe = db.getCollection("user_subscribe");
			dq_missed_file_path_lkp = db
					.getCollection("dq_missed_file_path_lkp");

			SEQUENCES = db.getCollection("SEQUENCES");

		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e.toString());
		}
	}

	public static BarkMongoDAO getModelDAO() {
		if (dao == null)
			dao = new BarkMongoDAO();
		return dao;
	}

	public List<DQModelEntity> getAllModelsByStatus(int status) {
		List<DQModelEntity> result = new ArrayList<DQModelEntity>();
		try {
			DBCursor temp = dq_model.find(new BasicDBObject("status", status));
			List<DBObject> all = temp.toArray();

			Gson gson = new Gson();
			for (int i = 0; i < all.size(); i++) {
				result.add(gson.fromJson(all.get(i).toString(),
						DQModelEntity.class));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e.toString());
		}
		return result;
	}

	public void upsertModel(DQModelEntity entity)
	{
		DBObject temp = dq_model.findOne(new BasicDBObject("modelId", entity.getModelName()));
		if(temp != null) dq_model.remove(temp);

		Gson gson = new Gson();
		DBObject t1 = (DBObject) JSON.parse(gson.toJson(entity));
		dq_model.save(t1);
	}

	public List<DBObject> getDBObjectAllModels() {
		try {

			DBCursor temp = dq_model.find();
			List<DBObject> all = temp.toArray();

			return all;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e.toString());
		}
		return null;
	}

	public List<DQModelEntity> getAllModels() {
		List<DQModelEntity> result = new ArrayList<DQModelEntity>();
		try {
			DBCursor temp = dq_model.find();
			List<DBObject> all = temp.toArray();

			Gson gson = new Gson();
			for (int i = 0; i < all.size(); i++) {
				result.add(gson.fromJson(all.get(i).toString(),
						DQModelEntity.class));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e.toString());
		}
		return result;
	}


	//get models concerned with data asset
	//allConcerned:	false-	only the models directly concerned with the data asset
	//				true -	the models directly concerned and non-directly concerned(eg. as the source asset of accuracy model)
	public List<DQModelEntity> getModelsByDataAsset(DataAsset da, boolean allConcerned) {
		List<DQModelEntity> result = new ArrayList<DQModelEntity>();
		List<DQModelEntity> allModels = getAllModels();
		Iterator<DQModelEntity> itr = allModels.iterator();
		while (itr.hasNext()) {
			DQModelEntity me = itr.next();
			if (me.getAssetId() == da.get_id()) {	//concerned directly
				result.add(me);
			} else if (allConcerned) {	//check the non-directly concerned models
				if (me.getModelType() == ModelTypeConstants.ACCURACY) {		//accuracy
					//find model
					DQModelEntity entity = findByName(me.getModelName());
					ModelInput mi = new ModelInput();
					mi.parseFromString(entity.getModelContent());

					//get mapping list to get the asset name
					String otherAsset = "";
					List<MappingItemInput> mappingList = mi.getMappings();
					Iterator<MappingItemInput> mpitr = mappingList.iterator();
					while (mpitr.hasNext()) {
						MappingItemInput mapping = mpitr.next();
						//since the target data asset is directly concerned, we should get source as the other one
						String col = mapping.getSrc();
						otherAsset = col.replaceFirst("\\..+", "");	//delete from the first .xxxx
						if (!otherAsset.isEmpty()) break ;
					}

					//check the other asset name equals to this asst or not
					if (otherAsset.equals(da.getAssetName())) {	//concerned non-directly
						result.add(me);
					}
				}
			}
		}
		return result;
	}

	public void insertModel(DQModelEntity entity) {
		Gson gson = new Gson();
		// DBObject t1 = gson.fromJson(gson.toJson(entity),
		// BasicDBObject.class);
		DBObject t1 = (DBObject) JSON.parse(gson.toJson(entity));
		dq_model.save(t1);
	}

	public void updateModel(DBObject old, DQModelEntity entity) {
		Gson gson = new Gson();
		// DBObject t1 = gson.fromJson(gson.toJson(entity),
		// BasicDBObject.class);
		DBObject t1 = (DBObject) JSON.parse(gson.toJson(entity));
		dq_model.update(old, t1);
	}

	public void deleteModelbyId(String id) {
		DBObject temp = dq_model.findOne(new BasicDBObject("_id", Long
				.parseLong(id)));
		if (temp != null)
			dq_model.remove(temp);
	}

	public Long getNextModelSequence() {
		return getNextSequence(model_seq);
	}

	public Long getNextMetricsSequence() {
		return getNextSequence(metric_seq);
	}

	public Long getNextAssetSequence() {
		return getNextSequence(asset_seq);
	}

	public Long getNextJobSequence() {
		return getNextSequence(job_seq);
	}

	public Long getNextFilePathSequence() {
		return getNextSequence(missing_data_file_seq);
	}

	public Long getNextSequence(String seq) {
		DBObject temp = SEQUENCES.findOne(new BasicDBObject("_id", seq));

		BasicDBObject document = new BasicDBObject();
		document.put("_id", seq);
		document.put(seq, new Long(
				(Long.parseLong(temp.get(seq).toString()) + 1)));

		SEQUENCES.save(document);

		return new Long((Long.parseLong(temp.get(seq).toString()) + 1));
	}

	public DQModelEntity findByColumn(String colName, String value) {

		DBObject temp = dq_model.findOne(new BasicDBObject(colName, value));

		if (temp == null) {
			return null;
		} else {
			Gson gson = new Gson();
			return gson.fromJson(temp.toString(), DQModelEntity.class);
		}

	}

	public DQModelEntity findByName(String name) {

		DBObject temp = dq_model.findOne(new BasicDBObject("modelName", name));

		if (temp == null) {
			return null;
		} else {
			Gson gson = new Gson();
			return gson.fromJson(temp.toString(), DQModelEntity.class);
		}

	}

	public DBObject findCountModelByAssetID(int assetID) {

		DBCursor temp = dq_model.find(new BasicDBObject("assetId", assetID));
		List<DBObject> all = temp.toArray();

		for (DBObject tempDBObject : all) {
			if (tempDBObject.get("modelType").equals(
					ModelTypeConstants.VALIDITY)) {
				String content = tempDBObject.get("modelContent").toString();
				String[] contents = content.split("\\|");
				if (contents[2].equals(ValidityTypeConstants.TOTAL_COUNT + "")) {
					return tempDBObject;
				}
			}
		}

		return null;

	}

	public void addModelReference(DBObject old, String reference) {
		dq_model.remove(old);
		if (old.containsKey("referenceModel"))
			if (!old.get("referenceModel").equals(""))
				old.put("referenceModel", old.get("referenceModel") + ","
						+ reference);
			else
				old.put("referenceModel", reference);
		dq_model.save(old);
	}

	public DBObject getAllMetricsByCondition(List<KeyValue> queryList) {
		// dq_metrics_values.
		BasicDBObject document = new BasicDBObject();
		for (KeyValue k : queryList) {
			document.put(k.getKey(), k.getValue());
		}

		DBObject o = dq_metrics_values.findOne(document);
		return o;
		// if(o!=null)
		// {
		// DQMetricsValue result = new
		// DQMetricsValue(o.get("metricName").toString()
		// ,o.get("metricType").toString()
		// ,o.get("assetId").toString()
		// ,(Date)o.get("timestamp")
		// ,Float.parseFloat(o.get("value").toString()));
		// result.set_id((Long)o.get("_id"));
		// return result;
		// }
		// else
		// return null;
	}

	public void saveDQMetricsValue(DQMetricsValue entity) {
		Gson gson = new Gson();
		// DBObject t1 = gson.fromJson(gson.toJson(entity),
		// BasicDBObject.class);
		DBObject t1 = (DBObject) JSON.parse(gson.toJson(entity));
		dq_metrics_values.save(t1);
	}

	public void updateDQMetricsValue(DQMetricsValue entity, DBObject old) {
		Gson gson = new Gson();
		// DBObject t1 = gson.fromJson(gson.toJson(entity),
		// BasicDBObject.class);
		DBObject t1 = (DBObject) JSON.parse(gson.toJson(entity));
		dq_metrics_values.update(old, t1);
	}

	public void saveDataAsset(DataAsset entity) {
		Gson gson = new Gson();
		// DBObject t1 = gson.fromJson(gson.toJson(entity),
		// BasicDBObject.class);
		DBObject t1 = (DBObject) JSON.parse(gson.toJson(entity));
		data_assets.save(t1);
	}

	public void updateDataAsset(DataAsset entity, DBObject old) {
		Gson gson = new Gson();
		// DBObject t1 = gson.fromJson(gson.toJson(entity),
		// BasicDBObject.class);
		DBObject t1 = (DBObject) JSON.parse(gson.toJson(entity));
		data_assets.remove(old);
		data_assets.save(t1);
	}

	public DQMetricsValue getLatestMetricsByAssetId(String assetId) {
		DBCursor temp = dq_metrics_values.find(new BasicDBObject("assetId",
				assetId));
		List<DBObject> all = temp.toArray();
		long latest = 0L;
		DBObject latestObject = null;
		for (DBObject o : all) {
			if (Long.parseLong(o.get("timestamp").toString()) - latest > 0) {
				latest = Long.parseLong(o.get("timestamp").toString());
				latestObject = o;
			}
		}
		if (latestObject == null)
			return null;
		else
			return new DQMetricsValue(
					latestObject.get("metricName").toString(),
					Long.parseLong(latestObject.get("timestamp").toString()),
					Float.parseFloat(latestObject.get("value").toString()));
	}

	public List<DBObject> getAllMetrics() {
		DBCursor temp = dq_metrics_values.find().sort(
				new BasicDBObject("timestamp", -1));
		List<DBObject> all = temp.toArray();
		return all;
	}

	public List<DQMetricsValue> getAllMetricsByMetricsName(String name) {
		DBCursor temp = dq_metrics_values.find(new BasicDBObject("metricName",
				name));
		List<DBObject> all = temp.toArray();
		List<DQMetricsValue> result = new ArrayList<DQMetricsValue>();
		for (DBObject o : all) {
			result.add(new DQMetricsValue(o.get("metricName").toString(), Long
					.parseLong(o.get("timestamp").toString()), Float
					.parseFloat(o.get("value").toString())));
		}
		return result;
	}

	public List<DBObject> getAllAssets() {
		DBCursor temp = data_assets.find();
		List<DBObject> all = temp.toArray();
		return all;
	}

	public DBObject getAssetByCondition(List<KeyValue> queryList) {
		// dq_metrics_values.
		BasicDBObject document = new BasicDBObject();
		for (KeyValue k : queryList) {
			document.put(k.getKey(), k.getValue());
		}

		DBObject o = data_assets.findOne(document);
		return o;
		// if(o!=null)
		// {
		// DataAsset result = new DataAsset(o);
		// return result;
		// }
		// else
		// return null;
	}

	public DataAsset getAssetById(Long id) {
		DBObject o = data_assets.findOne(new BasicDBObject("_id", id));
		if (o != null)
			return new DataAsset(o);
		else
			return null;
	}

	public void deleteAssetbyId(Long id) {
		DBObject temp = data_assets.findOne(new BasicDBObject("_id", id));
		if (temp != null)
			data_assets.remove(temp);
	}

	public DBObject getValidateSchedule(int assetId) {
		BasicDBObject document = new BasicDBObject();
		document.put("assetId", assetId);
		document.put("jobType", ModelTypeConstants.VALIDITY);
		return dq_schedule.findOne(document);
	}

	public void upsertNewSchedule(DQSchedule schedule, int type) {
		DBObject temp = null;
		if (type == ModelTypeConstants.ACCURACY)
			temp = dq_schedule.findOne(new BasicDBObject("modelList", schedule
					.getModelList()));
		else if (type == ModelTypeConstants.VALIDITY)
			temp = getValidateSchedule(schedule.getAssetId());
		if (temp != null)
			dq_schedule.remove(temp);
		Gson gson = new Gson();
		DBObject t1 = (DBObject) JSON.parse(gson.toJson(schedule));
		dq_schedule.save(t1);
	}

	public void upsertNewSchedule(DBObject schedule, int type) {
		DBObject temp = null;
		if (type == ModelTypeConstants.ACCURACY)
			temp = dq_schedule.findOne(new BasicDBObject("modelList", schedule
					.get("modelList")));
		else if (type == ModelTypeConstants.VALIDITY)
			temp = getValidateSchedule(Integer.parseInt(schedule.get("assetId")
					.toString()));
		if (temp != null)
			dq_schedule.remove(temp);
		dq_schedule.save(schedule);
	}

	public void updateScheduleTime(DBObject schedule, long time) {
		DBObject o = dq_schedule.findOne(new BasicDBObject("_id", schedule
				.get("_id")));
		if (o != null)
			dq_schedule.remove(o);
		o.put("starttime", time);
		dq_schedule.save(o);
	}

	public void deleteSchedule(String model) {
		DBObject temp = dq_schedule.findOne(new BasicDBObject("modelList",
				model));
		if (temp != null)
			dq_schedule.remove(temp);
	}

	public List<DBObject> getAllScheduling() {
		try {
			DBCursor temp = dq_schedule.find();
			List<DBObject> all = temp.toArray();

			return all;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int newJob(DQJob job) {
		try {
			DBObject temp = dq_job.findOne(new BasicDBObject("_id", job
					.get_id()));
			if (temp != null)
				return 0;
			Gson gson = new Gson();
			DBObject t1 = (DBObject) JSON.parse(gson.toJson(job));
			dq_job.save(t1);
			return 1;
		} catch (Exception e) {
			logger.warn("===========insert new job error==============="
					+ e.getMessage());
			return 0;
		}
	}

	public List<DBObject> getAllSchedulingByStatus(int status) {
		try {
			DBCursor temp = dq_job.find(new BasicDBObject("status", status));
			List<DBObject> all = temp.toArray();

			return all;
		} catch (Exception e) {
			logger.warn("===========get job with status==============="
					+ e.getMessage());
			return null;
		}
	}

	public void updateJobStatus(DBObject object, int status) {
		DBObject find = dq_job.findOne(object);
		if (find != null)
			dq_job.remove(object);

		object.put("status", status);
		dq_job.save(object);
	}

	public void updateJobEndingInfo(DBObject object, long endtime, float value) {
		DBObject find = dq_job.findOne(object);
		if (find != null)
			dq_job.remove(object);

		object.put("endtime", new Date().getTime());
		object.put("value", value);
		dq_job.save(object);
	}

	public DBObject getJobbyId(String id) {
		DBObject temp = dq_job.findOne(new BasicDBObject("_id", id));
		return temp;
	}

	public void upsertUserSubscribe(UserSubscribeItem item) {
		if (item.getNtaccount() == null)
			return;
		item.set_id(item.getNtaccount());// user_subscribe

		DBObject find = user_subscribe.findOne(new BasicDBObject("_id", item
				.get_id()));
		if (find != null)
			user_subscribe.remove(find);

		Gson gson = new Gson();
		DBObject t1 = (DBObject) JSON.parse(gson.toJson(item));
		user_subscribe.save(t1);
	}

	public UserSubscribeItem getUserSubscribeItem(String user) {
		Gson gson = new Gson();
		DBObject find = user_subscribe.findOne(new BasicDBObject("_id", user));
		if (find == null)
			return null;
		else
			return gson.fromJson(find.toString(), UserSubscribeItem.class);
	}

	public List<DBObject> findSampleByModelName(String name) {

		DBCursor temp = dq_missed_file_path_lkp
				.find(new BasicDBObject("modelName", name)).limit(20)
				.sort(new BasicDBObject("timestamp", -1));
		return temp.toArray();
	}

	public void insertNewSamplePath(SampleFilePathLKP entity) {
		Gson gson = new Gson();
		DBObject t1 = (DBObject) JSON.parse(gson.toJson(entity));
		dq_missed_file_path_lkp.save(t1);
	}
}
