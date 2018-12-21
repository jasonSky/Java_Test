package com.loadtest.simu;

import com.emm.util.DES3Util;
import com.emm.util.ResultHelper;

import net.sf.json.JSONObject;

public class SimThread extends Thread {
	
	final String fileDir =  TestMain.PDir + "Sim";
	final String fileExt = ".txt";
	final String expectCode = "200";
	final boolean response = false;
	final String passwordDEC = "RJKctynh0Ps=";// "O1UIMFpnO4M="  RJKctynh0Ps=;
	
	//中间变量
	String proxyAccessKey = null;
	String hostId = null;
	String emmAccessKey = null;
	String appPassToken = null;
	
	//逻辑处理
	public void run(){
		String suffix = currentThread().getName().substring(currentThread().getName().indexOf("-")+1);
		operate(suffix);
	}
	
	public void operate(String fileSuffix){
		//ip
		String proxyIP = "172.16.20.23";
		String emmIP = "172.16.20.22";
		//param
		String version02 = "0.2";
		String tenantId = "mdm";
		String nonce = "asdfasdfsadf"; // asdfasdfsadf
		//register
		String pincode = "123456";
		String pinhash = "e10adc3949ba59abbe56e057f20f883e";
		String appId = "com.nq.mdm";
		
		//整个线程执行总数
		long start = 0;
		long end = TestMain.count/TestMain.thread;
		if(TestMain.tag){
			start = System.currentTimeMillis();
			end = start + TestMain.seconds * 1000;
		}
		
		while(start < end){
			String record = "";
			String deNonce = "";
			String principal = "user_" + (int)(Math.random() * 30000+1);
			System.out.println(principal);
			String timestamp = System.currentTimeMillis() + "";
			
			try{
				// step 1
				JSONObject params = new JSONObject();
				params.put("version", version02);
				params.put("principal", principal);
				params.put("tenantId", tenantId);
				params.put("nonce", nonce);
				params.put("authorization", ResultHelper.encrypt(
						timestamp + ":" + nonce, pinhash, nonce));
				
				long begin1 = System.currentTimeMillis();
				String result = ResultHelper.activate(proxyIP,params.toString());
				long end1 = System.currentTimeMillis();
				String returnCode = ResultHelper.getJsonValueByKey(result, "returnCode");
				if(returnCode.trim().equals(expectCode)){
					record += (end1-begin1) + "ms\t";
					deNonce = ResultHelper.getJsonValueByKey(result, "nonce");
					if (response)
						System.out.println("deNonce:" + deNonce);
				}else{
					throw new Exception("A");
				}
				//
				hostId = ResultHelper.getJsonValueByKey(result, "hostId");
				byte[] dekey = ResultHelper.encryptPBKDF2(pinhash, deNonce);
				String resultproxyAccessKey = ResultHelper.getJsonValueByKey(result, "proxyAccessKey");
				proxyAccessKey = ResultHelper.decrypt(resultproxyAccessKey, dekey,
						ResultHelper.nonceDigest(deNonce).substring(8, 24).getBytes());
				
				//step 2
				params = new JSONObject();
				params.put("version", version02);
				params.put("hostId", hostId);
				params.put("nonce", nonce);
				String proxyAccessToken = ResultHelper.encrypt(timestamp + ":" + nonce, proxyAccessKey, nonce);
				params.put("proxyAccessToken", proxyAccessToken);
				params.put("provisionRequest", DES3Util.encryptMode(nonce, pincode));// 加密的request请求，使用pin做密钥加密过的
				long begin2 = System.currentTimeMillis();
				result = ResultHelper.provision(proxyIP, params.toString());
				long end2 = System.currentTimeMillis();
				returnCode = ResultHelper.getJsonValueByKey(result, "returnCode");
				if(returnCode.trim().equals(expectCode)){
					record += (end2-begin2) + "ms\t";
					emmAccessKey = ResultHelper.getJsonValueByKey(result, "emmAccessKey");
					if (response)
						System.out.println("emmAccessKey:" + emmAccessKey);
				}else{
					throw new Exception("B");
				}
				
				//step 3
				JSONObject requestString = new JSONObject();
				requestString.put("appId", appId);
				requestString.put("nonce", nonce);
				String emmAccessKeyDecrypt = DES3Util.decryptMode(emmAccessKey, pincode);
				String clientRequest = DES3Util.encryptMode(requestString.toString(), emmAccessKeyDecrypt);
		
				params = new JSONObject();
				params.put("version", version02);
				params.put("hostId", hostId);
				params.put("nonce", nonce);
				params.put("proxyAccessToken", proxyAccessToken);
				params.put("clientRequest", clientRequest);
				long begin3 = System.currentTimeMillis();
				result = ResultHelper.appPassToken(proxyIP, params.toString());
				long end3 = System.currentTimeMillis();
				returnCode = ResultHelper.getJsonValueByKey(result, "returnCode");
				if(returnCode.trim().equals(expectCode)){
					record += (end3-begin3) + "ms\t";
					appPassToken = ResultHelper.getJsonValueByKey(result, "appPassToken");
					if (response)
						System.out.println("appPassToken:" + appPassToken);
				}else{
					throw new Exception("C");
				}
				
				//step 4
				String appPassTokenDecrypt = DES3Util.decryptMode(appPassToken, emmAccessKeyDecrypt);
				String username = version02 + ":" + nonce + ":" + hostId; 
				resultproxyAccessKey = ResultHelper.encrypt(System.currentTimeMillis() + ":" + nonce,
						proxyAccessKey, nonce);
				String passwd = resultproxyAccessKey + ":" + appId + ":" + appPassTokenDecrypt;
				long begin4 = System.currentTimeMillis();
				String udid = ResultHelper.t3001(principal, passwordDEC, proxyIP, emmIP,username, passwd, hostId);
				long end4 = System.currentTimeMillis();
				record += (end4-begin4) + "ms\t";
				if (response)
					System.out.println("udid:" + udid);
			}catch(Exception ex){
				ex.printStackTrace();
			}finally{
				if(record.trim().equals("")){
					record = "\r\n";
				}
				FileHelper.saveToFile(record, fileDir + fileSuffix + fileExt, true);
			}
			
			if(TestMain.tag){
				start = System.currentTimeMillis();
			}else{
				start++;
			}
		}//end
		
	}
	
}
