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

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class UserSubscribeItem {
	String _id;
	String ntaccount;
	List<PlatformSubscribeItem> subscribes;

	public UserSubscribeItem()
	{
		;
	}

	public UserSubscribeItem(String user)
	{
		ntaccount = user;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getNtaccount() {
		return ntaccount;
	}

	public void setNtaccount(String ntaccount) {
		this.ntaccount = ntaccount;
	}

	public List<PlatformSubscribeItem> getSubscribes() {
		return subscribes;
	}

	public void setSubscribes(List<PlatformSubscribeItem> subscribes) {
		this.subscribes = subscribes;
	}

	public boolean isPlatformSelected(String platform)
	{
		for(PlatformSubscribeItem item : subscribes)
		{
			if(item.getPlatform().equals(platform) && item.isSelectAll()) return true;
		}
		return false;
	}

	public boolean isSystemSelected(String platform, String system)
	{
		for(PlatformSubscribeItem item : subscribes)
		{
			if(item.getPlatform().equals(platform))
			{
				List<SystemSubscribeItem> systems = item.getSystems();
				for(SystemSubscribeItem tempSystem : systems)
				{
					if(tempSystem.getSystem().equals(system) && tempSystem.isSelectAll()) return true;
				}
			}
		}
		return false;
	}

	public boolean isDataAssetSelected(String platform, String system, String dataasset)
	{
		for(PlatformSubscribeItem item : subscribes)
		{
			if(item.getPlatform().equals(platform))
			{
				List<SystemSubscribeItem> systems = item.getSystems();
				for(SystemSubscribeItem tempSystem : systems)
				{
					if(tempSystem.getSystem().equals(system))
					{
						List<String> dataassets = tempSystem.getDataassets();
						if(dataassets.contains(dataasset)) return true;
					}
				}
			}
		}
		return false;
	}

}
