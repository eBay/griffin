package org.apache.bark.domain;

import static org.junit.Assert.*;

import org.junit.Test;


public class SystemTypeTest {

    private static final String[] array = {"Bullseye", "GPS", "Hadoop", "PDS", "IDLS", "Pulsar", "Kafka", "Sojourner", "SiteSpeed", "EDW"};
    
    @Test
    public void test_indexOf() {
        for(int i = 0; i < array.length; i++) {
            assertEquals(i, SystemType.indexOf(array[i]));
        }
    }
}
