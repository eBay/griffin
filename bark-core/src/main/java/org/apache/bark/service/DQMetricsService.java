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

import java.util.List;

import org.apache.bark.domain.DqMetricsValue;
import org.apache.bark.domain.SampleFilePathLKP;
import org.apache.bark.vo.AssetLevelMetrics;
import org.apache.bark.vo.OverViewStatistics;
import org.apache.bark.vo.SampleOut;
import org.apache.bark.vo.SystemLevelMetrics;

public interface DQMetricsService {
	public void insertMetadata(DqMetricsValue dq);

	// public List<DQMetricsValue> getMetricsValueById(String assetId);

	public DqMetricsValue getLatestlMetricsbyId(String assetId);

	// public AssetLevelMetrics getAssetLevelMetricsbyId(String assetId);
	// public SystemLevelMetrics getSystemLevelMetricsbySystem(String system);

	// public List<String> fetchAllAssetIdBySystem(String system);

	// public List<SystemLevelMetrics> getAllSystemMetrcis();
	public List<SystemLevelMetrics> heatMap();

	public List<SystemLevelMetrics> briefMetrics(String system);

	public List<SystemLevelMetrics> dashboard(String system);

	public AssetLevelMetrics oneDataCompleteDashboard(String name);

	public AssetLevelMetrics oneDataBriefDashboard(String name);

	public AssetLevelMetrics metricsForReport(String name);

	public void updateLatestDQList();

	public OverViewStatistics getOverViewStats();

	public List<SystemLevelMetrics> mydashboard(String user);

	public List<SampleOut> listSampleFile(String modelName);

	public void downloadSample(String filePath);

	public void insertSampleFilePath(SampleFilePathLKP samplePath);

}
