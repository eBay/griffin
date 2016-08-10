package com.ebay.oss.bark.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ebay.oss.bark.domain.SystemType;


public class SystemTypeTest {

    private static final String[] array = {"Bullseye", "GPS", "Hadoop", "PDS", "IDLS", "Pulsar", "Kafka", "Sojourner", "SiteSpeed", "EDW"};
    
    @Test
    public void test_indexOf() {
        for(int i = 0; i < array.length; i++) {
            assertEquals(i, SystemType.indexOf(array[i]));
        }
    }
}
