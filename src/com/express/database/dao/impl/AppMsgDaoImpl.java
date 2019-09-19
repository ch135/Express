package com.express.database.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.express.database.dao.AppMsgDao;
import com.express.model.APPMsg;
import com.express.model.APPVersion;
import com.express.model.Order;
import com.express.model.SysNotice;
import com.express.util.HibernateUtil;

public class AppMsgDaoImpl implements AppMsgDao {

	@Override
	public APPVersion getAppVersion(String appType) {
		Transaction transaction=null;
		String hql="";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			hql="from APPVersion where appType = ? order by date desc";
			transaction=session.beginTransaction();
			Query query=session.createQuery(hql);
			query.setParameter(0, appType);
			query.setFirstResult(0);
			query.setMaxResults(5);
			List<APPVersion> list=query.list();
			transaction.commit();
			if(list.size()>0){
				return list.get(0);
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
	public APPMsg getAppCover() {
		Transaction transaction=null;
		String hql="";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			hql="from APPMsg order by date desc";
			transaction=session.beginTransaction();
			Query query=session.createQuery(hql);
			query.setFirstResult(0);
			query.setMaxResults(5);
			List<APPMsg> list=query.list();
			transaction.commit();
			if(list.size()>0){
				return list.get(0);
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
	
}
