package com.express.transaction.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.express.database.dao.BaseDao;
import com.express.database.dao.OrderDao;
import com.express.database.dao.impl.BaseDaoImpl;
import com.express.database.dao.impl.OrderDaoImpl;
import com.express.model.CompanyPaymentDetails;
import com.express.model.Expense;
import com.express.model.InCash;
import com.express.model.Order;
import com.express.model.User;
import com.express.model.UserChargeMoney;
import com.express.transaction.service.PayService;
import com.express.user.action.ExpressAction;
import com.express.user.service.ExpressService;
import com.express.user.service.UserServiceDAO;
import com.express.user.service.impl.ExpressServiceImpl;
import com.express.user.service.impl.UserServiceImpl;
import com.express.util.Constant;
import com.express.util.RandomUtil;

public class PayServiceImpl implements PayService {

	UserServiceDAO dao = new UserServiceImpl();
	BaseDao<User> dao1 = new BaseDaoImpl<User>();
	BaseDao<Order> orderDao = new BaseDaoImpl<Order>();
	BaseDao<Expense> expeDao = new BaseDaoImpl<Expense>();
	BaseDao<InCash> inCashBaseDao = new BaseDaoImpl<InCash>();
	BaseDao<UserChargeMoney> chargeDao = new BaseDaoImpl<UserChargeMoney>();
	BaseDao<CompanyPaymentDetails> cpdDao = new BaseDaoImpl<CompanyPaymentDetails>();
	OrderDao orderDao2 = new OrderDaoImpl();

	@Override
	public User checkPwd(User user) throws NoSuchFieldException {
		return dao.checkUser(user);
	}

	@Override
	public void balancePay(User user) throws NoSuchFieldException {
		dao1.updates(user);
	}

	@Override
	public void reOrderStatus(Order order) {
		try {
			orderDao.updates(order);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Order getOrder(String orderId) {
		return orderDao.getOne(new Order(), orderId);
	}

	@Override
	public void record(Expense expense) {
		expeDao.save(expense);
	}

	@Override
	public Expense withdraw(String userId, double value, String flag) {
		User user = dao1.getOne(new User(), userId);
		if (value <= user.getBalance()) {
			Expense expense = new Expense();
			user.setBalance(user.getBalance() - value);
			expense.setMoney(value);
			if (flag.equals("WeiXin")) {
				expense.setRecord("提现到微信");
			} else if (flag.equals("AliPay")) {
				expense.setRecord("提现到支付宝");
			} else {
				expense.setRecord("提现到银行卡");
			}
			expense.setUserid(userId);
			expense.setSpend(0);
			expense.setDate(new Date());
			dao1.update(user);
			expeDao.save(expense);
			return expense;
		} else {
			return null;
		}
	}
	
//	if (value <= user.getBalance()) {
//		User user2 = new User();
//		Expense expense = new Expense();
//		user2.setId(userId);
//		user2.setBalance(user.getBalance() - value);
//		expense.setMoney(value);
//		if (flag.equals("WeiXin")) {
//			expense.setRecord("提现到微信");
//		} else if (flag.equals("AliPay")) {
//			expense.setRecord("提现到支付宝");
//		} else {
//			expense.setRecord("提现到银行卡");
//		}
//		expense.setUserid(userId);
//		expense.setSpend(0);
//		expense.setDate(new Date());
//		try {
//			dao1.updates(user2);
//			expeDao.save(expense);
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		}
//		return expense;
//	} else {
//		return null;
//	}

	@Override
	public void creatRecord(String mobile, String account, String inCashType, double value, String name, Boolean result) {
		InCash inCash = new InCash();
		inCash.setId(RandomUtil.getRandomNum() + System.currentTimeMillis() + RandomUtil.getRandomNum());
		inCash.setMobile(mobile);
		inCash.setName(name);
		inCash.setInCashType(inCashType);
		inCash.setDate(new Date());
		inCash.setAccount(account);
		inCash.setBalance(value);
		inCash.setResults(result);
		inCashBaseDao.save(inCash);
	}

	@Override
	public void createCMRecord(String orderId, String userId, String mobile, Double money, String payType) {
		UserChargeMoney chargeMoney = new UserChargeMoney();
		chargeMoney.setId(orderId);
		chargeMoney.setUserId(userId);
		chargeMoney.setMobile(mobile);
		chargeMoney.setMoney(money);
		chargeMoney.setChargeType(payType);
		chargeMoney.setDate(new Date());
		chargeDao.save(chargeMoney);
	}

	@Override
	public boolean checkOrder(String userId) {
		return orderDao2.checkOrder(userId);
	}

	@Override
	public void createRecord(String orderId, String userId, String name, double value, String record) {
		CompanyPaymentDetails cpd = new CompanyPaymentDetails();
		if (Constant.TOLL <= 0) {
			ExpressService dao = new ExpressServiceImpl();
			Constant.TOLL = dao.getToll();
		}
		BigDecimal money = new BigDecimal(value * Constant.TOLL);
		cpd.setOrderId(orderId);
		cpd.setUserId(userId);
		cpd.setName(name);
		cpd.setMoney(money.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		cpd.setRecord(record);
		cpd.setDate(new Date());
		cpdDao.save(cpd);
	}

	@Override
	public User getUser(String userId) {
		return dao1.getOne(new User(), userId);
	}
	
	
	@Override
	public Boolean checkInCash(String mobile) {
		List<InCash> list = orderDao2.findInCashByDate(mobile);
		if(list.size()>=1)return true;
		return false;
	}

}
