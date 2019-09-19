package com.express.order.service;

import java.util.List;

import com.express.model.Coupon;
import com.express.model.CuserComment;
import com.express.model.Expense;
import com.express.model.Order;
import com.express.model.User;

public interface OrderService {

	public List<Order> rangeFindOrder(int first, Double receiveLongitude,Double receiveLatitude);
	
	public List<Order> findOrderById(int first,String Id,String paramKey);
	
	public void create(Order order);
	
	public boolean receive(Order order) throws NoSuchFieldException;
	
	public Order findOrder(Order order);
	
	public List<Order> getOrder(Order order,int first) throws NoSuchFieldException;
	
	public void saveGrade(CuserComment cuserComment);

	public void updateOrderStatus(Order order);

	public void rollBack(String userid,double orderFare);

	public void reduce(String courierid, int credit);

	public List<User> getCUser(double d, double e);

	public void saveRecord(Expense expense);

	public boolean checkUserAble(String userId);

	public boolean checkCUserAble(String userId);

	public Coupon checkCoupon(String couponId);

	public Coupon getCoupon(String couponId);

	public User findUser(String courierid);

	public void deleteCoupon(String couponId);

	public boolean checkTime(String userId);

	public void updateCUserAble(String userId);

	
}
