package com.invoke.pack;

import org.testng.annotations.Test;

public class ReflactTest {

	public static void main(String args[]) throws ClassNotFoundException, NoSuchMethodException, SecurityException{
		System.out.println(Class.forName("com.invoke.test.TestA").getMethod("setup", null).getAnnotation(Test.class).dependsOnMethods().length);
	}
	
}
