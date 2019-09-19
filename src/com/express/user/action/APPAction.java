package com.express.user.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.support.DaoSupport;

import com.express.model.APPVersion;
import com.express.model.CompanyMessage;
import com.express.user.service.APPService;
import com.express.user.service.impl.APPServiceImpl;
import com.express.util.DateUtil;
import com.express.util.JsonUtil;
import com.express.util.MsgUtil;
import com.express.util.SuperAction;
import com.express.util.ValueUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class APPAction extends SuperAction {

	Map<String, String> dataMap = new HashMap<String, String>();
	APPService service = new APPServiceImpl();

	/**
	 * 获取版本信息
	 */
	public void getVersion() {
		String appType = request.getParameter("appType");
		if (ValueUtil.isNull(appType)) {
			dataMap.put("result", "缺少参数");
			try {
				JsonUtil.writeToResponse(dataMap);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		APPVersion appVersion = service.findAppVersion(appType);
		String version;
		String path;
		if (appVersion != null) {
			version = appVersion.getAppVersion();
			path = appVersion.getAppPath();
		} else {
			version = "1.0.0";
			path = "";
		}
		dataMap.put("version", version);
		dataMap.put("path", path);
		try {
			JsonUtil.writeToResponse(dataMap);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取服务器时间
	 */
	public void getServerTime() {
		dataMap.put("date", new Date().getTime() + "");
		try {
			JsonUtil.writeToResponse(dataMap);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 跳转到红包页面
	 */
	public String toCouponPage() {
		String code = request.getParameter("code");
		System.out.println("微信code:" + code);
		String json = service.getWeixinJson(code);
		JsonParser jp = new JsonParser();
		JsonObject object = (JsonObject) jp.parse(json);
		String access_token = object.get("access_token").getAsString();
		String openid = object.get("openid").getAsString();
		String userJson = service.getUerJson(access_token, openid);
		JsonParser parser = new JsonParser();
		JsonObject jb = (JsonObject) parser.parse(userJson);
		String headimgurl = jb.get("headimgurl").getAsString();
		String nickname = jb.get("nickname").getAsString();
		request.setAttribute("openid", openid);
		request.setAttribute("headimgurl", headimgurl);
		request.setAttribute("nickname", nickname);
		return "coupon";
	}

	/**
	 * 获取公司以及APP信息
	 */
	public void appMsg() {
		CompanyMessage companyMessage = service.getAPPMsg();
		try {
			String appMsg = JsonUtil.beanToJson(companyMessage);
			dataMap.put("appMsg", appMsg);
			JsonUtil.writeToResponse(dataMap);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage() {
		String mobile = request.getParameter("mobile");
		String content = request.getParameter("content");
		MsgUtil.sendMsg(mobile, content);
		dataMap.put("result", "success");
		try {
			JsonUtil.writeToResponse(dataMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
