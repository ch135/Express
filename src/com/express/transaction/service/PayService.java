package com.express.transaction.service;

import java.util.List;

import com.express.model.CompanyPaymentDetails;
import com.express.model.Expense;
import com.express.model.Order;
import com.express.model.User;

public interface PayService {
	public User checkPwd(User user) throws NoSuchFieldException;

	public void balancePay(User user) throws NoSuchFieldException;

	public void reOrderStatus(Order order);

	public Order getOrder(String orderId);

	public void record(Expense expense);

	public Expense withdraw(String userId, double value, String flag);

	public void createCMRecord(String orderId, String userId, String mobile, Double money, String payType);

	public boolean checkOrder(String userId);

	public void createRecord(String orderId, String userId, String name, double value, String record);

	public User getUser(String userId);

	public void creatRecord(String mobile, String account, String inCashType, double value, String name, Boolean result);

	Boolean checkInCash(String userId);
}
