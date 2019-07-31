package com.hw.csdn_brush.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Test02 {

	public static void main(String[] args) {
		System.getProperties().setProperty("http.proxyHost", "10.83.224.234");
		System.getProperties().setProperty("http.proxyPort", "8080");
		BigDecimal t = new BigDecimal(0.01);
		String str = t.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		str = "11.2";
		
//		System.out.println((int)(Double.parseDouble(str)));
		Socket connect = new Socket();
        try {//101.254.100.21 443
            connect.connect(new InetSocketAddress("101.254.100.21", 443),100);//建立连接
            boolean res = connect.isConnected();//通过现有方法查看连通状态
            System.out.println(res);//true为连通
        } catch (IOException e) {
            System.out.println("false");//当连不通时，直接抛异常，异常捕获即可
        }finally{
            try {
                connect.close();
            } catch (IOException e) {
                System.out.println("false");
            }
        }

	}

}
