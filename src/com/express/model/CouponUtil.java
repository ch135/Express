package com.express.model;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.stream.DoubleStream;

import com.express.database.dao.CouponTypeDao;
import com.express.database.dao.impl.CouponTypeDaoImpl;
import com.express.util.Constant;

public class CouponUtil {
	
	
	
	public static double getCouponValue(){
		if(Constant.MAX<0){
			CouponTypeDao couponTypeDao = new CouponTypeDaoImpl();
			Constant.MIN = couponTypeDao.getCouponValue("min");
			Constant.MAX = couponTypeDao.getCouponValue("max");
		}
		DecimalFormat df = new DecimalFormat("######0.00");  
		System.out.println(Constant.MIN+"||||"+Constant.MAX);
		double value = Math.random()*(Constant.MAX-Constant.MIN)+Constant.MIN;
		System.out.println("value"+value);
		return Double.valueOf(df.format(value));
	}
}
