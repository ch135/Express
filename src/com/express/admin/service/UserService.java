package com.express.admin.service;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.express.model.AreaNorm;
import com.express.model.Order;
import com.express.model.User;
import com.express.model.UserAdvice;
import com.express.model.UserChargeMoney;
import com.express.model.UserIDcard;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public interface UserService {
	
	public List<UserAdvice> findAdvice(int first);
	
	public void updateIdentity(UserIDcard UserIDcard);
	
	public List<UserIDcard> findAllUserIdentity(int first,String flag,String area);
	
	public Integer findAllUserIdentity(String flag,String area);
	
	public Map<String, Integer> statistic(String cityName) throws JsonIOException, JsonSyntaxException, FileNotFoundException, UnsupportedEncodingException;

	public List<User> getAllUser(int first);
	
	public List<User> getAllUser(int first,String area);
	
	public List<User> getAllUser();
	
	public List<User> getAllUser(String area);

	public User getUser(String identity);

	public List<UserAdvice> getAllAdvice();

	public List<UserIDcard> getAllUserIdentity();

	public UserIDcard getIdentity(String identity);

	public void changeUserAble(String mobile,boolean able);

	public List<User> searchUser(String value);

	public void setUserPass(String mobile, boolean pass);

	public List<AreaNorm> getAreaNorm();

	public List<AreaNorm> getAreaNorms(String name);
	
	public boolean setAreaPrice(AreaNorm area);

	public List<AreaNorm> getAreaPrice(String areaName);

	public List<UserChargeMoney> getChargeRecord(int first,String area);

	public long getChargeRecordNum(String area);

	public List<UserChargeMoney> findUserCharge(String mobile);

	List<Order> findOrder(String userId, Integer page);

	Integer findOrderCount(String userId);

	public User getUserByMoBile(String mobile);
	
}
