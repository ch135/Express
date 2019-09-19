package com.express.database.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.express.database.dao.OrderDao;
import com.express.model.Coupon;
import com.express.model.InCash;
import com.express.model.Order;
import com.express.util.Constant;
import com.express.util.DateUtil;
import com.express.util.HibernateUtil;

public class OrderDaoImpl implements OrderDao {

	@Override
	public List<Order> getUnfinshOrder(int first,String area) {
		Transaction transaction = null;
		String hql = "";
		List<Order> list=null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if(area==null||"".equals(area))
			{
				hql = "from Order where orderStaus not BETWEEN ? and ? order by requestDate desc";
				Query query = session.createQuery(hql);
				query.setParameter(0, 4);
				query.setParameter(1, 5);
				query.setFirstResult(first);
				query.setMaxResults(10);
				list= query.list();
			}else{
				hql="select  * from order_record where sendAddress like ?  and orderStaus not in (?,?) order by requestDate desc";
				SQLQuery query = session.createSQLQuery(hql).addEntity(Order.class);
				query.setParameter(0, "%"+area+"%");
				query.setParameter(1, 4);
				query.setParameter(2, 5);
				query.setFirstResult(first);
				query.setMaxResults(10);
				list= query.list();
			}
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
	public Long getUnfinshOrderNum(String area) {
		Transaction transaction = null;
		String hql = "";
		Long count=0L;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if(area==null||"".equals(area))
			{
				hql = "select count(*) from Order where orderStaus not BETWEEN ? and ?";
				Query query = session.createQuery(hql);
				query.setParameter(0, 4);
				query.setParameter(1, 5);
				count = (Long) query.uniqueResult();
			}else{
				hql = "select  count(*) from Order where sendAddress like ?  and orderStaus not in (?,?) order by requestDate desc";
				Query query = session.createQuery(hql);
				query.setParameter(0, "%"+area+"%");
				query.setParameter(1, 4);
				query.setParameter(2, 5);
				count = (Long) query.uniqueResult();
			}
			transaction.commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return (long) 0;
	}

	@Override
	public void updateOrderStatus(String orderId, int orderStatus) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "update Order set orderstaus = ?,requestDate = ? where orderid = ? ";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, orderStatus);
			query.setParameter(1, new Date());
			query.setParameter(2, orderId);
			query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			System.out.println("回滚了！！！！！！！！！！");
			transaction.rollback();
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}

	@Override
	public List<Order> getFinshOrder(int first,String area) {
		Transaction transaction = null;
		String hql = "";
		List<Order> list=null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if(area==null||"".equals(area)){
				hql = "from Order where orderStaus BETWEEN ? and ? order by requestDate desc";
				Query query = session.createQuery(hql);
				query.setParameter(0, 4);
				query.setParameter(1, 5);
				query.setFirstResult(first);
				query.setMaxResults(10);
				list = query.list();
			}else{
				hql="select  * from order_record where sendAddress like ?  and orderStaus in (?,?) order by requestDate desc";
				SQLQuery query = session.createSQLQuery(hql).addEntity(Order.class);
				query.setParameter(0, "%"+area+"%");
				query.setParameter(1, 4);
				query.setParameter(2, 5);
				query.setFirstResult(first);
				query.setMaxResults(10);
				list= query.list();
			}
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
	public Long getFinshOrderNum(String area) {
		Transaction transaction = null;
		String hql = "";
		Long count =0L;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if(area==null||"".equals(area))
			{
				hql = "select count(*) from Order where orderStaus BETWEEN ? and ?";
				Query query = session.createQuery(hql);
				query.setParameter(0, 4);
				query.setParameter(1, 5);
				count = (Long) query.uniqueResult();
			}else{
				hql="select count(*) from order_record where sendAddress like ?  and orderStaus in (?,?) order by requestDate desc";
				SQLQuery query = session.createSQLQuery(hql);
				query.setParameter(0, "%"+area+"%");
				query.setParameter(1, 4);
				query.setParameter(2, 5);
				count = Long.parseLong( query.uniqueResult().toString());
			}
			transaction.commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return (long) 0;
	}

	@Override
	public List<Order> cuserGetOrder(String userId, int first, int maxResult) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "from Order where courierid = ? and orderStaus = ? order by finshDate desc";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, userId);
			query.setParameter(1, 5);
			query.setFirstResult(first);
			query.setMaxResults(maxResult);
			List<Order> list = query.list();
			transaction.commit();
			return list;
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return null;
	}

	@Override
	public Coupon findCoupon(String couponId) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "from Coupon where id = ?";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, couponId);
			List<Coupon> list = query.list();
			transaction.commit();
			if (list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}

	@Override
	public boolean checkOrder(String userId) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "from Order where courierid = ? and orderStaus BETWEEN ? and ?";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, userId);
			query.setParameter(1, 2);
			query.setParameter(2, 3);
			List<Order> list = query.list();
			transaction.commit();
			if (list.size() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}

	@Override
	public List<Order> getOrderList(String id, int first) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "from Order where userid = ? and orderStaus between ? and ? order by requestDate desc";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, id);
			query.setParameter(1, 0);
			query.setParameter(2, 4);
			query.setFirstResult(first);
			query.setMaxResults(15);
			List<Order> list = query.list();
			transaction.commit();
			if (list.size() > 0) {
				return list;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}

	@Override
	public Long getOrderCount(String area, String year, int i) {
		String month = null;
		if (i < 10) {
			month = "0" + i;
		} else {
			month = i + "";
		}
		Date date = DateUtil.stringToDate(year + "-" + month + "-00", "yyyy-MM-dd");
		Date lastdate = DateUtil.stringToDate(year + "-" + month + "-31", "yyyy-MM-dd");
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "select count(*) from Order where requestDate BETWEEN '" + DateUtil.getDateFormat(date, "yyyy-MM-dd") + "' and '" + DateUtil.getDateFormat(lastdate, "yyyy-MM-dd")
					+ "' and orderStaus BETWEEN ? and ? and sendAddress like '%" + area + "%'";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, 4);
			query.setParameter(1, 5);
			Long count = (Long) query.uniqueResult();
			transaction.commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return (long) 0;
	}
	
	@Override
	public List<InCash> findInCashByDate(String mobile) {
		List<InCash>list=new ArrayList<InCash>();
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "from InCash where date between ? and ? and mobile=?";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			query.setParameter(0, calendar.getTime());
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			query.setParameter(1, calendar.getTime());
			query.setParameter(2,mobile);
			list= query.list();
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
	public List<Order> getOvertimeOrder(int first, String area) {
		Transaction transaction = null;
		String hql = "";
		List<Order> list=null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if(area==null||"".equals(area))
			{
				hql = "from Order where orderStaus=-3 order by requestDate desc";
				Query query = session.createQuery(hql);
				query.setFirstResult(first);
				query.setMaxResults(10);
				list= query.list();
			}else{
				hql="select * from order_record where sendAddress like ?  and orderStaus=-3 order by requestDate desc";
				SQLQuery query = session.createSQLQuery(hql).addEntity(Order.class);
				query.setParameter(0, "%"+area+"%");
				query.setFirstResult(first);
				query.setMaxResults(10);
				list= query.list();
			}
			System.out.println("找到超时单了长度为++++++++" + list.size());
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
	public Long getOvertimeOrderNum(String area) {
		Transaction transaction = null;
		String hql = "";
		Long count=0L;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if(area==null||"".equals(area))
			{
				hql = "select count(*) from Order where orderStaus=?";
				Query query = session.createQuery(hql);
				query.setParameter(0, -3);
				count = (Long) query.uniqueResult();
			}else{
				hql = "select  count(*) from Order where sendAddress like ?  and orderStaus=? order by requestDate desc";
				Query query = session.createQuery(hql);
				query.setParameter(0, "%"+area+"%");
				query.setParameter(1, -3);
				count = (Long) query.uniqueResult();
			}
			transaction.commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return (long) 0;
	}

	@Override
	public List<Order> gettimeFinshOrder(Map<String, Object> map) {
		Transaction transaction = null;
		String hql = "";
		List<Order> list=null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if(map.get("area")==null||"".equals(map.get("area"))){
				hql = "from Order where orderStaus in (?,?) and cname=? and finshDate BETWEEN ? and ? order by requestDate desc";
				Query query = session.createQuery(hql);
				query.setParameter(0, 4);
				query.setParameter(1, 5);
				query.setParameter(2, map.get("name"));
				query.setParameter(3, map.get("start"));
				query.setParameter(4, map.get("end"));
				query.setFirstResult((int)map.get("number"));
				query.setMaxResults(10);
				list = query.list();
			}else{
				hql="select  * from order_record where sendAddress like ?  and orderStaus in (?,?) and cname=? and finshDate BETWEEN ? and ? order by requestDate desc";
				SQLQuery query = session.createSQLQuery(hql).addEntity(Order.class);
				query.setParameter(0, "%"+map.get("area")+"%");
				query.setParameter(1, 4);
				query.setParameter(2, 5);
				query.setParameter(3, map.get("name"));
				query.setParameter(4, map.get("start"));
				query.setParameter(5, map.get("end"));
				query.setFirstResult((int)map.get("number"));
				query.setMaxResults(10);
				list= query.list();
			}
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
	public Long gettimeFinshOrderNum(Map<String, Object> map) {
		Transaction transaction = null;
		String hql = "";
		Long count =0L;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if(map.get("area")==null||"".equals(map.get("area"))){
				hql = "select count(*) from Order where orderStaus in (?,?) and cname=? and finshDate BETWEEN ? and ?";
				Query query = session.createQuery(hql);
				query.setParameter(0, 4);
				query.setParameter(1, 5);
				query.setParameter(2, map.get("name"));
				query.setParameter(3, map.get("start"));
				query.setParameter(4, map.get("end"));
				count = (Long) query.uniqueResult();
			}else{
				hql="select count(*) from order_record where sendAddress like ? and orderStaus in (?,?) and cname=? and finshDate BETWEEN ? and ?";
				SQLQuery query = session.createSQLQuery(hql);
				query.setParameter(0, "%"+map.get("area")+"%");
				query.setParameter(1, 4);
				query.setParameter(2, 5);
				query.setParameter(3, map.get("name"));
				query.setParameter(4, map.get("start"));
				query.setParameter(5, map.get("end"));
				count = Long.parseLong( query.uniqueResult().toString());
			}
			transaction.commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return (long) 0;
	}
}
