package com.express.database.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.express.database.dao.AdminDao;
import com.express.model.APPVersion;
import com.express.model.Admin;
import com.express.model.CompanyPaymentDetails;
import com.express.model.SysNotice;
import com.express.util.DateUtil;
import com.express.util.HibernateUtil;

public class AdminDaoImpl implements AdminDao {

	@Override
	public List<SysNotice> getAllNotice(int first) {
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			String hql = "from SysNotice order by date desc";
			Query query = session.createQuery(hql);
			query.setFirstResult(first);
			query.setMaxResults(10);
			List<SysNotice> list = query.list();
			if (list.size() == 0) {
				System.out.println("查无数据");
			}
			System.out.println("查询到" + list.size() + "记录");
			transaction.commit();
			return list;
		} catch (Exception e) {
			System.out.println("出问题了");
			return null;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}

	@Override
	public long getOrderCount(String area) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "select count(*) from Order";
			if (area != null)
				hql += " where sendAddress like '%" + area + "%'";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
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
	public long getUserCount(String userType, String area) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "select count(*) from User where role = ?";
			if (area != null)
				hql += " and address like '%" + area + "%'";
			System.out.println(hql);
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, userType);
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
	public void deleteAdmin(String username) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "delete Admin where username = ? ";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, username);
			query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}

	@Override
	public Admin getAdmin(String username, String pwd) {
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			String hql = "from Admin where username = ? and password = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, username);
			query.setParameter(1, pwd);
			List<Admin> list = query.list();
			transaction.commit();
			if (list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println("出问题了");
			e.printStackTrace();
			return null;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}

	@Override
	public boolean updateAdminQX(String username, boolean userManage, boolean checkUser, boolean userMsg, boolean userAdvice, boolean userATM, boolean orderManage, boolean adminManage, boolean msgManage, boolean activitySet) {
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			String hql = "update Admin set userManage = ?," + "checkUser = ?,userMsg=?," + "userAdvice = ?,userATM = ?,orderManage = ?," + "adminManage = ?,msgManage = ?,activitySet = ? where username = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, userManage);
			query.setParameter(1, checkUser);
			query.setParameter(2, userMsg);
			query.setParameter(3, userAdvice);
			query.setParameter(4, userATM);
			query.setParameter(5, orderManage);
			query.setParameter(6, adminManage);
			query.setParameter(7, msgManage);
			query.setParameter(8, activitySet);
			query.setParameter(9, username);
			query.executeUpdate();
			transaction.commit();
			return true;
		} catch (Exception e) {
			transaction.rollback();
			System.out.println("出问题了");
			return false;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}

	@Override
	public Admin findAdmin(String username) {
		Transaction transaction = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			String hql = "from Admin where username = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, username);
			List<Admin> list = query.list();
			transaction.commit();
			if (list.size() > 0) {
				return list.get(0);
			}
			return null;
		} catch (Exception e) {
			System.out.println("出问题了");
			return null;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}

	@Override
	public long getCPDNUM(String area) {
		Transaction transaction = null;
		String hql = "";
		Long count = 0L;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if (area == null || "".equals(area)) {
				hql = "select count(*) from CompanyPaymentDetails";
				Query query = session.createQuery(hql);
				count = (Long) query.uniqueResult();
			} else {
				hql = "select count(*) from CompanyPaymentDetails c left join order_record o on o.orderid=c.id  where o.sendaddress like '%" + area + "%' ";
				SQLQuery query = session.createSQLQuery(hql);
				Object object = query.uniqueResult();
				count = Long.parseLong(object.toString());
			}
			transaction.commit();
			System.out.println("count----:" + count);
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
	public List<CompanyPaymentDetails> getCPD(int first, String area) {
		Transaction transaction = null;
		List<CompanyPaymentDetails> list = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if (area == null || "".equals(area)) {
				String hql = "from CompanyPaymentDetails order by date desc";
				Query query = session.createQuery(hql);
				query.setFirstResult(first);
				query.setMaxResults(10);
				list = query.list();
			} else {
				String hql = "select c.* from CompanyPaymentDetails c left join order_record o on c.id=o.orderid where o.sendaddress like '%" + area + "%' order by date desc";
				SQLQuery query = session.createSQLQuery(hql).addEntity(CompanyPaymentDetails.class);
				query.setFirstResult(first);
				query.setMaxResults(10);
				list = query.list();
			}
			transaction.commit();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("出问题了");
			return null;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}

	@Override
	public List<CompanyPaymentDetails> getCPD(int first, String date, String lastDate, String area) {

		System.out.println(date + "-----" + lastDate);
		Transaction transaction = null;
		List<CompanyPaymentDetails> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		transaction = session.beginTransaction();
		try {
			if (area == null || "".equals(area)) {
				String hql = "from CompanyPaymentDetails where date between '" + date + "' and '" + lastDate + "' order by date desc";
				Query query = session.createQuery(hql);
				query.setFirstResult(first);
				query.setMaxResults(10);
				list = query.list();
			} else {
				String hql = "select c.* from CompanyPaymentDetails c left join order_record o  on c.id=o.orderid where c.date between '" + date + "' and '" + lastDate + "' and o.sendaddress like '%" + area + "%' order by c.date desc";
				System.out.println(hql + "----------");
				SQLQuery query = session.createSQLQuery(hql).addEntity(CompanyPaymentDetails.class);
				query.setFirstResult(first);
				query.setMaxResults(10);
				list = query.list();
			}
			transaction.commit();
			return list;
		} catch (Exception e) {
			System.out.println("出问题了");
			return null;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}

	// @Override
	// public List<CompanyPaymentDetails> getCPD(int first, String date) {
	// System.out.println(date+"-----------");
	// Transaction transaction = null;
	// try {
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// transaction = session.beginTransaction();
	// String hql =
	// "from CompanyPaymentDetails where date between '"+date+" 00:00:00' and '"+date+" 23:59:59' order by date desc";
	// Query query=session.createQuery(hql);
	// query.setFirstResult(first);
	// query.setMaxResults(10);
	// List<CompanyPaymentDetails> list=query.list();
	// transaction.commit();
	// return list;
	// } catch (Exception e) {
	// System.out.println("出问题了");
	// return null;
	// }finally{
	// if(transaction!=null)
	// {
	// transaction=null;
	// }
	// }
	// }

	@Override
	public long getCPDNUM(String date, String lastDate, String area) {
		Transaction transaction = null;
		String hql = "";
		Long count = 0L;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if (area == null || "".equals(area)) {
				hql = "select count(*) from CompanyPaymentDetails where date between '" + date + "' and '" + lastDate + "'";
				Query query = session.createQuery(hql);
				count = (Long) query.uniqueResult();
			} else {
				hql = "select count(c.id) from CompanyPaymentDetails c left join order_record o on o.orderid=c.id  where c.date between '" + date + "' and '" + lastDate + "' and o.sendAddress like '%" + area + "%' order by c.date desc";
				SQLQuery query = session.createSQLQuery(hql);
				count = Long.parseLong(query.uniqueResult().toString());
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

	// @Override
	// public long getCPDNUM(String date) {
	// Transaction transaction=null;
	// String hql="";
	// try {
	// Session session=HibernateUtil.getSessionFactory().getCurrentSession();
	// hql="select count(*) from CompanyPaymentDetails where date between '"+date+" 00:00:00' and '"+date+" 23:59:59'";
	// transaction=session.beginTransaction();
	// Query query=session.createQuery(hql);
	// Long count = (Long) query.uniqueResult();
	// transaction.commit();
	// return count;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }finally
	// {
	// if(transaction!=null)
	// {
	// transaction=null;
	// }
	// }
	// return (long) 0;
	// }

	@Override
	public double getTollValueByArea(String area) {
		Transaction transaction = null;
		String hql = "";
		double money = 0;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if (area == null || "".equals(area)) {
				hql = "select sum(money) from CompanyPaymentDetails where money > 0";
				Query query = session.createQuery(hql);
				if (query.uniqueResult() != null) {
					money = (Double) query.uniqueResult();
				} else {
					money = 0;
				}
			} else {
				hql = "select sum(money) from CompanyPaymentDetails c left join order_record o on o.orderid=c.id  where money > 0  and o.sendAddress like '%" + area + "%'";
				SQLQuery query = session.createSQLQuery(hql);
				money = Double.parseDouble(query.uniqueResult().toString());
			}
			transaction.commit();
			return money;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return 0;
	}

	@Override
	public double getPayValueByArea(String area) {
		Transaction transaction = null;
		String hql = "";
		double money = 0;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if (area == null || "".equals(area)) {
				hql = "select sum(money) from CompanyPaymentDetails where money < 0";
				Query query = session.createQuery(hql);
				if (query.uniqueResult() != null) {
					money = (Double) query.uniqueResult();
				} else {
					money = 0;
				}
			} else {
				hql = "select sum(money) from CompanyPaymentDetails c left join order_record o on o.orderid=c.id  where money < 0  and o.sendAddress like '%" + area + "%'";
				SQLQuery query1 = session.createSQLQuery(hql);
				if (query1.uniqueResult() != null)
					money = Double.parseDouble(query1.uniqueResult().toString());
			}
			transaction.commit();
			return money;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return 0;
	}

	@Override
	public double getCouponValueByArea(String area) {
		Transaction transaction = null;
		String hql = "";
		Query query = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "select sum(coupon) from Order where orderStaus between 4 and 5 ";
			if (area != null && !"".equals(area))
				hql += "and sendAddress like '%" + area + "%'";
			transaction = session.beginTransaction();
			query = session.createQuery(hql);
			double money;
			if (query.uniqueResult() != null) {
				money = (Double) query.uniqueResult();
			} else {
				money = 0;
			}
			transaction.commit();
			return money;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return 0;
	}

	@Override
	public double getTollValue(String date, String lastDate, String area) {
		Transaction transaction = null;
		String hql = "";
		double money = 0;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if (area == null || "".equals(area)) {
				hql = "select sum(money) from CompanyPaymentDetails where money > 0 and date between '" + date + "' and '" + lastDate + "'";
				Query query = session.createQuery(hql);
				if (query.uniqueResult() != null) {
					money = (Double) query.uniqueResult();
				} else {
					money = 0;
				}
			} else {
				hql = "select sum(money) from CompanyPaymentDetails c left join order_record o on o.orderid=c.id  where c.money > 0 and date between '" + date + "' and '" + lastDate + "'  and o.sendAddress like '%" + area + "%'";
				SQLQuery query = session.createSQLQuery(hql);
				if (query.uniqueResult() != null)
					money = Double.parseDouble(query.uniqueResult().toString());
			}
			transaction.commit();
			return money;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return 0;
	}

	@Override
	public double getTollValue(String date, String area) {
		Transaction transaction = null;
		String hql = "";
		double money = 0;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if (area == null || "".equals(area)) {
				hql = "select sum(money) from CompanyPaymentDetails where money > 0 and date between '" + date + " 00:00:00' and '" + date + " 23:59:59'";
				Query query = session.createQuery(hql);
				if (query.uniqueResult() != null) {
					money = (Double) query.uniqueResult();
				}
			} else {
				hql = "select sum(money) from CompanyPaymentDetails c left join order_record o on o.orderid=c.id  where money > 0 and date between '" + date + " 00:00:00' and '" + date + " 23:59:59'" + " and o.sendAddress like '%" + area + "%'";
				SQLQuery query = session.createSQLQuery(hql);
				if (query.uniqueResult() != null) {
					money = Double.parseDouble(query.uniqueResult().toString());
				}
			}
			transaction.commit();
			return money;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return 0;
	}

	@Override
	public double getPayValue(String date, String lastDate, String area) {
		Transaction transaction = null;
		String hql = "";
		double money = 0;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if (area == null || "".equals(area)) {
				hql = "select sum(money) from CompanyPaymentDetails where money < 0 and date between '" + date + "' and '" + lastDate + "'";
				Query query = session.createQuery(hql);
				if (query.uniqueResult() != null) {
					money = (Double) query.uniqueResult();
				}
			} else {
				hql = "select sum(money) from CompanyPaymentDetails c left join order_record o on o.orderid=c.id  where money < 0 and o.sendAddress like '%" + area + "%'  and    date between '" + date + "' and '" + lastDate + "' ";
				SQLQuery query = session.createSQLQuery(hql);
				if (query.uniqueResult() != null) {
					money = Double.parseDouble(query.uniqueResult().toString());
				}
			}
			transaction.commit();
			return money;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return 0;
	}

	@Override
	public double getPayValue(String date, String area) {
		Transaction transaction = null;
		String hql = "";
		double money = 0;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if (area == null || "".equals(area)) {
				hql = "select sum(money) from CompanyPaymentDetails where money < 0 and date between '" + date + " 00:00:00' and '" + date + " 23:59:59'";
				Query query = session.createQuery(hql);
				if (query.uniqueResult() != null) {
					money = (Double) query.uniqueResult();
				}
			} else {
				hql = "select sum(money) from CompanyPaymentDetails c left join order_record o on o.orderid=c.id  where money < 0 and  date between '" + date + " 00:00:00' and '" + date + " 23:59:59'  and o.sendAddress like '%" + area + "%'";
				SQLQuery query = session.createSQLQuery(hql);
				if (query.uniqueResult() != null) {
					money = Double.parseDouble(query.uniqueResult().toString());
				}
			}
			transaction.commit();
			return money;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return 0;
	}

	@Override
	public double getCouponValue(String date, String lastDate, String area) {
		Transaction transaction = null;
		String hql = "";
		double money = 0;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if (area == null || "".equals(area)) {
				hql = "select sum(coupon) from Order where requestDate between '" + date + "' and '" + lastDate + "'";
				Query query = session.createQuery(hql);
				if (query.uniqueResult() != null) {
					money = (Double) query.uniqueResult();
				}
			} else {
				hql = "select sum(coupon) from Order_record where sendAddress like '%" + area + "%' and  requestDate between '" + date + "' and '" + lastDate + "'";
				SQLQuery query = session.createSQLQuery(hql);
				if (query.uniqueResult() != null) {
					money = Double.parseDouble(query.uniqueResult().toString());
				}
			}
			transaction.commit();
			return money;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return 0;
	}

	@Override
	public double getCouponValue(String date, String area) {
		Transaction transaction = null;
		String hql = "";
		double money = 0;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			if (area == null || "".equals(area)) {
				hql = "select sum(coupon) from Order where requestDate between '" + date + " 00:00:00' and '" + date + " 23:59:59'";
				Query query = session.createQuery(hql);
				if (query.uniqueResult() != null) {
					money = (Double) query.uniqueResult();
				}
			}else {
				hql = "select sum(coupon) from Order_record where requestDate between '" + date + " 00:00:00' and '" + date + " 23:59:59'"+ " and sendAddress like '%" + area + "%'";
				SQLQuery query = session.createSQLQuery(hql);
				if (query.uniqueResult() != null) {
					money = Double.parseDouble(query.uniqueResult().toString());
				}
			}
			transaction.commit();
			return money;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return 0;
	}

	@Override
	public List<APPVersion> getAppVersion(String appType) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "from APPVersion where appType = ? order by date desc";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, appType);
			query.setFirstResult(0);
			query.setMaxResults(1);
			List<APPVersion> list = query.list();
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
	public List<Admin> getAllAdmin(String area) {
		Transaction transaction = null;
		List<Admin> list = null;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			String hql = "from Admin ";
			if (area != null && !"".equals(area)) {
				hql += "where city  like ?";
			}
			Query query = session.createQuery(hql);
			if (area != null && !"".equals(area)) {
				query.setParameter(0, "%" + area + "%");
			}
			System.out.println(hql);
			list = query.list();
			transaction.commit();
		} catch (Exception e) {
			System.out.println("出问题了");
			e.printStackTrace();
			return null;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return list;
	}

}
