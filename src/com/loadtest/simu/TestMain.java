package com.loadtest.simu;

import java.io.File;

public class TestMain {

	public static String PDir = "";
	public static int stepTime = 200;
	public static int count = 1000;
	public static int thread = 50;
	public static int seconds = 300;
	public static boolean tag = false;
	
	/**
	 * 线程数
	 * 总次数
	 * 运行时间(S)
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String args[]) throws InterruptedException{
		String os = System.getProperty("os.name");  
		if(os.toLowerCase().startsWith("win")){  
			PDir = "C:\\";
		}else{
			PDir = "/root/";
		}
		
		thread = Integer.parseInt(args[0]);	
		count = Integer.parseInt(args[1]);
		if(args.length>2){
			seconds = Integer.parseInt(args[2]);
			tag = true;
		}
		//清理文件
		File fi = new File(PDir);
		File[] listFiles = fi.listFiles();
		for(int i=0;i<listFiles.length;i++){
			if(listFiles[i].getName().contains("Sim") && listFiles[i].getName().endsWith(".txt")){
				listFiles[i].delete();
			}
		}
		//启动线程
		for(int i=0;i<thread;i++){
			new SimThread().start();
			Thread.sleep(stepTime);
		}
	}
	
}
