package com.invoke.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SetGenr {
	
	public final static String dir = System.getProperty("user.dir") + File.separator + "src";
	
	public static List<String> getFile(String fileName){
		File file = new File(dir + File.separator + fileName);
		List<String> array = new ArrayList<String>();
		String line = "";
		try{
			InputStream is = new FileInputStream(file);
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			while ((line = reader.readLine()) != null) {
				array.add(line.replaceAll("[0-9]*\\s+", ""));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return array;
	}
	
	public static Object[] operateStrToTree(List<String> source){
		Map<String, List<String>> child1 = new HashMap<String, List<String>>();
		Map<String, List<String>> child2 = new HashMap<String, List<String>>();
		for(Iterator<String> it = source.iterator();it.hasNext();){
			String value = it.next();
			String[] keys = value.split("\\.");
			if(child1.containsKey(keys[0])){
				List<String> childval = child1.get(keys[0]);
				if(!childval.contains(keys[1])){
					childval.add(keys[1]);
				}
			}else{
				List<String> childval = new ArrayList<String>();
				childval.add(keys[1]);
				child1.put(keys[0], childval);
			}
			
			if(child2.containsKey(keys[1])){
				List<String> childval2 = child2.get(keys[1]);
				if(keys.length>2){
					if(!childval2.contains(keys[2])){
						childval2.add(keys[2]);
					}
				}else{
					child2.put(keys[1], childval2);
				}
			}else{
				List<String> childval2 = new ArrayList<String>();
				if(keys.length>2){
					childval2.add(keys[2]);
				}
				child2.put(keys[1], childval2);
			}
			
		}
		
		return new Object[]{child1, child2};
	}
	
	public static boolean isKeyExistArray(String[] childval, String key){
		boolean flag = false;
		for(int i=0;i<childval.length;i++){
			if(childval[i].equals(key)){
				flag = true;
			}
		}
		return flag;
	}
	
	public static void main(String args[]){
		operateStrToTree(getFile("source.txt"));
	}
	
}
