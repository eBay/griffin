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

import java.util.HashMap;
import java.util.List;

import org.apache.bark.common.BarkDbOperationException;
import org.apache.bark.model.DQModelEntity;
import org.apache.bark.model.ModelForFront;
import org.apache.bark.model.ModelInput;

public interface DQModelService {
	public List<ModelForFront> getAllModles();
	public int deleteModel(String name) throws BarkDbOperationException;
	public HashMap<String, String> getThresholds();
	public HashMap<String, String> getReferences();
	public DQModelEntity getGeneralModel(String name);


	public ModelInput getModelByName(String name) throws BarkDbOperationException;
	public void enableSchedule4Model(DQModelEntity input);
	
	/**
	 * Create a new Model
	 * @param input
	 * @return 0 if successful, -1 if already existing, other negative values for other reasons
	 */
	public int newModel(ModelInput input) throws BarkDbOperationException;
}
