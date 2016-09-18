package com.ebay.oss.griffin.service;

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

import com.ebay.oss.griffin.service.NotificationService;
import com.ebay.oss.griffin.vo.NotificationRecord;


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
        List<NotificationRecord> records = new ArrayList<>();

        Whitebox.setInternalState(NotificationServiceImpl.class, "records", records);

        List<NotificationRecord> result = notificationServiceImpl.getAll();
        assertNotNull(result);
        assertEquals(records, result);
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
}
