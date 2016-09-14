package com.ebay.oss.bark.service;

import static org.junit.Assert.*;

import org.junit.Test;


public class DqModelConverterTest {

    private DqModelConverter converter;
    
    @Test
    public void test_voOf_null() {
        converter = new DqModelConverter();
        assertNull(converter.voOf(null));
    }
}
