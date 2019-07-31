package com.hw.csdn_brush.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Beans {
	/**
	 * 拷贝两个Bean的属性（弱校验，增强容错性，但同名字段必须具有可直接赋值的相同类型或者父类型）
	 * 
	 * @param t
	 * @param s
	 * @param copyNull
	 *            当目标对象相关属性非空时，是否拷贝源对象的null值覆盖掉
	 * @param copyEmpty
	 *            当目标对象相关属性非空时，是否拷贝源对象的空值覆盖掉
	 */
	public static void copy(Object t, Object s, boolean copyNull, boolean copyEmpty) {
		if (s == null || t == null) {
			return;
		}
		Map<String, Entry<String, Object>> targetMap = new BeanMap(t).entryMap();
		Map<String, Entry<String, Object>> sourceMap = new BeanMap(s).entryMap();
		for (String prop : targetMap.keySet()) {
			VEntry<String, Object> targetEntry = (VEntry) targetMap.get(prop);
			if (isEmpty(targetEntry.getType())) {
				continue;
			}
			if (targetEntry.getType().isArray()) { // 不拷贝数组
				continue;
			}
			if (Collection.class.isAssignableFrom(targetEntry.getType())) { // 不拷贝集合
				continue;
			}
			if (Map.class.isAssignableFrom(targetEntry.getType())) { // 不拷贝映射
				continue;
			}
			VEntry<String, Object> sourceEntry = (VEntry) sourceMap.get(prop);
			if (sourceEntry == null) { // 属性不匹配
				continue;
			}
			if (!targetEntry.getType().isAssignableFrom(sourceEntry.getType())) { // 类型不匹配
				continue;
			}
			Object sourceObject = sourceEntry.getValue();
			if (sourceObject == null) {
				if (!copyNull) {
					continue;
				}
			}
			if (!CharSequence.class.isAssignableFrom(targetEntry.getType()) && isEmpty(sourceObject)) {
				if (!copyEmpty) {
					continue;
				}
			}
/*			if (Row.class.isAssignableFrom(targetEntry.getType())) { // Row类型不copy
				continue;
			}*/

			if (targetEntry.canWrite()) {
				targetEntry.setValue(sourceObject);
			}
		}
	}

	/**
	 * 默认拷贝方法，不拷贝null属性和空属性
	 * 
	 * @param t
	 * @param s
	 */
	public static void copy(Object t, Object s) {
		copy(t, s, false, false);
	}

	/**
	 * 判断一个对象是否为空
	 * 
	 * @param sv
	 * @return
	 */
	public static boolean isEmpty(Object sv) {
		if (sv == null) {
			return true;
		}
		if (sv instanceof byte[]) {
			return ((byte[]) sv).length == 0;
		}
		if (sv.getClass().isArray()) {
			return ((Object[]) sv).length == 0;
		}
		if (sv instanceof Collection) {
			return ((Collection) sv).isEmpty();
		}
		if (sv instanceof Map) {
			return ((Map) sv).isEmpty();
		}
		if (sv instanceof String) {
			return sv.toString().trim().equals("");
		}
		return sv.getClass().equals(Object.class);
	}

	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}

	/**
	 * 转换第一个字母成为大写
	 * 
	 * @param o
	 * @return
	 */
	public static String firtCharToUpper(Object o) {
		if (isNotEmpty(o) && o instanceof String) {
			String first = o.toString().substring(0, 1);
			return o.toString().replaceFirst(first, first.toUpperCase());
		}
		return null;
	}
	
	/**
	 * 校验手机号码
	 * @param mobileNo
	 * @return
	 */
    public static boolean checkStandardMobileNo(String mobileNo, String standardMobileRegex){
        boolean flag = false;
        try{
            Pattern regex = Pattern.compile(standardMobileRegex);
            Matcher matcher = regex.matcher(mobileNo);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
  }
}
