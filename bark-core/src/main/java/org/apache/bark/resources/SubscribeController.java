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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.bark.model.UserSubscribeItem;
import org.apache.bark.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;


@Component
//@Scope("request")
@Path("/subscribe")
public class SubscribeController {

	@Autowired
	private SubscribeService subscribeService;

	@GET
	@Path("/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	public UserSubscribeItem get(@PathParam("user") String user) {

		return subscribeService.getSubscribe(user);
	}

	@POST
	@Path("/")
	@Consumes({"application/json"})
	public void set(@RequestBody UserSubscribeItem input)
	{
		UserSubscribeItem tresult = new UserSubscribeItem("yosha");
		//		List<PlatformSubscribeItem> result = new ArrayList<PlatformSubscribeItem>();
		//
		//		SystemSubscribeItem t11 = new SystemSubscribeItem();
		//		t11.setSystem("bullseye");
		//		t11.setSelectAll(false);
		//		List<String> d11 = new ArrayList<String>();
		//		d11.add("0");
		//		d11.add("10");
		//		t11.setDataassets(d11);
		//
		//		SystemSubscribeItem t12 = new SystemSubscribeItem();
		//		t12.setSystem("IDLS");
		//		t12.setSelectAll(true);
		//		List<String> d12 = new ArrayList<String>();
		//		d12.add("1");
		//		d12.add("11");
		//		t12.setDataassets(d12);
		//
		//		PlatformSubscribeItem p1 = new PlatformSubscribeItem();
		//		p1.setSelectAll(false);
		//		p1.setPlatform("Apollo");
		//		p1.addSystem(t11);
		//		p1.addSystem(t12);
		//
		//		result.add(p1);
		//
		//		tresult.setSubscribes(result);

		subscribeService.subscribe(input);
	}

}