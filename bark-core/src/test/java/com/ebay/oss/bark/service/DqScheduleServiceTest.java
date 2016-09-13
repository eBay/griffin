package com.ebay.oss.bark.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebay.oss.bark.service.DqScheduleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:context.xml"})
public class DqScheduleServiceTest {
    @Autowired
    private DqScheduleService dqScheduleService;

    @Ignore
    @Test
    public void testSchedulingJobs(){

        System.out.println("===== Scheduling jobs begin =====");
        dqScheduleService.schedulingJobs();
        System.out.println("===== Scheduling jobs done =====");
        System.out.println();

    }
}
