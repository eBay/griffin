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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebay.oss.bark.common.Pair;
import com.ebay.oss.bark.domain.DqMetricsValue;
import com.ebay.oss.bark.domain.DqModel;
import com.ebay.oss.bark.domain.MetricType;
import com.ebay.oss.bark.domain.ModelType;
import com.ebay.oss.bark.domain.SampleFilePathLKP;
import com.ebay.oss.bark.domain.ScheduleType;
import com.ebay.oss.bark.domain.SystemType;
import com.ebay.oss.bark.repo.DataAssetRepo;
import com.ebay.oss.bark.repo.DqMetricsRepo;
import com.ebay.oss.bark.repo.DqModelRepo;
import com.ebay.oss.bark.repo.SampleFilePathRepo;
import com.ebay.oss.bark.vo.AssetLevelMetrics;
import com.ebay.oss.bark.vo.AssetLevelMetricsDetail;
import com.ebay.oss.bark.vo.BollingerBandsEntity;
import com.ebay.oss.bark.vo.DQHealthStats;
import com.ebay.oss.bark.vo.DqModelVo;
import com.ebay.oss.bark.vo.MADEntity;
import com.ebay.oss.bark.vo.OverViewStatistics;
import com.ebay.oss.bark.vo.SampleOut;
import com.ebay.oss.bark.vo.SystemLevelMetrics;
import com.ebay.oss.bark.vo.SystemLevelMetricsList;
import com.mongodb.DBObject;

@Service("dqmetrics")
public class DQMetricsServiceImpl implements DQMetricsService {

	private static Logger logger = LoggerFactory.getLogger(DQMetricsServiceImpl.class);

	@Autowired
	private DqModelService dqModelService;

	@Autowired
	private SubscribeService subscribeService;

	Map<String, String> modelSystem = new HashMap<String, String>();

	@Autowired
    private DqMetricsRepo metricsRepo;

	@Autowired
    private DqModelRepo dqModelRepo;

	@Autowired
    private DataAssetRepo dataAssetRepo;

	@Autowired
    private SampleFilePathRepo missedFileRepo;

	public static List<DqMetricsValue> cacheValues = new ArrayList<DqMetricsValue>();;

	public static SystemLevelMetricsList totalSystemLevelMetricsList;
	public static int trendLength = 20 * 24;
	public static int trendOffset = 24 * 7;

	public void insertMetadata(DqMetricsValue metrics) {

		List<Pair> queryList = new ArrayList<Pair>();
		queryList.add(new Pair("metricName", metrics.getMetricName()));
		// queryList.add(new KeyValue("metricType", dq.getMetricType()));
		// queryList.add(new KeyValue("assetId", dq.getAssetId()));
		queryList.add(new Pair("timestamp", metrics.getTimestamp()));

		List<Pair> updateValues = new ArrayList<Pair>();
		updateValues.add(new Pair("value", metrics.getValue()));

		DBObject item = metricsRepo.getByCondition(queryList);

		if (item == null) {
			long seq = metricsRepo.getNextId();
			logger.warn("log: new record inserted" + seq);
			metrics.set_id(seq);
			metricsRepo.save(metrics);
		} else {
			logger.warn("log: updated record");
			metricsRepo.update(metrics, item);
		}

	}

	public List<String> fetchAllAssetIdBySystems() {

		// List<String> assetIds = new ArrayList<String>();

		List<DqModel> allModels = dqModelRepo.getAll();

		for (DqModel model : allModels) {
			// assetIds.add(asset.get("assetName").toString());
			modelSystem.put(model.getModelName(),
					SystemType.val(model.getSystem()));
		}

		return null;

	}

	// FIXME to remove
	public DqMetricsValue getLatestlMetricsbyId(String assetId) {

		// Query<DQMetricsValue> q =
		// dqMetricsDao.getDatastore().createQuery(DQMetricsValue.class).filter("assetId",
		// assetId).order("-timestamp");
		// List<DQMetricsValue> v = (List<DQMetricsValue>) q.asList();
		//
		// if(v.isEmpty()){
		// return null;
		// }
		//
		// return v.get(0);
		return metricsRepo.getLatestByAssetId(assetId);
	}

	public void autoRefresh() {
		updateLatestDQList();
	}

    public void refreshAllDQMetricsValuesinCache() {
        fetchAllAssetIdBySystems();
        // cacheValues = dqMetricsDao.findByFieldInValues(DQMetricsValue.class,
        // "assetId", assetIds);

        cacheValues.clear();
        for (DqMetricsValue each : metricsRepo.getAll()) {
            cacheValues.add(each);
        }
    }

	public void updateLatestDQList() {
		try {
			logger.warn("==============updating all latest dq metrics==================");
			refreshAllDQMetricsValuesinCache();

			totalSystemLevelMetricsList = new SystemLevelMetricsList();
			for (DqMetricsValue temp : cacheValues) {
				// totalSystemLevelMetricsList.upsertNewAsset(temp, assetSystem,
				// 1);
				totalSystemLevelMetricsList.upsertNewAssetExecute(
						temp.getMetricName(), "", temp.getTimestamp(),
						temp.getValue(), modelSystem.get(temp.getMetricName()),
						0, 1, null);
			}

			totalSystemLevelMetricsList.updateDQFail(getThresholds());
			calculateReferenceMetrics();

			logger.warn("==============update all latest dq metrics done==================");
		} catch (Exception e) {
			logger.warn(e.toString());
			e.printStackTrace();
		}
	}

	@Autowired
	private DqModelRepo modelRepo;
	
	Map<String, String> getThresholds() {
	    Map<String, String> thresHolds = new HashMap<>();
	    for(DqModel each : modelRepo.getAll()) {
	        thresHolds.put(each.getModelName(), "" + each.getThreshold());
	    }
	    return thresHolds;
    }

    public List<MADEntity> MAD(List<String> list) {
		List<MADEntity> result = new ArrayList<MADEntity>();
		int preparePointNumber = 15;
		float up_coff = (float) 2.3;
		float down_coff = (float) 2.3;
		for (int i = preparePointNumber; i < list.size(); i++) {
			long total = 0;
			for (int j = i - preparePointNumber; j < i; j++) {
				long rawNumber = Long.parseLong(list.get(j));
				total = total + rawNumber;
			}
			long mean = total / preparePointNumber;
			long meantotal = 0;
			for (int j = i - preparePointNumber; j < i; j++) {
				long rawNumber = Integer.parseInt(list.get(j));
				long rawDiff = rawNumber - mean;
				if (rawDiff >= 0)
					meantotal = meantotal + rawDiff;
				else
					meantotal = meantotal - rawDiff;
			}
			long mad = meantotal / preparePointNumber;
			long upper = (long) (mean + mad * up_coff);
			long lower = (long) (mean - mad * down_coff);
			// logger.warn( list.get(i)+"\t"+upper +"\t"+lower);
			result.add(new MADEntity(upper, lower));
		}
		logger.warn("mad done");
		return result;
	}

	public List<BollingerBandsEntity> bollingerBand(List<String> list) {
		List<BollingerBandsEntity> result = new ArrayList<BollingerBandsEntity>();
		int preparePointNumber = 30;
		float up_coff = (float) 1.8;
		float down_coff = (float) 1.8;
		for (int i = preparePointNumber; i < list.size(); i++) {
			long total = 0;
			for (int j = i - preparePointNumber; j < i; j++) {
				long rawNumber = Long.parseLong(list.get(j));
				total = total + rawNumber;
			}
			long mean = total / preparePointNumber;
			long meantotal = 0;
			for (int j = i - preparePointNumber; j < i; j++) {
				long rawNumber = Integer.parseInt(list.get(j));
				long rawDiff = rawNumber - mean;
				meantotal += rawDiff * rawDiff;
			}
			long mad = (long) Math.sqrt(meantotal / preparePointNumber);
			long upper = (long) (mean + mad * up_coff);
			long lower = (long) (mean - mad * down_coff);
			// .out.println( list.get(i)+"\t"+upper +"\t"+lower);
			result.add(new BollingerBandsEntity(upper, lower, mean));
		}
		logger.warn("bollingerband done");
		return result;
	}

	public void calculateReferenceMetrics() {
		if (totalSystemLevelMetricsList == null)
			updateLatestDQList();

		Map<String, String> references = getReferences();
		
		for(String sourceName : references.keySet()) {
			String referencerNames = references.get(sourceName).toString();
			List<String> rNames = new ArrayList<String>();
			if (referencerNames.indexOf(",") == -1)
				rNames.add(referencerNames);
			else
				rNames = Arrays.asList(referencerNames.split(","));

			for (String referencerName : rNames) {
				logger.warn("==============anmoni loop start=================="
						+ referencerName + " " + sourceName);
				List<DqMetricsValue> metricList = metricsRepo.getByMetricsName(sourceName);// dqMetricsDao.findByField(DQMetricsValue.class,
                                                            // "metricName",
                                                            // sourceName);
				DqModel referencerModel = dqModelService
						.getGeneralModel(referencerName);
				if (referencerModel == null)
				{
					logger.warn("==============referencerModel is null================== "+referencerName);
					continue;
				}
				DqModel sourceModel = dqModelService
						.getGeneralModel(sourceName);
				if (sourceModel == null)
				{
					logger.warn("==============sourceModel is null================== "+sourceModel);
					continue;
				}

				if (sourceModel.getSchedule() == ScheduleType.DAILY) {
					trendLength = 20;
					trendOffset = 7;
				} else {
					trendLength = 20 * 24;
					trendOffset = 7 * 24;
				}

				Collections.sort(metricList);
				float threadshold = referencerModel.getThreshold();
				// type anomin detection trend
				if (referencerModel.getModelType() == ModelType.ANOMALY) {
					String content = referencerModel.getModelContent();
					String[] contents = content.split("\\|");
					int type = Integer.parseInt(contents[2]);

					// History Trend Detection
					if (type == 1) {
						logger.warn("==============trend start=================="
								+ referencerName
								+ " "
								+ sourceName
								+ " "
								+ trendLength + " " + trendOffset);
						if (metricList.size() <= trendLength
								+ trendOffset)
							continue;
						int dqfail = 0;
						if (metricList.get(0).getValue()
								/ metricList.get(trendOffset)
										.getValue() >= 1 + threadshold
								|| metricList.get(0).getValue()
										/ metricList.get(trendOffset)
												.getValue() <= 1 - threadshold)
							dqfail = 1;
						for (int i = 0; i <= trendLength; i++) {
							DqMetricsValue tempDQMetricsValue = metricList
									.get(i);
							float lastValue = metricList.get(
									i + trendOffset).getValue();
							totalSystemLevelMetricsList.upsertNewAssetExecute(
									referencerName,
									MetricType.Trend.toString(),
									tempDQMetricsValue.getTimestamp(),
									tempDQMetricsValue.getValue(), modelSystem
											.get(tempDQMetricsValue
													.getMetricName()), dqfail,
									1, new AssetLevelMetricsDetail(lastValue));
						}
						logger.warn("==============trend end==================");
					}
					// Bollinger Bands Detection
					else if (type == 2) {
						logger.warn("==============Bollinger start=================="
								+ referencerName + " " + sourceName);
						Collections.reverse(metricList);
						List<String> sourceValues = new ArrayList<String>();
						for (int i = 0; i < metricList.size(); i++) {
							sourceValues.add((long) metricList.get(i)
									.getValue() + "");
						}

						List<BollingerBandsEntity> bollingers = bollingerBand(sourceValues);

						int dqfail = 0;
						logger.warn("==============Bollinger size : "+bollingers.size() +" metrics size:"+metricList.size());
						if (metricList.size() > 0 && bollingers.size() > 0) {
							if (metricList.get(
									metricList.size() - 1).getValue() < bollingers
									.get(bollingers.size() - 1).getLower()) {
								dqfail = 1;
							}

							int offset = metricList.size()
									- bollingers.size();
							for (int i = offset; i < metricList.size(); i++) {
								DqMetricsValue tempDQMetricsValue = metricList
										.get(i);

								totalSystemLevelMetricsList
										.upsertNewAssetExecute(
												referencerName,
												MetricType.Bollinger.toString(),
												tempDQMetricsValue
														.getTimestamp(),
												tempDQMetricsValue.getValue(),
												modelSystem.get(tempDQMetricsValue
														.getMetricName()),
												dqfail,
												1,
												new AssetLevelMetricsDetail(
														new BollingerBandsEntity(
																bollingers
																		.get(i
																				- offset)
																		.getUpper(),
																bollingers
																		.get(i
																				- offset)
																		.getLower(),
																bollingers
																		.get(i
																				- offset)
																		.getMean())));
							}
						}
						logger.warn("==============Bollinger end=================="
								+ referencerName + " " + sourceName);
					}
					// MAD
					else if (type == 3) {
						logger.warn("==============MAD start=================="
								+ referencerName + " " + sourceName);
						Collections.reverse(metricList);
						List<String> sourceValues = new ArrayList<String>();
						for (int i = 0; i < metricList.size(); i++) {
							sourceValues.add((long) metricList.get(i)
									.getValue() + "");
						}
						List<MADEntity> MADList = MAD(sourceValues);

						int dqfail = 0;
						logger.warn("==============MAD size : "+MADList.size() +" metrics size:"+metricList.size());
						if (metricList.size() > 0 && MADList.size() > 0) {
							if (metricList.get(
									metricList.size() - 1).getValue() < MADList
									.get(MADList.size() - 1).getLower())
								dqfail = 1;

							int offset = metricList.size()
									- MADList.size();
							for (int i = offset; i < metricList.size(); i++) {
								DqMetricsValue tempDQMetricsValue = metricList
										.get(i);
								totalSystemLevelMetricsList
										.upsertNewAssetExecute(
												referencerName,
												MetricType.MAD.toString(),
												tempDQMetricsValue
														.getTimestamp(),
												tempDQMetricsValue.getValue(),
												modelSystem.get(tempDQMetricsValue
														.getMetricName()),
												dqfail,
												1,
												new AssetLevelMetricsDetail(
														new MADEntity(
																MADList.get(
																		i
																				- offset)
																		.getUpper(),
																MADList.get(
																		i
																				- offset)
																		.getLower())));
							}
						}
						logger.warn("==============MAD end==================");
					}
				}
				logger.warn("==============anmoni loop end=================="
						+ referencerName + " " + sourceName);
			}
		}
	}

	Map<String, String> getReferences() {
	    Map<String, String> map = new HashMap<>();
	    for(DqModel each : modelRepo.getAll()) {
	        map.put(each.getModelName(), each.getReferenceModel());
	    }
	    return map;
    }

    public List<SystemLevelMetrics> addAssetNames(
			List<SystemLevelMetrics> result) {
		List<DqModelVo> models = dqModelService.getAllModles();
		Map<String, String> modelMap = new HashMap<String, String>();

		for (DqModelVo model : models) {
			modelMap.put(
					model.getName(),
					model.getAssetName() == null ? "unknow" : model
							.getAssetName());
		}

		for (SystemLevelMetrics sys : result) {
			List<AssetLevelMetrics> assetList = sys.getMetrics();
			if (assetList != null && assetList.size() > 0) {
				for (AssetLevelMetrics metrics : assetList) {
					metrics.setAssetName(modelMap.get(metrics.getName()));
				}
			}
		}

		return result;
	}

	public Map<String, String> getAssetMap() {
		List<DqModelVo> models = dqModelService.getAllModles();
		Map<String, String> modelMap = new HashMap<String, String>();

		for (DqModelVo model : models) {
			modelMap.put(
					model.getName(),
					model.getAssetName() == null ? "unknow" : model
							.getAssetName());
		}

		return modelMap;
	}

	@Override
	public List<SystemLevelMetrics> briefMetrics(String system) {
		if (totalSystemLevelMetricsList == null)
			updateLatestDQList();
		return totalSystemLevelMetricsList.getListWithLatestNAssets(24, system,
				null, null);
	}

	@Override
	public List<SystemLevelMetrics> heatMap() {
		if (totalSystemLevelMetricsList == null)
			updateLatestDQList();
		return totalSystemLevelMetricsList.getHeatMap(getThresholds());
	}

	@Override
	public List<SystemLevelMetrics> dashboard(String system) {
		if (totalSystemLevelMetricsList == null)
			updateLatestDQList();
		return addAssetNames(totalSystemLevelMetricsList
				.getListWithLatestNAssets(30, system, null, null));
	}

	@Override
	public List<SystemLevelMetrics> mydashboard(String user) {
		if (totalSystemLevelMetricsList == null)
			updateLatestDQList();
		return addAssetNames(totalSystemLevelMetricsList
				.getListWithLatestNAssets(30, "all",
						subscribeService.getSubscribe(user), getAssetMap()));
	}

	@Override
	public AssetLevelMetrics oneDataCompleteDashboard(String name) {
		if (totalSystemLevelMetricsList == null)
			updateLatestDQList();
		return totalSystemLevelMetricsList.getListWithSpecificAssetName(name);
	}

	@Override
	public AssetLevelMetrics oneDataBriefDashboard(String name) {
		if (totalSystemLevelMetricsList == null)
			updateLatestDQList();
		return totalSystemLevelMetricsList.getListWithSpecificAssetName(name,
				30);
	}

	public OverViewStatistics getOverViewStats() {

		OverViewStatistics os = new OverViewStatistics();

		os.setAssets(dataAssetRepo.getAll().size());
		os.setMetrics(dqModelRepo.getAll().size());

		DQHealthStats health = new DQHealthStats();

		if (totalSystemLevelMetricsList == null)
			updateLatestDQList();

		List<SystemLevelMetrics> allMetrics = totalSystemLevelMetricsList
				.getLatestDQList();

		int healthCnt = 0;
		int invalidCnt = 0;

		for (SystemLevelMetrics metricS : allMetrics) {

			List<AssetLevelMetrics> metricsA = metricS.getMetrics();

			for (AssetLevelMetrics m : metricsA) {
				if (m.getDqfail() == 0) {
					healthCnt++;
				} else {
					invalidCnt++;
				}
			}
		}

		health.setHealth(healthCnt);
		health.setInvalid(invalidCnt);

		health.setWarn(0);
		os.setStatus(health);

		return os;

	}

	@Override
	/**
	 * Get the metrics for 24 hours
	 */
	public AssetLevelMetrics metricsForReport(String name) {
		if (totalSystemLevelMetricsList == null)
			updateLatestDQList();
		return totalSystemLevelMetricsList.getListWithSpecificAssetName(name,
				24);
	}

	public List<SampleOut> listSampleFile(String modelName) {

		List<SampleOut> samples = new ArrayList<SampleOut>();

		List<DBObject> dbos = missedFileRepo.findByModelName(modelName);

		for (DBObject dbo : dbos) {

			SampleOut so = new SampleOut();

			so.setDate(Long.parseLong(dbo.get("timestamp").toString()));
			so.setPath(dbo.get("hdfsPath").toString());

			samples.add(so);
		}

		return samples;

	}

	public void insertSampleFilePath(SampleFilePathLKP samplePath) {
		SampleFilePathLKP entity = new SampleFilePathLKP();

		entity.setId(missedFileRepo.getNextId());
		entity.setModelName(samplePath.getModelName());
		entity.setTimestamp(samplePath.getTimestamp());
		entity.setHdfsPath(samplePath.getHdfsPath());

		missedFileRepo.save(entity);

	}

	public void downloadSample(String filePath) {

	}

}
