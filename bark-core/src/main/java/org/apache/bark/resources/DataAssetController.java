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
package org.apache.bark.resources;

import java.util.Date;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.bark.domain.DataAsset;
import org.apache.bark.error.BarkDbOperationException;
import org.apache.bark.error.ErrorMessage;
import org.apache.bark.service.DataAssetService;
import org.apache.bark.service.NotificationService;
import org.apache.bark.vo.DataAssetInput;
import org.apache.bark.vo.NotificationRecord;
import org.apache.bark.vo.PlatformMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@Scope("request")
@Path("/dataassets")
public class DataAssetController {

	@Autowired
	private DataAssetService dataAssetService;
	@Autowired
	private NotificationService notificationService;

	/**
	 * Get data asset metadata
	 * @return
	 */
	@GET
	@Path("/metadata")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PlatformMetadata> getSrcTree() {
		return dataAssetService.getSourceTree();
	}

	/**
	 * Get schema definition of data asset
	 * @param id
	 * @return
	 * @throws BarkDbOperationException
	 */
	@GET
	@Path("/metadata/{asset_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public DataAsset getDataAssetById(@PathParam("asset_id") Long id) throws BarkDbOperationException {
		return dataAssetService.getDataAssetById(id);
	}

	/**
	 * Register a new data asset
	 * @param input
	 * @return
	 */
	@POST
	@Path("/")
	public Response registerNewDataAsset(DataAssetInput input) {
		try {
			dataAssetService.createDataAsset(input);
			notificationService.insert(new NotificationRecord(new Date().getTime(),  input.getOwner(), "create", "dataasset", input.getAssetName()));
			return Response.status(Response.Status.CREATED).build();
			//			return "{\"status\":\"0\" , \"result\":\"success\"}";
		} catch (BarkDbOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//			return "{\"status\":\"-1\" , \"result\":\""+ e.getMessage() +"\"}";
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new ErrorMessage(
							Response.Status.INTERNAL_SERVER_ERROR
							.getStatusCode(), e.getMessage()))
							.type(MediaType.APPLICATION_JSON).build();
		}

	}


	/**
	 * Get all data asset list
	 * @return
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DataAsset> getAssetList() { return dataAssetService.getAllDataAssets();	}

	/**
	 * Get one data asset  by id
	 * @return
	 * @throws BarkDbOperationException
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public DataAsset getOneAsset(@PathParam("id") Long id) throws BarkDbOperationException {
		return dataAssetService.getDataAssetById(id);
	}

	/**
	 * Remove a data asset by id
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{asset_id}")
	public String removeAssetById(@PathParam("asset_id") Long id) {
		try {
			dataAssetService.removeAssetById(id);
			return "{\"status\":\"0\" , \"result\":\"success\"}";
		} catch (BarkDbOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"status\":\"-1\" , \"result\":\""+ e.getMessage() +"\"}";
		}

	}

	/**
	 * Update a new data asset
	 * @param input
	 * @return
	 */
	@PUT
	@Path("/")
	public String updateDataAsset(DataAssetInput input) {
		try {
			dataAssetService.updateDataAsset(input);
			return "{\"status\":\"0\" , \"result\":\"success\"}";
		} catch (BarkDbOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"status\":\"-1\" , \"result\":\""+ e.getMessage() +"\"}";
		}
	}



}
