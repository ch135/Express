package com.express.admin.service.impl;

import java.util.List;
import java.util.Map;

import com.express.admin.service.OrderService;
import com.express.database.dao.BaseDao;
import com.express.database.dao.OrderDao;
import com.express.database.dao.impl.BaseDaoImpl;
import com.express.database.dao.impl.OrderDaoImpl;
import com.express.model.Order;

/**
 * 订单管理
 * 
 */
public class OrderServiceImpl implements OrderService {
	
	BaseDao<Order> dao = new BaseDaoImpl<Order>();
	OrderDao orderDao = new OrderDaoImpl();
	/**
	 * 关闭订单
	 */
	@Override
	public void closeOrder(Order order) {
		Order order1 = new Order();
		order1 = dao.getOne(order, order.getOrderId());
		//-4代表管理员关闭订单
		order1.setOrderStaus(-4);
		dao.update(order1);
	}
	
	/**
	 * 查看单个订单
	 */
	@Override
	public Order findOrder(Order order) {
		return dao.getOne(order, order.getOrderId());
	}

	@Override
	public List<Order> getUnfinshOrder(int first,String area) {
		return orderDao.getUnfinshOrder(first,area);
	}

	@Override
	public Long getUnfinshOrderNum(String area) {
		return orderDao.getUnfinshOrderNum(area);
	}

	@Override
	public List<Order> getFinshOrder(int first,String area) {
		return orderDao.getFinshOrder(first,area);
	}

	@Override
	public Long getFinshOrderNum(String area) {
		return orderDao.getFinshOrderNum(area);
	}
	
	@Override
	public List<Order> getOrderByUserId(String userId) {
		return null;
	}

	@Override
	public List<Order> getOvertimeOrder(int first, String area) {
		return orderDao.getOvertimeOrder(first,area);
	}

	@Override
	public Long getOvertimeOrderNum(String area) {
		return orderDao.getOvertimeOrderNum(area);
	}

	/**
	 * @author chenhao
	 * @time 2017/6/9
	 * 根据时间姓名查找订单
	 */
	@Override
	public List<Order> gettimefinshOrder(Map<String, Object> map) {
		
		return orderDao.gettimeFinshOrder(map);
	}

	@Override
	public Long gettimeFinshOrderNum(Map<String, Object> map) {
		return orderDao.gettimeFinshOrderNum(map);
	}
}
