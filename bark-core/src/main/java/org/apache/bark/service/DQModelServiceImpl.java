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
import java.util.List;

import org.apache.bark.common.ScheduleModelSeperator;
import org.apache.bark.domain.DqMetricsValue;
import org.apache.bark.domain.DataAsset;
import org.apache.bark.domain.DqJob;
import org.apache.bark.domain.DqModel;
import org.apache.bark.domain.DqSchedule;
import org.apache.bark.domain.ModelStatus;
import org.apache.bark.domain.ModelType;
import org.apache.bark.domain.ScheduleType;
import org.apache.bark.domain.ValidityType;
import org.apache.bark.error.BarkDbOperationException;
import org.apache.bark.repo.DataAssetRepo;
import org.apache.bark.repo.DqJobRepo;
import org.apache.bark.repo.DqMetricsRepo;
import org.apache.bark.repo.DqModelRepo;
import org.apache.bark.repo.DqScheduleRepo;
import org.apache.bark.vo.DqModelVo;
import org.apache.bark.vo.MappingItemInput;
import org.apache.bark.vo.ModelBasicInputNew;
import org.apache.bark.vo.ModelExtraInputNew;
import org.apache.bark.vo.ModelInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.DBObject;

@Service
public class DqModelServiceImpl implements DqModelService {

	private static Logger logger = LoggerFactory.getLogger(DqModelServiceImpl.class);

	public static String MODEL_TYPE_ACCUATE = "Accruacy";
	public static String MODEL_TYPE_VALIDATE = "Validate";
	public static String MODEL_TYPE_PUBLISH = "Publish";

//	public static String MODEL_CREATE_SCUCESS = "{\"status\":\"0\" , \"result\":\"success\"}";
//	public static String MODEL_CREATE_NAME_EXISTS = "{\"status\":\"-1\" , \"result\":\"Model name already exists, please change it.\"}";
	
	/** the minimum number of Jobs executed in the ModelStatus.TESTING, and then it could be shifted
	 * to ModelStatus.VERIFIED.
	 */
	public static final int MIN_TESTING_JOB_NUMBER = 5;

	@Autowired
	private DataAssetService dataAssetService;

	@Autowired
    private DqModelRepo dqModelRepo;

	@Autowired
    private DqScheduleRepo scheduleRepo;

	@Autowired
    private DqJobRepo jobRepo;

	@Autowired
    private DataAssetRepo dataAssetRepo;

	@Autowired
	private Converter<DqModel, DqModelVo> converter;

	@Autowired
    private DqMetricsRepo metricsRepo;
	
	@Override
	public List<DqModelVo> getAllModles() {
	    List<DqModelVo> allModels = new ArrayList<>();
		for(DqModel each : dqModelRepo.getAll()) {
            allModels.add( converter.voOf(each));
		}
		return allModels;
	}

	
    @Override
	public int deleteModel(String name) throws BarkDbOperationException {
		try {
			DqModel dqModel = dqModelRepo.findByName(name);

			// TODO need to mark related metrics as deleted, instead of real deletion
			// markMetricsDeleted(dqModel);

			dqModelRepo.delete(dqModel.getId());

			if (dqModel.getModelType() == ModelType.ACCURACY) {
				scheduleRepo.deleteByModelList(name);
			} else if (dqModel.getModelType() == ModelType.VALIDITY) {
				DBObject currentSchedule = scheduleRepo.getValiditySchedule(dqModel.getAssetId());
				if (currentSchedule == null || currentSchedule.get("modelList") == null) {
					return -1;
				}

				String rawModelList = currentSchedule.get("modelList") .toString();
				String newModelList = "";
				if (rawModelList.contains(ScheduleModelSeperator.SEPERATOR)) {
					String[] rawModelArray = rawModelList.split(ScheduleModelSeperator.SPLIT_SEPERATOR);
					for (int i = 0; i < rawModelArray.length; i++) {
						if (!rawModelArray[i].equals(name))
							newModelList = newModelList + ScheduleModelSeperator.SEPERATOR + rawModelArray[i];
					}
					newModelList = newModelList.substring(ScheduleModelSeperator.SEPERATOR.length());
					currentSchedule.put("modelList", newModelList);
					scheduleRepo.updateModelType(currentSchedule, dqModel.getModelType());

				} else if (rawModelList.equals(name)) {
				    scheduleRepo.deleteByModelList(name);
				}
			}

			return 0;
		} catch (Exception e) {
			logger.warn(e.toString());
			throw new BarkDbOperationException("Failed to delete model of '"
					+ name + "'", e);
		}
	}

	// FIXME to be removed
	@Override
	public DqModel getGeneralModel(String name) {
		return dqModelRepo.findByName(name);
	}

	private boolean hasModelWithName(String name) {
		return null != dqModelRepo.findByName(name);
	}

	@Override
	public ModelInput getModelByName(String name)
			throws BarkDbOperationException {
		DqModel dqModel = null;
		try {
			dqModel = dqModelRepo.findByName(name);

		} catch (Exception e) {
			logger.warn(e.toString());
			throw new BarkDbOperationException("Failed to find model with name of '" + name + "'", e);
		}

		if (dqModel == null) {
			throw new BarkDbOperationException(404, "The model of '" + name + "' doesn't exist!");
		}
		return convertModel(dqModel);
	}

	private ModelInput convertModel(DqModel sourceObject) {
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
		}

		return result;
	}

	public void newSampleJob4Model(DqModel input) {
		int type = input.getSchedule();
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        date.setMinutes(0);
        date.setSeconds(0);
        c.setTime(date);

        for (int i = 0; i < MIN_TESTING_JOB_NUMBER; i++) {
            if (type == ScheduleType.DAILY)
                c.add(Calendar.DATE, -1);
            else if (type == ScheduleType.HOURLY)
                c.add(Calendar.HOUR, -1);
            else if (type == ScheduleType.WEEKLY)
                c.add(Calendar.DATE, -7);
            else if (type == ScheduleType.MONTHLY)
                c.add(Calendar.MONTH, -1);
            else
                continue;

            long starttime = c.getTime().getTime() / 1000 * 1000;

            DqJob job = new DqJob();
            job.setModelList(input.getModelName());
            job.setStarttime(starttime);
            job.setStatus(0);
            job.setId(input.getModelName() + "_" + starttime);
            job.setJobType(input.getModelType());

            if (jobRepo.newJob(job) == 0) {
                logger.warn("===================new job failure");
                continue;
            }
        }
    }

    @Override
	public void enableSchedule4Model(DqModel dqModel) {
		if(dqModel==null) return;

		dqModel.setStatus(ModelStatus.DEPLOYED);
		dqModelRepo.update(dqModel);

		if (dqModel.getModelType() == ModelType.ACCURACY
				|| dqModel.getModelType() == ModelType.VALIDITY) {
			DqSchedule schedule = new DqSchedule();
			schedule.setId(scheduleRepo.getNextId());
			schedule.setScheduleType(dqModel.getSchedule());
			Date d = new Date(dqModel.getStarttime());
			d.setMinutes(0);
			d.setSeconds(0);
			schedule.setStarttime(d.getTime() / 1000 * 1000); // FIXME ????
			schedule.setStatus(0);
			schedule.setAssetId(dqModel.getAssetId());
			schedule.setJobType(dqModel.getModelType());

			String modellist = "";
			if (dqModel.getModelType() == ModelType.VALIDITY) {
				DBObject currentSchedule = scheduleRepo.getValiditySchedule(
								dqModel.getAssetId());
				if (currentSchedule != null) {
					if (currentSchedule.get("modelList") != null
							&& !currentSchedule.get("modelList").toString().equals("")) {
						modellist = currentSchedule.get("modelList")
								.toString();
						modellist = modellist
								+ ScheduleModelSeperator.SEPERATOR
								+ dqModel.getModelName();
					}
				}
				if (modellist.equals(""))
					modellist = dqModel.getModelName();
			} else
				modellist = dqModel.getModelName();
			schedule.setModelList(modellist);

			scheduleRepo.updateByModelType(schedule, dqModel.getModelType());
		}
	}

	@Override
	public int newModel(ModelInput input) throws BarkDbOperationException {
		try {
			if ( hasModelWithName(input.getBasic().getName()) ) {
				throw new BarkDbOperationException("Record already existing");
			}

			DqModel entity = new DqModel();
			entity.setId(dqModelRepo.getNextId());
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
			if (input.getBasic().getStarttime() == 0) {
				entity.setStarttime(new Date().getTime());
			} else {
				entity.setStarttime(input.getBasic().getStarttime());
			}

			if (input.getBasic().getType() == ModelType.ACCURACY) {

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
				entity.setStatus(ModelStatus.TESTING);

				newSampleJob4Model(entity);

			} else if (input.getBasic().getType() == ModelType.VALIDITY) {

				StringBuffer content = new StringBuffer("");
				content.append(input.getExtra().getSrcDb() + "|"
						+ input.getExtra().getSrcDataSet() + "|"
						+ input.getExtra().getVaType() + "|"
						+ input.getExtra().getColumn());
				entity.setModelContent(content.toString());

				if(input.getExtra().getVaType() == ValidityType.TOTAL_COUNT){
					entity.setStatus(input.getBasic().getStatus());
				}else{
					entity.setStatus(ModelStatus.TESTING);
				}

				newSampleJob4Model(entity);

			} else if (input.getBasic().getType() == ModelType.ANOMALY) {
				StringBuffer content = new StringBuffer("");
				content.append(input.getExtra().getSrcDb() + "|"
						+ input.getExtra().getSrcDataSet() + "|"
						+ input.getExtra().getAnType());
				entity.setModelContent(content.toString());
				entity.setStatus(ModelStatus.DEPLOYED);

				int index = 1;
				DBObject countModel;
				while (true) {
					countModel = dqModelRepo.findCountModelByAssetID(input.getBasic().getDataasetId());
					if (countModel == null) {
						DataAsset asset = dataAssetRepo.getById(
										new Long(input.getBasic().getDataasetId()));
						ModelBasicInputNew basic = new ModelBasicInputNew();
						ModelExtraInputNew extra = new ModelExtraInputNew();
						basic.setDataaset(input.getBasic().getDataaset());
						basic.setDataasetId(input.getBasic().getDataasetId());
						basic.setDesc("Count for " + input.getBasic().getDataaset());
						basic.setEmail(input.getBasic().getEmail());
						basic.setName("Count_" + input.getBasic().getName() + "_" + index);
						basic.setOwner(input.getBasic().getOwner());
						basic.setScheduleType(input.getBasic() .getScheduleType());
						basic.setStatus(input.getBasic().getStatus());
						basic.setSystem(input.getBasic().getSystem());
						basic.setType(ModelType.VALIDITY);

						extra.setVaType(ValidityType.TOTAL_COUNT);
						extra.setColumn("wholeDataSet");
						extra.setSrcDataSet(asset.getSystem());
						extra.setSrcDb(asset.getPlatform());

						ModelInput tempCountModel = new ModelInput();
						tempCountModel.setBasic(basic);
						tempCountModel.setExtra(extra);

						newModel(tempCountModel);
						index++;
					} else {
						break;
					}
				}
				dqModelRepo.addReference(countModel, input.getBasic().getName());

			} else if (input.getBasic().getType() == ModelType.PUBLISH) {
				StringBuffer content = new StringBuffer("");
				content.append(input.getExtra().getPublishUrl());
				entity.setStatus(ModelStatus.DEPLOYED);

				entity.setModelContent(content.toString());
			}

			dqModelRepo.update(entity);

			return 0;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new BarkDbOperationException("Failed to insert a new Model", e);
		}

	}

	public ModelBasicInputNew getViewModelForFront(DqModel sourceObject) {
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

	@Override
    public void updateModelStatus(int fromStatus, int toStatus) {
        List<DqModel> allmodels = dqModelRepo.getByStatus(fromStatus);
        for (DqModel model : allmodels) {
            List<DqMetricsValue> allMetrics = metricsRepo.getByMetricsName(model.getModelName());
            if (allMetrics.size() >= MIN_TESTING_JOB_NUMBER) {
                model.setStatus(toStatus);
                dqModelRepo.update(model);
            }
        }
    }

}