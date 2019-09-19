package com.express.admin.service;

import java.util.List;
import java.util.Map;

import com.express.model.Order;
import com.express.model.UserAdvice;


public interface OrderService {
	
	public void closeOrder(Order order);
	
	public Order findOrder(Order order);

	public List<Order> getUnfinshOrder(int first,String area);

	public List<Order> getOvertimeOrder(int first,String area);
	
	public Long getUnfinshOrderNum(String area);
	

	public Long getOvertimeOrderNum(String area);

	public List<Order> getFinshOrder(int first,String area);
	
	public List<Order> gettimefinshOrder(Map<String,Object> map);

	public Long getFinshOrderNum(String area);
	
	public Long gettimeFinshOrderNum(Map<String,Object> map);

	List<Order> getOrderByUserId(String userId);
}
