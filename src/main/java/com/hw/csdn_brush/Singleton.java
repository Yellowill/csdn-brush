package com.hw.csdn_brush;

import com.hw.csdn_brush.utils.Beans;

//最优单例问题
public class Singleton {
	//1.标准饿汉模式--立即加载
//	private static Singleton singletonInstance =  new Singleton();
//	
//	public static Singleton getInstance() {
//		return singletonInstance;
//	}
	
	private static Singleton singletonInstance;
	
	//2.标准懒汉模式--延迟加载，懒加载，需要的时候才加载
	public static synchronized Singleton getInstance() {
		if(Beans.isEmpty(singletonInstance)) {
			singletonInstance = new Singleton(); 
		}
		return singletonInstance;
	}
	
	//3.最佳单例模式，静态内部类
//	当 Singleton 类加载时，静态内部类 SingletonHolder 没有被加载进内存。
//	只有当调用 getUniqueInstance() 方法从而触发 SingletonHolder.INSTANCE 时 
//	SingletonHolder 才会被加载，此时初始化 INSTANCE 实例，并且 JVM 能确保 INSTANCE 只被实例化一次。
//	这种方式不仅具有延迟初始化的好处，而且由 JVM 提供了对线程安全的支持。
	public static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getUniqueInstance() {
        return SingletonHolder.INSTANCE;
    }
	
}
