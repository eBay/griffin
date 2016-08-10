package com.ebay.oss.bark.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebay.oss.bark.service.LoginService;




@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:context.xml"})
public class LoginServiceTest {
	@Autowired
	private LoginService loginService;

	@Test
	public void testLogin(){

		//login success
		String fullname = loginService.login("_http_dart_service", "P[q}X9t9_N");
		assertEquals(fullname, "_http_dart_service");
		System.out.println("login success");

		//login fail
		String errorname = loginService.login("_http_dart_service", "12345678");
		assertEquals(errorname, null);
		System.out.println("login fail");

		System.out.println();

	}
	
	
	@Test
	public void test(){
		
		String s = "accuracy_viewitem_queue_1468904400000";
		
		String[] ss = s.split("_");
		
		String aaa = ss[ss.length-1];
		
		String b = s.replace("_"+aaa, "");
		
		
		System.out.println(aaa + "s: " + b);
		
	}

}