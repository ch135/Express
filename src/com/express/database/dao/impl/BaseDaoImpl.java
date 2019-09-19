package com.express.database.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;



















import com.express.database.dao.BaseDao;
import com.express.model.Order;
import com.express.model.UserAdvice;
import com.express.util.Constant;
import com.express.util.HibernateUtil;
import com.express.util.ObjectUtil;



/**
 * 数据库操作方法
 * @author LWK
 *
 * @param <T>
 */
public class BaseDaoImpl<T> implements BaseDao<T> {
	
	/**
	 * 保存
	 */
	@Override
	public void save(T entity) {
		Transaction transaction=null;
		try {
			System.out.println("我要去获取transaction");
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction=session.beginTransaction();
			System.out.println(entity);
			session.save(entity);
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
	

	/**
	 * 通过对象删除实体
	 */
	@Override
	public void delete(T entity) {
		Transaction transaction=null;
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			transaction=session.beginTransaction();
			session.delete(entity);
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
	

	/**
	 * 通过单个参数查询记录并分页
	 */
	public List<T> getByParam(int first,String paramName,Object paramValue,String clazz,String byRank) {
		Transaction transaction=null;
		String hql="";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			hql="from "+clazz+" where "+paramName+"=? order by "+byRank+" desc";
			transaction=session.beginTransaction();
			Query query=session.createQuery(hql);
			query.setParameter(0, paramValue);
			query.setFirstResult(first);
			query.setMaxResults(Constant.PAGE);
			List<T> list=query.list();
			transaction.commit();
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
	 * 通过单个参数查询记录
	 */
	@Override
	public List<T> getByParam(String paramName, Object paramValue, String clazz) {
		Transaction transaction=null;
		String hql="";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			hql="from "+clazz+" where "+paramName+"=?";
			transaction=session.beginTransaction();
			Query query=session.createQuery(hql);
			query.setParameter(0, paramValue);
			List<T> list=query.list();
			transaction.commit();
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
	 * 通过多个参数查询记录
	 * @throws NoSuchFieldException 
	 */
	public List<T> getByParams(T entity,int first) throws NoSuchFieldException {
		String[] strings = ObjectUtil.getField(entity);
		Object[] objects = ObjectUtil.getFieldValuesByName(strings, entity);
		Transaction transaction=null;
		String hql="";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			transaction=session.beginTransaction();
			for (int i = 0; i < strings.length; i++) {
				hql = hql + strings[i]+"=? ";
				if(i==strings.length-1){
					break;
				}
				hql = hql+" and ";
			}
			hql="from "+entity.getClass().getSimpleName()+" where "+hql+" order by date desc";
			Query query=session.createQuery(hql);
			query.setFirstResult(first);
			query.setMaxResults(15);
			for (int j = 0; j < objects.length; j++) {
				query.setParameter(j, objects[j]);
			}
			List<T> list=query.list();
			transaction.commit();
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
	
	public List<T> getByParams(String[] paramName,Object[] paramValue,String clazz) {
		Transaction transaction=null;
		String hql="";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			transaction=session.beginTransaction();
			for (int i = 0; i < paramName.length; i++) {
				hql = hql + paramName[i]+"=? ";
				if(i==paramName.length-1){
					break;
				}
				hql = hql+" and ";
			}
			hql="from "+clazz+" where "+hql;
			Query query=session.createQuery(hql);
			for (int j = 0; j < paramValue.length; j++) {
				query.setParameter(j, paramValue[j]);
			}
			List<T> list=query.list();
			transaction.commit();
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
	 * 更新
	 */
	@Override
	public void update(T entity) {
		Transaction transaction=null;
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			transaction=session.beginTransaction();
			session.update(entity);
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


/**
 * 获取一个实例对象
 */
	@Override
	public T getOne(T entity,String id) {
		Transaction transaction= null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			T t = (T) session.get(entity.getClass(), id);
			transaction.commit();
			return t;
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
	


	/**
	 * 更新多个指定参数
	 * @throws NoSuchFieldException 
	 */
	@Override
	public void updates(T entity) throws NoSuchFieldException {
		String[] strings = ObjectUtil.getField(entity);
		Object[] objects = ObjectUtil.getFieldValuesByName(strings, entity);
		Transaction transaction=null;
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			transaction=session.beginTransaction();
			String hql = "update " + entity.getClass().getSimpleName() + " set ";
			for (int i = 1; i < strings.length; i++) {
				hql = hql + strings[i] +  " = ? ";
				if(i == strings.length-1 ){
					break;
				}
				hql = hql + " , ";
			}
			hql = hql + "where "+strings[0]+" = ?";
			Query query = session.createQuery(hql);
			for (int j = 1; j < objects.length; j++) {
				query.setParameter(j-1, objects[j]);
			}
			query.setParameter(strings.length-1, objects[0]); 
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


	/**
	 * 获取所有对象进行分页
	 */
	@Override
	public List<T> getAll(T entity,int first) {
		System.out.println("开始查询用户信息");
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			System.out.println("查询的表是:"+entity.getClass().getSimpleName()+"----"+first);
			String hql = "from "+entity.getClass().getSimpleName();
			Query query=session.createQuery(hql);
			query.setFirstResult(first);
			query.setMaxResults(10);
			List<T> list=query.list();
			if(list.size()==0){
				System.out.println("查无数据");
			}
			System.out.println("查询到"+list.size()+"记录");
			transaction.commit();
			return list;
		} catch (Exception e) {
			System.out.println("出问题了");
			return null;
		}finally{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}
	
	
	@Override
	public List<T> getAll(T entity,int first,String area) {
		System.out.println("开始查询用户信息");
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			System.out.println("查询的表是:"+entity.getClass().getSimpleName()+"----"+first);
			String hql = "from "+entity.getClass().getSimpleName();
			if(area!=null&&!"".equals(area))
				hql+=" where address like '%"+area+"%'";
			System.out.println(hql);
			Query query=session.createQuery(hql);
			query.setFirstResult(first);
			query.setMaxResults(10);
			List<T> list=query.list();
			if(list.size()==0){
				System.out.println("查无数据");
			}
			System.out.println("查询到"+list.size()+"记录");
			transaction.commit();
			return list;
		} catch (Exception e) {
			System.out.println("出问题了");
			return null;
		}finally{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}
	
	
	/**
	 * 获取所有对象
	 */
	@Override
	public List<T> getAll(T entity) {
		System.out.println("-------"+entity.getClass().getSimpleName());
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			String hql = "from "+entity.getClass().getSimpleName();
			Query query=session.createQuery(hql);
			List<T> list=query.list();
//			System.out.println("++++++++"+list.size());
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
	public List<T> getAll(T entity,String area) {
		System.out.println("-------"+entity.getClass().getSimpleName());
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			String hql = "from "+entity.getClass().getSimpleName();
			if(area!=null&&!"".equals(area))
				hql+=" where address like '%"+area+"%'";
			Query query=session.createQuery(hql);
			List<T> list=query.list();
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
	public T getByParams(T entity) throws NoSuchFieldException {
		String[] strings = ObjectUtil.getField(entity);
		Object[] objects = ObjectUtil.getFieldValuesByName(strings, entity);
		Transaction transaction=null;
		String hql="";
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
			hql=" from "+entity.getClass().getSimpleName()+" where "+hql;
			System.out.println("hql:"+hql);
			Query query=session.createQuery(hql);
			for (int j = 0; j < objects.length; j++) {
				//System.out.println(j+"======要查询的条件的值====="+objects[j]);
				query.setParameter(j, objects[j]);
			}
			List<T> list = query.list();
			if(list.size()>0){
				T t = (T) list.get(0);
				transaction.commit();
				return t;
			}else{
				//System.out.println("没查询导数据，死掉了");
				transaction.commit();
				return null;
			}
		} catch (Exception e) {
			//System.out.println("报错了");
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
	public int amountCity(String cityName) {
		Transaction transaction=null;
		String hql="from User as u where u.address like :address";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			transaction=session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setString("address","%"+cityName+"%");
			int amount = query.list().size();
			transaction.commit();
			return amount;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}finally
		{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}


	/**
	 * 更新快递员评价
	 */
	@Override
	public void updateCuserComment(String hql, float grade) {
		Transaction transaction=null;
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			transaction=session.beginTransaction();
			System.out.println(hql);
			Query query = session.createQuery(hql);
			query.setParameter(0, grade); 
			query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			System.out.println("执行出错了");
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
	public List<T> getSome(T entity, int first,String flag,String area) throws NoSuchFieldException {
		System.out.println("开始查询用户待信息");
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			String hql = "";
			if(flag.equals("0")){
				hql = "from "+entity.getClass().getSimpleName()+" where results is null";
			}else{
				hql = "from "+entity.getClass().getSimpleName()+" where results is not null";
			}
			Query query=session.createQuery(hql);
			query.setFirstResult(first);
			query.setMaxResults(10);
			List<T> list=query.list();
			if(list.size()==0){
				System.out.println("查无数据");
			}
			System.out.println("查询到"+list.size()+"记录");
			transaction.commit();
			return list;
		} catch (Exception e) {
			System.out.println("出问题了");
			return null;
		}finally{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}

	@Override
	public List<T> getSomeAll(T entity,String flag) throws NoSuchFieldException {
		System.out.println("开始查询用户待信息");
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			String hql = "";
			if(flag.equals("0")){
				hql = "from "+entity.getClass().getSimpleName()+" where results is null";
			}else{
				hql = "from "+entity.getClass().getSimpleName()+" where results is not null";
			}
			Query query=session.createQuery(hql);
			List<T> list=query.list();
			if(list.size()==0){
				System.out.println("查无数据");
			}
			System.out.println("查询到"+list.size()+"记录");
			transaction.commit();
			return list;
		} catch (Exception e) {
			System.out.println("出问题了");
			return null;
		}finally{
			if(transaction!=null)
			{
				transaction=null;
			}
		}
	}


	@Override
	public List<Order> getOrder(String id, int first) {
		Transaction transaction=null;
		String hql="";
		try {
			Session session=HibernateUtil.getSessionFactory().getCurrentSession();
			hql="from Order where courierid=? and orderStaus BETWEEN ? and ? order by requestDate desc";
			transaction=session.beginTransaction();
			Query query=session.createQuery(hql);
			query.setParameter(0, id);
			query.setParameter(1, 2);
			query.setParameter(2, 3);
			query.setFirstResult(first);
			query.setMaxResults(Constant.PAGE);
			List<Order> list=query.list();
			System.out.println("找到东西了长度为++++++++"+list.size());
			transaction.commit();
			return list;
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
