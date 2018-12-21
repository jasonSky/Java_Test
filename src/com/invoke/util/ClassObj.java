package com.invoke.util;

import java.util.ArrayList;
import java.util.List;

public class ClassObj {

	String className;
	List<MethodObj> methodObj = new ArrayList<MethodObj>();
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<MethodObj> getMethodObj() {
		return methodObj;
	}
	public void setMethodObj(List<MethodObj> methodObj) {
		this.methodObj = methodObj;
	}
	
}
