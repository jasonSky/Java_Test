package com.qiubai;  

public class content implements Cloneable { 

	 @Override
	 public Object clone() throws CloneNotSupportedException {
		 return super.clone();
	 }
	 
	 public 	long 	id;	//
	 public 	String 	title;	//
	 public 	String 	content;	//

 }
