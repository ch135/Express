package com.express.util;


import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

	
	private static SessionFactory sessionFactory;//会话工厂属性
	
	//构造方法私有化，保证单例模式
	private HibernateUtil()
	{
		
	}
	
	//共有的静态方法，获得会话工厂对象
	public static SessionFactory getSessionFactory()
	{
		if(sessionFactory==null)
		{
			try {
				StandardServiceRegistry serviceRegistry= new StandardServiceRegistryBuilder().configure().build();  
				sessionFactory = new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
				return sessionFactory;
			} catch (SessionException e) {
				System.out.println("获取session出错");
				return sessionFactory;
			}
		
		}
		else {
			return sessionFactory;
		}
	}
	
	//打开并返回一个Session 
    public static Session openSession(){
        return getSessionFactory().openSession();
    }
    
    //关闭Session
    public static void closeSession(Session session){
        if(null != session){
            session.close();
        }
    }
	
//	public static SessionFactory getSessionFactory()
//	{
//		System.out.println("开始测试");
//		if(sessionFactory==null)
//		{
//			System.out.println("session为空");
//			StandardServiceRegistry  serviceRegistry=new StandardServiceRegistryBuilder().configure().build(); 
//            sessionFactory=new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
//			return sessionFactory;
//		}else {
//			System.out.println("session不为空");
//			return sessionFactory;
//		}
//		}
}
