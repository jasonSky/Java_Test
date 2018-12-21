package com.invoke.util;

import java.util.ArrayList;
import java.util.List;

public class TestObj {

	String testName;
	List<ClassObj> classObj = new ArrayList<ClassObj>();
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public List<ClassObj> getClassObj() {
		return classObj;
	}
	public void setClassObj(List<ClassObj> classObj) {
		this.classObj = classObj;
	}
	
}
