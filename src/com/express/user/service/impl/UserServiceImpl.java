package com.express.user.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import com.express.alipay.util.AlipayCore;
import com.express.database.dao.AppMsgDao;
import com.express.database.dao.BaseDao;
import com.express.database.dao.CouponTypeDao;
import com.express.database.dao.OrderDao;
import com.express.database.dao.impl.AppMsgDaoImpl;
import com.express.database.dao.impl.BaseDaoImpl;
import com.express.database.dao.impl.CouponTypeDaoImpl;
import com.express.database.dao.impl.OrderDaoImpl;
import com.express.model.APPMsg;
import com.express.model.Coupon;
import com.express.model.CouponUtil;
import com.express.model.Expense;
import com.express.model.InCash;
import com.express.model.Order;
import com.express.model.ShareCoupon;
import com.express.model.ShareWeiXin;
import com.express.model.User;
import com.express.model.UserAdvice;
import com.express.model.UserIDcard;
import com.express.user.service.UserServiceDAO;
import com.express.util.Constant;
import com.express.util.HibernateUtil;
import com.express.util.MD5Util;
import com.express.util.ObjectUtil;
import com.express.util.XMLUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserServiceImpl implements UserServiceDAO {

	BaseDao<User> dao = new BaseDaoImpl<User>();
	BaseDao<Coupon> couponDao = new BaseDaoImpl<Coupon>();
	BaseDao<UserIDcard> dao1 = new BaseDaoImpl<UserIDcard>();
	BaseDao<Expense> dao2 = new BaseDaoImpl<Expense>();
	BaseDao<ShareWeiXin> shareWXDao = new BaseDaoImpl<ShareWeiXin>();
	BaseDao<ShareCoupon> shareBaseDao = new BaseDaoImpl<ShareCoupon>();
	CouponTypeDao couponTypeDao = new CouponTypeDaoImpl();
	OrderDao orderDao = new OrderDaoImpl();
	AppMsgDao appMsgDao = new AppMsgDaoImpl();
	private static double couponValue = -1;

	public boolean insertUser(User user) {

		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			session.save(user);
			transaction.commit();// 提交事务
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}

	}

	/**
	 * 将用户提交的身份信息保存到临时表,并修改认证状态
	 */
	@Override
	public void idAuth(UserIDcard userTemp) {
		if (dao1.getOne(userTemp, userTemp.getId()) != null) {
			dao1.update(userTemp);
		} else {
			dao1.save(userTemp);
		}
	}

	/**
	 * 通过手机查找用户
	 */
	@Override
	public User findUserByPhone(String mobile) {
		User user = null;
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "from User where mobile=?";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, mobile);
			query.setMaxResults(1);
			user = (User) query.uniqueResult();
			transaction.commit();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("找不到用户");
			return user;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}
	
	/**
	 * 通过unionId查找用户
	 */
	@Override
	public User findUnionIdByPhone(String unionId) {
		User user = null;
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "from User where unionId=?";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, unionId);
			query.setMaxResults(1);
			user = (User) query.uniqueResult();
			transaction.commit();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("找不到用户");
			return user;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}
	

	// 更新用户信息
	@Override
	public void updataUser(User user) {
		try {
			dao.updates(user);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过手机号和密码查找用户
	 */
	@Override
	public boolean findUser(String mobile, String pwd) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "from User where mobile=? and loginPassword=?";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, mobile);
			query.setParameter(1, pwd);
			List list = query.list();
			transaction.commit();
			System.out.println("查询的号码和密码:" + mobile + "------" + pwd + "------" + pwd);
			if (list.size() > 0) {
				System.out.println("密码正确");
				return true;
			} else {
				System.out.println("密码错误");
				return false;
			}
		} catch (Exception e) {
			System.out.println("登录出bug了~~~~~");
			e.printStackTrace();
			return false;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}

	/**
	 * 修改密码
	 */
	@Override
	public void reLoginPwd(String mobile, String pwd) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update User u set u.loginPassword=? where u.mobile = ?");
			query.setParameter(0, pwd);
			query.setParameter(1, mobile);
			query.executeUpdate();
			transaction.commit();// 提交事务
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}

	}

	/**
	 * 更换头像
	 */
	@Override
	public void changeIcon(User user) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update User u set u.path=? where u.mobile = ?");
			query.setParameter(0, user.getPath());
			query.setParameter(1, user.getMobile());
			query.executeUpdate();
			transaction.commit();// 提交事务
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}

	}

	@Override
	public void saveAdvice(UserAdvice entity) {
		BaseDao<UserAdvice> dao = new BaseDaoImpl<UserAdvice>();
		dao.save(entity);
	}

	@Override
	public User checkUser(User user) throws NoSuchFieldException {
		User user1 = dao.getByParams(user);
		return user1;
	}

	@Override
	public void rePayPwd(User user) throws NoSuchFieldException {
		dao.updates(user);
	}

	@Override
	public void rePayPwdByPhone(User user) throws NoSuchFieldException {
		System.out.println("======" + user.getMobile() + "======");
		User user1 = dao.getByParam("mobile", user.getMobile(), "User").get(0);
		user.setId(user1.getId());
		dao.updates(user);
	}

	@Override
	public List<Expense> findExpenses(Expense expense, int first) throws NoSuchFieldException {
		return dao2.getByParams(expense, first);
	}

	@Override
	public List<Order> getOrder(String id, int first) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "from Order where userid=? and orderStaus BETWEEN ? and ? order by requestDate desc";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, id);
			query.setParameter(1, 0);
			query.setParameter(2, 3);
			query.setFirstResult(first);
			query.setMaxResults(Constant.PAGE);
			List<Order> list = query.list();
			System.out.println("找到东西了长度为++++++++" + list.size());
			transaction.commit();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return null;
	}

	@Override
	public User getUser(String userId) {
		return dao.getOne(new User(), userId);
	}

	@Override
	public List<Order> getTwoOrder(String userId) {
		return orderDao.cuserGetOrder(userId, 0, 2);
	}

	@Override
	public String getCover() {
		APPMsg appMsg = appMsgDao.getAppCover();
		if (appMsg != null) {
			return appMsg.getCoverPath();
		}
		return null;
	}

	@Override
	public List<Coupon> getCoupon(String mobile) {
		couponTypeDao.delectCoupon();
		Coupon coupon = new Coupon();
		coupon.setMobile(mobile);
		return couponTypeDao.getUserCoupon(mobile);
	}

	@Override
	public void userAddCoupon(String mobile) {
		if (couponValue < 0) {
			couponValue = couponTypeDao.getCouponValue("registerCoupon");
		}
		Coupon coupon = new Coupon();
		coupon.setMobile(mobile);
		coupon.setValue(couponValue);
		coupon.setStatement("注册红包");
		coupon.setDate(new Date());
		couponDao.save(coupon);
		// for (int i = 0; i < 3; i++) {
		// couponDao.save(coupon);
		// }
	}

	@Override
	public void userGetCoupon(String mobile, double value) {
		Coupon coupon = new Coupon();
		coupon.setMobile(mobile);
		coupon.setValue(value);
		coupon.setStatement("分享红包");
		coupon.setDate(new Date());
		couponDao.save(coupon);
	}

	@Override
	public ShareWeiXin getShareWeiXin() {
		return shareWXDao.getAll(new ShareWeiXin()).get(0);
	}

	@Override
	public ShareCoupon checkShare(String mobile, String orderId) {
		ShareCoupon shareCoupon = new ShareCoupon();
		shareCoupon.setMobile(mobile);
		shareCoupon.setOrderId(orderId);
		try {
			ShareCoupon shareCoupon1 = shareBaseDao.getByParams(shareCoupon);
			return shareCoupon1;
		} catch (NoSuchFieldException e) {
			return null;
		}
	}

	@Override
	public void insertShareCoupon(String orderId, String mobile, double value) {
		ShareCoupon shareCoupon = new ShareCoupon();
		shareCoupon.setOrderId(orderId);
		shareCoupon.setMobile(mobile);
		shareCoupon.setValue(value);
		shareCoupon.setDate(new Date());
		shareBaseDao.save(shareCoupon);
	}

	@Override
	public void deleteUser(String userId) {
		User user = new User();
		user.setId(userId);
		dao.delete(user);
	}

	@Override
	public List<InCash> findInCashByDate(Date date) {
		
		return null;
	}

}
