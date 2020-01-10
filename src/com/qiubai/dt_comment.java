package com.qiubai;  

public class dt_comment implements Cloneable { 

	 @Override
	 public Object clone() throws CloneNotSupportedException {
		 return super.clone();
	 }
	 
	 public 	long 	id;	//
	 public 	String 	name;	//
	 public 	String 	comment;	//
	 public 	String 	img = null;	//
	 public 	int 	upNum;	//
	 public 	int 	commentNum;	//

 }
