package com.invoke.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.io.IOUtils;

public class StreamGobbler extends Thread {  
    private InputStream inputStream;  
    private String type;  
    private OutputStream outputStream;  

    public StreamGobbler(InputStream inputStream, String type) {  
        this(inputStream,type,null);  
    }  
    public StreamGobbler(InputStream inputStream, String type,OutputStream outputStream) {  
        this.inputStream=inputStream;  
        this.type=type;  
        this.outputStream=outputStream;  
    }  
    public void run() {  
        PrintWriter printWriter=null;  
        InputStreamReader inputStreamReader=null;  
        BufferedReader bufferedReader=null;  
        try {  
            if(outputStream!=null){  
                printWriter=new PrintWriter(outputStream);  
            }  
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);  
            String line = null;  
            while ((line = bufferedReader.readLine())!= null) {  
                if(outputStream!=null){  
                    printWriter.write(line);  
                }  
                System.out.println(type+"> "+line); 
                FileHelper.saveToFile(type+"> "+line, "runlog.log", false);
            }  
            if(outputStream!=null){  
                printWriter.flush();  
            }  
        } catch (IOException ex) {  
            ex.printStackTrace();  
        }finally{  
            //关闭IO流  
            IOUtils.closeQuietly(bufferedReader);  
            IOUtils.closeQuietly(inputStreamReader);  
            IOUtils.closeQuietly(printWriter);  
        }  
    }  
}  