package com.invoke.test;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class TestBase {
	
	@BeforeTest
	public void setup(){
		System.out.println("setup");
	}
	
	@AfterTest
	public void clearup(){
		System.out.println("clearup");
	}
	
}
