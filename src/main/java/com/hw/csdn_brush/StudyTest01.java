package com.hw.csdn_brush;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class StudyTest01 {

	public static void main(String[] args) {
		BigDecimal a1 = new BigDecimal(0.01);
		BigDecimal a2 = new BigDecimal("0.01");
		Double a3 = new Double(0.01);
		System.out.println("a1-------" + a1);
		System.out.println("a2-------" + a2);
		System.out.println("a3-------" + a3);
//		System.out.println(Math.round(11.5) + "====" + Math.round(-11.5));
		
		byte[] pdfBytes = new byte[0];
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println(pdfBytes.length);
		String st= (String) map.get("t");
		System.out.println("st-----"+st);
	}
	

}
