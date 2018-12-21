package com.invoke.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

/**
 * remoteUrl如何填写是值得注意的
　　如果是无需密码的共享，则类似如下格式：
　　smb://ip/sharefolder（例如：smb://192.168.0.77/test）
　　如果需要用户名、密码，则类似如下格式：
　　Smb://username:password@ip/sharefolder（例如：smb://chb:123456@192.168.0.1/test）
　　// smb:域名;用户名:密码@目的IP/文件夹/文件名.xxx
　　//test.smbGet("smb://szpcg;jiang.t:xxx@192.168.193.13/Jake/test.txt", "c://") ;
　　test.smbPut("smb://szpcg;jiang.t:xxx@192.168.193.13/Jake", "c://test.txt") ;
 * @author Administrator
 */

public class UploadDownloadUtil {
		private String domainip = "192.168.22.240";  
		private String username = "jiangqiang";  
		private String password = "123@qazsew";  
		private String remoteurl = "smb://"+domainip;
	
		public UploadDownloadUtil(){}
		
		public UploadDownloadUtil(String domainip, String username, String password){
			this.domainip = domainip;
			this.username = username;
			this.password = password;
			this.remoteurl = "smb://"+domainip;
		}
		
		public NtlmPasswordAuthentication getAuth(){
			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domainip, username, password);
			return auth;
		}
		
		/**
		* Description: 从共享目录拷贝文件到本地
		* @Version1.0 Sep 25, 2009 3:48:38 PM
		* @param remoteUrl 共享目录上的文件路径
		* @param localDir 本地目录
		*/
		public void smbGet(String remoteUrl,String localDir) {
				InputStream in = null;
				OutputStream out = null;
				try {
					
					SmbFile remoteFile = new SmbFile(remoteUrl, getAuth());
					if(!remoteFile.exists()){
						System.out.println("共享文件不存在");
						return;
					}
			
					String fileName = remoteFile.getName();
					File localFile = new File(localDir+File.separator+fileName);
					in = new BufferedInputStream(new SmbFileInputStream(remoteFile));
					out = new BufferedOutputStream(new FileOutputStream(localFile));
					byte[] buffer = new byte[1024];
					while(in.read(buffer)!=-1){
						out.write(buffer);
						buffer = new byte[1024];
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						out.close();
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

		}

		/**
		* Description: 从本地上传文件到共享目录
		* @Version1.0 Sep 25, 2009 3:49:00 PM
		* @param remoteUrl 共享文件目录
		* @param localFilePath 本地文件绝对路径
		*/
		public void smbPut(String remoteFolder,String localFilePath) {
			InputStream in = null;
			OutputStream out = null;
			try {
				File localFile = new File(localFilePath);
				String fileName = localFile.getName();
				SmbFile remoteFile = new SmbFile(remoteFolder+File.separator+fileName, getAuth());
				in = new BufferedInputStream(new FileInputStream(localFile));
				out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));
				byte[] buffer = new byte[1024];
				while(in.read(buffer)!=-1){
					out.write(buffer);
					buffer = new byte[1024];
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public static void main(String[] args){
			UploadDownloadUtil test = new UploadDownloadUtil() ;
			// smb:域名;用户名:密码@目的IP/文件夹/文件名.xxx
			//\\192.168.22.240\资源下载\ApiTestCase
			test.smbGet(test.remoteurl+"/资源下载/ApiTestCase/AppTestCase.xls", "c:/") ;
//			test.smbPut("smb://szpcg;jiang.t:xxx@192.168.193.13/Jake", "c://test.txt") ;
		}
}