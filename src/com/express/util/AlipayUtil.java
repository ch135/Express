package com.express.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;







import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.express.alipay.sign.RSA;
import com.express.alipay.util.AlipayCore;
import com.express.model.AlipayBean;
import com.fasterxml.jackson.core.JsonProcessingException;

public class AlipayUtil {

	public static String pay(String orderId,String price,String notify_url) throws AlipayApiException, JsonProcessingException, UnsupportedEncodingException {
		String date = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
		AlipayBean alipayBean = new AlipayBean();
		alipayBean.setBody("快快送订单支付");
		alipayBean.setSubject("快快送订单支付");
		alipayBean.setOut_trade_no(orderId);
		alipayBean.setTimeout_express("60m");
		alipayBean.setTotal_amount(price);
		alipayBean.setSeller_id(Constant.PID);
		alipayBean.setProduct_code("QUICK_MSECURITY_PAY");
		String biz_content = JsonUtil.beanToJson(alipayBean);
		
		Map<String, String> alipay = new HashMap<String, String>();
		alipay.put("app_id", Constant.APP_ID);
		alipay.put("biz_content", biz_content);
		alipay.put("charset", "UTF-8");
		alipay.put("method", "alipay.trade.app.pay");
		alipay.put("notify_url", notify_url);
		alipay.put("sign_type", "RSA");
		alipay.put("timestamp", date);
		alipay.put("version", "1.0");
		String content = AlipayCore.createLinkString(alipay);
		
		String sign = RSA.sign(content, Constant.APP_PRIVATE_KEY, "UTF-8");
		
		String sign_encode = URLEncoder.encode(sign, "utf-8");
		
		
		Map<String, String> alipay1 = new HashMap<String, String>();
		alipay1.put("app_id", URLEncoder.encode(Constant.APP_ID, "utf-8"));
		alipay1.put("biz_content",URLEncoder.encode(biz_content, "utf-8") );
		alipay1.put("charset", URLEncoder.encode("UTF-8", "utf-8"));
		alipay1.put("method", URLEncoder.encode("alipay.trade.app.pay", "utf-8"));
		alipay1.put("notify_url", URLEncoder.encode(notify_url, "utf-8"));
		alipay1.put("sign_type", URLEncoder.encode("RSA", "utf-8"));
		alipay1.put("timestamp", URLEncoder.encode(date, "utf-8"));
		alipay1.put("version", URLEncoder.encode("1.0", "utf-8"));
		String alipay_encode = AlipayCore.createLinkString(alipay1);
		String content_sign = alipay_encode+"&sign="+sign_encode;
		return content_sign;
		
		
	}
	
	public static String content(String orderId,String price) throws JsonProcessingException{
		AlipayBean alipayBean = new AlipayBean();
		alipayBean.setBody("同城快递支付");
		alipayBean.setSubject("订单支付");
		alipayBean.setOut_trade_no(orderId);
		alipayBean.setTimeout_express("60m");
		alipayBean.setTotal_amount(price);
		alipayBean.setProduct_code("QUICK_MSECURITY_PAY");
		String biz_content = JsonUtil.beanToJson(alipayBean);
		System.out.println(biz_content);
		return biz_content;
	}
}
