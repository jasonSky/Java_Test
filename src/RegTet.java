import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegTet {

	 public static String readFile(String path) throws FileNotFoundException {
	        long start = System.currentTimeMillis();//开始时间
	        int bufSize = 1024;//1K缓冲区
            String str = "";
	        File fin = new File(path);
	        /*
	         * 通道就是为操作文件而建立的一个连接。（读写文件、内存映射等）
	         * 此处的getChannel()可以获取通道；
	         * 用FileChannel.open(filename)也可以创建一个通道。
	         * "r"表示只读。
	         *
	         * RandomAccessFile是独立与I/O流家族的类，其父类是Object。
	         * 该类因为有个指针可以挪动，所以，可以从任意位置开始读取文件数据。
	         * **/
	        FileChannel fcin = new RandomAccessFile(fin, "r").getChannel();
	        //给字节缓冲区分配大小
	        ByteBuffer rBuffer = ByteBuffer.allocate(bufSize);                     
	        String enterStr = "\n";
	        try {
	            byte[] bs = new byte[bufSize];
	            String tempString = null;
	            while (fcin.read(rBuffer) != -1) {//每次读1k到缓冲区
	                int rSize = rBuffer.position();//记录缓冲区当前位置
	                rBuffer.rewind();//位置归零，标记取消，方便下次循环重新读入缓冲区。
	                rBuffer.get(bs);//将缓冲区数据读到字节数组中
	                rBuffer.clear();//清除缓冲
	                /*
	                 * 用默认编码将指定字节数组的数据构造成一个字符串
	                 * bs:指定的字节数组，0：数组起始位置；rSize：数组结束位置
	                 * */
	                tempString = new String(bs, 0, rSize);
	                int fromIndex = 0;//每次读的开始位置
	                int endIndex = 0;//每次读的结束位置
	                //按行读String数据
	                while ((endIndex = tempString.indexOf(enterStr, fromIndex)) != -1) {
	                    String line = tempString.substring(fromIndex, endIndex);//转换一行
	                    str+=line;
	                    //System.out.print(line);                 
	                    fromIndex = endIndex + 1;
	                }
	            }
	            long end = System.currentTimeMillis();//结束时间
	            //System.out.println("传统IO读取数据,指定缓冲区大小，总共耗时："+(end - start)+"ms");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			return str;
	    }
	
	public static void main(String args[]) throws FileNotFoundException, InterruptedException{
		String result = readFile("src\\abc.txt");
		long start2 = System.currentTimeMillis();//开始时间
		//Thread.sleep(100);
		int begin = result.lastIndexOf("cmdnum\":");
		int end = result.substring(begin+8).indexOf(",");
		//System.out.println(begin + "  " + end);
		System.out.println("result:" + result.substring(begin+8, begin+8+end));
		long end2 = System.currentTimeMillis();//结束时间
		System.out.println("处理字符串总共耗时："+(end2 - start2)+"ms, end2:" + end2 + ",start:"+start2);
		//String pipei3= "cmdnum\":(\\d).*?]\\}\\]\\}\\}\\]\\}";
	}
	
}
