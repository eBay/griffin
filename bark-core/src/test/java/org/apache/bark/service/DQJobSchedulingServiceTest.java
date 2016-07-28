package org.apache.bark.service;

import com.mongodb.DBObject;
import org.apache.bark.dao.BarkMongoDAO;
import org.apache.bark.service.DQJobSchedulingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:context.xml"})
public class DQJobSchedulingServiceTest {
    @Autowired
    private DQJobSchedulingService dqJobSchedulingService;

    @Test
    public void testSchedulingJobs(){

        dqJobSchedulingService.schedulingJobs();
        System.out.println("scheduling jobs success");

    }
}
