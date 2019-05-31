package com.hw.csdn_brush.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestUtils {

	public static void main(String[] args) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        //fmt.setTimeZone(new TimeZone()); // 如果需要设置时间区域，可以在这里设置
        System.out.println(fmt.format(new Date()).equals(fmt.format(new Date(new Date().getTime() + 24*60*60*1000))));
        

//        qr.setValue(result.getString("VALUE"));
//        qr.setDescription(result.getString("DESCRIPTION"));
//        qr.setResult(result.getString("RESULT"));
//        qr.setAgencyCode(result.getString("AGENCYCODE"));
//        qr.setLopProdType(result.getString("LOPPRODTYPE"));
//        
	}

}
