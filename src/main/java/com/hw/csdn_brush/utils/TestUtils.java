package com.hw.csdn_brush.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        
//        Objects.equals(null, "test");
//        Integer a = new Integer(3);
//        Integer b = new Integer(3);
//        System.out.println(a == b);//false
//        System.out.println(a.equals(b));//true
        String[] str = new String [] {"you","wu"};
        List list = Arrays.asList(str);
        System.out.println(list.get(0));
        
        str[0] = "first";
        
        System.out.println(list.get(0));
        
        //如何正确的将数组转换为ArrayList?
        //最简单的方法：List list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        Integer [] myArray = { 1, 2, 3 };
        List myList = Arrays.stream(myArray).collect(Collectors.toList());
        //基本类型也可以实现转换（依赖boxed的装箱操作）
        int [] myArray2 = { 1, 2, 3 };
        List myList2 = Arrays.stream(myArray2).boxed().collect(Collectors.toList());
        
        //[^\\x00-\\xff] 全角半角    ^[a-z0-9A-Z(（）)]+$
//        String strReg ="azAZ12 你好！! (（）)你好ａｚＡＺ１２　！（）";
        String strReg ="azAZ12()（）";
        String regex = "^[a-z0-9A-Z(（）)]+$";
        System.out.println(strReg.matches(regex));
        
        
        char[] chars = strReg.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            String temp = String.valueOf(chars[i]);
            // 判断是全角字符
            if (temp.matches("[^\\x00-\\xff]")) {
               System.out.println("全角   " + temp);
            }
            // 判断是半角字符
            else {
               System.out.println("半角    " + temp);
            }
        }
	}

}
