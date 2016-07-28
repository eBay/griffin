package org.apache.bark.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.bark.common.BarkDbOperationException;
import org.apache.bark.model.*;
import org.apache.bark.service.DataAssetService;
import org.apache.bark.service.SubscribeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:context.xml"})
public class SubscribeServiceTest {

    @Autowired
    private SubscribeService subscribeService;

    @Test
    public void testSubscribeService() {

        String testUser = "lliu13", testId = "testSubscribe";

        UserSubscribeItem usi = new UserSubscribeItem();
        usi.setNtaccount(testUser);
        usi.set_id(testId);
        usi.setSubscribes(new ArrayList<PlatformSubscribeItem>());
        int sub = subscribeService.subscribe(usi);
        assertEquals(sub, 0);
        System.out.println("subscribe success");

        UserSubscribeItem fusi = subscribeService.getSubscribe(testUser);
        assertNotNull(fusi);
        System.out.println("find subscribe service success");
    }

}
