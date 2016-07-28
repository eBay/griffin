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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.bark.common.BarkDbOperationException;
import org.apache.bark.common.HDFSUtils;
import org.apache.bark.common.KeyValue;
import org.apache.bark.common.ModelStatusConstants;
import org.apache.bark.common.ModelTypeConstants;
import org.apache.bark.common.ScheduleTypeConstants;
import org.apache.bark.common.SystemTypeConstants;
import org.apache.bark.common.ValidityTypeConstants;
import org.apache.bark.dao.BarkMongoDAO;
import org.apache.bark.model.DQModelEntity;
import org.apache.bark.model.DataAsset;
import org.apache.bark.model.DataAssetIndex;
import org.apache.bark.model.DataAssetInput;
import org.apache.bark.model.DataSchema;
import org.apache.bark.model.ModelBasicInputNew;
import org.apache.bark.model.ModelExtraInputNew;
import org.apache.bark.model.ModelForFront;
import org.apache.bark.model.ModelInput;
import org.apache.bark.model.PartitionFormat;
import org.apache.bark.model.PlatformMetadata;
import org.apache.bark.model.SystemMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
//import org.springframework.validation.annotation.Validated;

import com.mongodb.DBObject;

@Service
// @Validated
public class DataAssetServiceImpl implements DataAssetService {
	private static Logger logger = LoggerFactory
			.getLogger(DataAssetServiceImpl.class);

	// @Autowired
	// private DataAssetDao dataAssetDao;

	@Autowired
	private DQModelService dqModelService;			//dqmodel service
	@Autowired
	private Environment env;

	public static List<ModelForFront> allModels;
	public static HashMap<String, String> thresholds;

	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<DataAsset> getAllDataAssets() {

		List<DataAsset> dal = new ArrayList<DataAsset>();
		List<DBObject> allAssets = BarkMongoDAO.getModelDAO().getAllAssets();

		for (DBObject o : allAssets) {

			DataAsset da = new DataAsset();

			da.set_id(new Long((long) Double.parseDouble(o.get("_id")
					.toString())));
			da.setPlatform(o.get("platform").toString());
			da.setSystem(o.get("system").toString());
			da.setAssetName(o.get("assetName").toString());
			da.setAssetType(o.get("assetType").toString());
			da.setAssetHDFSPath(o.get("assetHDFSPath").toString());
			da.setSchema((List<DataSchema>) o.get("schema"));
			if (o.get("partition") != null)
				da.setPartitions((List<PartitionFormat>) o.get("partitions"));

			if (!o.containsField("owner")) {
				da.setOwner("");
			} else {
				da.setOwner(o.get("owner").toString());
			}

			if (!o.containsField("timestamp")) {
				da.setTimestamp(new Date());
			} else {
				da.setTimestamp(new Date(o.get("timestamp").toString()));
			}

			dal.add(da);
		}

		return dal;

	}

	@Override
	public int createDataAsset(DataAssetInput input)
			throws BarkDbOperationException {

		DataAsset da = new DataAsset();

		List<KeyValue> queryList = new ArrayList<KeyValue>();
		queryList.add(new KeyValue("assetName", input.getAssetName()));
		queryList.add(new KeyValue("assetType", input.getAssetType()));
		queryList.add(new KeyValue("system", input.getSystem()));

		List<KeyValue> updateValues = new ArrayList<KeyValue>();
		updateValues.add(new KeyValue("schema", input.getSchema()));
		updateValues.add(new KeyValue("platform", input.getPlatform()));
		updateValues
		.add(new KeyValue("assetHDFSPath", input.getAssetHDFSPath()));
		updateValues.add(new KeyValue("owner", input.getOwner()));

		DBObject item = BarkMongoDAO.getModelDAO().getAssetByCondition(
				queryList);

		if (item != null) {
			throw new BarkDbOperationException("Record already existing");
		}

		String hdfsPath = input.getAssetHDFSPath();


		if (hdfsPath.endsWith("/")) {
			hdfsPath = hdfsPath.substring(0, hdfsPath.length() - 1);
			logger.warn( "hdfsPath: " + hdfsPath);

		}

		if(!"qa".equals(env.getProperty("env"))){//in prod environment, need to validate the hdfs path

			if (!HDFSUtils.checkHDFSFolder(hdfsPath)) {


				throw new BarkDbOperationException(
						"Hdfs Path is invalid, please input valid hdfs path!");
			}
		}

		try {

			long seq = BarkMongoDAO.getModelDAO().getNextAssetSequence();
			da.set_id(seq);
			da.setAssetName(input.getAssetName());
			da.setAssetType(input.getAssetType());
			da.setPlatform(input.getPlatform());
			da.setSystem(input.getSystem());
			da.setAssetHDFSPath(input.getAssetHDFSPath());
			da.setSchema(input.getSchema());
			da.setOwner(input.getOwner());
			da.setPartitions(input.getPartitions());
			da.setTimestamp(new Date());

			logger.debug("log: new record inserted" + seq);
			BarkMongoDAO.getModelDAO().saveDataAsset(da);

			//create new total count dqmodel
			{
				ModelInput tempCountModel = new ModelInput();
				ModelBasicInputNew basic = new ModelBasicInputNew();
				ModelExtraInputNew extra = new ModelExtraInputNew();
				basic.setDataaset(input.getAssetName());
				basic.setDataasetId((int)seq);
				basic.setDesc("Count for " + input.getAssetName());
				basic.setEmail(input.getOwner() + "@ebay.com");
				basic.setName("TotalCount_" + input.getAssetName());
				basic.setOwner(input.getOwner());
				basic.setScheduleType(ScheduleTypeConstants.DAILY);
				basic.setStatus(ModelStatusConstants.DEPLOYED);
				basic.setSystem(SystemTypeConstants.indexOf(input.getSystem()));
				basic.setType(ModelTypeConstants.VALIDITY);
				extra.setVaType(ValidityTypeConstants.TOTAL_COUNT);
				extra.setColumn("wholeDataSet");
				extra.setSrcDataSet(input.getAssetName());
				extra.setSrcDb(input.getPlatform());
				tempCountModel.setBasic(basic);
				tempCountModel.setExtra(extra);
				dqModelService.newModel(tempCountModel);
			}

			return 0;

		} catch (Exception e) {
			throw new BarkDbOperationException(
					"Failed to create a new data asset", e);
		}

	}

	@Override
	public int updateDataAsset(DataAssetInput input)
			throws BarkDbOperationException {
		try {

			DataAsset da = new DataAsset();

			List<KeyValue> queryList = new ArrayList<KeyValue>();
			queryList.add(new KeyValue("assetName", input.getAssetName()));
			queryList.add(new KeyValue("assetType", input.getAssetType()));
			queryList.add(new KeyValue("system", input.getSystem()));

			List<KeyValue> updateValues = new ArrayList<KeyValue>();
			updateValues.add(new KeyValue("schema", input.getSchema()));
			updateValues.add(new KeyValue("platform", input.getPlatform()));
			updateValues.add(new KeyValue("assetHDFSPath", input
					.getAssetHDFSPath()));
			updateValues.add(new KeyValue("owner", input.getOwner()));

			DBObject item = BarkMongoDAO.getModelDAO().getAssetByCondition(
					queryList);

			da.setAssetName(input.getAssetName());
			da.setAssetType(input.getAssetType());
			da.setPlatform(input.getPlatform());
			da.setSystem(input.getSystem());
			da.setAssetHDFSPath(input.getAssetHDFSPath());
			da.setSchema(input.getSchema());
			da.setOwner(input.getOwner());
			da.setPartitions(input.getPartitions());
			da.setTimestamp(new Date());

			if (item == null) {
				throw new BarkDbOperationException(
						"The data asset doesn't exist");

			} else {
				logger.warn( "log: updated record, id is: "
						+ (long) Double.parseDouble(item.get("_id").toString()));
				da.set_id(new Long((long) Double.parseDouble(item.get("_id")
						.toString())));
				BarkMongoDAO.getModelDAO().updateDataAsset(da, item);
				return 0;
			}
		} catch (Exception e) {
			throw new BarkDbOperationException("Failed to update data asset", e);
		}

	}

	@Override
	public DataAsset getDataAssetById(Long id) throws BarkDbOperationException {

		// List<DataAsset> da = dataAssetDao.findByField(DataAsset.class, "_id",
		// id);
		//
		// if(!da.isEmpty()){
		// return da.get(0);
		// }
		//
		// return null;

		return BarkMongoDAO.getModelDAO().getAssetById(id);
	}

	@Override
	public List<PlatformMetadata> getSourceTree() {

		List<PlatformMetadata> alltree = new ArrayList<PlatformMetadata>();

		List<DBObject> allAssets = BarkMongoDAO.getModelDAO().getAllAssets();

		List<String> platforms = new ArrayList<String>(
				getAllPlatforms(allAssets));
		for (String platform : platforms) {

			PlatformMetadata plat = new PlatformMetadata();
			plat.setPlatform(platform);

			List<String> datasets = new ArrayList<String>(getSystemsByPlatform(
					platform, allAssets));
			List<SystemMetadata> d = new ArrayList<SystemMetadata>();

			for (String dataset : datasets) {

				SystemMetadata db = new SystemMetadata();
				db.setName(dataset);

				List<DataAssetIndex> assets = new ArrayList<DataAssetIndex>();

				Map<Long, String> tableB = getAssetsBySystem(platform, dataset,
						allAssets);
				for (Entry<Long, String> entry : tableB.entrySet()) {
					DataAssetIndex dai = new DataAssetIndex();
					dai.setId(entry.getKey());
					dai.setName(entry.getValue());

					assets.add(dai);
				}

				db.setAssets(assets);
				d.add(db);

			}

			plat.setSystems(d);
			alltree.add(plat);

		}

		return alltree;

	}

	public Set<String> getAllPlatforms(List<DBObject> records) {

		Set<String> p = new HashSet<String>();
		for (DBObject o : records) {
			p.add(o.get("platform").toString());
		}
		return p;
	}

	public Set<String> getSystemsByPlatform(String platform,
			List<DBObject> records) {
		Set<String> p = new HashSet<String>();
		for (DBObject o : records) {
			if (o.get("platform").equals(platform)) {
				p.add(o.get("system").toString());
			}
		}
		return p;

	}

	public Map<Long, String> getAssetsBySystem(String platform, String system,
			List<DBObject> records) {
		Map<Long, String> p = new HashMap<Long, String>();
		for (DBObject o : records) {
			if (o.get("platform").equals(platform)
					&& o.get("system").equals(system)) {
				p.put(((Number) o.get("_id")).longValue(), o.get("assetName")
						.toString());
			}
		}
		return p;

	}

	@Override
	public int removeAssetById(Long id) throws BarkDbOperationException {
		try {
			//delete concern models first
			removeConcernModelsOfAsset(id);

			BarkMongoDAO.getModelDAO().deleteAssetbyId(id);
		} catch (Exception e) {
			throw new BarkDbOperationException(
					"Failed to delete data asset with id of '" + id + "'", e);
		}

		return 0;

	}

	//remove all the models concerned with the given data asset
	private int removeConcernModelsOfAsset(Long dataAssetId) {
		try {
			DataAsset da = BarkMongoDAO.getModelDAO().getAssetById(dataAssetId);
			if (da != null) {
				//delete all the accuracy models with this given source asset
				List<DQModelEntity> models = BarkMongoDAO.getModelDAO().getModelsByDataAsset(da, true);
				Iterator<DQModelEntity> itr = models.iterator();
				while (itr.hasNext()) {
					DQModelEntity me = itr.next();
					dqModelService.deleteModel(me.getModelName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

}
