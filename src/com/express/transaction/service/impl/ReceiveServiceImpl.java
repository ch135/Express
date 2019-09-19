package com.express.transaction.service.impl;

import java.math.BigDecimal;

import com.express.database.dao.BaseDao;
import com.express.database.dao.impl.BaseDaoImpl;
import com.express.model.CompanyPaymentDetails;
import com.express.model.Expense;
import com.express.model.Order;
import com.express.model.User;
import com.express.model.UserChargeMoney;
import com.express.transaction.service.ReceiveService;

public class ReceiveServiceImpl implements ReceiveService {

	BaseDao<Order> dao = new BaseDaoImpl<Order>();
	BaseDao<Expense> expeDao = new BaseDaoImpl<Expense>();
	BaseDao<UserChargeMoney> chargeDao = new BaseDaoImpl<UserChargeMoney>();
	BaseDao<User> userDao = new BaseDaoImpl<User>();
	BaseDao<CompanyPaymentDetails> cpdDao = new BaseDaoImpl<CompanyPaymentDetails>();
	
	@Override
	public Order getOrder(String orderId) {
			return dao.getOne(new Order(),orderId);
	}
	@Override
	public void reOrderStatus(Order order) {
		order.setOrderStaus(1);
		try {
			dao.updates(order);
			System.out.println("更新成功");
		} catch (NoSuchFieldException e) {
			System.out.println("更新失败");
			e.printStackTrace();
		}
	}
	@Override
	public void record(Expense expense) {
		expeDao.save(expense);
	}
	
	@Override
	public UserChargeMoney getUser(String orderId) {
		return chargeDao.getOne(new UserChargeMoney(),orderId);
	}
	
	@Override
	public void addBlance(String userId, double total_amount) {
		User user = userDao.getOne(new User(), userId);
		user.setId(userId);
		BigDecimal money = new BigDecimal(user.getBalance()+total_amount);
		user.setBalance(money.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
		try {
			userDao.updates(user);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void createRecord(CompanyPaymentDetails cpd) {
		cpdDao.save(cpd);
	}

}
