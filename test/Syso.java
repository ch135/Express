import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.ServletActionContext;
import org.apache.tomcat.jni.Time;

import com.alipay.api.AlipayApiException;
import com.express.alipay.sign.RSA;
import com.express.database.dao.BaseDao;
import com.express.database.dao.impl.BaseDaoImpl;
import com.express.model.AreaNorm;
import com.express.model.CouponUtil;
import com.express.model.User;
import com.express.user.service.ExpressService;
import com.express.user.service.impl.ExpressServiceImpl;
import com.express.util.AlipayUtil;
import com.express.util.CityUtil;
import com.express.util.Constant;
import com.express.util.DateUtil;
import com.express.util.IdentityCheck;
import com.express.util.MD5Util;
import com.express.util.MsgUtil;
import com.express.util.MsgUtil;
import com.express.util.ObjectUtil;
import com.express.util.PriceUtil;
import com.express.util.RandomUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;



public class Syso {
	private static double toll;//手续费
	ExpressService dao = new ExpressServiceImpl();
	
	private double a;
	private Date date;
//	private float c;
	public Date getDate() {
		return date;
	}
	public double getA() {
		return a;
	}
	public void setA(double a) {
		this.a = a;
	}
	public void setDate(Date date) {
		this.date = date;
	}
//	public float getC() {
//		return c;
//	}
//	public void setC(float c) {
//		this.c = c;
//	}
	
	public static int say(){
		return 0;
	}
	
	private static boolean isEn(String...re){
		for (int i = 0; i < re.length; i++) {
			if(re[i]==null){
				System.out.println("空值");
				return false;
			}
		}
		return true;
	}
	
//	public void main1(String[] args) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
//		Pattern pattern = Pattern.compile("^\\d{11}$");
//		Matcher matcher = pattern.matcher("15767978870");
//		boolean b= matcher.matches();
//		if(b){
//			System.out.println("yes");
//		}else{
//			System.out.println("no");
//		}
		/**
		 * double相乘
		 * @throws FileNotFoundException 
		 * @throws JsonSyntaxException 
		 * @throws JsonIOException 
		 */
//		Double a = 2.01;
//		Double b = 100.0;
//		BigDecimal b1 = new BigDecimal("2.01");  
//	    BigDecimal b2 = new BigDecimal("100");  
//	    double c = b1.multiply(b2).doubleValue(); 
//		System.out.println((int)c);
//		/**
//		 * 密码加密
//		 */
//		Date date = new Date();
//		String random = RandomUtil.getfindRandonNum()+date.getTime();
//		System.out.println(random);
//		System.out.println(MD5Util.getMD5(random));
////		
//		Random random = new Random();
//		System.out.println(random.doubles(2.2, 2.8));
//		
//	}
	
	public static void main(String[] args) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
//		BaseDao<AreaNorm> dao = new BaseDaoImpl<AreaNorm>();
//		AreaNorm areaNorm = new AreaNorm();
//		List<String> proList = CityUtil.getProList();
//		for (int i = 0; i < proList.size(); i++) {
//			String pro = proList.get(i);
//			//
//			if(!(pro.equals("北京")||pro.equals("上海")||pro.equals("重庆")||pro.equals("天津")||pro.equals("香港")||pro.equals("澳门")||pro.equals("台湾")||pro.equals("海外"))){
//				System.out.println(pro+"+++++++++++++");
//			List<String> list = CityUtil.getCityList(pro);
//			System.out.println(list.size()+"----------");
//				for (int j = 0; j < list.size(); j++) {
//					List<String> list2 = CityUtil.getCountyList(pro, list.get(j));
//					for (int k = 0; k < list2.size(); k++) {
//						System.out.println(list2.get(k));
//						areaNorm.setName(list2.get(k));
//						areaNorm.setValue(5);
//						dao.save(areaNorm);
//					}
//				}
//			}
//		}
//		List<String> list;
//		try {
//			list = CityUtil.getCountyList("广东","深圳");
//			for (int i = 0; i < list.size(); i++) {
//				System.out.println(list.get(i));
//			}
//		} catch (JsonIOException e) {
//			e.printStackTrace();
//		} catch (JsonSyntaxException e) {
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = dateFormater.parse( "2017-12-11 00:00:00" );
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(dateFormater.format(date));
	}


}
