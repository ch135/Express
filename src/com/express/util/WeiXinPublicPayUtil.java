package com.express.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.express.alipay.util.AlipayCore;
import com.express.model.WeChatPayDatas;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class WeiXinPublicPayUtil {

	public static String pay(String orderId, double price, String ip, String nonce_str, String notify_url, String openid) throws UnsupportedEncodingException, URIException {
		System.out.println("pay调用中");
		// String nonce_str = MD5Util.getUUID().replace("-", "");
		BigDecimal b1 = new BigDecimal(price + "");
		BigDecimal b2 = new BigDecimal("100");
		double total_fee = b1.multiply(b2).doubleValue();
		Map<String, String> map = new HashMap<String, String>();
		map.put("openid", openid);
		map.put("body", "快快送订单支付");
		map.put("nonce_str", nonce_str);// 随机字符串
		map.put("notify_url", notify_url);// 通知地址
		map.put("out_trade_no", orderId);// 商户订单号
		map.put("spbill_create_ip", ip);// 终端IP
		map.put("total_fee", ((int) total_fee) + "");// 总金额
		// map.put("total_fee", "1");//总金额
		map.put("trade_type", "JSAPI");// 交易类型
		map.put("appid", Constant.WEIXIN_WEB_ID_USER);// 小程序ID
		map.put("mch_id", Constant.WEIXIN_WEB_PID_USER);// 商户号
		String content = AlipayCore.createLinkString(map);
		String sign_encode = content + "&key=" + Constant.WEIXIN_WEB_KEY_USER;
		String sign = MD5Util.MD5Encode(sign_encode, "UTF-8");
		System.out.println(sign_encode);
		System.out.println(sign.toUpperCase());
		map.put("sign", sign.toUpperCase());

		System.out.println(XMLUtil.map2XmlString(map));

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("https://api.mch.weixin.qq.com/pay/unifiedorder");
		RequestEntity requestEntity = new StringRequestEntity(XMLUtil.map2XmlString(map), null, "UTF-8");
		method.setRequestEntity(requestEntity);
		client.getParams().setContentCharset("UTF-8");
		client.getParams().setConnectionManagerTimeout(10000);
		System.out.println("分割线--------------1");
		try {
			System.out.println("分割线--------------2");
			client.executeMethod(method);
			System.out.println("返回结果：" + method.getResponseBodyAsString());
			return method.getResponseBodyAsString();
		} catch (Exception e) {
			System.out.println("出错了！！！！！！！！！！！！！");
			e.printStackTrace();
		}
		System.out.println("验证出错了");
		return null;
	}

	public static WeChatPayDatas publicPay(String prepayid, String nonce_str, String openid) {
		WeChatPayDatas wDatas = new WeChatPayDatas();
		Long timeStamp = (System.currentTimeMillis() / 1000);
		Map<String, String> map = new HashMap<String, String>();
		wDatas.setNoncestr(nonce_str);
		wDatas.setPrepayId(prepayid);
		wDatas.setTimestamp(timeStamp.toString());
		wDatas.setPartnerId(Constant.WEIXIN_WEB_PID_USER);
		map.put("appid", Constant.WEIXIN_WEB_ID_USER);
		map.put("timestamp", timeStamp.toString());
		map.put("noncestr", nonce_str);
		map.put("prepayid", "prepayid:" + prepayid);
		map.put("signType", "MD5");
		String content = AlipayCore.createLinkString(map);
		String sign_encode = null;
		sign_encode = content + "&key=" + Constant.WEIXIN_WEB_KEY_USER;
		System.out.println("sign_encode" + sign_encode);
		String sign = MD5Util.MD5Encode(sign_encode, "UTF-8");
		System.out.println("sign:" + sign.toUpperCase());
		wDatas.setSign(sign.toUpperCase());
		return wDatas;
	}

	public static String getSign(Map<String, String> map) {
		String content = AlipayCore.createLinkString(map);
		String sign_encode = content + "&key=" + Constant.WEIXIN_WEB_KEY_USER;
		String sign = MD5Util.MD5Encode(sign_encode, "UTF-8");
		System.out.println("sign_encode:" + sign.toUpperCase());
		return sign.toUpperCase();
	}

	public static Map<String, String> codeToSession_key(String code) {
		Map<String, String> map = new HashMap<String, String>();
		HttpClient client = new HttpClient();
		client.getParams().setContentCharset("UTF-8");
		client.getParams().setConnectionManagerTimeout(10000);
		String url = "https://api.weixin.qq.com/sns/jscode2session?";
		url += "secret=" + Constant.WEIXIN_WEB_APPID_USER;
		url += "&appid=" + Constant.WEIXIN_WEB_ID_USER;
		url += "&js_code=" + code;
		url += "&grant_type=authorization_code";
		PostMethod postMethod = new PostMethod(url);
		try {
			client.executeMethod(postMethod);
			String response = postMethod.getResponseBodyAsString();
			System.out.println(response);
			Map<String, String> data = parseData(response);
			String error = data.get("errmsg");
			if (error != null) {
				map.put("error", "无法获取用户openid");
				System.out.println("无法获取用户openid");
			} else {
				String openid = data.get("openid");
				if (openid == null)
					openid = "";
				map.put("openid", openid);
			}
		} catch (IOException e) {
			map.put("error", "无法获取用户openid");
			e.printStackTrace();
		}
		return map;
	}

	// 将json转换为map
	private static Map<String, String> parseData(String data) {
		GsonBuilder gb = new GsonBuilder();
		Gson g = gb.create();
		Map<String, String> map = g.fromJson(data, new TypeToken<Map<String, String>>() {
		}.getType());
		return map;
	}

	@Test
	public void test1() {

	}

	@SuppressWarnings("deprecation")
	public static Map<String, String> ATM(String openid, double amount, String spbill_create_ip, String partner_trade_no, String role) throws Exception {
		if ("common".equals(role)) {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			String nonce_str = MD5Util.getUUID().replace("-", "");
			String result = "";
			String t = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			t = t.substring(0, t.lastIndexOf("classes"));
			FileInputStream instream = new FileInputStream(new File(t + "/cert/common/apiclient_cert.p12"));
			try {
				keyStore.load(instream, "1395691002".toCharArray());
			} finally {
				instream.close();
			}
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "1395691002".toCharArray()).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			try {
				HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers");
				System.out.println("executing request" + httpPost.getRequestLine());
				BigDecimal b1 = new BigDecimal(amount + "");
				BigDecimal b2 = new BigDecimal("100");
				double total_fee = b1.multiply(b2).doubleValue();
				Map<String, String> map = new HashMap<String, String>();
				map.put("mch_appid", Constant.WEIXIN_APP_ID_USER);
				map.put("mchid", Constant.WEIXIN_PID_USER);
				map.put("nonce_str", nonce_str);
				map.put("partner_trade_no", partner_trade_no);
				map.put("openid", openid);
				map.put("check_name", "NO_CHECK");
				map.put("amount", ((int) total_fee) + "");
				map.put("desc", "快快送提现");
				map.put("spbill_create_ip", spbill_create_ip);
				String content = AlipayCore.createLinkString(map);
				String sign_encode = content + "&key=" + Constant.WEIXIN_KEY_USER;
				String sign = MD5Util.MD5Encode(sign_encode, "UTF-8");
				map.put("sign", sign.toUpperCase());
				StringEntity reqEntity = new StringEntity(XMLUtil.map2XmlString(map), null, "UTF-8");
				httpPost.setEntity(reqEntity);
				CloseableHttpResponse response = httpclient.execute(httpPost);
				try {
					HttpEntity entity = response.getEntity();
					System.out.println(response.getStatusLine());
					if (entity != null) {
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
						String text;
						while ((text = bufferedReader.readLine()) != null) {
							result += text;
						}
						result = new String(result.getBytes("GBK"),"utf-8");
					}
					EntityUtils.consume(entity);
				} finally {
					response.close();
				}
			} finally {
				httpclient.close();
			}
			Map<String, String> map2 = XMLUtil.readStringXmlOut(result);
			return map2;
		} else {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			String nonce_str = MD5Util.getUUID().replace("-", "");
			String result = "";
			String t = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			t = t.substring(0, t.lastIndexOf("classes"));
			FileInputStream instream = new FileInputStream(new File(t + "/cert/courier/apiclient_cert.p12"));
			try {
				keyStore.load(instream, "1395685702".toCharArray());
			} finally {
				instream.close();
			}
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "1395685702".toCharArray()).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			try {
				HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers");
				System.out.println("executing request" + httpPost.getRequestLine());
				BigDecimal b1 = new BigDecimal(amount + "");
				BigDecimal b2 = new BigDecimal("100");
				double total_fee = b1.multiply(b2).doubleValue();
				Map<String, String> map = new HashMap<String, String>();
				map.put("mch_appid", Constant.WEIXIN_APP_ID_CUSER);
				map.put("mchid", Constant.WEIXIN_PID_CUSER);
				map.put("nonce_str", nonce_str);
				map.put("partner_trade_no", partner_trade_no);
				map.put("openid", openid);
				map.put("check_name", "NO_CHECK");
				map.put("amount", ((int) total_fee) + "");
				map.put("desc", "快快送提现");
				map.put("spbill_create_ip", spbill_create_ip);
				String content = AlipayCore.createLinkString(map);
				String sign_encode = content + "&key=" + Constant.WEIXIN_KEY_CUSER;
				String sign = MD5Util.MD5Encode(sign_encode, "UTF-8");
				map.put("sign", sign.toUpperCase());
				StringEntity reqEntity = new StringEntity(XMLUtil.map2XmlString(map), null, "UTF-8");
				httpPost.setEntity(reqEntity);
				CloseableHttpResponse response = httpclient.execute(httpPost);
				try {
					HttpEntity entity = response.getEntity();
					System.out.println(response.getStatusLine());
					if (entity != null) {
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
						String text;
						while ((text = bufferedReader.readLine()) != null) {
							result += text;
						}
						result = new String(result.getBytes("GBK"),"utf-8");
					}
					EntityUtils.consume(entity);
				} finally {
					response.close();
				}
			} finally {
				httpclient.close();
			}
			Map<String, String> map2 = XMLUtil.readStringXmlOut(result);
			return map2;
		}
	}
	
	
	@Test
	public void test11() throws UnsupportedEncodingException{
		String msg="<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[鍙傛暟閿欒:openid杈撳叆閿欒,璇锋鏌penid鏄惁灞炰簬璇ュ晢鎴峰彿]]></return_msg><result_code><![CDATA[FAIL]]></result_code><err_code><![CDATA[OPENID_ERROR]]></err_code><err_code_des><![CDATA[鍙傛暟閿欒:openid杈撳叆閿欒,璇锋鏌penid鏄惁灞炰簬璇ュ晢鎴峰彿]]></err_code_des></xml>";
		byte[] bs = msg.getBytes("GBK");
		String string = new String(bs,"UTF-8");
		System.out.println(string);
	}
}
