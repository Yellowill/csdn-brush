package com.hw.csdn_brush.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BeanMap {
	private static final long serialVersionUID = 3561701476588049518L;
	static ThreadLocal<Map<Class, PropertyDescriptor[]>> getPropertyDescriptorsMapTL = new ThreadLocal<Map<Class, PropertyDescriptor[]>>();

	public static PropertyDescriptor[] getPropertyDescriptors(Class c)
			throws IntrospectionException {
		Map propertyDescriptorsMap = getPropertyDescriptorsMapTL.get();
		if (propertyDescriptorsMap == null) {
			propertyDescriptorsMap = new HashMap<Class, PropertyDescriptor[]>();
			getPropertyDescriptorsMapTL.set(propertyDescriptorsMap);
		}
		PropertyDescriptor[] propertyDescriptors = (PropertyDescriptor[]) propertyDescriptorsMap
				.get(c);
		if (propertyDescriptors == null) {
			propertyDescriptors = Introspector.getBeanInfo(c)
					.getPropertyDescriptors();
			propertyDescriptorsMap.put(c, propertyDescriptors);
		}
		return propertyDescriptors;
	}

	public static final Object[] NULL_ARGUMENTS = {};
	Object obj;
	Map<String, Entry<String, Object>> entryMap;
	Set<Entry<String, Object>> entrySet;
	boolean ignoreCase = false;

	public BeanMap(Object obj) {
		this(obj, false);
	}

	public BeanMap(Object obj, boolean ignoreCase) {
		this.obj = obj;
		this.ignoreCase = ignoreCase;
		entryMap();
	}

	public Map<String, Entry<String, Object>> entryMap() {
		if (entryMap == null) {
			synchronized (this) {
				entryMap = new HashMap<String, Map.Entry<String, Object>>() {
					@Override
					public java.util.Map.Entry<String, Object> get(Object key) {
						key = wrapKey(key);
						return super.get(key);
					}

					@Override
					public boolean containsKey(Object key) {
						key = wrapKey(key);
						return super.containsKey(key);
					}

					@Override
					public java.util.Map.Entry<String, Object> put(String key,
							java.util.Map.Entry<String, Object> entry) {
						key = wrapKey(key);
						return super.put(key, entry);
					}
				};
				try {
					if (obj == null) {
						return entryMap;
					}
					for (final PropertyDescriptor propertyDescriptor : getPropertyDescriptors(obj
							.getClass())) {
						if ("class".equals(propertyDescriptor.getName())) {
							continue;
						}
						final String name;
						if (ignoreCase) {
							name = propertyDescriptor.getName().toLowerCase();
						} else {
							name = propertyDescriptor.getName();
						}
						if (entryMap.containsKey(name)) {
							throw new RuntimeException(String.format(
									"the property name %s repeated", name));
						}
						Entry entry = new VEntry() {
							Method readMethod;
							Method writeMethod;
							Class type;
							Object annotation;

							public Method getReadMethod() {
								if (readMethod == null) {
									readMethod = propertyDescriptor
											.getReadMethod();
								}
								return readMethod;
							}

							public Method getWriteMethod() {
								if (writeMethod == null) {
									writeMethod = propertyDescriptor
											.getWriteMethod();
								}
								return writeMethod;
							}

							public Object getKey() {
								return name;
							}

							public Object getValue() {
								if (getReadMethod() == null) {
									return null;
								}
								try {
									getReadMethod().setAccessible(true);
									return getReadMethod().invoke(obj,
											NULL_ARGUMENTS);
								} catch (Exception e) {
									throw new RuntimeException(e);
								}
							}

							public Object setValue(Object value) {
								Object old = getValue();

								if (getWriteMethod() == null) {
									throw new UnsupportedOperationException(
											"the property:"
													+ name
													+ " is not exist a write method");
								}
								try {
									getWriteMethod().setAccessible(true);
									getWriteMethod().invoke(obj, value);
								} catch (Exception e) {
									throw new RuntimeException(e);
								}
								return old;
							}

							public Class getType() {
								if (type == null) {
									type = propertyDescriptor.getPropertyType();
								}
								return type;
							}

							public boolean canRead() {
								return getReadMethod() != null;
							}

							public boolean canWrite() {
								return getWriteMethod() != null;
							}

							public Object getAnnotation(Class o) {
								if (this.getReadMethod() != null) {
									if (this.getReadMethod()
											.isAnnotationPresent(o)) {
										annotation = this.getReadMethod().getAnnotation(o);
									}
								}
								return annotation;
							}
						};
						entryMap.put(name, entry);
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				entryMap = Collections.unmodifiableMap(entryMap);
			}
		}
		return entryMap;
	}

	public Set<Entry<String, Object>> entrySet() {
		if (entrySet == null) {
			synchronized (this) {
				entrySet = new AbstractSet<Map.Entry<String, Object>>() {
					@Override
					public Iterator<java.util.Map.Entry<String, Object>> iterator() {
						return entryMap.values().iterator();
					}

					@Override
					public int size() {
						return entryMap.size();
					}
				};
				entrySet = Collections.unmodifiableSet(entrySet);
			}
		}
		return entrySet;
	}

	public boolean containsKey(Object key) {
		return entryMap.containsKey(key);
	}

	private String wrapKey(Object key) {
		if (ignoreCase) {
			key = key == null ? null : key.toString().toLowerCase();
		}
		return key.toString();
	}

	public Object put(String key, Object value) {
		Entry entry = entryMap.get(key);
		if (entry == null) {
			throw new RuntimeException("this obj have no field name:" + key);
		}
		return entry.setValue(value);
	}

	public Set<String> keySet() {
		return entryMap.keySet();
	}

	public Object get(Object key) {
		Entry entry = entryMap.get(key);
		if (entry == null) {
			return null;
		}
		return entry.getValue();
	}
}
