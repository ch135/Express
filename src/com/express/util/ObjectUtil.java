package com.express.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ObjectUtil {

	/**
	 * 
	 * 获取对象属性，返回一个字符串数组
	 * 
	 * @param o对象
	 * @return String[] 字符串数组
	 */
	public static String[] getField(Object o) throws NoSuchFieldException {
		try {
			Field[] fields = o.getClass().getDeclaredFields();
			// String[] fieldNames = new String[fields.length];
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < fields.length; i++) {
				if (getFieldValueByName(fields[i].getName().toString(), o)) {
					// System.out.println("字段的名字为------：" +
					// fields[i].getName());
					list.add(fields[i].getName());
				}
			}
			String[] fieldNames = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				fieldNames[i] = list.get(i);
			}
			return fieldNames;
		} catch (SecurityException e) {
			e.printStackTrace();
			System.out.println(e.toString());
			return null;
		}
	}

	/**
	 * 
	 * 使用反射根据属性名称获取属性值
	 * 
	 * 
	 * 
	 * @param fieldName
	 *            属性名称
	 * 
	 * @param o
	 *            操作对象
	 * 
	 * @return Object 属性值
	 */

	public static Object[] getFieldValuesByName(String[] fieldName, Object o) {
		try {
			Object[] objects = new Object[fieldName.length];
			for (int i = 0; i < fieldName.length; i++) {
				String firstLetter = fieldName[i].substring(0, 1).toUpperCase();
				String getter = "get" + firstLetter + fieldName[i].substring(1);
				Method method = o.getClass().getMethod(getter, new Class[] {});
				objects[i] = method.invoke(o, new Object[] {});

			}
			return objects;
		} catch (Exception e) {
			System.out.println("属性组不存在");

		}
		return null;

	}

	/**
	 * 
	 * 使用反射根据属性名称获取属性值
	 * 
	 * 
	 * 
	 * @param fieldName
	 *            属性名称
	 * 
	 * @param o
	 *            操作对象
	 * 
	 * @return Object 属性值
	 */

	public static boolean getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			if (value == null || value.equals(0) || value.equals(0.0) || value.equals(0.0f) || value.equals(false)) {
				return false;
			} else {
				// System.out.println("值为—————————————" + value);
				return true;
			}
		} catch (Exception e) {
			System.out.println("属性不存在");
			return false;
		}
	}
}
