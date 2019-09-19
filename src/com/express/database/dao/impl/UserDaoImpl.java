package com.express.database.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import com.express.database.dao.UserDao;
import com.express.model.APPVersion;
import com.express.model.Order;
import com.express.model.User;
import com.express.model.UserChargeMoney;
import com.express.model.UserIDcard;
import com.express.util.HibernateUtil;
import com.express.util.ObjectUtil;

public class UserDaoImpl implements UserDao {
	@Override
	public Integer getUserCount(String flag,String area){
		Transaction transaction=null;
		String hql="";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			hql="select count(*) from User where  address like '%"+area+"%' and state="+flag;
			transaction=session.beginTransaction();
			Query query=session.createQuery(hql);
			Long count =  (Long) query.uniqueResult();
			transaction.commit();
			return count.intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
		return 0;
	}
	
	@Override
	public List<User> getNearUser(double lat, double lat2,double lon, double lon2) {
		Transaction transaction=null;
		String hql="";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			hql="from User where longitude BETWEEN ? and ? and latitude BETWEEN ? and ? and role=?";
			transaction=session.beginTransaction();
			Query query=session.createQuery(hql);
			query.setParameter(0, lon2);
			query.setParameter(1, lon);
			query.setParameter(2, lat2);
			query.setParameter(3, lat);
			query.setParameter(4, "courier");
			List<User> list=query.list();
			transaction.commit();
			if(list.size()>0){
				return list;
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
		return null;
	}

	@Override
	public void updateUserAble(String mobile, boolean able) {
		System.out.println("更新接单权限");
		Transaction transaction=null;
		String hql="";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			hql="update User set accept_able = ? where mobile = ?";
			transaction=session.beginTransaction();
			Query query=session.createQuery(hql);
			query.setParameter(0, able);
			query.setParameter(1, mobile);
			query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}finally
		{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
		
	}

	@Override
	public List<User> getUser(User user) throws NoSuchFieldException {
		String[] strings = ObjectUtil.getField(user);
		Object[] objects = ObjectUtil.getFieldValuesByName(strings, user);
		Transaction transaction=null;
		String hql= "";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			transaction=session.beginTransaction();
			for (int i = 0; i < strings.length; i++) {
				hql = hql + strings[i]+" = ? ";
				if(i==strings.length-1){
					break;
				}
				hql = hql+" and ";
			}
			hql=" from User where "+hql;
			System.out.println("hql:"+hql);
			Query query=session.createQuery(hql);
			for (int j = 0; j < objects.length; j++) {
				System.out.println(j+"======要查询的条件的值====="+objects[j]);
				query.setParameter(j, objects[j]);
			}
			List<User> list = query.list();
			transaction.commit();
			if(list.size()>0){
				return list;
			}else{
				System.out.println("没查询导数据，死掉了");
				return null;
			}
		} catch (Exception e) {
			System.out.println("报错了");
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

	@Override
	public void setPass(String mobile, boolean pass) {
		System.out.println("更新免押金权限");
		Transaction transaction=null;
		String hql="";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			hql="update User set pass = ? where mobile = ?";
			transaction=session.beginTransaction();
			Query query=session.createQuery(hql);
			query.setParameter(0, pass);
			query.setParameter(1, mobile);
			query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}finally
		{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}

	@Override
	public List<User> getUser(String object, String objectValue) {
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			String hql = "from User where "+object+" like '%"+objectValue+"%'";
			System.out.println(hql);
			Query query = session.createQuery(hql);
			List<User> list = query.list();
			transaction.commit();
			if(list.size()>0){
				return list;
			}else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public long getMemberNum(String area) {
		Transaction transaction=null;
		String hql="";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			hql="select count(*) from User where address like '%"+area+"%'";
			transaction=session.beginTransaction();
			Query query=session.createQuery(hql);
			Long count =  (Long) query.uniqueResult();
			transaction.commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
		return (long) 0;
	}

	@Override
	public List<UserChargeMoney> getChargeRecord(int first,String area) {
		Transaction transaction = null;
		List<UserChargeMoney> list=null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if(area==null||"".equals(area))
			{
				String hql = "from UserChargeMoney order by date desc";
				Query query = session.createQuery(hql);
				query.setFirstResult(first);
				query.setMaxResults(10);
				list = query.list();
				transaction.commit();
			}
			else{
				String hql = "select uc.* from UserChargeMoney uc left join user u on uc.userid=u.id where u.address like '%"+area+"%' order by uc.date desc";
				SQLQuery query = session.createSQLQuery(hql).addEntity(UserChargeMoney.class);
				query.setFirstResult(first);
				query.setMaxResults(10);
				list = query.list();
				transaction.commit();
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public long getChargeRecordNum(String area) {
		Transaction transaction=null;
		String hql="";
		Long count =0L;
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			transaction=session.beginTransaction();
			if(area==null||"".equals(area)){
			hql="select count(*) from UserChargeMoney";
			Query query=session.createQuery(hql);
			count =  (Long) query.uniqueResult();
			}
			else{
				hql="select count(*) from UserChargeMoney uc left join user u on uc.userid=u.id where u.address like '%"+area+"%'";
				SQLQuery query = session.createSQLQuery(hql);
				 count = Long.parseLong(query.uniqueResult().toString());
			}
			transaction.commit();
			System.out.println("count----:"+count);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
		return (long) 0;
	}

	@Override
	public List<UserChargeMoney> findUserCharge(String mobile) {
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			String hql = "from UserChargeMoney where mobile = ?";
			System.out.println(hql);
			Query query = session.createQuery(hql);
			query.setParameter(0, mobile);
			List<UserChargeMoney> list = query.list();
			transaction.commit();
			if(list.size()>0){
				return list;
			}else {
				return null;
			}
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public List<UserIDcard> findUserCard(int first, String flag, String area) {
		System.out.println("开始查询用户待信息");
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			StringBuffer buffer = new StringBuffer("");
			if(area==null||"".equals(area)){
				if(flag.equals("0")){
					buffer.append("select  u.*  from UserIDcard u where  u.results is  null");
				}else{
					buffer.append("select  u.*  from UserIDcard u where u.results is not  null");
				}
			}else{
				if(flag.equals("0")){
					buffer.append("SELECT uid.* FROM useridcard uid LEFT JOIN USER u ON uid.id=u.id WHERE uid.results IS NULL  AND u.address LIKE '%"+area+"%' ");
				}else{
					buffer.append("SELECT uid.* FROM useridcard uid LEFT JOIN USER u ON uid.id=u.id WHERE uid.results IS not NULL AND u.address LIKE '%"+area+"%'");
				}
			}
			System.out.println(buffer.toString());
			SQLQuery query = session.createSQLQuery(buffer.toString()).addEntity(UserIDcard.class);
			query.setFirstResult(first);
			query.setMaxResults(10);
			List<UserIDcard> list=query.list();
			if(list.size()==0){
				System.out.println("查无数据");
			}
			System.out.println("查询到"+list.size()+"记录");
			transaction.commit();
			return list;
		} catch (Exception e) {
			System.out.println("出问题了");
			e.printStackTrace();
			return null;
		}finally{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}

	@Override
	public List<Order> findOrder(String userid,Integer page) {
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			StringBuffer buffer = new StringBuffer("");
			buffer.append("select * from Order_record where courierid=?");
			SQLQuery query = session.createSQLQuery(buffer.toString()).addEntity(Order.class);
			query.setString(0, userid);
			query.setFirstResult(page*10);
			query.setMaxResults(10);
			List<Order> list=query.list();
			transaction.commit();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}
	
	
	
	@Override
	public Integer findOrderCount(String userid) {
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			StringBuffer buffer = new StringBuffer("");
			buffer.append("select count(*) from Order_record where courierid=?");
			SQLQuery query = session.createSQLQuery(buffer.toString());
			query.setString(0, userid);
			int value = ((Number)query.uniqueResult()).intValue();  
			transaction.commit();
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}finally{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}
	
	@Override
	public User findUserByMobile(String mobile) {
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			StringBuffer buffer = new StringBuffer("");
			buffer.append("select * from User where mobile=?");
			SQLQuery query = session.createSQLQuery(buffer.toString()).addEntity(User.class);
			query.setString(0, mobile);
			User user = (User) query.uniqueResult();
			transaction.commit();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
		return null;
	}

}
