package com.express.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * 企业信使的短信验证码
 * 
 * @author LK
 *
 */
public class MsgUtil {

	public static String msg(String mobile, String news) {
		String URL_SEND = "http://112.74.130.54:5588/sms.aspx";// 请求的url
		String userid = "1376";// 企业id
		String account = "TR-YDHY";// 发送者账号
		String password = "TR-YDHY";// 发送者密码
		String sendTime = "";// 为空表示立即发送，定时发送格式2010-10-24 09:08:10
		String action = "send";// 固定为send
		String extno = "";// 请先询问配置的通道是否支持扩展子号，如果不支持，请填空。子号只能为数字，且最多5位数。
		String code = RandomUtil.getfourRandonNum();// 获得五位随机数验证码
		String content = Constant.MSG + news + code;// 短信的内容，内容需要UTF-8编码
		System.out.println("发送的短信为：" + content);

		NameValuePair[] data = { new NameValuePair("action", action), new NameValuePair("userid", userid), new NameValuePair("account", account), new NameValuePair("password", password), new NameValuePair("mobile", mobile), new NameValuePair("content", content),
				new NameValuePair("sendTime", sendTime), new NameValuePair("extno", extno) };

		doPost(URL_SEND, data);

		return code;
	}
	
	public static String sendMessage(String mobile, String content) {
		String URL_SEND = "http://112.74.130.54:5588/sms.aspx";// 请求的url
		String userid = "1376";// 企业id
		String account = "TR-YDHY";// 发送者账号
		String password = "TR-YDHY";// 发送者密码
		String sendTime = "";// 为空表示立即发送，定时发送格式2010-10-24 09:08:10
		String action = "send";// 固定为send
		String extno = "";// 请先询问配置的通道是否支持扩展子号，如果不支持，请填空。子号只能为数字，且最多5位数。
		content ="【乐优大联盟】您的验证码是："+content;// 短信的内容，内容需要UTF-8编码
		System.out.println("发送的短信为：" + content);
		NameValuePair[] data = { new NameValuePair("action", action), new NameValuePair("userid", userid), new NameValuePair("account", account), new NameValuePair("password", password), new NameValuePair("mobile", mobile), new NameValuePair("content", content),
				new NameValuePair("sendTime", sendTime), new NameValuePair("extno", extno) };
		String string = doPost(URL_SEND, data);
		return string;
	}
	

	public static void msg(String mobile, String news, String orderid) {
		String URL_SEND = "http://112.74.130.54:5588/sms.aspx";// 请求的url
		String userid = "1376";// 企业id
		String account = "TR-YDHY";// 发送者账号
		String password = "TR-YDHY";// 发送者密码
		String sendTime = "";// 为空表示立即发送，定时发送格式2010-10-24 09:08:10
		String action = "send";// 固定为send
		String extno = "";// 请先询问配置的通道是否支持扩展子号，如果不支持，请填空。子号只能为数字，且最多5位数。
		String content = Constant.MSG + news + orderid;// 短信的内容，内容需要UTF-8编码
		System.out.println("发送的短信为：" + content);

		NameValuePair[] data = { new NameValuePair("action", action), new NameValuePair("userid", userid), new NameValuePair("account", account), new NameValuePair("password", password), new NameValuePair("mobile", mobile), new NameValuePair("content", content),
				new NameValuePair("sendTime", sendTime), new NameValuePair("extno", extno) };

		doPost(URL_SEND, data);
	}

	public static String doPost(String url, NameValuePair[] data) {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");
		method.addParameters(data);
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

	public static void sendMsg(String mobile, String content) {
		String URL_SEND = "http://112.74.130.54:5588/sms.aspx";// 请求的url
		String userid = "1376";// 企业id
		String account = "TR-YDHY";// 发送者账号
		String password = "TR-YDHY";// 发送者密码
		String sendTime = "";// 为空表示立即发送，定时发送格式2010-10-24 09:08:10
		String action = "send";// 固定为send
		String extno = "";// 请先询问配置的通道是否支持扩展子号，如果不支持，请填空。子号只能为数字，且最多5位数。
		System.out.println("发送的短信为：" + content);
		NameValuePair[] data = { new NameValuePair("action", action), new NameValuePair("userid", userid), new NameValuePair("account", account), new NameValuePair("password", password), new NameValuePair("mobile", mobile), new NameValuePair("content", content),
				new NameValuePair("sendTime", sendTime), new NameValuePair("extno", extno) };
		doPost(URL_SEND, data);
	}

}
