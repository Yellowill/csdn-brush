package com.hw.csdn_brush.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.hw.csdn_brush.bean.AgencyIp;


public class JsoupUtil {

	  /**
     * 模拟浏览器行为的请求头获取Document
     * @param url
     * @return
     * @throws IOException
     */
    public static Document getDoc(String url) throws IOException {
    	 
//    	* 现在很多站点都是SSL对数据传输进行加密，这也让普通的HttpConnection无法正常的获取该页面的内容，
//   	 * 而Jsoup起初也对此没有做出相应的处理， 
//   	 * 想了一下是否可以让Jsoup可以识别所有的SSL加密过的页面，查询了一些资料，发现可以为本地HttpsURLConnection配置一个“万能证书”，其原理是就是：
//   	 * 重置HttpsURLConnection的DefaultHostnameVerifier，使其对任意站点进行验证时都返回true
//   	 * 重置httpsURLConnection的DefaultSSLSocketFactory， 使其生成随机证书
//   	 * 后来Jsoup Connection提供了validateTLSCertificates(boolean validate)//是否进行TLS证书验证,不推荐
//		try {
//			// 重置HttpsURLConnection的DefaultHostnameVerifier，使其对任意站点进行验证时都返回true
//			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//				public boolean verify(String hostname, SSLSession session) {
//					return true;
//				}
//			});
//			// 创建随机证书生成工厂
//			//SSLContext context = SSLContext.getInstance("TLS");
//			SSLContext context = SSLContext.getInstance("TLSv1.2");//TLSv1,TLSv1.1,TLSv1.2,SSLv3
//			context.init(null, new X509TrustManager[] { new X509TrustManager() {
//				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//				}
// 
//				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//				}
// 
//				public X509Certificate[] getAcceptedIssuers() {
//					return new X509Certificate[0];
//				}
//			} }, new SecureRandom());
// 
//			// 重置httpsURLConnection的DefaultSSLSocketFactory， 使其生成随机证书
//			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        /**
         * 在爬之前最好看一下浏览器访问目标网站的Request Header信息，然后进行模仿
         */
    	//公司环境的网络代理，一般情况应该不用设置此项
		Proxy p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.83.224.234", 8080));
		Connection con = Jsoup.connect(url);
		con.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		con.header("Accept-Encoding", "zip, deflate, br");
		con.header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
		con.header("Cache-Control","max-age=0");
		con.header("Connection","keep-alive");
		con.header("Upgrade-Insecure-Requests","1");
		con.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        
		con.proxy(p);
		con.timeout(0);
		con.validateTLSCertificates(false);
		Document doc = con.get();
		
		return doc;
    }
	 /**
     * 获取代理IP地址
     * @param url
     * @return
     */
    public static List<AgencyIp> getIp(String url) {
        List<AgencyIp> ipList = null;
        try {
            //1.向ip代理地址发起get请求，拿到代理的ip
            Document doc = getDoc(url);

            //2,将得到的ip地址解析除字符串
            String ipStr = doc.body().text().trim().toString();

            ipList = new ArrayList<AgencyIp>();

            List<String> ips = new ArrayList<String>();
            String lines[] = ipStr.split("\r\n");
            for(int i=0; i<lines.length;i++){
                //out.println(lines[i]+"<br />");
                //匹配到ip地址
                Pattern ipreg = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3} \\d{4}");
                Matcher ip = ipreg.matcher(lines[i]);
                while(ip.find()){
                    System.out.println(ip.group().replace(" ",":"));
                    ips.add(ip.group().replace(" ",":"));
                }
            }
            //4.循环遍历得到的ip字符串，封装成AgencyIp的bean

            for(final String ip : ips) {
                System.out.println(ip);
                AgencyIp AgencyIp = new AgencyIp();
                String[] temp = ip.split(":");
                AgencyIp.setAddress(temp[0].trim());
                AgencyIp.setPort(temp[1].trim());
                ipList.add(AgencyIp);
            }
        } catch (IOException e) {
            System.out.println("本地加载文档----错误！");
        }
        return ipList;
    }
    
    /**
     * 访问文章 线程处理方式
     * @param blogUrl
     * @param ipList
     */
    public static void visit(String blogUrl,List<AgencyIp> ipList,int time){
        int count = 0;

        for(int i = 0; i< time; i++) {
            //2.设置ip代理
            for(final AgencyIp AgencyIp : ipList) {
            	System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
                System.setProperty("http.maxRedirects", "50");
                System.getProperties().setProperty("proxySet", "true");
                System.getProperties().setProperty("http.proxyHost", AgencyIp.getAddress());
                System.getProperties().setProperty("http.proxyPort", AgencyIp.getPort());

                try {
                    Thread.sleep(1000);
                    Document doc = getDoc(blogUrl);
                    if(doc != null) {
                        count++;
                        System.out.println("成功刷新次数: " + count);
                        System.out.println("文章地址:" + blogUrl);
//                        System.out.println("doc====================================="+doc);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
