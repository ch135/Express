package com.express.user.service;

import java.util.Map;

import com.express.model.APPVersion;
import com.express.model.CompanyMessage;

public interface APPService {

	public APPVersion findAppVersion(String appType);

	public String getWeixinJson(String code);

	public String getUerJson(String access_token, String openid);

	public CompanyMessage getAPPMsg();

	Map<String, String> getWeixinJsonForUser(String code, String role);

	Map<String, String> getWeixinMsg(String access_tooken, String openId, String role);
	
}
