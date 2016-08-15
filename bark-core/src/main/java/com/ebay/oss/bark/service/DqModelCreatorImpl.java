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
package com.ebay.oss.bark.service;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.oss.bark.domain.DataAsset;
import com.ebay.oss.bark.domain.DqJob;
import com.ebay.oss.bark.domain.DqModel;
import com.ebay.oss.bark.domain.ModelStatus;
import com.ebay.oss.bark.domain.ModelType;
import com.ebay.oss.bark.domain.ScheduleType;
import com.ebay.oss.bark.domain.ValidityType;
import com.ebay.oss.bark.error.BarkDbOperationException;
import com.ebay.oss.bark.repo.DataAssetRepo;
import com.ebay.oss.bark.repo.DqJobRepo;
import com.ebay.oss.bark.repo.DqModelRepo;
import com.ebay.oss.bark.vo.MappingItemInput;
import com.ebay.oss.bark.vo.ModelBasicInputNew;
import com.ebay.oss.bark.vo.ModelExtraInputNew;
import com.ebay.oss.bark.vo.ModelInput;
import com.mongodb.DBObject;

@Component
public class DqModelCreatorImpl implements DqModelCreator {

    private static Logger logger = LoggerFactory.getLogger(DqModelCreatorImpl.class);

    @Autowired
	DqModelRepo dqModelRepo;

    @Autowired
    DataAssetRepo dataAssetRepo;

	@Autowired
    private DqJobRepo jobRepo;

    private boolean hasModelWithName(String name) {
		return null != dqModelRepo.findByName(name);
	}
    @Override
    public int newModel(ModelInput input) {
		if ( hasModelWithName(input.getBasic().getName()) ) {
			throw new BarkDbOperationException("Record already existing");
		}
		
		try {
			
			DqModel entity = createModel(input);

			if (input.getBasic().getType() == ModelType.ACCURACY) {
			    entity.setModelContent(contentAccuracy(input));

			    entity.setStatus(ModelStatus.TESTING);

			    newSampleJob4Model(entity);
			} else if (input.getBasic().getType() == ModelType.VALIDITY) {
			    entity.setModelContent( contentValidity(input) );

			    if(input.getExtra().getVaType() == ValidityType.TOTAL_COUNT){
			        entity.setStatus(input.getBasic().getStatus());
			    }else{
			        entity.setStatus(ModelStatus.TESTING);
			    }

			    newSampleJob4Model(entity);
			} else if (input.getBasic().getType() == ModelType.ANOMALY) {
			    entity.setModelContent( contentAnomaly(input) );

			    entity.setStatus(ModelStatus.DEPLOYED);

			    DBObject countModel = createCountModel(input);
			    dqModelRepo.addReference(countModel, input.getBasic().getName());

			} else if (input.getBasic().getType() == ModelType.PUBLISH) {
			    enrichPublish(entity, input);
			}

			dqModelRepo.update(entity);

			return 0;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new BarkDbOperationException("Failed to create a new Model", e);
		}

    }

    protected DqModel createModel(ModelInput input) {
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

        return entity;
    }
    protected String contentAccuracy( ModelInput input) {
	    String content = input.getExtra().getSrcDb() + "|"
	                    + input.getExtra().getSrcDataSet() + "|"
	                    + input.getExtra().getTargetDb() + "|"
	                    + input.getExtra().getTargetDataSet() + "|";

	    String delimeter = "";
	    for(MappingItemInput itm : input.getMappings()) {
	        content += delimeter 
	                        + itm.getSrc() + ","
	                        + itm.getTarget() + ","
	                        + itm.isIsPk() + ","
	                        + itm.getMatchMethod();
	        delimeter = ";";
	    }

	    return content;

    }
    protected String contentValidity(ModelInput input) {
        return input.getExtra().getSrcDb() + "|"
                        + input.getExtra().getSrcDataSet() + "|"
                        + input.getExtra().getVaType() + "|"
                        + input.getExtra().getColumn();
    }
    protected String contentAnomaly(ModelInput input) {
        return input.getExtra().getSrcDb() + "|"
                        + input.getExtra().getSrcDataSet() + "|"
                        + input.getExtra().getAnType();
    }
    protected DBObject createCountModel(ModelInput input) {
        int index = 1;
        DBObject countModel;
        while (true) {
            countModel = dqModelRepo.findCountModelByAssetID(input.getBasic().getDataasetId());
            if (countModel != null) {
                break;
            } 

            DataAsset asset = dataAssetRepo.getById(new Long(input.getBasic().getDataasetId()));
            ModelBasicInputNew basic = new ModelBasicInputNew();
            ModelExtraInputNew extra = new ModelExtraInputNew();
            basic.setDataaset(input.getBasic().getDataaset());
            basic.setDataasetId(input.getBasic().getDataasetId());
            basic.setDesc("Count for " + input.getBasic().getDataaset());
            basic.setEmail(input.getBasic().getEmail());
            basic.setName("Count_" + input.getBasic().getName() + "_" + (index++) );
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
        }
        return countModel;
    }
    protected void enrichPublish(DqModel entity, ModelInput input) {
        String content = input.getExtra().getPublishUrl();
        entity.setModelContent(content.toString());

        entity.setStatus(ModelStatus.DEPLOYED);
    }

    void newSampleJob4Model(DqModel input) {
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

}
