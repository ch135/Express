package com.express.user.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.express.database.dao.AreaNormDao;
import com.express.database.dao.BaseDao;
import com.express.database.dao.CouponTypeDao;
import com.express.database.dao.OrderDao;
import com.express.database.dao.impl.AreaNormImpl;
import com.express.database.dao.impl.BaseDaoImpl;
import com.express.database.dao.impl.CouponTypeDaoImpl;
import com.express.database.dao.impl.OrderDaoImpl;
import com.express.model.AreaNorm;
import com.express.model.Coupon;
import com.express.model.Expense;
import com.express.model.Order;
import com.express.model.User;
import com.express.model.UserComplaint;
import com.express.user.service.ExpressService;
import com.express.util.RandomUtil;

public class ExpressServiceImpl implements ExpressService {

	BaseDao<User> userDao = new BaseDaoImpl<User>();
	BaseDao<Order> dao = new BaseDaoImpl<Order>();
	BaseDao<UserComplaint> ucBaseDao =new BaseDaoImpl<UserComplaint>();
	BaseDao<Expense> expenseDao = new BaseDaoImpl<Expense>();
	BaseDao<Coupon> couponDao = new BaseDaoImpl<Coupon>();
	CouponTypeDao couponTypeDao = new CouponTypeDaoImpl();
	OrderDao orderDao = new OrderDaoImpl();
	Order order = new Order();
	
	
	@Override
	public void complete(String orderId) {
		Order order2 = dao.getOne(order, orderId);
		order2.setFinshDate(new Date());
		order2.setOrderStaus(4);
		dao.update(order2);
	}


	@Override
	public void updateOrderStatus(Order order) {
		try {
			dao.updates(order);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}



	@Override
	public List<Order> getAllOrder(String userId,int first) {
		return orderDao.cuserGetOrder(userId, first, 15);
	}


	@Override
	public void updateUserOrderCount(String userId) {
		User user = userDao.getOne(new User(), userId);
		User user2 = new User();
		user2.setId(userId);
		user2.setOrder_count(user.getOrder_count()+1);
		int credit = user.getCredit();
		if((credit+5)<100){
			user2.setCredit(credit+5);
		}else{
			user2.setCredit(100);
		}
		try {
			userDao.updates(user2);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}


	@Override
	public User getUser(String id) {
		return userDao.getOne(new User(), id);
	}


	@Override
	public Order findOrder(String orderId) {
		return dao.getOne(new Order(), orderId);
	}


	@Override
	public void saveComplaint(String userId, String name, String cuserId,
			String cname, String orderId, String complaint) {
		UserComplaint uc = new UserComplaint();
		uc.setUserId(userId);
		uc.setName(name);
		uc.setCuserId(cuserId);
		uc.setCname(cname);
		uc.setOrderId(orderId);
		uc.setContent(complaint);
		uc.setDate(new Date());
		ucBaseDao.save(uc);
	}


	@Override
	public void cuserIncome(String cuserId, double orderFace) {
		User user = userDao.getOne(new User(), cuserId);
		BigDecimal money = new BigDecimal(user.getBalance()+orderFace);
		user.setBalance(money.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
		try {
			userDao.updates(user);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void saveRecord(Expense expense) {
		expenseDao.save(expense);
	}


	@Override
	public String createNewOrder(String orderId) {
		Order order = dao.getOne(new Order(), orderId);
		Long time = System.currentTimeMillis();
		String newOrderId = RandomUtil.getRandomNum()+time.toString()+RandomUtil.getRandomNum();
		order.setOrderId(newOrderId);
		order.setCgrade(0);
		order.setCmobile("");
		order.setCname("");
		order.setCourierid("");
		order.setCpath("");
		order.setOrderStaus(0);
		order.setRequestDate(new Date());
		dao.save(order);
		return newOrderId;
	}


	@Override
	public void userAddCoupon(String mobile) {
		Coupon coupon = new Coupon();
		coupon.setMobile(mobile);
		coupon.setValue(couponTypeDao.getCouponValue("awardCoupon"));
		coupon.setStatement("奖励红包");
		coupon.setDate(new Date());
		couponDao.save(coupon);
	}


	@Override
	public double getToll() {
		return couponTypeDao.getCouponValue("toll");
	}


	@Override
	public List<AreaNorm> getAreaPrice(String areaName) {
		AreaNormDao dao = new AreaNormImpl();
		return dao.getValue(areaName);
	}

}
