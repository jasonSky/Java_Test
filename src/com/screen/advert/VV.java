package com.screen.advert;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jacob.com.LibraryLoader;

import autoitx4java.AutoItX;

public class VV {
	
	public static Robot rbt = null;
	public static AutoItX autoitx = null;
	static{
		File file = null;
		String arch = System.getenv("PROCESSOR_ARCHITECTURE");
		String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");
		if(arch.endsWith("64")|| wow64Arch != null && wow64Arch.endsWith("64")){
			file = getFile("jacob", "/lib/jacob-1.18-M3-x64.dll");
			if(file == null)
				file = new File("lib", "jacob-1.18-M3-x64.dll");
		}else{
			file = getFile("jacob", "/lib/jacob-1.18-M3-x86.dll");
			if(file == null)
				file = new File("lib", "jacob-1.18-M3-x86.dll");
		}
//		System.out.println("====" + file.getAbsolutePath());
		System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
        autoitx=new AutoItX();   //得到Autoit的实体类
	}
	
	public static void enter(int key){
			if(rbt  == null){
				try {
					rbt = new Robot();
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//按下
			rbt.keyPress(key);
			//释放
			rbt.keyRelease(key);
	}
	
	public static File getFile(String prefix, String resource){
		File file = null;
	    URL res = VV.class.getResource(resource);
	    if (res!= null && res.toString().startsWith("jar:")) {
	        try {
	            InputStream input = VV.class.getResourceAsStream(resource);
	            file = File.createTempFile(prefix, ".dll");
				OutputStream out = new FileOutputStream(file);
	            int read;
	            byte[] bytes = new byte[1024];

	            while ((read = input.read(bytes)) != -1) {
	                out.write(bytes, 0, read);
	            }
	            
	            out.close();
	            input.close();
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }
	    
	    return file;
	}
	
	public void send(String txt, int times, String title){
		autoitx.winActivate(title);//"Single dogs"
		for(int i=0;i<times;i++){
			autoitx.send(txt);//str.substring(i, i+1)//"xvc"
			autoitx.send("{Enter}", false);
		}
	}
	
	public void sendC(String txt, String title){
		autoitx.winActivate(title);//"Single dogs"
		for(int i=0;i<txt.length();i++){
			autoitx.send(txt.substring(i, i+1));//str.substring(i, i+1)//"xvc"
			autoitx.send("{Enter}", false);
		}
	}
	
//	public static void main(String args[]){
//		autoitx.winActivate("Single dogs");
//////		#表情解析
////		String str = "1 微笑 /wx 36 折磨 /zhem 71 炸弹 /zhd 2 撇嘴 /pz 37 衰 /shuai 72 刀 /dao 3 色 /se 38 骷髅 /kl 73 足球 /zq 4 发呆 /fd 39 敲打 /qiao 74 瓢虫 /pch 5 得意 /dy 40 再见 /zj 75 便便 /bb 6 流泪 /ll 41 擦汗 /ch 76 月亮 /yl 7 害羞 /hx 42 抠鼻 /kb 77 太阳 /ty 8 闭嘴 /bz 43 鼓掌 /gz 78 礼物 /lw 9 睡 /shui 44 糗大了 /qd 79 拥抱 /yb 10 大哭 /dk 45 坏笑 /huaix 80 强 /qiang 11 尴尬 /gg 46 左哼哼 /zhh 81 弱 /ruo 12 发怒 /fn 47 右哼哼 /yhh 82 握手 /ws 13 调皮 /tp 48 哈欠 /hq 83 胜利 /shl 14 呲牙 /cy 49 鄙视 /bs 84 抱拳 /bq 15 惊讶 /jy 50 委屈 /wq 85 勾引 /gy 16 难过 /ng 51 快哭了 /kk 86 拳头 /qt 17 酷 /kuk 52 阴险 /yx 87 差劲 /cj 18 冷汗 /lengh 53 亲亲 /qq 88 爱你 /aini 19 抓狂 /zk 54 吓 /xia 89 不 /bu 20 吐 /tuu 55 可怜 /kel 90 好 /hd 21 偷笑 /tx 56 菜刀 /cd 91 爱情 /aiq 22 可爱 /ka 57 西瓜 /xig 92 飞吻 /fw 23 白眼 /baiy 58 啤酒 /pj 93 跳跳 /tiao 24 傲慢 /am 59 篮球 /lq 94 发抖 /fad 25 饥饿 /jie 60 乒乓 /pp 95 怄火 /oh 26 困 /kun 61 咖啡 /kf 96 转圈 /zhq 27 惊恐 /jk 62 饭 /fan 97 磕头 /kt 28 流汗 /lh 63 猪头 /zt 98 回头 /ht 29 憨笑 /hanx 64 玫瑰 /mg 99 跳绳 /tsh 30 大兵 /db 65 凋谢 /dx 100 挥手 /hsh 31 奋斗 /fendou 66 示爱 /sa 101 激动 /jd 32 咒骂 /zhm 67 爱心 /xin 102 街舞 /jw 33 疑问 /yiw 68 心碎 /xs 103 献吻 /xw 34 嘘…… /xu 69 蛋糕 /dg 104 左太极 /zuotj 35 晕 /yun 70 闪电 /shd 105 右太极 /youtj";
////	      List<String> bq = new ArrayList<String>();
////	      Pattern pattern = Pattern. compile("(/[a-z]*)");
////	      Matcher matcher = pattern.matcher(str);
////	       while(matcher.find()){
////	             bq.add(matcher.group(0));
////	             autoitx.send(matcher.group(0)); 
////	             autoitx.send("{Enter}", false); 
////	      }
////	      System. out.println(bq.size());
//		String str = "外地的也寄吗";
//		for(int i=0;i<100;i++){
//			autoitx.send("xvc");//str.substring(i, i+1)//"xvc"
//            autoitx.send("{Enter}", false);
//		}
//
//	}
	
}
