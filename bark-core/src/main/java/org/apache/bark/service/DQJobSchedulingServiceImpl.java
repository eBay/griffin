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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.bark.common.JobStatusConstants;
import org.apache.bark.common.KeyValue;
import org.apache.bark.common.ModelSampleFlowConstants;
import org.apache.bark.common.ModelStatusConstants;
import org.apache.bark.common.ModelTypeConstants;
import org.apache.bark.common.ScheduleModelSeperator;
import org.apache.bark.common.ScheduleTypeConstants;
import org.apache.bark.common.SystemTypeConstants;
import org.apache.bark.dao.BarkMongoDAO;
import org.apache.bark.model.AccuracyHiveJobConfig;
import org.apache.bark.model.AccuracyHiveJobConfigDetail;
import org.apache.bark.model.DQJob;
import org.apache.bark.model.DQMetricsValue;
import org.apache.bark.model.DQModelEntity;
import org.apache.bark.model.DataAsset;
import org.apache.bark.model.PartitionConfig;
import org.apache.bark.model.PartitionFormat;
import org.apache.bark.model.SampleFilePathLKP;
import org.apache.bark.model.ValidateHiveJobConfig;
import org.apache.bark.model.ValidateHiveJobConfigLv1Detail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
//import org.springframework.validation.annotation.Validated;

import com.google.gson.Gson;
import com.mongodb.DBObject;

@Service
@Component("dqjob")
//@Validated
public class DQJobSchedulingServiceImpl implements DQJobSchedulingService {
	private static Logger logger = LoggerFactory.getLogger(DQJobSchedulingServiceImpl.class);

	public static String resultFile = "_RESULT";
	public static String startFile = "_START";
	public static String finishFile = "_FINISH";

	@Autowired
	DQMetricsService dqMetricsService;

	@Override
	public void schedulingJobs() {
		logger.warn("===========checking new jobs===============");

		createJobToRunBySchedule();

		generateAllWaitingJobsRunningConfigs();

		checkAllJOBSStatus();

		updateModelStatus();

		logger.warn("===========checking jobs done===============");
	}

	public void updateModelStatus()
	{
		List<DQModelEntity> allmodels = BarkMongoDAO.getModelDAO().getAllModelsByStatus(ModelStatusConstants.TESTING);
		for(DQModelEntity model : allmodels)
		{
			List<DQMetricsValue> allMetrics = BarkMongoDAO.getModelDAO().getAllMetricsByMetricsName(model.getModelName());
			if(allMetrics.size()>=ModelSampleFlowConstants.sampelCount)
			{
				model.setStatus(ModelStatusConstants.VERIFIED);
				BarkMongoDAO.getModelDAO().upsertModel(model);
			}
		}
	}

	public void createJobToRunBySchedule()
	{
		try{
			List<DBObject> all = BarkMongoDAO.getModelDAO().getAllScheduling();
			for(DBObject tempScheduleEntity : all)
			{
				long now = new Date().getTime();
				long starttime = Long.parseLong(tempScheduleEntity.get("starttime").toString());
				if(now<starttime) continue;

				long time = Long.parseLong(tempScheduleEntity.get("starttime").toString());
				int type = (int) Float.parseFloat(tempScheduleEntity.get("scheduleType").toString());
				Calendar c=Calendar.getInstance();
				Date date=new Date(time);
				c.setTime(date);
				if(type==ScheduleTypeConstants.DAILY)
					c.add(Calendar.DATE,1);
				else if(type==ScheduleTypeConstants.HOURLY)
					c.add(Calendar.HOUR,1);
				else if(type==ScheduleTypeConstants.WEEKLY)
					c.add(Calendar.DATE,7);
				else if(type==ScheduleTypeConstants.MONTHLY)
					c.add(Calendar.MONTH,1);
				else
					continue;

				DQJob job = new DQJob();
				job.setModelList(tempScheduleEntity.get("modelList").toString());
				job.setStarttime(starttime);
				job.setStatus(0);
				job.set_id(tempScheduleEntity.get("modelList").toString()+"_"+starttime);
				job.setJobType(Integer.parseInt(tempScheduleEntity.get("jobType").toString()));
				int result = BarkMongoDAO.getModelDAO().newJob(job);
				if(result==0)
				{
					logger.warn( "===================new model failure");
					continue;
				}



				time = c.getTime().getTime();
				BarkMongoDAO.getModelDAO().updateScheduleTime(tempScheduleEntity, time);


			}
		}
		catch(Exception e)
		{
			logger.warn(e.toString(), e);

		}
	}

	//	public static void main(String args[])
	//	{
	//		DQJobSchedulingServiceImpl a = new DQJobSchedulingServiceImpl();
	//		System.out.println(a.updateHDFSDirTemplateString("/apps/hdmi-technology/b_des/hive/be_view_event_queue/dt=[YYYYMMDD]/hour=[HH]/[YYYY]/[YY]/[YYYY-MM-DD]/[MM]/[DD]", "20160525", "17"));
	//	}

	public String updateHDFSDirTemplateString(String dir,String dateString,String hourString)
	{
		String result = dir;
		result = result.replaceAll("\\[YYYYMMDD\\]", dateString);
		result = result.replaceAll("\\[YYYY\\-MM\\-DD\\]", dateString.substring(0,4)+"-"+dateString.substring(4,6)+"-"+dateString.substring(6,8));
		result = result.replaceAll("\\[YYYY\\]", dateString.substring(0,4));
		result = result.replaceAll("\\[YY\\]", dateString.substring(2,4));
		result = result.replaceAll("\\[MM\\]", dateString.substring(4,6));
		result = result.replaceAll("\\[DD\\]", dateString.substring(6,8));
		result = result.replaceAll("\\[HH\\]", hourString);
		result = result.replaceAll("\\[yyyymmdd\\]", dateString);
		result = result.replaceAll("\\[yyyy\\-mm\\-dd\\]", dateString.substring(0,4)+"-"+dateString.substring(4,6)+"-"+dateString.substring(6,8));
		result = result.replaceAll("\\[yyyy\\]", dateString.substring(0,4));
		result = result.replaceAll("\\[yy\\]", dateString.substring(3,4));
		result = result.replaceAll("\\[mm\\]", dateString.substring(4,6));
		result = result.replaceAll("\\[dd\\]", dateString.substring(6,8));
		result = result.replaceAll("\\[hh\\]", hourString);
		return result;
	}

	public void generateAllWaitingJobsRunningConfigs()
	{
		try{
			logger.warn("===========generating running config===============");
			Properties env = new Properties();
			env.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("application.properties"));
			String environment = env.getProperty("env");

			List<DBObject> list = BarkMongoDAO.getModelDAO().getAllSchedulingByStatus(JobStatusConstants.READY);
			for(DBObject tempDBObject : list)
			{
				String jobid = (String) tempDBObject.get("_id");
				int jobtype = Integer.parseInt(tempDBObject.get("jobType").toString());
				StringBuffer doneFiles = new StringBuffer();
				StringBuffer runningParameter = new StringBuffer();

				if(jobtype==ModelTypeConstants.ACCURACY)
				{
					String modelid = (String) tempDBObject.get("modelList");
					long ts = (long) tempDBObject.get("starttime");
					Date dt = new Date(ts);
					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
					String dateString = formatter.format(dt);
					SimpleDateFormat formatter2 = new SimpleDateFormat("HH");
					String hourString = formatter2.format(dt);

					DQModelEntity model = BarkMongoDAO.getModelDAO().findByColumn("modelId", modelid);
					if(model==null)
					{
						logger.warn( "===================can not find model "+modelid);
						continue;
					}

					String content = model.getModelContent();

					String[] contents = content.split("\\|");
					String srcPlatform = contents[0];
					String srcSystem = contents[1];
					String tgtPlatform = contents[2];
					String tgtSystem = contents[3];

					String[] attributesArray = contents[4].split(";");
					String[] attributes = attributesArray[0].split(",");
					String srcDataset = attributes[0].split("\\.")[0];
					String tgtDataset = attributes[1].split("\\.")[0];

					//					runningParameter.append(System.getProperty("line.separator")+srcPlatform+" "+srcSystem+" "+srcDataset);
					//					runningParameter.append(System.getProperty("line.separator")+tgtPlatform+" "+tgtSystem+" "+tgtDataset);

					List<KeyValue> queryList = new ArrayList<KeyValue>();
					queryList.add(new KeyValue("platform", srcPlatform));
					queryList.add(new KeyValue("system", srcSystem));
					queryList.add(new KeyValue("assetName", srcDataset));
					DBObject srcObj = BarkMongoDAO.getModelDAO().getAssetByCondition(queryList);
					DataAsset srcAsset = new DataAsset(srcObj);

					List<KeyValue> queryList2 = new ArrayList<KeyValue>();
					queryList2.add(new KeyValue("platform", tgtPlatform));
					queryList2.add(new KeyValue("system", tgtSystem));
					queryList2.add(new KeyValue("assetName", tgtDataset));
					DBObject tgtObj = BarkMongoDAO.getModelDAO().getAssetByCondition(queryList2);
					DataAsset tgtAsset = new DataAsset(tgtObj);

					doneFiles.append(updateHDFSDirTemplateString(srcAsset.getAssetHDFSPath(),dateString,hourString)
							+System.getProperty("line.separator")
							+updateHDFSDirTemplateString(tgtAsset.getAssetHDFSPath(),dateString,hourString)
							+System.getProperty("line.separator"));
					if(model.getSchedule()==ScheduleTypeConstants.HOURLY && model.getSystem()==SystemTypeConstants.BULLSEYE)
					{
						Date dt4be = new Date(ts+3600000);
						SimpleDateFormat formatter4be = new SimpleDateFormat("yyyyMMdd");
						String dateString4be = formatter.format(dt4be);
						SimpleDateFormat formatter24be = new SimpleDateFormat("HH");
						String hourString4be = formatter2.format(dt4be);
						doneFiles.append(updateHDFSDirTemplateString(tgtAsset.getAssetHDFSPath(),dateString4be,hourString4be)
								+System.getProperty("line.separator"));
					}


					AccuracyHiveJobConfig config = new AccuracyHiveJobConfig();
					List<AccuracyHiveJobConfigDetail> configDetailList = new ArrayList<AccuracyHiveJobConfigDetail>();
					for(String tempAttribute : attributesArray)
					{
						String[] tempAttributeArray = tempAttribute.split(",");
						String srcColName = tempAttributeArray[0].split("\\.")[1];
						String tgtColName = tempAttributeArray[1].split("\\.")[1];
						configDetailList.add(new AccuracyHiveJobConfigDetail(
								srcAsset.getColId(srcColName), srcColName
								, tgtAsset.getColId(tgtColName), tgtColName
								,tempAttributeArray[3], Boolean.parseBoolean(tempAttributeArray[2].toUpperCase())
								) );
					}
					config.setAccuracyMapping(configDetailList);
					config.setSource(srcAsset.getAssetName());
					config.setTarget(tgtAsset.getAssetName());

					config.setSrcPartitions(getPartitionList(srcAsset, ts));

					List<List<PartitionConfig>> tgtPartitions = new ArrayList<List<PartitionConfig>>();
					tgtPartitions.add(getPartitionList(tgtAsset, ts));
					if(model.getSchedule()==ScheduleTypeConstants.HOURLY && model.getSystem()==SystemTypeConstants.BULLSEYE)
						tgtPartitions.add(getPartitionList(tgtAsset, ts+3600000));

					config.setTgtPartitions(tgtPartitions);

					Gson gson = new Gson();
					runningParameter.append(gson.toJson(config)+System.getProperty("line.separator"));
				}
				else if(jobtype==ModelTypeConstants.VALIDITY)
				{
					String modelList = (String) tempDBObject.get("modelList");
					long ts = (long) tempDBObject.get("starttime");
					Date dt = new Date(ts);
					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
					String dateString = formatter.format(dt);
					SimpleDateFormat formatter2 = new SimpleDateFormat("HH");
					String hourString = formatter2.format(dt);

					List<String> models = new ArrayList<String>();
					if(!modelList.contains(ScheduleModelSeperator.SEPERATOR))
					{
						models.add(modelList);
					}
					else
					{
						models = Arrays.asList(modelList.split(ScheduleModelSeperator.SPLIT_SEPERATOR));
					}


					if(models.size()==0) return;
					logger.debug("+++ model id value: " + models.get(0));
					DQModelEntity model = BarkMongoDAO.getModelDAO().findByColumn("modelId", models.get(0));
					logger.debug("--- model: " + model);
					DataAsset srcAsset = BarkMongoDAO.getModelDAO().getAssetById((long)model.getAssetId());


					doneFiles.append(updateHDFSDirTemplateString(srcAsset.getAssetHDFSPath(),dateString,hourString)
							+System.getProperty("line.separator"));

					ValidateHiveJobConfig config = new ValidateHiveJobConfig(srcAsset.getAssetName());
					config.setTimePartitions(getPartitionList(srcAsset, ts));

					List<ValidateHiveJobConfigLv1Detail> validityReq = new ArrayList<ValidateHiveJobConfigLv1Detail>();

					for(String modelname : models)
					{
						model = BarkMongoDAO.getModelDAO().findByColumn("modelId", modelname);
						if(model==null)
						{
							logger.warn("===================can not find model "+modelname);
							continue;
						}

						String content = model.getModelContent();
						String[] contents = content.split("\\|");
						String calType = contents[2];
						String calColname = contents[3];

						config.addColumnCalculation(srcAsset.getColId(calColname), calColname, Integer.parseInt(calType));
					}

					Gson gson = new Gson();
					runningParameter.append(gson.toJson(config)+System.getProperty("line.separator"));
				}




				logger.warn( "===================="+env.getProperty("job.local.folder")+File.separator+jobid+File.separator+"cmd.txt");

				String dir = env.getProperty("job.local.folder")+File.separator+jobid+File.separator+"cmd.txt";
				createFile(dir);
				File file = new File(dir);
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(runningParameter.toString());
				bw.flush();
				bw.close();

				String dir2 = env.getProperty("job.local.folder")+File.separator+jobid+File.separator+"watchfile.txt";
				createFile(dir2);
				File file2 = new File(dir2);
				FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
				BufferedWriter bw2 = new BufferedWriter(fw2);
				bw2.write(doneFiles.toString());
				bw2.flush();
				bw2.close();

				logger.warn("====================create file done");

				if(environment.equals("prod"))
				{
					String hdfs = env.getProperty("job.hdfs.folder")+"/"+env.getProperty("job.hdfs.runningfoldername");
					Process process1 = Runtime.getRuntime().exec("hadoop fs -mkdir "+hdfs+File.separator+jobid);
					logger.warn("====================hadoop fs -mkdir "+hdfs+File.separator+jobid);
					process1.waitFor();;
					Process process2 = Runtime.getRuntime().exec("hadoop fs -put "+dir+" "+hdfs+File.separator+jobid+File.separator);
					logger.warn( "====================hadoop fs -put "+dir+" "+hdfs+File.separator+jobid+File.separator);
					process2.waitFor();
					Process process2_1 = Runtime.getRuntime().exec("hadoop fs -put "+dir2+" "+hdfs+File.separator+jobid+File.separator+"_watchfile");
					logger.warn("====================hadoop fs -put "+dir2+" "+hdfs+File.separator+jobid+File.separator+"_watchfile");
					process2_1.waitFor();
					Process process3 = Runtime.getRuntime().exec("hadoop fs -touchz "+hdfs+File.separator+jobid+File.separator+"_type_"+jobtype+".done");
					logger.warn( "====================hadoop fs -touchz "+hdfs+File.separator+jobid+File.separator+"_type_"+jobtype+".done");
					process3.waitFor();

				}

				//file.delete();
				new File(env.getProperty("job.local.folder")+File.separator+jobid).delete();
				logger.warn( "====================delete file done");

				BarkMongoDAO.getModelDAO().updateJobStatus(tempDBObject, JobStatusConstants.WAITING);
				logger.warn("====================udpate status done");


			}


		}
		catch(Exception e)
		{
			logger.warn(e.toString(), e);
		}

	}

	public List<PartitionConfig> getPartitionList(DataAsset srcAsset, long ts)
	{
		Date dt = new Date(ts);
		List<PartitionConfig> partitions = new ArrayList<PartitionConfig>();
		List<PartitionFormat> lv1partitions = srcAsset.getPartitions();
		if(lv1partitions!=null)
		{
			for(PartitionFormat tempPartitionFormat : lv1partitions)
			{
				SimpleDateFormat tempFormatter = new SimpleDateFormat(tempPartitionFormat.getFormat());
				String tempdateString = tempFormatter.format(dt);
				partitions.add(new PartitionConfig(tempPartitionFormat.getName(), tempdateString));
			}
		}
		return partitions;
	}

	public void checkAllJOBSStatus()
	{
		try {
			Properties env = new Properties();
			env.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("application.properties"));
			String hdfsbasedir = env.getProperty("job.hdfs.folder");
			String runningfoldername = env.getProperty("job.hdfs.runningfoldername");
			String historyfoldername = env.getProperty("job.hdfs.historyfoldername");
			String environment = env.getProperty("env");
			String localdir = env.getProperty("job.local.folder");
			if(!environment.equals("prod")) return;

			int result;
			Process processLV1 = Runtime.getRuntime().exec("hadoop fs -ls "+hdfsbasedir+"/"+runningfoldername);
			result = processLV1.waitFor();
			if(result != 0)
			{
				logger.warn("===================="+"hadoop fs -ls "+hdfsbasedir+"/"+runningfoldername+" error");
				return;
			}

			BufferedReader readerLV1 = new BufferedReader(
					new InputStreamReader(processLV1.getInputStream()));
			if(readerLV1==null) return;
			String lineLV1;
			int index;
			while ((lineLV1 = readerLV1.readLine()) != null) {
				index = lineLV1.indexOf("/");
				if(index==-1) continue;
				String runningJobFolderLV1Dir = lineLV1.substring(index);
				logger.warn("===================checking hdfs folder"+runningJobFolderLV1Dir);

				Process processLV2 = Runtime.getRuntime().exec("hadoop fs -ls "+runningJobFolderLV1Dir);
				result = processLV2.waitFor();
				if(result != 0)
				{
					logger.warn("===================="+"hadoop fs -ls "+runningJobFolderLV1Dir+" error");
					continue;
				}
				BufferedReader readerLV2 = new BufferedReader(
						new InputStreamReader(processLV2.getInputStream()));
				if(readerLV2==null) return;
				String lineLV2;
				int startindi = 0;
				int finishindi = 0;
				int resultindi = 0;
				while ((lineLV2 = readerLV2.readLine()) != null) {
					index = lineLV2.indexOf("/");
					if(index==-1) continue;
					String runningJobContentDir = lineLV2.substring(index);
					logger.warn("===================checking hdfs folder"+runningJobContentDir);
					if(runningJobContentDir.indexOf(startFile)!=-1)
						startindi = 1;
					else if(runningJobContentDir.indexOf(finishFile)!=-1)
						finishindi = 1;
					else if(runningJobContentDir.indexOf(resultFile)!=-1)
						resultindi = 1;
				}

				String jobID = runningJobFolderLV1Dir.substring(runningJobFolderLV1Dir.indexOf(runningfoldername)+runningfoldername.length()+1);
				logger.warn("===================job id: "+jobID);
				DBObject job = BarkMongoDAO.getModelDAO().getJobbyId(jobID);
				if(job==null)
				{
					logger.warn("===================no such job: "+job);
					return;
				}

				if(startindi == 1)
				{
					logger.warn("===================start");
					BarkMongoDAO.getModelDAO().updateJobStatus(job, JobStatusConstants.STARTED);
					logger.warn("===================udpate job status to started");
					//					Process processChangeStartFile = Runtime.getRuntime().exec("hadoop fs -mv "+runningJobFolderLV1Dir+"/"+startFile+" "+runningJobFolderLV1Dir+"/_RUNNING");
					//					result = processChangeStartFile.waitFor();
				}

				if(finishindi == 1 && resultindi ==1)
				{
					logger.warn("===================finished");

					String historyJobFolderLV1Dir = runningJobFolderLV1Dir.replaceAll(runningfoldername, historyfoldername);
					Process processMoveFolder = Runtime.getRuntime().exec("hadoop fs -mv "+runningJobFolderLV1Dir+" "+historyJobFolderLV1Dir);
					result = processMoveFolder.waitFor();
					if(result != 0)
					{
						logger.warn("===================="+"hadoop fs -mv "+runningJobFolderLV1Dir+" "+historyJobFolderLV1Dir+" error");
						continue;
					}
					logger.warn("===================moved to history folder");





					if(environment.equals("prod"))
					{
						logger.warn("===================publish metrics.");

						String hdfs = env.getProperty("job.hdfs.folder")+"/"+env.getProperty("job.hdfs.historyfoldername");
						String resultLocalFileDir = localdir+File.separator+jobID+File.separator+resultFile;
						createFile(resultLocalFileDir);
						new File(resultLocalFileDir).delete();
						Process process1 = Runtime.getRuntime().exec("hadoop fs -get "+hdfs+File.separator+jobID+File.separator+resultFile+" "+resultLocalFileDir);
						logger.warn("====================hadoop fs -get "+hdfs+File.separator+jobID+File.separator+resultFile+" "+resultLocalFileDir);
						process1.waitFor();

						File rFile = new File(resultLocalFileDir);
						BufferedReader reader = new BufferedReader(new FileReader(rFile));
						String resultValue = reader.readLine();

						String metricsNames = jobID.substring(0, jobID.lastIndexOf("_"));
						List<String> metricsNameArray = new ArrayList<String>();
						if(!metricsNames.contains(ScheduleModelSeperator.SEPERATOR))
						{
							metricsNameArray.add(metricsNames);
						}
						else
						{
							metricsNameArray = Arrays.asList(metricsNames.split(ScheduleModelSeperator.SPLIT_SEPERATOR));
						}

						for(String metricsName : metricsNameArray)
						{
							DQModelEntity model = BarkMongoDAO.getModelDAO().findByName(metricsName);
							if(model.getModelType() == ModelTypeConstants.ACCURACY)
							{
								float floatResultValue = -1;
								long ts = -1;
								try{
									floatResultValue = Float.parseFloat(resultValue);
									ts = Long.parseLong(jobID.substring(jobID.lastIndexOf("_")+1));
								}
								catch(Exception e)
								{
									logger.warn(e.toString(), e);
								}
								if(floatResultValue >= 0 && ts>= 0)
								{
									DQMetricsValue newDQMetricsValue = new DQMetricsValue();
									newDQMetricsValue.setMetricName(jobID.substring(0, jobID.lastIndexOf("_")));
									newDQMetricsValue.setTimestamp(ts);
									newDQMetricsValue.setValue(floatResultValue);
									logger.warn("===================new accuracy dq metrics: "+newDQMetricsValue.getMetricName()+" "+newDQMetricsValue.getTimestamp()+" "+newDQMetricsValue.getTimestamp());
									dqMetricsService.insertMetadata(newDQMetricsValue);

									BarkMongoDAO.getModelDAO().updateJobEndingInfo(job, ts, floatResultValue);
								}

								//insert missing data path to mongo

								SampleFilePathLKP sfp = new SampleFilePathLKP();

								sfp.setHdfsPath(historyJobFolderLV1Dir + "/" + "missingRec.txt");
								sfp.setModelName(jobID.substring(0, jobID.lastIndexOf("_")));
								sfp.setTimestamp(ts);

								dqMetricsService.insertSampleFilePath(sfp);


							}
							else if(model.getModelType() == ModelTypeConstants.VALIDITY)
							{
								Gson gson = new Gson();
								ValidateHiveJobConfig resultObject = gson.fromJson(resultValue.toString(), ValidateHiveJobConfig.class);
								String content = model.getModelContent();
								String[] contents = content.split("\\|");
								String calType = contents[2];
								String calColname = contents[3];
								long tempValue = resultObject.getValue(calColname, Integer.parseInt(calType));

								long ts = -1;
								try{
									ts = Long.parseLong(jobID.substring(jobID.lastIndexOf("_")+1));
								}
								catch(Exception e)
								{
									logger.warn(e.toString(), e);
								}

								if(tempValue >= 0 && ts>= 0)
								{
									DQMetricsValue newDQMetricsValue = new DQMetricsValue();
									newDQMetricsValue.setMetricName(metricsName);
									newDQMetricsValue.setTimestamp(ts);
									newDQMetricsValue.setValue(tempValue);
									logger.warn("===================new validity dq metrics: "+metricsName+" "+ts+" "+tempValue);
									dqMetricsService.insertMetadata(newDQMetricsValue);

									BarkMongoDAO.getModelDAO().updateJobEndingInfo(job, ts, tempValue);
								}
							}

							logger.warn("===================publish metrics done.");
						}
					}




					BarkMongoDAO.getModelDAO().updateJobStatus(job, JobStatusConstants.FINISHED);

				}
				else
				{
					logger.warn("===================waiting");
				}

			}



		} catch (Exception e) {
			logger.warn(e.toString(), e);
		}
	}

	public boolean createFile(String destFileName) {
		File file = new File(destFileName);
		if(file.exists()) {
			return false;
		}
		if (destFileName.endsWith(File.separator)) {
			return false;
		}
		if(!file.getParentFile().exists()) {
			if(!file.getParentFile().mkdirs()) {
				return false;
			}
		}
		try {
			if (file.createNewFile()) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
