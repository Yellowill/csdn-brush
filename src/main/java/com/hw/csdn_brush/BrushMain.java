package com.hw.csdn_brush;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hw.csdn_brush.bean.AgencyIp;
import com.hw.csdn_brush.utils.JsoupUtil;

public class BrushMain {

	//原地址https://blog.csdn.net/qq_28033239
	private static String targetCSDNUrl = "https://blog.csdn.net/";//csdn首页地址
	
	private static String personalUrl = "qq_28033239";//用作正则匹配
	 /**
     * 获取所有个人博客地址
     * @return
     */
    public static List<String> getCsdnBlogsUrl(){
        List<String> urls = new ArrayList<String>();
        try {//----https://blog.csdn.net/qq_28033239  targetCSDNUrl + personalUrl
            Document doc = JsoupUtil.getcsdnDoc(targetCSDNUrl + personalUrl);//https://blog.csdn.net/qq_31423975  ---耀明的地址
            Element body = doc.body();
            Pattern compile = Pattern.compile(personalUrl + "/article/details/\\d{8}$");
            Elements es=body.select("a");
            /**
             * 用set去重
             */
            HashSet<String> set = new HashSet<String>();
            for (Iterator it = es.iterator(); it.hasNext();) {
                Element e = (Element) it.next();
                if (compile.matcher(e.attr("href")).find()){
                    set.add(e.attr("href"));
                }
            }
            urls.addAll(set);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urls;
    }
    
    public static void main(String[] args) {
    	System.getProperties().setProperty("http.proxyHost", "10.83.224.234");
//    	System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
        //1.向http代理地址api发起请求，获得想要的代理ip地址 这样的网址有很多。找到方便提取IP的即可
        String url = "http://www.xicidaili.com/nn/";
        final List<AgencyIp> ipList = JsoupUtil.getIp(url);
        System.out.println("----获取代理IP数目为：" + ipList.size() + "----");
        Random rand = new Random();
        int tt = rand.nextInt(15);
        System.out.println("----Start tt---：" + tt + "----");
        System.out.println("----UrlListLength---：" + getCsdnBlogsUrl().size() + "----");
        List<String> urls = getCsdnBlogsUrl().subList(tt, tt+6);//subList(3, 10)可自行控制访问的地址
        System.out.println("----需要访问的地址数目为 ：" + urls.size() + "----");
        //-----线程池方式
        System.out.println("----开启线程池----");
//        ExecutorService pool = Executors.newFixedThreadPool(5);
//        for (final String u:urls){
//        	pool.submit(new visitExecutor(u,ipList,1));
//        }
//        //结束线程池
//        pool.shutdown();
        //---常规线程处理方式
        for (final String u:urls){
            new Thread(new Runnable() {
                public void run() {
                    System.out.println("文章地址:" + u);
                    System.out.println(Thread.currentThread().getName() + "正在执行。。。"); 
                    JsoupUtil.visit(u,ipList,50);
                }
            }).start();
        }
    }
    /**
     * 线程池处理方式
     * @author ext-huangw
     */
    private static class visitExecutor implements Runnable{
    	String blogUrl = null;
    	List<AgencyIp> ipList = null;
    	int loopCount = 1;
    	public visitExecutor (String blogUrl,List<AgencyIp> ipList,int loopCount) {
    		this.blogUrl = blogUrl;
    		this.ipList = ipList;
    		this.loopCount = loopCount;
    	}
		public void run() {
			System.out.println(Thread.currentThread().getName() + "正在执行。。。"); 
			JsoupUtil.visit(blogUrl,ipList,loopCount);
		}
    }
    
  
}
