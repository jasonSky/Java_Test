package com.jason.datetrans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.CharMatcher;

public class Main {

	MySqlDataFactory dataFactory = new MySqlDataFactory();
	public String logFile = "log.txt";
	public int count=1;
	
	public void readFile(String filePath) {
		Map<String, String> urls = new HashMap<String, String>();
		try {
			@SuppressWarnings("resource")
			BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF-8"));
			String strLine = null;
			while(null != (strLine = bufferedReader.readLine())){
				System.out.println(strLine);
				String key = CharMatcher.WHITESPACE.trimFrom(strLine.substring(0, strLine.indexOf(":")).trim());
				if(key.equals(""))
					key = "null"+count;
				urls.put(key, strLine.substring(strLine.indexOf(":")+1).trim());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		insertData(urls);
	}
	
	public void insertData(Map<String, String> data) {
		for(String key : data.keySet()) {
			if(!dataFactory.selectM3u8ByName(key)) {
				writeDataToFile(logFile, key+":"+data.get(key));
				dataFactory.insertM3u8(key, data.get(key));
			}
		}
	}
	
	public void operatorFile(String dir) {
		File fi = new File(dir);
		File[] listFiles = fi.listFiles();
		for(int i=0;i<listFiles.length;i++) {
			if(listFiles[i].isDirectory()) {
				operatorFile(listFiles[i].getAbsolutePath());
			}else {
				String fileName = listFiles[i].getAbsolutePath();
				System.out.println(fileName);
				if(fileName.endsWith(".txt")) {
					readFile(fileName);
				}
			}
		}
	}
	
	public void writeDataToFile(String fileName, String data) {
		BufferedWriter fw = null;
		try {
			fw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(fileName), true), "UTF-8"));
			fw.write("");
			fw.append(data);
			fw.newLine();
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String args[]) {
		//truncate table m3u8
		new Main().operatorFile(args[0]);
		DBUtil.closeConnection(DBUtil.getConnection());
	}
	
}
