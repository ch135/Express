package com.express.util;

public class ValueUtil {
	
	public static boolean isNull(String...re){
		for (int i = 0; i < re.length; i++) {
			if(re[i]==null){
				System.out.println("空值");
				return true;
			}
		}
		return false;
	}
}
