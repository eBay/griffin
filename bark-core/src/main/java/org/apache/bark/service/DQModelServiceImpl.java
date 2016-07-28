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
package org.apache.bark.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.bark.common.BarkDbOperationException;
import org.apache.bark.common.ModelSampleFlowConstants;
import org.apache.bark.common.ModelStatusConstants;
import org.apache.bark.common.ModelTypeConstants;
import org.apache.bark.common.ScheduleModelSeperator;
import org.apache.bark.common.ScheduleTypeConstants;
import org.apache.bark.common.ValidityTypeConstants;
import org.apache.bark.dao.BarkMongoDAO;
import org.apache.bark.model.DQJob;
import org.apache.bark.model.DQModelEntity;
import org.apache.bark.model.DQSchedule;
import org.apache.bark.model.DataAsset;
import org.apache.bark.model.MappingItemInput;
import org.apache.bark.model.ModelBasicInputNew;
import org.apache.bark.model.ModelExtraInputNew;
import org.apache.bark.model.ModelForFront;
import org.apache.bark.model.ModelInput;
import org.apache.bark.model.ModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.validation.annotation.Validated;

import com.mongodb.DBObject;

@Service
// @Validated
public class DQModelServiceImpl implements DQModelService {
	private static Logger logger = LoggerFactory
			.getLogger(DQModelServiceImpl.class);

	public static String MODEL_TYPE_ACCUATE = "Accruacy";
	public static String MODEL_TYPE_VALIDATE = "Validate";
	public static String MODEL_TYPE_PUBLISH = "Publish";
	public static String MODEL_CREATE_SCUCESS = "{\"status\":\"0\" , \"result\":\"success\"}";
	public static String MODEL_CREATE_NAME_EXISTS = "{\"status\":\"-1\" , \"result\":\"Model name already exists, please change it.\"}";

	// @Autowired
	// private DQModelDao dqModelDao;

	@Autowired
	private DataAssetService dataAssetService;

	public static List<ModelForFront> allModels;
	public static HashMap<String, String> thresholds;
	public static HashMap<String, String> references;

	@Override
	public List<ModelForFront> getAllModles() {
		List<DBObject> l = BarkMongoDAO.getModelDAO().getDBObjectAllModels();

		allModels = new ArrayList<ModelForFront>();
		thresholds = new HashMap<String, String>();
		references = new HashMap<String, String>();
		for (int i = 0; i < l.size(); i++) {
			DBObject o = l.get(i);
			allModels.add(new ModelForFront(o.get("modelName") == null ? null
					: o.get("modelName").toString(),
					o.get("system") == null ? -1 : Integer.parseInt(o.get(
							"system").toString()),
							o.get("modelDesc") == null ? null : o.get("modelDesc")
									.toString(), o.get("modelType") == null ? -1
											: Integer.parseInt(o.get("modelType").toString()),
											o.get("timestamp") == null ? new Date(0) : new Date(Long
													.parseLong(o.get("timestamp").toString())), o
													.get("status") == null ? null : o.get("status")
															.toString(), o.get("assetName") == null ? "unknown"
																	: o.get("assetName").toString(),
																	o.get("owner") == null ? null : o.get("owner").toString()));
			thresholds.put(o.get("modelName").toString(),
					o.get("threshold") == null ? null : o.get("threshold")
							.toString());
			if (o.get("referenceModel") != null) {
				if (o.get("referenceModel").toString().length() > 0) {
					references.put(o.get("modelName").toString(),
							o.get("referenceModel").toString());
				}
			}
		}

		return allModels;
	}

	@Override
	public int deleteModel(String name) throws BarkDbOperationException {
		try {
			DQModelEntity temp = BarkMongoDAO.getModelDAO().findByName(name);

			//flag concern metrics first
			flagConcernMetricsOfModel(temp);

			BarkMongoDAO.getModelDAO().deleteModelbyId(temp.get_id().toString());

			if (temp.getModelType() == ModelTypeConstants.ACCURACY)
				BarkMongoDAO.getModelDAO().deleteSchedule(name);
			else if (temp.getModelType() == ModelTypeConstants.VALIDITY) {
				DBObject currentSchedule = BarkMongoDAO.getModelDAO()
						.getValidateSchedule(temp.getAssetId());
				if (currentSchedule == null)
					return -1;
				if (currentSchedule.get("modelList").toString() == null)
					return -1;
				String rawModelList = currentSchedule.get("modelList")
						.toString();
				String newModelList = "";
				if (rawModelList.contains(ScheduleModelSeperator.SEPERATOR)) {
					String[] rawModelArray = rawModelList
							.split(ScheduleModelSeperator.SPLIT_SEPERATOR);
					for (int i = 0; i < rawModelArray.length; i++) {
						if (!rawModelArray[i].equals(name))
							newModelList = newModelList
							+ ScheduleModelSeperator.SEPERATOR
							+ rawModelArray[i];
					}
					newModelList = newModelList
							.substring(ScheduleModelSeperator.SEPERATOR
									.length());
					currentSchedule.put("modelList", newModelList);
					BarkMongoDAO.getModelDAO().upsertNewSchedule(
							currentSchedule, temp.getModelType());
				} else {
					if (rawModelList.equals(name))
						BarkMongoDAO.getModelDAO().deleteSchedule(name);
				}
			}

			return 0;
		} catch (Exception e) {
			logger.warn(e.toString());
			throw new BarkDbOperationException("Failed to delete model of '"
					+ name + "'", e);
		}
	}

	//flag all the metrics concerned with the given model
	private void flagConcernMetricsOfModel(DQModelEntity model) {
		if (model == null) return ;
		//ModelInput mi = convertModel(model);
		//get the concern metrics and flag them...
	}

	@Override
	public DQModelEntity getGeneralModel(String name) {
		return BarkMongoDAO.getModelDAO().findByName(name);
	}

	public int checkGeneralModelByName(String name) {
		DQModelEntity sourceObject = BarkMongoDAO.getModelDAO()
				.findByName(name);
		if (sourceObject == null)
			return 0;
		else
			return 1;
	}

	@Override
	public HashMap<String, String> getThresholds() {
		if (thresholds == null)
			getAllModles();
		return thresholds;
	}

	@Override
	public HashMap<String, String> getReferences() {
		if (references == null)
			getAllModles();
		return references;
	}

	@Override
	public ModelInput getModelByName(String name)
			throws BarkDbOperationException {
		DQModelEntity sourceObject = null;
		try {
			sourceObject = BarkMongoDAO.getModelDAO().findByName(name);

		} catch (Exception e) {
			logger.warn(e.toString());
			throw new BarkDbOperationException(
					"Failed to find model with name of '" + name + "'", e);
		}

		if (sourceObject == null) {
			throw new BarkDbOperationException(404, "The model of '" + name
					+ "' doesn't exist!");
		}
		return convertModel(sourceObject);
	}

	private ModelInput convertModel(DQModelEntity sourceObject) {
		// Object result = sourceObject;
		int modelType = sourceObject.getModelType();
		ModelInput result = new ModelInput();
		result.setBasic(getViewModelForFront(sourceObject));

		if (modelType == ModelType.ACCURACY) {
			result.parseFromString(sourceObject.getModelContent());
		} else if (modelType == ModelType.VALIDITY) {

			ModelExtraInputNew extra = result.getExtra();
			String content = sourceObject.getModelContent();
			String[] contents = content.split("\\|");
			extra.setSrcDb(contents[0]);
			extra.setSrcDataSet(contents[1]);
			extra.setColumn(contents[3]);

			int type = Integer.parseInt(contents[2]);
			extra.setVaType(type);

		} else if (modelType == ModelType.ANOMALY) {

			ModelExtraInputNew extra = result.getExtra();
			String content = sourceObject.getModelContent();
			String[] contents = content.split("\\|");
			extra.setSrcDb(contents[0]);
			extra.setSrcDataSet(contents[1]);
			int type = Integer.parseInt(contents[2]);
			extra.setAnType(type);

		} else if (modelType == ModelType.PUBLISH) {

			result.getExtra().setPublishUrl(sourceObject.getModelContent());

			return result;
		}

		return result;
	}

	public void newSampleJob4Model(DQModelEntity input)
	{
		int type = input.getSchedule();
		Calendar c=Calendar.getInstance();
		Date date=new Date();
		date.setMinutes(0);
		date.setSeconds(0);
		c.setTime(date);
		for(int i=0;i<ModelSampleFlowConstants.sampelCount;i++)
		{
			if(type==ScheduleTypeConstants.DAILY)
				c.add(Calendar.DATE,-1);
			else if(type==ScheduleTypeConstants.HOURLY)
				c.add(Calendar.HOUR,-1);
			else if(type==ScheduleTypeConstants.WEEKLY)
				c.add(Calendar.DATE,-7);
			else if(type==ScheduleTypeConstants.MONTHLY)
				c.add(Calendar.MONTH,-1);
			else
				continue;

			long starttime = c.getTime().getTime() / 1000 * 1000;

			DQJob job = new DQJob();
			job.setModelList(input.getModelName());
			job.setStarttime(starttime);
			job.setStatus(0);
			job.set_id(input.getModelName()+"_"+starttime);
			job.setJobType(input.getModelType());
			int result = BarkMongoDAO.getModelDAO().newJob(job);
			if(result==0)
			{
				logger.warn( "===================new job failure");
				continue;
			}
		}
	}

	@Override
	public void enableSchedule4Model(DQModelEntity input)
	{
		if(input==null) return;

		input.setStatus(ModelStatusConstants.DEPLOYED);
		BarkMongoDAO.getModelDAO().upsertModel(input);

		if (input.getModelType() == ModelTypeConstants.ACCURACY
				|| input.getModelType() == ModelTypeConstants.VALIDITY) {
			DQSchedule schedule = new DQSchedule();
			schedule.set_id(BarkMongoDAO.getModelDAO().getNextJobSequence());
			schedule.setScheduleType(input.getSchedule());
			Date d = new Date(input.getStarttime());
			d.setMinutes(0);
			d.setSeconds(0);
			schedule.setStarttime(d.getTime() / 1000 * 1000);
			schedule.setStatus(0);
			schedule.setAssetId(input.getAssetId());
			schedule.setJobType(input.getModelType());

			String modellist = "";
			if (input.getModelType() == ModelTypeConstants.VALIDITY) {
				DBObject currentSchedule = BarkMongoDAO.getModelDAO()
						.getValidateSchedule(
								input.getAssetId());
				if (currentSchedule != null) {
					if (currentSchedule.get("modelList") != null
							&& !currentSchedule.get("modelList").toString()
							.equals("")) {
						modellist = currentSchedule.get("modelList")
								.toString();
						modellist = modellist
								+ ScheduleModelSeperator.SEPERATOR
								+ input.getModelName();
					}
				}
				if (modellist.equals(""))
					modellist = input.getModelName();
			} else
				modellist = input.getModelName();
			schedule.setModelList(modellist);

			BarkMongoDAO.getModelDAO().upsertNewSchedule(schedule,
					input.getModelType());
		}
	}

	@Override
	public int newModel(ModelInput input) throws BarkDbOperationException {
		try {
			if (checkGeneralModelByName(input.getBasic().getName()) == 1) {
				throw new BarkDbOperationException("Record already existing");
			}

			DQModelEntity entity = new DQModelEntity();

			// set basic information first
			entity.set_id(BarkMongoDAO.getModelDAO().getNextModelSequence());
			entity.setModelId(input.getBasic().getName());
			entity.setModelName(input.getBasic().getName());
			entity.setNotificationEmail(input.getBasic().getEmail());
			entity.setOwner(input.getBasic().getOwner());
			entity.setSchedule(input.getBasic().getScheduleType());
			entity.setSystem(input.getBasic().getSystem());
			entity.setThreshold(input.getBasic().getThreshold());
			entity.setModelDesc(input.getBasic().getDesc());
			entity.setTimestamp(new Date().getTime());
			entity.setAssetName(input.getBasic().getDataaset());
			entity.setAssetId(input.getBasic().getDataasetId());
			entity.setReferenceModel("");
			entity.setModelType(input.getBasic().getType());
			if (input.getBasic().getStarttime() == 0)
				entity.setStarttime(new Date().getTime());
			else
				entity.setStarttime(input.getBasic().getStarttime());
			// entity.setJobStatus(0);

			if (input.getBasic().getType() == ModelTypeConstants.ACCURACY) {

				StringBuffer content = new StringBuffer("");
				content.append(input.getExtra().getSrcDb() + "|"
						+ input.getExtra().getSrcDataSet() + "|"
						+ input.getExtra().getTargetDb() + "|"
						+ input.getExtra().getTargetDataSet() + "|");
				List<MappingItemInput> mapping = input.getMappings();
				String delimeter = "";
				for (int i = 0; i < mapping.size(); i++) {
					content.append(delimeter + mapping.get(i).getSrc() + ","
							+ mapping.get(i).getTarget() + ","
							+ mapping.get(i).isIsPk() + ","
							+ mapping.get(i).getMatchMethod());
					delimeter = ";";
				}
				entity.setModelContent(content.toString());
				entity.setStatus(ModelStatusConstants.TESTING);

				newSampleJob4Model(entity);

			} else if (input.getBasic().getType() == ModelTypeConstants.VALIDITY) {

				StringBuffer content = new StringBuffer("");
				content.append(input.getExtra().getSrcDb() + "|"
						+ input.getExtra().getSrcDataSet() + "|"
						+ input.getExtra().getVaType() + "|"
						+ input.getExtra().getColumn());
				entity.setModelContent(content.toString());

				if(input.getExtra().getVaType() == ValidityTypeConstants.TOTAL_COUNT){
					entity.setStatus(input.getBasic().getStatus());
				}else{
					entity.setStatus(ModelStatusConstants.TESTING);
				}

				newSampleJob4Model(entity);

			} else if (input.getBasic().getType() == ModelTypeConstants.ANOMALY_DETECTION) {
				StringBuffer content = new StringBuffer("");
				content.append(input.getExtra().getSrcDb() + "|"
						+ input.getExtra().getSrcDataSet() + "|"
						+ input.getExtra().getAnType());
				entity.setModelContent(content.toString());
				entity.setStatus(ModelStatusConstants.DEPLOYED);

				int index = 1;
				DBObject countModel;
				while (true) {
					countModel = BarkMongoDAO.getModelDAO()
							.findCountModelByAssetID(
									input.getBasic().getDataasetId());
					if (countModel == null) {
						DataAsset asset = BarkMongoDAO.getModelDAO()
								.getAssetById(
										new Long(input.getBasic()
												.getDataasetId()));
						ModelInput tempCountModel = new ModelInput();
						ModelBasicInputNew basic = new ModelBasicInputNew();
						ModelExtraInputNew extra = new ModelExtraInputNew();
						basic.setDataaset(input.getBasic().getDataaset());
						basic.setDataasetId(input.getBasic().getDataasetId());
						basic.setDesc("Count for "
								+ input.getBasic().getDataaset());
						basic.setEmail(input.getBasic().getEmail());
						basic.setName("Count_" + input.getBasic().getName()
								+ "_" + index);
						basic.setOwner(input.getBasic().getOwner());
						basic.setScheduleType(input.getBasic()
								.getScheduleType());
						basic.setStatus(input.getBasic().getStatus());
						basic.setSystem(input.getBasic().getSystem());
						basic.setType(ModelTypeConstants.VALIDITY);
						extra.setVaType(ValidityTypeConstants.TOTAL_COUNT);
						extra.setColumn("wholeDataSet");
						extra.setSrcDataSet(asset.getSystem());
						extra.setSrcDb(asset.getPlatform());
						tempCountModel.setBasic(basic);
						tempCountModel.setExtra(extra);
						newModel(tempCountModel);
						index++;
					} else {
						break;
					}
				}
				BarkMongoDAO.getModelDAO().addModelReference(countModel,
						input.getBasic().getName());

			} else if (input.getBasic().getType() == ModelTypeConstants.PUBLISH_METRICS) {
				StringBuffer content = new StringBuffer("");
				content.append(input.getExtra().getPublishUrl());
				entity.setStatus(ModelStatusConstants.DEPLOYED);

				entity.setModelContent(content.toString());
			}

			BarkMongoDAO.getModelDAO().insertModel(entity);

			return 0;
		} catch (Exception e) {
			logger.warn(e.toString());
			throw new BarkDbOperationException("Failed to insert a new Model",
					e);
		}

	}

	public ModelBasicInputNew getViewModelForFront(DQModelEntity sourceObject) {
		ModelBasicInputNew basic = new ModelBasicInputNew();
		basic.setDesc(sourceObject.getModelDesc());
		basic.setName(sourceObject.getModelName());
		basic.setDataaset(sourceObject.getAssetName());
		basic.setDataasetId(sourceObject.getAssetId());
		basic.setStatus(sourceObject.getStatus());
		basic.setType(sourceObject.getModelType());
		basic.setScheduleType(sourceObject.getSchedule());
		basic.setSystem(sourceObject.getSystem());
		basic.setEmail(sourceObject.getNotificationEmail());
		basic.setOwner(sourceObject.getOwner());
		basic.setThreshold(sourceObject.getThreshold());

		return basic;
	}

}