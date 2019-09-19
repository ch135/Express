package com.express.database.dao;

import com.express.model.APPMsg;
import com.express.model.APPVersion;

public interface AppMsgDao{

	public APPVersion getAppVersion(String appType);

	public APPMsg getAppCover();
	
}
