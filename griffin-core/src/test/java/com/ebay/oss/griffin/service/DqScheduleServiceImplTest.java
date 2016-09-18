package com.ebay.oss.griffin.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebay.oss.griffin.service.DqScheduleService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DqScheduleServiceImplTest {

    private DqScheduleServiceImpl dqScheduleServiceImpl;

    @Before
    public void setUp() {
        dqScheduleServiceImpl = new DqScheduleServiceImpl();
    }

    @Test
    public void testSchedulingJobs(){
        DqScheduleServiceImpl spyDqScheduleServiceImpl = spy(dqScheduleServiceImpl);

        doNothing().when(spyDqScheduleServiceImpl).createJobToRunBySchedule();
        doNothing().when(spyDqScheduleServiceImpl).generateAllWaitingJobsRunningConfigs();
        doNothing().when(spyDqScheduleServiceImpl).checkAllJOBSStatus();
        doNothing().when(spyDqScheduleServiceImpl).updateModelStatus(anyInt(), anyInt());

        spyDqScheduleServiceImpl.schedulingJobs();

        verify(spyDqScheduleServiceImpl).createJobToRunBySchedule();
        verify(spyDqScheduleServiceImpl).generateAllWaitingJobsRunningConfigs();
        verify(spyDqScheduleServiceImpl).checkAllJOBSStatus();
        verify(spyDqScheduleServiceImpl).updateModelStatus(anyInt(), anyInt());

    }
}
