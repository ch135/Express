package com.express.admin.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.express.admin.service.AdminService;
import com.express.database.dao.AdminDao;
import com.express.database.dao.BaseDao;
import com.express.database.dao.CouponTypeDao;
import com.express.database.dao.InCashDao;
import com.express.database.dao.OrderDao;
import com.express.database.dao.UserDao;
import com.express.database.dao.impl.AdminDaoImpl;
import com.express.database.dao.impl.BaseDaoImpl;
import com.express.database.dao.impl.CouponTypeDaoImpl;
import com.express.database.dao.impl.InCashDaoImpl;
import com.express.database.dao.impl.OrderDaoImpl;
import com.express.database.dao.impl.UserDaoImpl;
import com.express.model.APPMsg;
import com.express.model.APPVersion;
import com.express.model.Admin;
import com.express.model.CompanyPaymentDetails;
import com.express.model.CouponType;
import com.express.model.InCash;
import com.express.model.ShareWeiXin;
import com.express.model.SysNotice;
import com.express.util.DateUtil;
import com.express.util.HibernateUtil;

public class AdminServiceImpl implements AdminService {
	BaseDao<Admin> dao = new BaseDaoImpl<Admin>();
	BaseDao<SysNotice> noticeDao = new BaseDaoImpl<SysNotice>();
	InCashDao inCashDao = new InCashDaoImpl();
	BaseDao<APPMsg> appMsgDao = new BaseDaoImpl<APPMsg>();
	BaseDao<APPVersion> appVersionDao = new BaseDaoImpl<APPVersion>();
	BaseDao<SysNotice> sysNoticeDao = new BaseDaoImpl<SysNotice>();
	AdminDao adminDao = new AdminDaoImpl();
	CouponTypeDao couponTypeDao = new CouponTypeDaoImpl();

	/**
	 * 管理员登录
	 */
	@Override
	public boolean adminLogin(Admin admin) {

		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.openSession();
			hql = "from Admin where username=? and password=?";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, admin.getUsername());
			query.setParameter(1, admin.getPassword());
			List<Admin> list = query.list();
			transaction.commit();
			if (list.size() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}

	/**
	 * 创建管理员
	 */
	@Override
	public boolean addAdmin(Admin admin) {
		Transaction transaction = null;

		try {
			Session session = HibernateUtil.openSession();
			transaction = session.beginTransaction();
			session.save(admin);
			transaction.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}

	/**
	 * 检测管理员是否存在
	 * 
	 * @throws NoSuchFieldException
	 */
	@Override
	public boolean checkAdmin(Admin admin) throws NoSuchFieldException {

		if (dao.getByParams(admin) != null) {
			if (!admin.getUsername().equals("admin")) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean rePwd(Admin admin) throws NoSuchFieldException {
		dao.updates(admin);
		return false;
	}

	@Override
	public List<Admin> getAllAdmin(int first) {
		return dao.getAll(new Admin(), first);
	}

	@Override
	public List<SysNotice> getNotice(int first) {
		return adminDao.getAllNotice(first);
	}

	@Override
	public List<SysNotice> getAllNotice() {
		return noticeDao.getAll(new SysNotice());
	}

	@Override
	public List<InCash> getIncashRecord() {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.openSession();
			hql = "from InCash where results is false order by date";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			List<InCash> list = query.list();
			Date date = new Date(System.currentTimeMillis());
			for (InCash inCash : list) {
				inCash.setDealDate(date);
			}
			transaction.commit();
			if (list.size() > 0) {
				return list;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}
	
	@Override
	public List<InCash> getIncashRecord(Date dateTime) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "from InCash where dealDate=? order by date";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameter(0, dateTime);
			List<InCash> list = query.list();
			transaction.commit();
			if (list.size() > 0) {
				return list;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}
	

	@Override
	public List<InCash> getIncashRecord(int first) {
		Transaction transaction = null;
		String hql = "";
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			//hql = "from InCash where results is false order by date";
			hql = "from InCash  order by results asc, date desc";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setFirstResult(first);
			query.setMaxResults(10);
			List<InCash> list = query.list();
			transaction.commit();
			if (list.size() > 0) {
				return list;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
	}
	
	@Override
	public Integer getIncashRecordTimeCount() {
		Transaction transaction = null;
		String hql = "";
		int value=0;
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "select count(distinct dealDate) from InCash";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			value = ((Number)(query.uniqueResult())).intValue();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return value;
	}
	
	
	
	@Override
	public List<Date> getIncashRecordTime(int page) {
		Transaction transaction = null;
		String hql = "";
		List<Date> list =new ArrayList<Date>();
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			hql = "select distinct dealDate from InCash where dealDate is not null  order by dealDate desc";
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setMaxResults(10);
			query.setFirstResult(page*10);
			list = query.list();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (transaction != null) {
				transaction = null;
			}
		}
		return list;
	}

	@Override
	public float getInCashNum() {
		return inCashDao.getInCashNum();
	}

	@Override
	public List<Admin> getAdmin(String area) {
		List<Admin> list = adminDao.getAllAdmin(area);
		return list;
	}

	@Override
	public void save(APPVersion appVersion) {
		appVersionDao.save(appVersion);
	}

	@Override
	public void save(APPMsg appMsg) {
		appMsgDao.save(appMsg);

	}

	@Override
	public void save(SysNotice notice) {
		sysNoticeDao.save(notice);
	}

	@Override
	public long getOrderCount(String area) {
		return adminDao.getOrderCount(area);
	}

	@Override
	public long getUserCount(String userType, String area) {
		return adminDao.getUserCount(userType, area);
	}

	@Override
	public List<CouponType> getCouponType() {
		return couponTypeDao.getCouponType();
	}

	@Override
	public void changeCouponValue(String couponType, double value) {
		System.out.println("------------------" + couponType);
		boolean result = couponTypeDao.updateValue(couponType, value);
		System.out.println("------------------" + result);
	}

	@Override
	public String getAPPVersion(String appType) {
		List<APPVersion> list = adminDao.getAppVersion(appType);
		String appVersion;
		if (list != null) {
			appVersion = list.get(0).getAppVersion();
		} else {
			appVersion = "1.0.0";
		}
		return appVersion;
	}

	@Override
	public void setRecord2finsh() {
		inCashDao.setRecord2finsh();
	}

	@Override
	public void deleteAdmin(String username) {
		adminDao.deleteAdmin(username);
	}

	@Override
	public void setCouponType() {
		CouponType ct = new CouponType();
		for (int i = 1; i < 5; i++) {
			ct.setId(String.valueOf(i));
			switch (i) {
			case 1:
				ct.setType("toll");
				break;
			case 2:
				ct.setType("registerCoupon");
				break;
			case 3:
				ct.setType("min");
				break;
			case 4:
				ct.setType("max");
				break;
			default:
				break;
			}
			ct.setValue(0);
			couponTypeDao.save(ct);
		}
	}

	@Override
	public Admin getAdmin(String username, String password) {
		return adminDao.getAdmin(username, password);
	}

	@Override
	public boolean updateUserQX(String username, String userManage, String checkUser, String userMsg, String userAdvice, String userATM, String orderManage, String adminManage, String msgManage,
			String activitySet) {
		List<String> list = new ArrayList<String>();
		list.add(userManage);
		list.add(checkUser);
		list.add(userMsg);
		list.add(userAdvice);
		list.add(userATM);
		list.add(orderManage);
		list.add(adminManage);
		list.add(msgManage);
		list.add(activitySet);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		List<Boolean> list2 = new ArrayList<Boolean>();
		for (int i = 0; i < list.size(); i++) {
			if ("on".equals(list.get(i))) {
				list2.add(i, true);
			} else {
				list2.add(i, false);
			}
		}
		return adminDao.updateAdminQX(username, list2.get(0), list2.get(1), list2.get(2), list2.get(3), list2.get(4), list2.get(5), list2.get(6), list2.get(7), list2.get(8));
	}

	@Override
	public Admin findAdmin(String username) {
		Admin admin = adminDao.findAdmin(username);
		return admin;
	}

	@Override
	public void updateShareHB(ShareWeiXin shareWeiXin) {
		BaseDao<ShareWeiXin> sdao = new BaseDaoImpl<ShareWeiXin>();
		if (sdao.getOne(shareWeiXin, "1") != null) {
			sdao.update(shareWeiXin);
		} else {
			sdao.save(shareWeiXin);
		}
	}

	@Override
	public List<CompanyPaymentDetails> getCPD(int first, String area) {
		return adminDao.getCPD(first, area);
	}

	@Override
	public long getCPDNUM(String area) {
		return adminDao.getCPDNUM(area);
	}

	@Override
	public long getMemberNum(String area) {
		UserDao userDao = new UserDaoImpl();
		return userDao.getMemberNum(area);
	}

	@Override
	public List<Long> getOrderCountList(String area, String year) {
		OrderDao orderDao = new OrderDaoImpl();
		List<Long> list = new ArrayList<Long>();
		int atYear = DateUtil.atYear();
		int month;
		if (Integer.valueOf(year) < atYear) {
			month = 12;
		} else {
			month = DateUtil.atMonth();
		}
		for (int i = 0; i < month; i++) {
			list.add(orderDao.getOrderCount(area, year, i + 1));
		}
		return list;
	}

	@Override
	public List<CompanyPaymentDetails> getCPD(int first, String year, String month, String day,String area) {
		if (year.equals("不限")) {
			return adminDao.getCPD(first, "");
		}
		if (month.equals("不限")) {
			System.out.println("查年");
			String date = year + "-01-01";
			String lastDate = year + "-12-31";
			return adminDao.getCPD(first, date, lastDate,area);
		} else if (day.equals("不限")) {
			System.out.println("查月");
			int m = Integer.valueOf(month);
			if (m < 10) {
				month = "0" + month;
			}
			String date = year + "-" + month + "-01";
			String lastDate = year + "-" + month + "-31";
			return adminDao.getCPD(first, date, lastDate,area);
		} else {
			System.out.println("查日");
			int m = Integer.valueOf(month);
			int d = Integer.valueOf(day);
			if (m < 10) {
				month = "0" + month;
			}
			if (d < 10) {
				day = "0" + day;
			}
			String date = year + "-" + month + "-" + day;
			return adminDao.getCPD(first, date,date+" 23:59:59",area);
		}
	}

	@Override
	public long getCPDNUM(String year, String month, String day,String area) {
		if (year.equals("不限")) {
			adminDao.getCPDNUM("");
		}
		if (month.equals("不限")) {
			String date = year + "-01-01";
			String lastDate = year + "-12-31";
			return adminDao.getCPDNUM(date, lastDate,area);
		} else if (day.equals("不限")) {
			int m = Integer.valueOf(month);
			if (m < 10) {
				month = "0" + month;
			}
			String date = year + "-" + month + "-01";
			String lastDate = year + "-" + month + "-31";
			return adminDao.getCPDNUM(date, lastDate,area);
		} else {
			int m = Integer.valueOf(month);
			int d = Integer.valueOf(day);
			if (m < 10) {
				month = "0" + month;
			}
			if (d < 10) {
				day = "0" + day;
			}
			String date = year + "-" + month + "-" + day;
			return adminDao.getCPDNUM(date);
		}
	}

	@Override
	public double getTollValue(String area) {
		BigDecimal value = new BigDecimal(adminDao.getTollValueByArea(area));
		return value.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	@Override
	public double getPayValue(String area) {
		BigDecimal value = new BigDecimal(adminDao.getPayValueByArea(area));
		return value.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	@Override
	public double getCouponValue(String area) {
		BigDecimal value = new BigDecimal(adminDao.getCouponValueByArea(area));
		return value.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	@Override
	public double getTollValue(String year, String month, String day,String area) {
		if (year.equals("不限")) {
			BigDecimal value = new BigDecimal(adminDao.getTollValueByArea(area));
			return value.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		if (month.equals("不限")) {
			System.out.println("查年");
			String date = year + "-01-01";
			String lastDate = year + "-12-31";
			BigDecimal value = new BigDecimal(adminDao.getTollValue(date, lastDate,area));
			return value.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else if (day.equals("不限")) {
			System.out.println("查月");
			int m = Integer.valueOf(month);
			if (m < 10) {
				month = "0" + month;
			}
			String date = year + "-" + month + "-01";
			String lastDate = year + "-" + month + "-31";
			BigDecimal value = new BigDecimal(adminDao.getTollValue(date, lastDate,area));
			return value.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else {
			System.out.println("查日");
			int m = Integer.valueOf(month);
			int d = Integer.valueOf(day);
			if (m < 10) {
				month = "0" + month;
			}
			if (d < 10) {
				day = "0" + day;
			}
			String date = year + "-" + month + "-" + day;
			BigDecimal value = new BigDecimal(adminDao.getTollValue(date,area));
			return value.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
	}

	@Override
	public double getPayValue(String year, String month, String day,String area) {
		if (year.equals("不限")) {
			return adminDao.getPayValueByArea(area);
		}
		if (month.equals("不限")) {
			System.out.println("查年");
			String date = year + "-01-01";
			String lastDate = year + "-12-31";
			BigDecimal value = new BigDecimal(adminDao.getPayValue(date, lastDate,area));
			return value.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else if (day.equals("不限")) {
			System.out.println("查月");
			int m = Integer.valueOf(month);
			if (m < 10) {
				month = "0" + month;
			}
			String date = year + "-" + month + "-01";
			String lastDate = year + "-" + month + "-31";
			BigDecimal value = new BigDecimal(adminDao.getPayValue(date, lastDate,area));
			return value.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else {
			System.out.println("查日");
			int m = Integer.valueOf(month);
			int d = Integer.valueOf(day);
			if (m < 10) {
				month = "0" + month;
			}
			if (d < 10) {
				day = "0" + day;
			}
			String date = year + "-" + month + "-" + day;
			BigDecimal value = new BigDecimal(adminDao.getPayValue(date,area));
			return value.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
	}

	@Override
	public double getCouponValue(String year, String month, String day,String area) {
		if (year.equals("不限")) {
			return adminDao.getCouponValueByArea(area);
		}
		if (month.equals("不限")) {
			System.out.println("查年");
			String date = year + "-01-01";
			String lastDate = year + "-12-31";
			BigDecimal value = new BigDecimal(adminDao.getCouponValue(date, lastDate,area));
			return value.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else if (day.equals("不限")) {
			System.out.println("查月");
			int m = Integer.valueOf(month);
			if (m < 10) {
				month = "0" + month;
			}
			String date = year + "-" + month + "-01";
			String lastDate = year + "-" + month + "-31";
			BigDecimal value = new BigDecimal(adminDao.getCouponValue(date, lastDate,area));
			return value.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else {
			System.out.println("查日");
			int m = Integer.valueOf(month);
			int d = Integer.valueOf(day);
			if (m < 10) {
				month = "0" + month;
			}
			if (d < 10) {
				day = "0" + day;
			}
			String date = year + "-" + month + "-" + day;
			BigDecimal value = new BigDecimal(adminDao.getCouponValue(date,area));
			return value.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
	}

	@Override
	public InCash getIncashRecord(String inCashId) {
		InCash cash = inCashDao.getInCash(inCashId);
		return cash;
	}
	
	@Override
	public void updateIncash(InCash inCash) {
		inCashDao.updateInCash(inCash);
	}
	
}
