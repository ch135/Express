package com.express.database.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.express.database.dao.CouponTypeDao;
import com.express.model.Coupon;
import com.express.model.CouponType;
import com.express.util.DateUtil;
import com.express.util.HibernateUtil;

public class CouponTypeDaoImpl implements CouponTypeDao {

	@Override
	public double getCouponValue(String couponType) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "from CouponType where type = ?";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, couponType);
			List<CouponType> list = query.list();
			transaction.commit();
			if(list.size()>0){
				return list.get(0).getValue();
			}else{
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}finally
		{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}

	@Override
	public void deleteCoupon(String couponId) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "delete Coupon where id = ?";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, couponId);
			query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		}finally
		{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}

	@Override
	public List<CouponType> getCouponType() {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "from CouponType";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			List<CouponType> list = query.list();
			transaction.commit();
			if(list.size()>0){
				return list;
			}else{
				return null;
			}
		} catch (Exception e) {
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
	public boolean updateValue(String couponType, double value) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "update CouponType set value = ? where type = ?";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, value);
			query.setParameter(1, couponType);
			query.executeUpdate();
			transaction.commit();
			return true;
		} catch (Exception e) {
			transaction.rollback();
			return false;
		}finally
		{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}

	@Override
	public List<Coupon> getUserCoupon(String mobile) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "from Coupon where mobile = ? order by date desc";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, mobile);
			List<Coupon> list = query.list();
			transaction.commit();
			if(list.size()>0){
				return list;
			}else{
				return null;
			}
		} catch (Exception e) {
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
	public void save(CouponType ct) {
		Transaction transaction=null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();;
			transaction=session.beginTransaction();
			session.save(ct);
			transaction.commit();//提交事务
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
	public void delectCoupon() {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "delete Coupon where date between '1970-01-01' and ?";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, DateUtil.lastMonth());
			query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		}finally
		{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}

}
