package com.invoke.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegMatch {

	public static String readFile(String path){
		String strout = "";
		File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
            	strout += tempString + "\r\n";
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		return strout;
	}
	
	public static void main(String rags[]){
		String str = readFile("C:\\Users\\Administrator.USER-20140223MZ\\Desktop\\ttt.txt");
		
//		String on="sh \"${mvnHome}/bin/mvn clean package -Dmaven.test.skip=true\"";
		String off="aaaa";
//		boolean flag =true;
		String ciTag = "//--------ci editor--------";
		Pattern pattern =Pattern.compile(ciTag+"(.*)"+ciTag, Pattern.DOTALL);
		Matcher matcher=pattern.matcher(str);
		boolean flag = matcher.find();
		if(flag){
			str = str.replace(matcher.group(1), "\r\n"+off+"\r\n");
		}
		
		System.out.println(str);
//		if(flag){
//			str.replaceAll("^sh \"${mvn clean package.*(?=true\")", on);
//		}
//		System.out.println(str);

//		str = str.replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "").replaceAll("\r\n", "");
//	  	System.out.println(str);
//		String reg="data=\\{([\\s\\S]*)\\},";
//		Pattern pattern = Pattern.compile(reg);
//        Matcher matcher=pattern.matcher(str);
//        if(matcher.find()){
//        	System.out.println(matcher.group(1));
//        }
	}
	
}
