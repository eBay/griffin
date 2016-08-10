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

import org.apache.bark.domain.UserSubscription;
import org.apache.bark.repo.UserSubscriptionRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscribeServiceImpl implements SubscribeService{
	
	final static Logger logger = LoggerFactory.getLogger(SubscribeServiceImpl.class);
	
	@Autowired
    UserSubscriptionRepo subscriptionRepo;

	@Override
	public void subscribe(UserSubscription item) {
		subscriptionRepo.upsertUserSubscribe(item);
	}

	@Override
	public UserSubscription getSubscribe(String user) {
		return subscriptionRepo.getUserSubscribeItem(user);
	}
	
}
