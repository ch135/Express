package com.express.user.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

import com.baidu.mapapi.common.SysOSUtil;
import com.express.database.dao.AppMsgDao;
import com.express.database.dao.BaseDao;
import com.express.database.dao.impl.AppMsgDaoImpl;
import com.express.database.dao.impl.BaseDaoImpl;
import com.express.model.APPVersion;
import com.express.model.CompanyMessage;
import com.express.user.service.APPService;
import com.express.util.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;

public class APPServiceImpl implements APPService {
	AppMsgDao dao = new AppMsgDaoImpl();

	@Override
	public APPVersion findAppVersion(String appType) {
		return dao.getAppVersion(appType);
	}

	@Override
	public Map<String, String> getWeixinMsg(String access_tooken, String openId, String role) {
		ObjectMapper mapper = new ObjectMapper();
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("https://api.weixin.qq.com/sns/userinfo");
		NameValuePair[] data = { new NameValuePair("access_token", access_tooken), new NameValuePair("openid", openId), };
		method.addParameters(data);
		client.getParams().setContentCharset("UTF-8");
		client.getParams().setConnectionManagerTimeout(10000);
		try {
			client.executeMethod(method);
			@SuppressWarnings("unchecked")
			Map<String, String> map = mapper.readValue(method.getResponseBodyAsString(), Map.class);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<String, String> getWeixinJsonForUser(String code, String role) {
		ObjectMapper mapper = new ObjectMapper();
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("https://api.weixin.qq.com/sns/oauth2/access_token");
		String appId = "", appsecret = "";
		if ("common".equals(role)) {
			appId = Constant.WEIXIN_APP_ID_USER;
			appsecret = Constant.WEIXIN_SECRET_USER;
		} else {
			appId = Constant.WEIXIN_APP_ID_CUSER;
			appsecret = Constant.WEIXIN_SECRET_CURSR;
		}
		NameValuePair[] data = { new NameValuePair("appid", appId), new NameValuePair("secret", appsecret), new NameValuePair("code", code), new NameValuePair("grant_type", "authorization_code") };
		method.addParameters(data);
		client.getParams().setContentCharset("UTF-8");
		client.getParams().setConnectionManagerTimeout(10000);
		try {
			client.executeMethod(method);
			String string = method.getResponseBodyAsString();
			@SuppressWarnings("unchecked")
			Map<String, String> map = mapper.readValue(string, Map.class);
			String access_tooken = map.get("access_token");
			String openId = map.get("openid");
			Map<String, String> map2 = getWeixinMsg(access_tooken, openId, role);
			String unionid = map2.get("unionid");
			map.put("unionid", unionid);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getWeixinJson(String code) {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("https://api.weixin.qq.com/sns/oauth2/access_token");
		NameValuePair[] data = { new NameValuePair("appid", Constant.WEIXIN_GZH_APPID), new NameValuePair("secret", Constant.WEIXIN_SECRET), new NameValuePair("code", code), new NameValuePair("grant_type", "authorization_code") };
		method.addParameters(data);
		// String url =
		// "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
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

	@Override
	public String getUerJson(String access_token, String openid) {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("https://api.weixin.qq.com/sns/userinfo");
		NameValuePair[] data = { new NameValuePair("appid", Constant.WEIXIN_GZH_APPID), new NameValuePair("access_token", access_token), new NameValuePair("openid", openid), new NameValuePair("lang", "zh_CN") };
		method.addParameters(data);
		// String url =
		// "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
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

	@Override
	public CompanyMessage getAPPMsg() {
		BaseDao<CompanyMessage> dao = new BaseDaoImpl<CompanyMessage>();
		List<CompanyMessage> list = dao.getAll(new CompanyMessage());
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

}
