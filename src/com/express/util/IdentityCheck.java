package com.express.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;






import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class IdentityCheck {	
	/**
	 * @param httpUrl
	 *            :请求接口
	 * @param identity
	 *            :身份证号
	 * @return 返回结果
	 */
	public static String Check(String identity){
		String url = "http://apis.juhe.cn/idcard/index?key=b1243752946af4fb64a62b8a671f59cc&cardno="+identity;
		String json;
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		method.setRequestHeader("ContentType",
				"application/x-www-form-urlencoded;charset=UTF-8");
		client.getParams().setContentCharset("UTF-8");
		client.getParams().setConnectionManagerTimeout(10000);
		try {
			client.executeMethod(method);
			json =  method.getResponseBodyAsString();
			System.out.println(json);
		} catch (Exception e) {
			System.out.println("请求失败");
			return null;
		}
		
		JsonParser jp = new JsonParser();
		JsonObject object = (JsonObject) jp.parse(json);
		
		String sex = null;
		String resultcode = object.get("resultcode").getAsString();
		if(resultcode.equals("200")){
			JsonObject jobject = object.get("result").getAsJsonObject();
		    return jobject.get("sex").getAsString();
		}else {
			return null;
		}
	}


	

}
