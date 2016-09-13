package com.ebay.oss.bark.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebay.oss.bark.service.NotificationService;
import com.ebay.oss.bark.vo.NotificationRecord;


@RunWith(PowerMockRunner.class)
@PrepareForTest({NotificationServiceImpl.class})
public class NotificationServiceImplTest {

    private NotificationServiceImpl notificationServiceImpl;

    @Before
    public void setUp() {
        notificationServiceImpl = new NotificationServiceImpl();
    }

    @Test
    public void testGetAll() {
        List<NotificationRecord> records = notificationServiceImpl.getAll();
        assertNotNull(records);
        assertEquals(0, records.size());
    }

    @Test
    public void testInsert() {
        NotificationRecord record = new NotificationRecord();

        List<NotificationRecord> records = new ArrayList<>();

        Whitebox.setInternalState(NotificationServiceImpl.class, "count", 1);
        Whitebox.setInternalState(NotificationServiceImpl.class, "records", records);

        notificationServiceImpl.insert(record);

        assertEquals(1, record.getId());
        assertEquals(1, records.size());
        assertEquals(record, records.get(0));
    }

    @Test
    public void testDelete() {
        NotificationRecord record = new NotificationRecord();
        record.setId(3);
        List<NotificationRecord> records = new ArrayList<>();
        records.add(record);

        Whitebox.setInternalState(NotificationServiceImpl.class, "records", records);

        notificationServiceImpl.delete(record);
        assertEquals(new ArrayList<NotificationRecord>(), records);

        NotificationServiceImpl spyNotificationServiceImpl = spy(notificationServiceImpl);
        doNothing().when(spyNotificationServiceImpl).delete(anyInt());
        spyNotificationServiceImpl.delete(record);
        verify(spyNotificationServiceImpl).delete(anyInt());
    }

    @Test
    public void testDeleteWithIntParam() {
        NotificationRecord record = new NotificationRecord();
        record.setId(3);
        List<NotificationRecord> records = new ArrayList<>();
        records.add(record);

        Whitebox.setInternalState(NotificationServiceImpl.class, "records", records);

        notificationServiceImpl.delete(3);
        assertEquals(new ArrayList<NotificationRecord>(), records);
    }

    @Test
    public void testGet() {
        NotificationRecord record = new NotificationRecord();
        record.setId(3);
        List<NotificationRecord> records = new ArrayList<>();
        records.add(record);

        Whitebox.setInternalState(NotificationServiceImpl.class, "records", records);

        NotificationRecord result1 = notificationServiceImpl.get(3);
        NotificationRecord result2 = notificationServiceImpl.get(2);
        assertSame(result1, record);
        assertNull(result2);
    }

    @Test
    public void testGetTop() {
        NotificationRecord record1 = new NotificationRecord();
        NotificationRecord record2 = new NotificationRecord();
        NotificationRecord record3 = new NotificationRecord();
        List<NotificationRecord> records = new ArrayList<>();
        records.add(record1);
        records.add(record2);
        records.add(record3);

        Whitebox.setInternalState(NotificationServiceImpl.class, "records", records);

        List<NotificationRecord> expect = new ArrayList<>();
        expect.add(record1);
        expect.add(record2);

        List<NotificationRecord> result = notificationServiceImpl.getTop(2);

        assertEquals(expect, result);
    }

//    private void testInsert(int n) {
//        System.out.println("===== Insert " + n + " Notification Records =====");
//        for (int i = 1; i <= n; i++) {
//            NotificationRecord rcd = new NotificationRecord(new Date().getTime(), "lliu13", "Operation" + i, "Target" + i, "NotifyRecord" + i);
//            notificationService.insert(rcd);
//        }
//        System.out.println();
//    }
//
//    private NotificationRecord testGet(int id, boolean expectNull) {
//        NotificationRecord fnr = notificationService.get(id);
//        if (expectNull) {
//            assertNull(fnr);
//        } else {
//            assertNotNull(fnr);
//            System.out.println("get notification record: " + fnr.getName() + ": " + fnr.getTarget() + " " + fnr.getOperation());
//        }
//        return fnr;
//    }
//
//    private void testGetTop(int n) {
//        System.out.println("===== Get Top " + n + " Notification Record =====");
//        List<NotificationRecord> fnrList = notificationService.getTop(n);
//        assertEquals(fnrList.size(), n);
//        for (NotificationRecord nr : fnrList) {
//            System.out.println(nr.getName() + ": " + nr.getTarget() + " " + nr.getOperation());
//        }
//        System.out.println();
//    }
//
//    private void testDelete(NotificationRecord nfr) {
//        notificationService.delete(nfr);
//    }
//    private void testDelete(int id) {
//        notificationService.delete(id);
//    }
//
//    @Test
//    public void TestNotificationService() {
//
//        //getAll
//        System.out.println("===== All Notification Records 0 =====");
//        List<NotificationRecord> nrList = testGetAll();
//        int sz = nrList.size();
//        System.out.println("current notification records list size: " + sz);
//        System.out.println();
//
//        //insert
//        int nInsert = 7;
//        testInsert(nInsert);
//
//        //getAll
//        System.out.println("===== All Notification Records 1 =====");
//        int sz1 = nrList.size();
//        assertEquals(sz1 - sz, nInsert);
//        System.out.println("current notification records list size: " + sz1);
//        System.out.println();
//
//        //get
//        System.out.println("===== Get Notification Record =====");
//        NotificationRecord fnr = testGet(3, false);
//        testGet(9009, true);
//
//        //getTop
//        testGetTop(5);
//
//        //delete
//        System.out.println("===== delete 2 Notification Records =====");
//        testDelete(fnr);
//        testDelete(6);
//        NotificationRecord nrcd = new NotificationRecord(new Date().getTime(), "lliu13", "Operation", "Target", "NotifyRecord");
//        testDelete(nrcd);
//        System.out.println();
//
//        //getAll
//        System.out.println("===== All Notification Records 2 =====");
//        int sz2 = nrList.size();
//        assertEquals(sz1 - sz2, 2);
//        System.out.println("current notification records list size: " + sz2);
//        System.out.println();
//    }
}
