package com.emm;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class Down {

		public static String baseURL = "http://192.168.22.205/tag/";
		public static String path_android_pack = "BuildPackage/Android/";
	
		public String[] getTagAndSave(String folder, String serverIp) throws ClientProtocolException, IOException{
			//获取最新的tag路径
			String tagPath = "";
			String preTagPath = "";
			String result = sendGet(baseURL, null);
			String reg="EMM[0-9]{1,}\\.[0-9]{1,}\\..*?_[0-9]{12}/";
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher=pattern.matcher(result);
			int i =0;
			while(matcher.find()){
				if(i %2==0){
					preTagPath = tagPath;
					tagPath = matcher.group(0);
				}
				i++;
			}
			System.out.println(preTagPath);
	
			//获取acp, asdk路径
			String downPathDir = baseURL + tagPath +path_android_pack;
			String result2 = sendGet(downPathDir, null);
			String jarName = getRegStr("ASDK-(.*?)\\.jar", result2);
			String apkName = getRegStr("AEMM-([0-9\\.]*?)\\.apk", result2);
			
			//删除文件夹
			deleteAllFilesOfDir(new File(folder));
			//下载两个文件
			download(downPathDir + jarName , folder);
			downPathDir = "https://" + serverIp + "/NQClientUpdate/Clients/android/NQMDM/4.0/" +apkName.substring(25,34);
			download2(downPathDir + "/" + apkName , folder, serverIp.length());
			
			return new String[]{jarName, apkName};
		}
		
		public static String getApkName(String dir, String prefix){
			String apkFileName = "";
			File[] ff = new File(dir).listFiles();
			for(int i=0;i<ff.length;i++){
				if(ff[i].getName().endsWith(".apk") && ff[i].getName().startsWith(prefix)){
					apkFileName = ff[i].getName();
				}
			}
			return apkFileName;
		}
		
		public String saveFile(String dir, String serverIp){
			String apkName = getApkName(dir, "AEMM");
			System.out.println(apkName);
//			//删除文件夹
//			deleteApkFileOfDir(new File(dir));
			//
			String downPathDir = "https://" + serverIp + "/NQClientUpdate/Clients/android/NQMDM/4.0/" +apkName.substring(25,34);
			download2(downPathDir + "/" + apkName , dir, serverIp.length());
			
			return apkName;
		}
		
		public String getRegStr(String regstr, String source){
			String reg=regstr;
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher=pattern.matcher(source);
			if(matcher.find()){
				return matcher.group(0);
			}
			return "";
		}
		
		//删除当前目录中的apk文件
		public static void deleteApkFileOfDir(File path) {
			if (!path.exists())
				return;
			
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile() && files[i].getAbsolutePath().endsWith(".apk")) {
					files[i].delete();
				}
			}
		}
		
		public static void deleteAllFilesOfDir(File path) {
			if (!path.exists())
				return;
			if (path.isFile() && path.toString().endsWith(".apk")) {
				path.delete();
				return;
			}
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteAllFilesOfDir(files[i]);
			}
			path.delete();
		}
		
		public static final int cache = 10 * 1024;
		
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
		
		/** 
		* 根据url下载文件，保存到filepath中 
		* @param url 
		* @param filepath 
		* @return 
		*/	
		public static String download(String url, String filepath) {	
			try {	
				System.out.println("url: " + url);
				HttpClient client = HttpClients.createDefault();
				HttpGet httpget = new HttpGet(url);	
				HttpResponse response = client.execute(httpget);	
		
				HttpEntity entity = response.getEntity();	
				InputStream is = entity.getContent();	
				filepath += url.substring(url.lastIndexOf("/")+1);	
				File file = new File(filepath);
				file.getParentFile().mkdirs();
				
				FileOutputStream fileout = new FileOutputStream(file);	
				/** 
				 * 根据实际运行效果 设置缓冲区大小 
				 */	
				byte[] buffer=new byte[cache];	
				int ch = 0;	
				while ((ch = is.read(buffer)) != -1) {	
				 fileout.write(buffer,0,ch);	
				}	
				is.close();	
				fileout.flush();	
				fileout.close();	
		
			 } catch (Exception e) {	
				e.printStackTrace();	
			 }	
			 return null;	
		}	
		
		public static String download2(String url, String filepath, int length) {	
			try {	
				System.out.println("url: " + url);
				String host = url.substring(8, length);
				HttpClient client = getHttpClient(url, host);
				HttpGet httpget = new HttpGet(url);
				HttpResponse response = client.execute(httpget);
		
				HttpEntity entity = response.getEntity();	
				InputStream is = entity.getContent();	
				filepath += url.substring(url.lastIndexOf("/")+1);	
				File file = new File(filepath);
				System.out.println(file.getAbsolutePath());
				file.getParentFile().mkdirs();
				
				FileOutputStream fileout = new FileOutputStream(file);	
				/** 
				 * 根据实际运行效果 设置缓冲区大小 
				 */	
				byte[] buffer=new byte[cache];	
				int ch = 0;	
				while ((ch = is.read(buffer)) != -1) {	
				 fileout.write(buffer,0,ch);	
				}	
				is.close();	
				fileout.flush();	
				fileout.close();	
		
			 } catch (Exception e) {	
				e.printStackTrace();	
			 }	
			 return null;	
		}
		
		public static HttpClient getHttpClient(String url, String proxyHost)
						throws KeyStoreException, NoSuchAlgorithmException,
						CertificateException, FileNotFoundException, IOException,
						KeyManagementException, UnrecoverableKeyException {
			
			// 创建全局的requestConfig  
	        RequestConfig requestConfig = RequestConfig.custom()  
	                .setConnectTimeout(10000)  
	                .setSocketTimeout(10000)
	                .build();  
	        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws CertificateException {
					// TODO Auto-generated method stub
					return true;
				}
	        }).build();
			javax.net.ssl.HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
	        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);

	        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	                .register("http", PlainConnectionSocketFactory.getSocketFactory())
	                .register("https", sslSocketFactory)
	                .build();
	        //      -- allows multi-threaded use
	        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
	        
			HttpClient httpClient = HttpClients.custom()
					 .setDefaultRequestConfig(requestConfig)
					 .setConnectionManager(connMgr)
					 .setSSLSocketFactory(sslSocketFactory)
					 .build();
			return httpClient;
		}
		
		public static String sliceOne(String testStr){
		    String[] strs = testStr.split("[^0-9]");//根据不是数字的字符拆分字符串
		    String numStr = strs[strs.length-1];//取出最后一组数字
		    if(numStr != null && numStr.length()>0){//如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常
		        int n = numStr.length();//取出字符串的长度
		        String sub = numStr.substring(numStr.length()-4);
		        int num = Integer.parseInt(sub)-1;//将该数字加一
		        String added = String.valueOf(num);
		        n = Math.min(n, added.length());
		        //拼接字符串
		        return testStr.subSequence(0, testStr.length()-n -4)+added;
		    }else{
		        throw new NumberFormatException();
		    }
		}
		
		public static void main(String args[]){
			String property = System.getProperty("user.dir");
			new Down().saveFile(property.substring(0,  property.lastIndexOf("\\")+1), args[0]);
		}
	
}
