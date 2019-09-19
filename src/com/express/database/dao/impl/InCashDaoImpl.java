package com.express.database.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.express.database.dao.InCashDao;
import com.express.model.InCash;
import com.express.util.HibernateUtil;

public class InCashDaoImpl implements InCashDao {

	@Override
	public Long getInCashNum() {
		Transaction transaction=null;
		String hql="";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			//hql="select count(*) from InCash where results is false";
			hql="select count(*) from InCash";
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
	public void setRecord2finsh() {
		Transaction transaction=null;
		String hql="";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			hql="update InCash set results = ? where results is false";
			transaction=session.beginTransaction();
			Query query=session.createQuery(hql);
			query.setParameter(0, true);
			query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
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
	public InCash getInCash(String inCashId) {
		Transaction transaction=null;
		String hql="";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			hql="from InCash where id=?";
			transaction=session.beginTransaction();
			Query query=session.createQuery(hql);
			query.setParameter(0, inCashId);
			InCash inCash = (InCash) query.uniqueResult();
			transaction.commit();
			return inCash;
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
	public void updateInCash(InCash inCash) {
		Transaction transaction=null;
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			transaction=session.beginTransaction();
			session.update(inCash);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}
	
}
