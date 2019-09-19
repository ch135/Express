package com.express.admin.service.impl;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.express.admin.service.UserService;
import com.express.database.dao.AreaNormDao;
import com.express.database.dao.BaseDao;
import com.express.database.dao.UserDao;
import com.express.database.dao.impl.AreaNormImpl;
import com.express.database.dao.impl.BaseDaoImpl;
import com.express.database.dao.impl.UserDaoImpl;
import com.express.model.AreaNorm;
import com.express.model.Order;
import com.express.model.User;
import com.express.model.UserAdvice;
import com.express.model.UserChargeMoney;
import com.express.model.UserIDcard;
import com.express.util.CityUtil;
import com.express.util.HibernateUtil;
import com.express.util.JsonUtil;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class UserServiceImpl implements UserService {

	BaseDao<UserAdvice> dao = new BaseDaoImpl<UserAdvice>();
	BaseDao<UserIDcard> dao1 = new BaseDaoImpl<UserIDcard>();
	BaseDao<User> dao2 = new BaseDaoImpl<User>();
	UserAdvice userAdvice = new UserAdvice();
	UserIDcard userTemp = new UserIDcard();
	UserDao userDao = new UserDaoImpl();
	User user = new User();
	
	/**
	 * 获取用户建议记录分页
	 */
	@Override
	public List<UserAdvice> findAdvice(int first) {
		return dao.getAll(userAdvice, first);
	}
	
	/**
	 * 更新用户认证状态
	 */
	@Override
	public void updateIdentity(UserIDcard userIDcard) {
		try {
			dao1.updates(userIDcard);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取所有审核信息
	 */
	@Override
	public List<UserIDcard> findAllUserIdentity(int first,String flag,String area) {
			List<UserIDcard> list = userDao.findUserCard(first, flag, area);
			System.out.println(list);
			return list;
	}

	@Override
	public Map<String, Integer> statistic(String cityName) throws JsonIOException, JsonSyntaxException, FileNotFoundException, UnsupportedEncodingException {
		List<String> cityList = CityUtil.getCityList(cityName);
		Map<String, Integer>map = new HashMap<String, Integer>();
		for (int i = 0; i < cityList.size(); i++) {
			map.put(cityList.get(i), dao2.amountCity(cityList.get(i)));
		}
		return map;
	}

	@Override
	public List<User> getAllUser(int first) {
		return dao2.getAll(new User(), first);
	
	}
	
	@Override
	public List<User> getAllUser(int first,String area) {
		return dao2.getAll(new User(), first,area);
	}

	@Override
	public List<User> getAllUser() {
		return dao2.getAll(new User());
	}
	
	@Override
	public List<User> getAllUser(String area) {
		return dao2.getAll(new User(),area);
	}


	@Override
	public User getUser(String mobile) {
		user.setMobile(mobile);
		try {
			return dao2.getByParams(user);
		} catch (NoSuchFieldException e) {
			System.out.println("查不到，失败了，返回空");
			return null;
		}
	}

	@Override
	public List getAllAdvice() {
		return dao.getAll(new UserAdvice());
	}

	@Override
	public List getAllUserIdentity() {
		return dao1.getAll(new UserIDcard());
	}

	@Override
	public UserIDcard getIdentity(String identity) {
		userTemp.setIdentity(identity);
		try {
			return dao1.getByParams(userTemp);
		} catch (NoSuchFieldException e) {
			System.out.println("查不到，失败了，返回空");
			return null;
		}
	}

	@Override
	public Integer findAllUserIdentity(String flag,String area) {
		return userDao.getUserCount(flag, area);
	}

	@Override
	public void changeUserAble(String mobile,boolean able) {
		System.out.println("soso");
		userDao.updateUserAble(mobile,able);
	}

	@Override
	public List<User> searchUser(String value) {
		//匹配是否是名字
		Pattern pattern = Pattern.compile("^[\u0391-\uFFE5]+$");
		Matcher matcher = pattern.matcher(value);
		List<User> list;
		if(matcher.matches()){
			System.out.println("名字");
			list = userDao.getUser("name",value);
			return list;
		}else{
			//匹配是否是非负数
			System.out.println("非负数");
			Pattern pattern2 = Pattern.compile("^^\\d+$");
			Matcher matcher2 = pattern2.matcher(value);
			if(matcher2.matches()){
				list = userDao.getUser("mobile",value);
				return list;
			}else {
				return null;
			}
		}
	}

	@Override
	public void setUserPass(String mobile, boolean pass) {
		userDao.setPass(mobile,pass);
	}

	@Override
	public List<AreaNorm> getAreaNorm() {
		AreaNormDao dao = new AreaNormImpl();
		return dao.getAreaAreaNorm();
	}

	@Override
	public boolean setAreaPrice(AreaNorm area) {
		AreaNormDao dao = new AreaNormImpl();
		return dao.setAreaPrice(area);
	}

	@Override
	public List<AreaNorm> getAreaPrice(String areaName) {
		AreaNormDao dao = new AreaNormImpl();
		return dao.getValue(areaName);
	}

	@Override
	public List<UserChargeMoney> getChargeRecord(int first,String area) {
		return userDao.getChargeRecord(first,area);
	}

	@Override
	public long getChargeRecordNum(String area) {
		return userDao.getChargeRecordNum(area);
	}

	@Override
	public List<UserChargeMoney> findUserCharge(String mobile) {
		return userDao.findUserCharge(mobile);
	}
	
	@Override
	public List<Order> findOrder(String userId,Integer page) {
		return userDao.findOrder(userId,page);
	}
	
	@Override
	public Integer findOrderCount(String userId) {
		return userDao.findOrderCount(userId);
	}

	@Override
	public User getUserByMoBile(String mobile) {
		return userDao.findUserByMobile(mobile);
	}

	@Override
	public List<AreaNorm> getAreaNorms(String name) {
		AreaNormDao dao = new AreaNormImpl();
		return dao.getAreaNorms(name);
	}

}
