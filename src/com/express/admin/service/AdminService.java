package com.express.admin.service;

import java.util.Date;
import java.util.List;

import com.express.model.APPMsg;
import com.express.model.APPVersion;
import com.express.model.Admin;
import com.express.model.CompanyPaymentDetails;
import com.express.model.CouponType;
import com.express.model.InCash;
import com.express.model.ShareWeiXin;
import com.express.model.SysNotice;


/**
 * 管理员业务接口
 * @author Administrator
 *
 */
public interface AdminService {
	
	public boolean adminLogin(Admin admin);
	
	public boolean addAdmin(Admin admin);
	
	public boolean checkAdmin(Admin admin) throws NoSuchFieldException;
	
	public boolean rePwd(Admin admin) throws NoSuchFieldException;
	
	public List<Admin> getAllAdmin(int first);

	public List<SysNotice> getNotice(int first);

	public List<SysNotice> getAllNotice();

	public List<InCash> getIncashRecord();

	public List<InCash> getIncashRecord(int first);

	public float getInCashNum();

	public List<Admin> getAdmin(String area);

	public void save(APPVersion appVersion);

	public void save(APPMsg appMsg);

	public void save(SysNotice notice);

	public long getOrderCount(String area);

	public long getUserCount(String userType,String area);

	public List<CouponType> getCouponType();

	public void changeCouponValue(String couponType, double value);

	public String getAPPVersion(String string);

	public void setRecord2finsh();

	public void deleteAdmin(String username);

	public void setCouponType();

	public Admin getAdmin(String username, String password);

	public boolean updateUserQX(String username, String userManage,
			String checkUser, String userMsg, String userAdvice,
			String userATM, String orderManage, String adminManage,
			String msgManage, String activitySet);

	public Admin findAdmin(String username);

	public void updateShareHB(ShareWeiXin shareWeiXin);

	public List<CompanyPaymentDetails> getCPD(int i,String area);

	public long getCPDNUM(String area);

	public long getMemberNum(String string);

	public List<Long> getOrderCountList(String area, String year);

	public List<CompanyPaymentDetails> getCPD(int i, String year,String month, String day,String area);

	public long getCPDNUM(String year,String month, String day,String area);

	public double getTollValue(String area);

	public double getPayValue(String area);

	public double getCouponValue(String area);

	public double getTollValue(String year, String month, String day,String area);

	public double getPayValue(String year, String month, String day,String area);

	public double getCouponValue(String year, String month, String day,String area);

	Integer getIncashRecordTimeCount();

	List<Date> getIncashRecordTime(int page);

	List<InCash> getIncashRecord(Date date);
	
	InCash getIncashRecord(String inCashId);

	void updateIncash(InCash InCash);

}
