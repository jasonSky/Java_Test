package com.emm;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InstallTime {

	public static void main(String[] args) {
		//apk名字
		String str="AEMM-5.2.0.520000396.apk";//gpstest-1.2.2.apk
		//abd 安装指令
		String command1 ="adb install -r " + str;
		//安装时会出现log,根据关键字确定何时安装，何时结束
		Runtime runtime = Runtime.getRuntime();
		double totalTime = 0;
		double pushTime = 0;
		try {
			//执行命令，有一段传输数据的时间，不能计算在内
			Process getSIBaseVersionProcess2  =  runtime.exec(command1);
			long startTime=System.currentTimeMillis();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
						getSIBaseVersionProcess2.getInputStream()));
			String SIBaseVersion = " ";
			 while((SIBaseVersion =bufferedReader.readLine())!=null){
				System.out.println(SIBaseVersion);
				if (SIBaseVersion.contains("bytes in")) {
					//取push时间
					String sub = SIBaseVersion.substring(SIBaseVersion.indexOf("bytes in")+9, SIBaseVersion.length()-2);
					pushTime = Double.parseDouble(sub);
					//System.out.println("PushTime: " + pushTime);
				}
				//安装成功
				if (SIBaseVersion.equals("Success")) {
					//时间
					long endTime=System.currentTimeMillis();
					long time = endTime-startTime;
					totalTime =1.0* time/1000;
					//System.out.println("TotalTime: " + totalTime);
				}
			 }
		} catch (Exception e) {
			System.out.println("[Error][Install]" + e.getMessage());
		}
		System.err.println("Time="+(totalTime-pushTime)+" S");
	}
	
}