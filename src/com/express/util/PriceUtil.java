package com.express.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.DefaultHttpParams;

import com.express.database.dao.AreaNormDao;
import com.express.database.dao.impl.AreaNormImpl;
import com.express.database.dao.impl.CouponTypeDaoImpl;
import com.express.model.AreaNorm;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PriceUtil {
	public static AreaNormDao dao = new AreaNormImpl();
	public static CouponTypeDaoImpl couDao=new CouponTypeDaoImpl();
	public static double orderPrice(double lat1,double lng1,double lat2,double lng2,double coupon,String area) throws ParseException{
		System.out.println("发件地:"+lat1+","+lng1);
		System.out.println("收件地:"+lat2+","+lng2);
		NameValuePair[] data = { 
				new NameValuePair("output", "json"),
				new NameValuePair("origins", lat1+","+lng1),
				new NameValuePair("destinations", lat2+","+lng2),
				new NameValuePair("mcode",Constant.BAIDU_MAP_MCODE),
				new NameValuePair("ak", Constant.BAIDU_MAP_AK)};
		String origins = lat1+","+lng1;
		String destinations = lat2+","+lng2;
		String results = doPost(Constant.BAIDU_MAP_URL,origins,destinations);
		System.out.println(results);
		JsonParser jP = new JsonParser();
        JsonObject jobj=jP.parse(results).getAsJsonObject();
        String status = jobj.get("status").getAsString();
        if(status.equals("0")){
        	JsonArray result = jobj.getAsJsonArray("result");
        	JsonObject js = result.get(0).getAsJsonObject();
        	JsonObject distanceJs = js.get("distance").getAsJsonObject();
        	double value = distanceJs.get("value").getAsDouble()/1000;
        	List<AreaNorm> areas = dao.getValue(area);
        	double startPrice=5.0;
        	SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        	String now=sdf.format(new Date());
        	long ns=sdf.parse(now).getTime();
        	for(AreaNorm ar :areas){
        		long start=ar.getStart_time().getTime();
        		long end=ar.getEnd_time().getTime();
        		if(start<=ns&&end>ns){
        			startPrice=ar.getValue();
        			break;
        		}
        	}
        	System.out.println("起始价格为========="+startPrice);
//        	double min = couDao.getCouponValue("min");
//        	double max = couDao.getCouponValue("max");
//        	if(coupon>max)coupon=max;
//        	if(coupon!=0&&coupon<min)coupon=min;
        	double money = startPrice - coupon;
        	DecimalFormat df= new DecimalFormat("######0.0");   
        	if(value>=3.0&&value<=8.0){
        		money = (value-3)*2+money;
        	}else if(value>8.0){
        		money = (value-8)*3+money;
        	}
        	if(money<0){
        		money=0;
        	}
        	money=Double.parseDouble(df.format(money));
        	BigDecimal money2 = new BigDecimal(money);
        	System.out.println(lat1+":"+lng1+":"+lat2+":"+lng2+":"+ coupon+":"+ area);
        	System.out.println("---------"+money2.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
        	return money2.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }else{
        	return 0;
        }
	}

	public static String doPost(String url, String origins,String destinations) {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod("http://api.map.baidu.com/routematrix/v2/driving?tactics=11&output=json&origins="+origins+"&destinations="+destinations+"&mcode="+Constant.BAIDU_MAP_MCODE+"&ak="+Constant.BAIDU_MAP_AK);
//		PostMethod method = new PostMethod("http://api.map.baidu.com/routematrix/v2/driving?tactics=11&output=json&origins=23.074582,114.42426&destinations=23.044911,114.433917&mcode=E7:12:35:0E:5D:CC:73:3E:B3:22:DE:8C:AA:60:CD:B8:20:32:45:FB;com.waibao.team.cityexpressforsend&ak=FUgUj6RqZQ8ly716WGSzzIjRu86Pomry");

		method.setRequestHeader("ContentType",
				"application/x-www-form-urlencoded;charset=UTF-8");
		client.getParams().setContentCharset("UTF-8");
		client.getParams().setConnectionManagerTimeout(10000);
		try {
			client.executeMethod(method);
			return method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static double getDistant(double lat1,double lng1,double lat2,double lng2){
		System.out.println("发件地:"+lat1+","+lng1);
		System.out.println("收件地:"+lat2+","+lng2);
		NameValuePair[] data = { 
				new NameValuePair("output", "json"),
				new NameValuePair("origins", lat1+","+lng1),
				new NameValuePair("destinations", lat2+","+lng2),
				new NameValuePair("mcode",Constant.BAIDU_MAP_MCODE),
				new NameValuePair("ak", Constant.BAIDU_MAP_AK)};
		String origins = lat1+","+lng1;
		String destinations = lat2+","+lng2;
		DefaultHttpParams.getDefaultParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
		String results = doPost(Constant.BAIDU_MAP_URL,origins,destinations);
		JsonParser jP = new JsonParser();
        JsonObject jobj=jP.parse(results).getAsJsonObject();
        String status = jobj.get("status").getAsString();
        if(status.equals("0")){
        	JsonArray result = jobj.getAsJsonArray("result");
        	JsonObject js = result.get(0).getAsJsonObject();
        	JsonObject distanceJs = js.get("distance").getAsJsonObject();
        	double value = distanceJs.get("value").getAsDouble()/1000;
        	return value;
        }else{
        	return 0.0;
        }
	}
}
