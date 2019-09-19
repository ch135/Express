package com.express.transaction.service;

import com.express.model.CompanyPaymentDetails;
import com.express.model.Expense;
import com.express.model.Order;
import com.express.model.UserChargeMoney;

public interface ReceiveService {
	
	public Order getOrder(String orderId);
	
	public void reOrderStatus(Order order);
	
	public void record(Expense expense);

	public UserChargeMoney getUser(String orderId);

	public void addBlance(String userId, double total_amount);

	public void createRecord(CompanyPaymentDetails cpd);
}
