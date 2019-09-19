package com.express.database.dao;

import java.util.List;

import com.express.model.Order;
import com.express.model.User;
import com.express.model.UserChargeMoney;
import com.express.model.UserIDcard;

public interface UserDao {

	public Integer getUserCount(String flag,String area);
	
	List<User> getNearUser(double lat,double lat2 ,double lon, double lon2);

	void updateUserAble(String mobile, boolean able);

	public List<User> getUser(User user) throws NoSuchFieldException;

	void setPass(String mobile, boolean pass);
	
	public List<User> getUser(String object,String objectValue);

	long getMemberNum(String area);

	List<UserChargeMoney> getChargeRecord(int first,String area);

	long getChargeRecordNum(String area);

	List<UserChargeMoney> findUserCharge(String mobile);

	/**
	 * 获取审核信息
	 */
	public List<UserIDcard>findUserCard(int first,String flag,String area);
	
	List<Order> findOrder(String userid, Integer page);

	Integer findOrderCount(String userid);

	User findUserByMobile(String mobile);
}
