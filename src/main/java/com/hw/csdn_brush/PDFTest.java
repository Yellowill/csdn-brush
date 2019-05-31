package com.hw.csdn_brush;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import javax.net.ssl.SSLSession;

import com.hw.csdn_brush.utils.FileUtil;


public class PDFTest {
    /**
     * http发送请求
     */
    private String doPost(String requestUrl, String contentStr) throws Exception {
         HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {  
             return true;  
           }  
        };  
        
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        StringBuffer responseResult = new StringBuffer();
        HttpURLConnection httpURLConnection = null;
        try {
           URL realUrl = new URL(requestUrl);
           // 打开和URL之间的连接
          HttpsURLConnection https = (HttpsURLConnection)realUrl.openConnection();  
          if (realUrl.getProtocol().toLowerCase().equals("https")) {  
              https.setHostnameVerifier(DO_NOT_VERIFY);  
              httpURLConnection = https;  
          } else {  
              httpURLConnection = (HttpURLConnection)realUrl.openConnection();  
          }  
          //conn.connect(); 
            
           // 设置通用的请求属性
          httpURLConnection.setRequestMethod("POST"); // 设置请求方式  
          httpURLConnection.setRequestProperty("Accept", "application/xml"); // 设置接收数据的格式  
          httpURLConnection.setRequestProperty("Content-Type", "application/xml"); // 设置发送数据的格式  
          httpURLConnection.setRequestProperty("Charset", "UTF-8");
           // 发送POST请求必须设置如下两行
           httpURLConnection.setDoOutput(true);
           httpURLConnection.setDoInput(true);
           // Post 请求不能使用缓存
           httpURLConnection.setUseCaches(false);
           
           httpURLConnection.setConnectTimeout(1000 * 60);
           httpURLConnection.setReadTimeout(1000 * 60);
           
           // 获取URLConnection对象对应的输出流
           OutputStream os = httpURLConnection.getOutputStream();    
           os.write(contentStr.getBytes("UTF-8"));   
           os.close(); 
           
           // 根据ResponseCode判断连接是否成功
           int responseCode = httpURLConnection.getResponseCode();
           if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("Error===" + responseCode);
           } else {
                System.out.println("Post Success!");
           }
            
           // 定义BufferedReader输入流来读取URL的ResponseData
           bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
           String line;
           while ((line = bufferedReader.readLine()) != null) {
               responseResult.append("\n").append(line);
           }
        } catch (Exception e) {
           System.out.println("send post request error!" + e);
           throw e;
        } finally {
           httpURLConnection.disconnect();
           try {
               if (printWriter != null) {
                   printWriter.close();
               }
               if (bufferedReader != null) {
                   bufferedReader.close();
               }
           } catch (IOException ex) {
                System.out.println("close source error!" + ex);
           }
    
        }
        
        return responseResult.toString();
    }
    
    
    
    public static void main(String[] args) throws UnsupportedEncodingException, IOException, Exception {  
       
        String suningServletUrl = "https://test.allianz.cn/IG_ePolicy/azcn-issuepolicy";
        
        String filePatch = "D:\\work\\test\\0006.txt"; 
        
        PDFTest test = new PDFTest();
        
        //发送请求
        test.postAction(suningServletUrl, filePatch);
        
    }
    
    private void postAction(String url, String filePatch) throws UnsupportedEncodingException, Exception {
        String filePatch2 = "D:\\work\\test\\00066.pdf";
    	String str = FileUtil.getFileContent(filePatch);
        System.out.println("读取文件报文：\n" + str);
        String aesXml = str;//本来是打算做其他事的，想了想没做，暂时这样写
        System.out.println("aesXml: " +  aesXml);
        
        String response = this.doPost(url, aesXml);
        System.out.println("返回报文：response: " +  response);
        BufferedInputStream bin = null;
        FileOutputStream fout = null;
        BufferedOutputStream bout = null;
        byte[] srtbyte = response.getBytes();
        System.out.println("开始写入PDF文件。。。");
        long startTime = System.currentTimeMillis();
        try {
            //创建一个将bytes作为其缓冲区的ByteArrayInputStream对象
            ByteArrayInputStream bais = new ByteArrayInputStream(srtbyte);

            //创建从底层输入流中读取数据的缓冲输入流对象
            bin = new BufferedInputStream(bais);
            //指定输出的文件
            File file = new File(filePatch2);
            if (!file.exists()){

                file.createNewFile();
            }
            //创建到指定文件的输出流
            fout= new FileOutputStream(file);
            //为文件输出流对接缓冲输出流对象
            bout = new BufferedOutputStream(fout);
            byte [] buffers = new byte[1024];
            int len = bin.read(buffers);
            while(len != -1){
                bout.write(buffers, 0, len);
                len = bin.read(buffers);
            }
            //刷新此输出流并强制写出所有缓冲的输出字节，必须这行代码，否则有可能有问题
            bout.flush();
        }catch(IOException e) {
            e.printStackTrace();
        }finally{
            try {
                bin.close();
                fout.close();
                bout.close();
                System.err.println("cost " + (System.currentTimeMillis() - startTime) + "ms");
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
