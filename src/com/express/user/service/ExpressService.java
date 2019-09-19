package com.express.user.service;

import java.util.List;

import com.express.model.AreaNorm;
import com.express.model.Expense;
import com.express.model.Order;
import com.express.model.User;

public interface ExpressService {
	
	public void complete(String orderId);

	public void updateOrderStatus(Order order);

	public List<Order> getAllOrder(String userId,int first);

	public void updateUserOrderCount(String userId);

	public User getUser(String id);

	public Order findOrder(String orderId);

	public void saveComplaint(String userId, String name, String cuserId,
			String cname, String orderId, String complaint);

	public void cuserIncome(String cuserId, double money);

	public void saveRecord(Expense expense);

	public String createNewOrder(String orderId);

	public void userAddCoupon(String sendMobile);

	public double getToll();

	public List<AreaNorm> getAreaPrice(String areaName);		
}
