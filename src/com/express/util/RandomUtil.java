package com.express.util;

import java.util.Random;

public class RandomUtil {

	static Random random = new Random();
	static int randomNumber;
	static String backNum;
	
	public static String  getRandomNum() {
		randomNumber =  (random.nextInt(89) + 10);
		backNum = Integer.toString(randomNumber);
		return backNum;
	}
	
	public static String getfindRandonNum(){
		randomNumber = (random.nextInt(89999) + 10000);
		backNum = Integer.toString(randomNumber);
		return backNum;
	}
	
	public static String getfourRandonNum(){
		randomNumber = (random.nextInt(8999) + 1000);
		backNum = Integer.toString(randomNumber);
		return backNum;
	}
	
	public static void main(String[] args) {
		int i = 0;
		while (i<100) {
			i++;
			System.out.println(getfourRandonNum());
			
		}
		
	}
}
