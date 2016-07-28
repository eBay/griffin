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

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;
@XmlRootElement
@Entity("hive_table_metadata")
public class HiveTable extends DoBase {

	@Property("tableId")
	private Long tableId;

	@Property("platform")
	private String platform;

	@Property("db")
	private String db;

	@Property("tableName")
	private String tableName;

	@Embedded
	private List<HiveColumn> columns;

	public Long getTableId() {
		return tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<HiveColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<HiveColumn> columns) {
		this.columns = columns;
	}


}
