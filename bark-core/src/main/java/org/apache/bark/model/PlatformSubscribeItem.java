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
package org.apache.bark.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class PlatformSubscribeItem {
	String platform;
	boolean selectAll;
	List<SystemSubscribeItem> systems;

	public PlatformSubscribeItem()
	{
		systems = new ArrayList<SystemSubscribeItem>();
	}

	public PlatformSubscribeItem(String platform)
	{
		this.platform = platform;
		systems = new ArrayList<SystemSubscribeItem>();
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public boolean isSelectAll() {
		return selectAll;
	}

	public void setSelectAll(boolean selectAll) {
		this.selectAll = selectAll;
	}

	public List<SystemSubscribeItem> getSystems() {
		return systems;
	}

	public void setSystems(List<SystemSubscribeItem> systems) {
		this.systems = systems;
	}

	public void addSystem(SystemSubscribeItem s)
	{
		this.systems.add(s);
	}


}
