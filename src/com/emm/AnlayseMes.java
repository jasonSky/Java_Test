package com.emm;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.loadtest.simu.FileHelper;

public class AnlayseMes extends Thread{

	int index;
	public static long start = 100000000;
	public static long end = 101000000;
	public static int step =100000 ;
	public static int count = (int)(end-start)/step;
	public static int countDown = (int)(end-start)/step;
	final static CountDownLatch cdl = new CountDownLatch(countDown);
	public static FileHelper fh = new FileHelper();
	public static String resultFile = System.getProperty("user.dir") + "//src//AnalyzeData//result";
	//public static String retryCodeFile = System.getProperty("user.dir") + "//src//AnalyzeData//retryCode";
	public static String errorCodeFile = System.getProperty("user.dir") + "//src//AnalyzeData//errorCode";
	public static String connectLostCodeFile = System.getProperty("user.dir") + "//src//AnalyzeData//connectLostCode";
	public static long k = 0;
	public static String[] title = new String[]{"CODE","COMPANY","LOCATE"};
	
	public AnlayseMes(int value){
		index = value;
	}
	
	public static void delay(int i){
		try {
			Thread.sleep(i*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String sendGet(String url, String param) throws ClientProtocolException, IOException {
		String result = "";
		HttpClient httpClient = HttpClients.createDefault();
		String urlNameString = url;
		if(param!=null){
			urlNameString += "?" + param;
		}
		//System.out.println("urlNameString:" + urlNameString);
		HttpGet httpget = new HttpGet(urlNameString); 
		httpget.setHeader("Charset", "UTF-8");
		
		HttpResponse response;
		response = httpClient.execute(httpget);
		
		String responserHeader = response.getHeaders("Content-Type")[0].getValue();
		if (responserHeader.contains("image"))
			result = null;
		else if (responserHeader.contains("application/octet-stream"))
			result = null;
		else{
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity); 
		}
		
		return result;
	}

	public static String regStr(String str, String pattern){
		//Pattern.DOTALL|Pattern.MULTILINE
		Pattern r = Pattern.compile(pattern, Pattern.DOTALL);
		Matcher m = r.matcher(str);
		if (m.find()) {
			return m.group(2).trim();
		}
		return "";
	}
	
	//https://www.dandb.com/search/?search_type=duns&is_duns_lookup=false&duns=554434248&country=CP&submit=Search+Now
	//554434248
	public static String[] getResult(long code, String subtrix){
		String result = "";
		try{
			result = sendGet("https://www.dandb.com/search/?search_type=duns&is_duns_lookup=false&duns="+code+"&country=CP&submit=Search+Now", null);
		}catch(Exception ex){
			fh.writeDataToFile(connectLostCodeFile+subtrix, String.valueOf(code));
			System.out.println("SERVER CLOSE CONNECTION!");
			return null;
		}
		
		String company = regStr(result, "<span class=\"company\"><h2>((.*?)<)");
		String locate = regStr(result, "<span class=\"search_results_address\">((.*?)</span>)").replaceAll("<br />", "");
		String error = regStr(result, "<div id=\"search_error\">((.*?)<)");
		String error2 = regStr(result, "<div id=\"errorDesc\">((.*?)<)");
		
		if(error.equals("") && error2.equals("")){
			System.out.println("=================");
			System.out.println(code);
			System.out.println(company);
			System.out.println(locate);
			System.out.println("=================");
			k++;
			return new String[]{String.valueOf(code), company, locate};
		}else if(!error.equals("")){
			fh.writeDataToFile(errorCodeFile+subtrix, String.valueOf(code));
			System.out.println(error);
			return null;
		}else if(!error2.equals("")){
			//server 拒绝 重试一次
			System.out.println(code + " retry ...");
			delay(1);
			getResult(code, subtrix);
		}
		
		return null;
	}
	
	//100000000 + 100000*index  //max index 899999999/100000=8999
	public void run(){
		for(int i=0;i<step;i++){
			long code = start + index*step +i;
			String[] vals = getResult(code, index+".txt");
			//写入excel
			try {
				if(vals!=null)
					ExcelWrite.writeToExcel(resultFile+index+".xls","sheet1", vals);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		cdl.countDown();
	}
	
	public static void main(String args[]) throws Exception{
		for(int i=0;i<count;i++){
			ExcelWrite.createExcel(resultFile+i+".xls","sheet1",title);
			new AnlayseMes(i).start();
			delay(9);
		}
		
		cdl.await();
		System.out.println("Analyze Corret Num:" + k +", percent: " + k/(count*step));
	}
	
}
