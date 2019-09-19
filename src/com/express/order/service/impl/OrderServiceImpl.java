package com.express.order.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.sql.Update;

import com.express.database.dao.BaseDao;
import com.express.database.dao.CouponTypeDao;
import com.express.database.dao.OrderDao;
import com.express.database.dao.UserDao;
import com.express.database.dao.impl.BaseDaoImpl;
import com.express.database.dao.impl.CouponTypeDaoImpl;
import com.express.database.dao.impl.OrderDaoImpl;
import com.express.database.dao.impl.UserDaoImpl;
import com.express.model.Coupon;
import com.express.model.CuserComment;
import com.express.model.Expense;
import com.express.model.Order;
import com.express.model.User;
import com.express.order.service.OrderService;
import com.express.util.Constant;
import com.express.util.HibernateUtil;
import com.express.util.RandomUtil;

public class OrderServiceImpl implements OrderService{
	
	BaseDao<Order> dao = new BaseDaoImpl<Order>();
	BaseDao<CuserComment> dao2 = new BaseDaoImpl<CuserComment>();
	BaseDao<User> userDao = new BaseDaoImpl<User>();
	BaseDao<Expense> expenseDao = new BaseDaoImpl<Expense>();
	UserDao userDao2 = new UserDaoImpl();
	OrderDao orderDao = new OrderDaoImpl();
	CouponTypeDao couponTypeDao = new CouponTypeDaoImpl();
	
	/**
	 * 快递员查看可以接订单
	 */
	@Override
	public List<Order> rangeFindOrder(int first, Double receiveLongitude,Double receiveLatitude) {
		Transaction transaction=null;
		System.out.println(receiveLongitude);
		String hql="";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			//orderStaus=1代表订单已支付
			hql="from Order where  sendLongitude BETWEEN ? and ? and sendLatitude BETWEEN ? and ? and orderStaus =1 order by requestDate desc";
			transaction=session.beginTransaction();
			Query query=session.createQuery(hql);
			query.setParameter(0, receiveLongitude-0.05);
			query.setParameter(1, receiveLongitude+0.05);
			query.setParameter(2, receiveLatitude-0.05);
			query.setParameter(3, receiveLatitude+0.05);
			query.setFirstResult(first);
			query.setMaxResults(Constant.PAGE);
			List list=query.list();
			transaction.commit();
			System.out.println(list.size());
				return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally
		{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}

	/**
	 * 用户查看已下订单
	 */
	@Override
	public List<Order> findOrderById(int first,String Id,String paramKey) {
		System.out.println("用户查看已下订单");
		return dao.getByParam(first,paramKey, Id, Order.class.getSimpleName(),"requestDate");
	}

	/**
	 * 用户创建订单
	 */
	@Override
	public void create(Order order) {
		dao.save(order);
	}

	/**
	 * 快递员抢单
	 * @throws NoSuchFieldException 
	 */
	@Override
	public synchronized boolean receive(Order order) throws NoSuchFieldException {
		Order order1 = dao.getOne(order, order.getOrderId());
		//1是订单状态表示可以接单
		if(order1.getOrderStaus()==1){
			order.setReceiveDate(new Date());
			order.setOrderStaus(2);
			dao.updates(order);
			System.out.println("接单成功");
			return true;
		}
		System.out.println("订单已被接");
		return false;
	}

	/**
	 * 用户查看单个订单
	 */
	@Override
	public Order findOrder(Order order) {
		return dao.getOne(order, order.getOrderId());
	}

	/**
	 * 用户获取订单并分页
	 */
	@Override
	public List<Order> getOrder(Order order,int first) throws NoSuchFieldException {
		return dao.getOrder(order.getCourierid(), first);
	}
	
	/**
	 * 用户评分
	 */
	public void saveGrade(CuserComment cuserComment){
		String hql = "update CuserComment set ";
		CuserComment cuserComment1 = dao2.getOne(cuserComment, cuserComment.getCuserid());
		String pingjia = "";
		if(cuserComment.getAttitude()!=0){
			hql = hql +"attitude = attitude + 1,";
			pingjia = pingjia + "态度好 ×"+(cuserComment1.getAttitude()+1)+";";
		}else{
			pingjia = "态度好 ×"+cuserComment1.getAttitude()+";";
		}
		if(cuserComment.getSpeed()!=0){
			hql = hql +"speed = speed + 1,";
			pingjia = pingjia + "速度快 ×"+(cuserComment1.getSpeed()+1)+";";
		}else{
			pingjia = pingjia + "速度快 ×"+cuserComment1.getSpeed()+";";
		}
		if(cuserComment.getIntegrity()!=0){
			hql = hql +"integrity = integrity + 1,";
			pingjia = pingjia + "包裹完好 ×"+(cuserComment1.getIntegrity()+1)+";";
		}else{
			pingjia = pingjia + "包裹完好 ×"+cuserComment1.getIntegrity()+";";
		}
		if(cuserComment.getGood()!=0){
			hql = hql +"good = good + 1,";
			pingjia = pingjia + "好评 ×"+(cuserComment1.getGood()+1)+";";
		}else{
			pingjia = pingjia +"好评 ×"+cuserComment1.getGood()+";";
		}
		if(cuserComment.getOnTime()!=0){
			hql = hql +"onTime = onTime + 1,";
			pingjia = pingjia + "准时 ×"+(cuserComment1.getOnTime()+1)+";";
		}else{
			pingjia = pingjia +"准时 ×"+cuserComment1.getOnTime()+";";
		}
		hql = hql + "grade = (((grade*timce)+?)/(timce+1)),timce=timce+1 where cuserid = '"+cuserComment.getCuserid()+"'";
		User user = new User();
		user.setId(cuserComment.getCuserid());
		System.out.println("pingjia"+pingjia);
		user.setPingjia(pingjia);
		try {
			userDao.updates(user);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		dao2.updateCuserComment(hql, cuserComment.getGrade());
	}

	@Override
	public void updateOrderStatus(Order order) {
		try {
			dao.updates(order);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void rollBack(String userid,double orderFare){
		Expense expense = new Expense();
		User user = userDao.getOne(new User(),userid);
		BigDecimal money = new BigDecimal(user.getBalance()+orderFare);
		user.setBalance(money.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
		expense.setUserid(userid);
		expense.setMoney(orderFare);
		expense.setRecord("订单退款");
		expense.setDate(new Date());
		expense.setSpend(1);
		try {
			userDao.updates(user);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reduce(String courierid, int credit) {
		User user = userDao.getOne(new User(),courierid);
		user.setCredit(user.getCredit()-credit);
		try {
			userDao.updates(user);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<User> getCUser(double lat, double lon) {
		List<User> list = userDao2.getNearUser(lat+0.009,lat-0.009,lon+0.009,lon-0.009);
		if(list!=null){
			return list;
		}
		List<User> list1 = userDao2.getNearUser(lat+0.018,lat-0.018,lon+0.018,lon-0.018);
		if(list1!=null){
			return list1;
		}
		List<User> list2 = userDao2.getNearUser(lat+0.018,lat-0.018,lon+0.018,lon-0.018);
		if(list2!=null){
			return list2;
		}
		List<User> list3 = userDao2.getNearUser(lat+0.027,lat-0.027,lon+0.027,lon-0.027);
		if(list3!=null){
			return list3;
		}
		List<User> list4 = userDao2.getNearUser(lat+0.036,lat-0.036,lon+0.036,lon-0.036);
		if(list4!=null){
			return list4;
		}
		return null;
	}

	@Override
	public void saveRecord(Expense expense) {
		expenseDao.save(expense);
	}

	@Override
	public boolean checkUserAble(String userId) {
		User user = userDao.getOne(new User(), userId);
		if(user!=null){
			return user.getSend_able();
		}else{
			return false;
		}
	}

	@Override
	public boolean checkCUserAble(String userId) {
		User user = userDao.getOne(new User(), userId);
		if(user!=null){
			return user.getAccept_able();
		}else{
			return false;
		}
	}

	@Override
	public Coupon checkCoupon(String couponId) {
		return orderDao.findCoupon(couponId);
	}

	@Override
	public Coupon getCoupon(String couponId) {
		return orderDao.findCoupon(couponId);
	}

	@Override
	public User findUser(String courierid) {
		return userDao.getOne(new User(), courierid);
	}

	@Override
	public void deleteCoupon(String couponId) {
		couponTypeDao.deleteCoupon(couponId);
	}

	@Override
	public boolean checkTime(String userId) {
		User user = userDao.getOne(new User(), userId);
		long time = 0;
		if(user.getForbidTime()!=null){
			time = user.getForbidTime().getTime();
		}
		if(time>1000*60*60*24*7){
			return true;
		}
		return false;
	}

	@Override
	public void updateCUserAble(String userId) {
		User user = new User();
		user.setId(userId);
		user.setAccept_able(true);
		try {
			userDao.updates(user);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
}
