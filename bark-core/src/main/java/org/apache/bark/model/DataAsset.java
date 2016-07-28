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
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
@XmlRootElement
@XmlSeeAlso({BasicDBObject.class})
@Entity("data_assets")
public class DataAsset extends DoBase{

	@Property("platform")
	private String platform;

	@Property("system")
	private String system;

	@Property("assetName")
	private String assetName;

	@Property("assetType")
	private String assetType;

	@Property("assetHDFSPath")
	private String assetHDFSPath;

	@Property("owner")
	private String owner;

	@Property("timestamp")
	private Date timestamp;

	@Embedded
	private List<DataSchema> schema;

	@Embedded
	private List<PartitionFormat> partitions;

	public DataAsset()
	{
		;
	}

	public DataAsset(DBObject o)
	{
		this.set_id(new Long((long)Double.parseDouble(o.get("_id").toString())));
		if(o.get("assetHDFSPath")!=null) this.setAssetHDFSPath(o.get("assetHDFSPath").toString());
		if(o.get("assetName")!=null) this.setAssetName(o.get("assetName").toString());
		if(o.get("assetType")!=null) this.setAssetType(o.get("assetType").toString());
		if(o.get("owner")!=null) this.setOwner(o.get("owner").toString());
		if(o.get("platform")!=null) this.setPlatform(o.get("platform").toString());

		List<PartitionFormat> partitionlist = new ArrayList<PartitionFormat>();
		if(o.get("partitions")!=null)
		{
			List<DBObject> tlist = (List<DBObject>) o.get("partitions");

			for(DBObject temp : tlist)
			{
				partitionlist.add(new PartitionFormat(temp.get("name").toString(), temp.get("format").toString()));
			}
		}
		this.setPartitions(partitionlist);

		if(o.get("schema")!=null)
		{
			List<DBObject> tlist = (List<DBObject>) o.get("schema");
			List<DataSchema> list = new ArrayList<DataSchema>();
			for(DBObject temp : tlist)
			{
				list.add(new DataSchema(temp.get("name").toString(), temp.get("type").toString(), temp.get("desc").toString(), temp.get("sample").toString()));
			}
			this.setSchema(list);
		}
		if(o.get("system")!=null) this.setSystem(o.get("system").toString());
		//if(o.get("timestamp")!=null) this.setTimestamp(new Date(o.get("timestamp").toString()));
		this.setTimestamp(new Date());
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getAssetHDFSPath() {
		return assetHDFSPath;
	}

	public void setAssetHDFSPath(String assetHDFSPath) {
		this.assetHDFSPath = assetHDFSPath;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public List<DataSchema> getSchema() {
		return schema;
	}

	public void setSchema(List<DataSchema> schema) {
		this.schema = schema;
	}

	public List<PartitionFormat> getPartitions() {
		return partitions;
	}

	public void setPartitions(List<PartitionFormat> partitions) {
		this.partitions = partitions;
	}

	public int getColId(String colName)
	{
		for(int i=0;i<schema.size();i++)
		{
			if(schema.get(i).getName().equals(colName))
				return i;
		}
		return -1;
	}
}
