package com.express.database.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.express.database.dao.AreaNormDao;
import com.express.model.AreaNorm;
import com.express.model.CouponType;
import com.express.util.HibernateUtil;

public class AreaNormImpl implements AreaNormDao {

	@Override
	public List<AreaNorm> getValue(String area) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "from AreaNorm where name = ?";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, area);
			List<AreaNorm> areaNorm = (List<AreaNorm>) query.list();
			transaction.commit();
			if(areaNorm != null){
				return areaNorm;
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
	public List<AreaNorm> getAreaAreaNorm() {
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			String hql = "from AreaNorm";
			Query query=session.createQuery(hql);
			List<AreaNorm> list=query.list();
			System.out.println("++++++++"+list.size());
			transaction.commit();
			return list;
		} catch (Exception e) {
			return null;
		}finally{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}

	@Override
	public boolean setAreaPrice(AreaNorm area) {
		Transaction transaction = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		transaction = session.beginTransaction();
		try {
			String hql = "update AreaNorm set value = ?,start_time=?,end_time=? where id = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, area.getValue());
			query.setParameter(1, area.getStart_time());
			query.setParameter(2, area.getEnd_time());
			query.setParameter(3, area.getId());
			query.executeUpdate();
			transaction.commit();
			return true;
		} catch (Exception e) {
			transaction.rollback();
			return false;
		}finally{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}

	@Override
	public List<AreaNorm> getAreaNorms(String name) {
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			String hql = "from AreaNorm where name=?";
			Query query=session.createQuery(hql);
			query.setString(0, name);
			List<AreaNorm> list=query.list();
			
			System.out.println("++++++++"+list.size());
			transaction.commit();
			return list;
		} catch (Exception e) {
			return null;
		}finally{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}

}
