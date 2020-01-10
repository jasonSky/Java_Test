package com.qiubai;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class QiuBai {
	private static final String LOGIN_URL = "http://www.qiushibaike.com/hot/";
	private static final String LOGIN_URL2 = "http://www.lovehhy.net/Joke/Detail/QSBK/";
	static DBOperator dbOperator = new DBOperator();
	
	public static boolean containsStr(String[] strs, String str){
		boolean flag = false;
		for(int i=0;i<strs.length;i++){
			if(strs[i].equals(str)){
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	public static void delay(int i){
		try {
			System.out.println("delay 30s...");
			Thread.sleep(i * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("resource")
	public static byte[] getFileData(String filePath) throws Exception{
			File file = new File(filePath);  
			long fileSize = file.length();  
			FileInputStream fi = new FileInputStream(file);  
			byte[] buffer = new byte[(int) fileSize];  
			int offset = 0;  
			int numRead = 0;  
			while (offset < buffer.length && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {  
				offset += numRead;  
			}  
			// 确保所有数据均被读取  
			if (offset != buffer.length) {
				throw new IOException("Could not completely read file "	+ file.getName());  
			}  
			fi.close();
			return buffer;
	}
	
	public static void testHtmlUnitAdd() throws Exception{
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");  
		final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
		System.out.println("启动webclient...");
		webClient.getOptions().setUseInsecureSSL(true);//ssl使用内部默认
		webClient.getOptions().setThrowExceptionOnScriptError(false); //此行必须要加
		webClient.getOptions().setTimeout(300000);
		HtmlPage page = (HtmlPage) webClient.getPage(LOGIN_URL);
		// 根据form的名字获取页面表单，也可以通过索引来获取：page.getForms().get(0)
		for(int i=0;i<10;i++){
			DomElement cotent = page.getElementById("content-left");
			DomElement de = null;
			Iterable<DomElement> itraterble= cotent.getChildElements();
			for(Iterator<DomElement> it= itraterble.iterator();it.hasNext();){
				de = it.next();
				if(de.getNodeName().equals("ul"))
					break;
				String name = de.getFirstElementChild().getTextContent().replaceAll("\\n", "");
				String image_icon_url = de.getFirstElementChild().getFirstElementChild().getFirstElementChild().getAttribute("src");
				String text = de.getFirstElementChild().getNextElementSibling().getTextContent().replaceAll("\n", "");
				DomElement thumb = de.getFirstElementChild().getNextElementSibling().getNextElementSibling();
				String text_image_src = "";
				String voteText = "";
				if(thumb.getAttribute("class").equals("thumb")){
					text_image_src = thumb.getFirstElementChild().getFirstElementChild().getAttribute("src");
					voteText = de.getFirstElementChild().getNextElementSibling().getNextElementSibling().getNextElementSibling().getTextContent();
				}else{
					voteText = de.getFirstElementChild().getNextElementSibling().getNextElementSibling().getTextContent();
				}
				
				String taolunTxt = "";
				String namesub = "";
				String commentsub = "";
				String commentNumsub = "";
				DomElement taolun = de.getLastElementChild();
				if(taolun.getTagName().equals("a")){
					taolunTxt = taolun.getFirstElementChild().getTextContent();
					namesub = taolun.getFirstElementChild().getFirstElementChild().getNextElementSibling().getTextContent().replace("\r\n", "").replaceAll("\\s", "");
					commentsub = taolun.getFirstElementChild().getLastElementChild().getTextContent().replace("\r\n", "").replaceAll("\\s", "");
					commentNumsub = taolun.getFirstElementChild().getLastElementChild().getFirstElementChild().getTextContent().replace("\r\n", "").replaceAll("\\s", "");
				}
				
				dt_comment cObj = new dt_comment();
				cObj.name = name.replaceAll("\r\n", "").replaceAll("\\s", "");
				cObj.img = image_icon_url;
				cObj.comment = text + "<>" + text_image_src;
				String[] split = voteText.replaceAll("\r\n", "").replaceAll("\\s", "").replace("好笑", "").replace("评论", "").split("\\·");
				cObj.upNum = Integer.parseInt(split[0]);
				cObj.commentNum = Integer.parseInt(split[1]);
				cObj.id = dbOperator.addObject(cObj);
				if(!taolunTxt.equals("")){
					dt_comment cObj_comment = new dt_comment();
					cObj_comment.name = namesub;
					cObj_comment.comment = commentsub.substring(1,commentsub.length()-3);
					cObj_comment.commentNum = Integer.parseInt(commentNumsub);
					cObj_comment.id = dbOperator.addObject(cObj_comment);
					dt_relation rela = new dt_relation();
					rela.master_id = cObj.id;
					rela.slave_id = cObj_comment.id;
					dbOperator.addObject(rela, true);
				}
			}
			
			page = de.getLastElementChild().getFirstElementChild().click();//下一页
		}
		
		
		webClient.close();
	}
	
	public static void testtravelData() throws Exception{
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");  
		final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
		System.out.println("启动webclient...");
		webClient.getOptions().setUseInsecureSSL(true);//ssl使用内部默认
		webClient.getOptions().setThrowExceptionOnScriptError(false); //此行必须要加
		webClient.getOptions().setTimeout(300000);
		HtmlPage page = (HtmlPage) webClient.getPage(LOGIN_URL2);
		// 根据form的名字获取页面表单，也可以通过索引来获取：page.getForms().get(0)
		for(int i=0;i<10;i++){
			DomElement cotent = page.getElementById("footzoon");
			DomElement de = null;
			Iterable<HtmlElement> itraterble= cotent.getElementsByTagName("h3");
			for(Iterator<HtmlElement> it= itraterble.iterator();it.hasNext();){
				de = it.next();
				String title = de.getTextContent();
				String voteText = de.getNextSibling().getTextContent();
				content cObj = new content();
				cObj.title = title;
				cObj.content = voteText.trim();
				cObj.id = dbOperator.addObject(cObj);
			}
			DomNodeList<HtmlElement> elementsByTagName = page.getElementById("ct_page").getElementsByTagName("li");
			page =  (elementsByTagName.get(elementsByTagName.getLength()-1)).getFirstElementChild().click();//下一页
		}
		
		DBUtil.closeConnection();
		webClient.close();
	}
	
	public static void main(String args[]) throws Exception{
//		testHtmlUnitAdd();
		testtravelData();
//		Down.download(downApkUrl, mdmFile);
	}
	
}
