package com.invoke.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.exec.ExecuteException;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class TestA extends TestBase{
	
	@Test
	public void test1(){
		System.out.println("test1");
	}
	
	@Test
	public void test2(){
		System.out.println("test2");
		Assert.assertFalse(true);
	}
	
	@Test(dependsOnMethods = { "test2" })
	public void test3(){
		System.out.println("test3");
		Assert.assertFalse(false);
	}
	
	public static void exec(int k) throws ExecuteException, IOException{
		Process p = null;
		int exitvalue = 0;
		try {
			p = Runtime.getRuntime().exec("cmd"); 
			PrintWriter out = new PrintWriter(p.getOutputStream());
			String[] cmd = new String[]{"C:", "cd C:/Users","ping www.baidu.com"} ; //你的cmd命令
			new StreamGobbler(p.getErrorStream(), "ERROR"+k).start();
			new StreamGobbler(p.getInputStream(), "INPUT"+k).start();
			for(int i=0;i<cmd.length;i++){
				out.println(cmd[i]);  //输入你的命令
				out.flush(); //写到控制台
			}
			
			exitvalue = p.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			System.out.println(exitvalue);
			if (p != null && p.exitValue() != 0) {
				p.destroy();
			}
		}
		
	}
	
	public static void main(String args[]) throws ExecuteException, IOException{
//		exec(1);
//		exec(2);
//		System.out.println(System.currentTimeMillis());
		String dateStr = "2016-10-20 09:36:48";
		Calendar c = Calendar.getInstance();  
		try {
			c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		System.out.println(c.getTimeInMillis());
	}
	
}
