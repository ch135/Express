package com.express.database.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.express.model.Coupon;
import com.express.model.InCash;
import com.express.model.Order;

public interface OrderDao {

	public List<Order> getUnfinshOrder(int first,String area);

	public List<Order> getOvertimeOrder(int first,String area);
	
	public Long getUnfinshOrderNum(String area);
	
	public Long getOvertimeOrderNum(String area);

	public void updateOrderStatus(String orderId, int orderStatus);

	public List<Order> getFinshOrder(int first,String area);
	
	public List<Order> gettimeFinshOrder(Map<String,Object> map);

	public Long getFinshOrderNum(String area);
	
	public Long gettimeFinshOrderNum(Map<String,Object> map);

	public List<Order> cuserGetOrder(String userId,int first,int maxResult);

	public Coupon findCoupon(String couponId);

	public boolean checkOrder(String userId);

	public List<Order> getOrderList(String id, int first);

	public Long getOrderCount(String area, String year, int i);

	List<InCash> findInCashByDate(String userId);
	
}
