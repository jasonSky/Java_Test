package com.loadtest.simu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
	String filepath = "";

	public FileHelper(String filepath) {
		this.filepath = filepath;
	}

	public FileHelper() {
	}

	public File createFile(String fileName) {
		File txtFile = new File(fileName);
		try {
			if (!txtFile.exists()) {
				txtFile.createNewFile();
			} else {
				txtFile.delete();
				txtFile.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return txtFile;
		
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
	
	//push file log
	public static void saveToFile(String text, String path, boolean isClose) {
		File file = new File(path);
		BufferedWriter bf = null;
		try {
			FileOutputStream outputStream = new FileOutputStream(file, true);
			OutputStreamWriter outWriter = new OutputStreamWriter(outputStream);
			bf = new BufferedWriter(outWriter);
			bf.append(text);
			bf.newLine();
			bf.flush();

			if (isClose) {
				bf.close();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<String> readDataToList(String filePath){
		String line;
		List<String> array = new ArrayList<String>();
		File file = new File(filePath);
		if (!file.exists()) 
			System.out.println("无法找到文件：" + filePath);
		try{
			InputStream is = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			while ((line = reader.readLine()) != null) {		  //   这一行有毛病,因为只读取第一行		  
				array.add(line);				//这一行也有毛病，这里添加也错了， 但是要怎么写呢？ 请教高手
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return array;
	}
}
