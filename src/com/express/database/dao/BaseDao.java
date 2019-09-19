package com.express.database.dao;

import java.util.List;

import com.express.model.Order;

public interface BaseDao<T> {

	public abstract void save(T entity);

	public abstract void delete(T entity);
	
	public abstract List<T> getByParam(int first,String paramName,Object paramValue,String clazz,String byRank);
	
	public abstract List<T> getByParam(String paramName,Object paramValue,String clazz);

	public abstract List<T> getByParams(String[] paramName,Object[] paramValue,String clazz);
	
	public List<T> getByParams(T entity,int first) throws NoSuchFieldException;
	
	public T getByParams(T entity) throws NoSuchFieldException;

	public abstract void update(T entity);
	
	public abstract T getOne(T entity,String id);
	
	public abstract void updates(T entity) throws NoSuchFieldException;
	
	public List<T> getAll(T entity,int first);

	public List<T> getAll(T entity,int first,String area);
	
	public List<T> getAll(T entity); 
	
	public List<T> getAll(T entity,String area); 
	
	public int amountCity(String cityName);
	
	public void updateCuserComment(String hql,float grade);
	
	public List<T> getSome(T entity,int first,String flag,String area) throws NoSuchFieldException;

	public List<T> getSomeAll(T entity, String flag) throws NoSuchFieldException;
		
	public List<Order> getOrder(String id,int first);

}