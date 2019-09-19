package com.express.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.alipay.api.internal.util.XmlUtils;
import com.express.alipay.util.AlipayCore;
import com.express.model.WeChatPayDatas;

public class WeiXinPayUtil {
	
	public static String pay(String orderId,double price,String ip,String nonce_str,String notify_url,String userType) throws UnsupportedEncodingException, URIException{
		System.out.println("pay调用中");
//		String nonce_str = MD5Util.getUUID().replace("-", "");
		BigDecimal b1 = new BigDecimal(price+"");  
	    BigDecimal b2 = new BigDecimal("100");  
	    double total_fee = b1.multiply(b2).doubleValue(); 
		Map<String, String> map = new HashMap<String, String>();
		map.put("body", "快快送订单支付");
		map.put("nonce_str", nonce_str);
		map.put("notify_url", notify_url);
		map.put("out_trade_no", orderId);
		map.put("spbill_create_ip", ip);
		map.put("total_fee", ((int)total_fee)+"");
		map.put("trade_type", "APP");
		if(userType.equals("0")){
			map.put("appid", Constant.WEIXIN_APP_ID_USER);
			map.put("mch_id", Constant.WEIXIN_PID_USER);
		}else {
			map.put("appid", Constant.WEIXIN_APP_ID_CUSER);
			map.put("mch_id", Constant.WEIXIN_PID_CUSER);
		}
		String content = AlipayCore.createLinkString(map);
		String sign_encode = null;
		if(userType.equals("0")){
			sign_encode = content+"&key="+Constant.WEIXIN_KEY_USER;
		}else{
		    sign_encode = content+"&key="+Constant.WEIXIN_KEY_CUSER;
		}
		String sign = MD5Util.MD5Encode(sign_encode, "UTF-8");
		System.out.println(sign_encode);	
		System.out.println(sign.toUpperCase());
		map.put("sign", sign.toUpperCase());
		
		System.out.println(XMLUtil.map2XmlString(map));
		
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("https://api.mch.weixin.qq.com/pay/unifiedorder");
		RequestEntity requestEntity = new StringRequestEntity(XMLUtil.map2XmlString(map),null,"UTF-8");
        method.setRequestEntity(requestEntity);
		client.getParams().setContentCharset("UTF-8");
		client.getParams().setConnectionManagerTimeout(10000);
		System.out.println("分割线--------------1");
			try {
				System.out.println("分割线--------------2");
				client.executeMethod(method);
				System.out.println("返回结果："+method.getResponseBodyAsString());
				return method.getResponseBodyAsString();
			} catch (Exception e) {
				System.out.println("出错了！！！！！！！！！！！！！");
				e.printStackTrace();
			}
			System.out.println("验证出错了");
		return null;
	}
	
	public static WeChatPayDatas appPay(String prepayid,String nonce_str,String userType){
		WeChatPayDatas wDatas = new WeChatPayDatas();
		Long timeStamp = (System.currentTimeMillis()/1000);
//		String noncestr = "4028d88157b3a82a0157b3a830020000";
		Map<String, String> map = new HashMap<String, String>();
		wDatas.setNoncestr(nonce_str);
		wDatas.setPrepayId(prepayid);
		wDatas.setTimestamp(timeStamp.toString());
		if(userType.equals("0")){
			wDatas.setPartnerId(Constant.WEIXIN_PID_USER);
			map.put("appid", Constant.WEIXIN_APP_ID_USER);
			map.put("partnerid", Constant.WEIXIN_PID_USER);
		}else{
			wDatas.setPartnerId(Constant.WEIXIN_PID_CUSER);
			map.put("appid", Constant.WEIXIN_APP_ID_CUSER);
			map.put("partnerid", Constant.WEIXIN_PID_CUSER);
		}
		map.put("noncestr", nonce_str);
		map.put("prepayid", prepayid);
		map.put("timestamp", timeStamp.toString());
		map.put("package", "Sign=WXPay");
		String content = AlipayCore.createLinkString(map);
		String sign_encode = null;
		if(userType.equals("0")){
			sign_encode = content+"&key="+Constant.WEIXIN_KEY_USER;
		}else{
			sign_encode = content+"&key="+Constant.WEIXIN_KEY_CUSER;
		}
		System.out.println("sign_encode"+sign_encode);
		String sign = MD5Util.MD5Encode(sign_encode, "UTF-8");
		System.out.println("sign:"+sign.toUpperCase());
		wDatas.setSign(sign.toUpperCase());
//		String sign_content = content+"&sign="+sign;
		return wDatas;
	}
	
	
	public static String getSign(Map<String, String> map){
		String content = AlipayCore.createLinkString(map);
		String sign_encode = content+"&key="+Constant.WEIXIN_KEY_USER;
		String sign = MD5Util.MD5Encode(sign_encode, "UTF-8");
		System.out.println("sign_encode:"+sign.toUpperCase());
		return sign.toUpperCase();
	}
	
	public static String getcSign(Map<String, String> map){
		String content = AlipayCore.createLinkString(map);
		String sign_encode = content+"&key="+Constant.WEIXIN_KEY_CUSER;
		String sign = MD5Util.MD5Encode(sign_encode, "UTF-8");
		System.out.println("sign_encode:"+sign.toUpperCase());
		return sign.toUpperCase();
	}
}
