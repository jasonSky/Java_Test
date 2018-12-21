package com.invoke.test;

import org.testng.annotations.Test;

import junit.framework.Assert;

public class TestB extends TestBase{
	
	@Test
	public void test1(){
		System.out.println("test1");
	}
	
	@Test(dependsOnMethods = { "test2" })
	public void test3(){
		System.out.println("test3");
		Assert.assertFalse(false);
	}
	
	@Test(dependsOnMethods = { "test1" })
	public void test2(){
		System.out.println("test2");
		Assert.assertFalse(false);
	}
	
}
