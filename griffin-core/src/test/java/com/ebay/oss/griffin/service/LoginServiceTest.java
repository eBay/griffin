package com.ebay.oss.griffin.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebay.oss.griffin.service.LoginService;


public class LoginServiceTest {

	private DummyLoginService dummyLoginService;

	@Before
	public void setUp() {
		dummyLoginService = new DummyLoginService();
	}

	@Test
	public void testLogin(){

		//login success
		String fullname = dummyLoginService.login("alex", "alex");
		assertEquals(fullname, "alex");

	}




}
