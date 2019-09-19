package com.express.database.dao;

import java.util.List;

import com.express.model.APPVersion;
import com.express.model.Admin;
import com.express.model.CompanyPaymentDetails;
import com.express.model.SysNotice;

public interface AdminDao {
	
	public List<Admin>getAllAdmin(String area);

	List<SysNotice> getAllNotice(int first);

	long getOrderCount(String area);

	long getUserCount(String userType,String area);

	void deleteAdmin(String username);

	Admin getAdmin(String username,String pwd);

	boolean updateAdminQX(String username, boolean userManage, boolean checkUser,
			boolean userMsg, boolean userAdvice, boolean userATM,
			boolean orderManage, boolean adminManage, boolean msgManage,
			boolean activitySet);

	Admin findAdmin(String username);

	long getCPDNUM(String area);

	List<CompanyPaymentDetails> getCPD(int first,String area);

	List<CompanyPaymentDetails> getCPD(int first, String date, String lastDate,String area);

	//List<CompanyPaymentDetails> getCPD(int first, String date);

	long getCPDNUM(String date, String lastDate,String area);

	//long getCPDNUM(String date);

	double getTollValueByArea(String area);

	double getPayValueByArea(String area);

	double getCouponValueByArea(String area);

	double getTollValue(String date, String lastDate,String area);

	double getTollValue(String date,String area);

	double getPayValue(String date, String lastDate,String area);

	double getPayValue(String date,String area);

	double getCouponValue(String date, String lastDate,String area);

	double getCouponValue(String date,String area);

	List<APPVersion> getAppVersion(String appType);
	
}
