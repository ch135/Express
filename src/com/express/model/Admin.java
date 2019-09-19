package com.express.model;

/**
 * 管理员类
 * @author Cbs
 *
 */
public class Admin {

	private String id;
	private String username;//管理员用户名
	private String password;//管理员密码
	private String role;//角色
	private boolean userManage; //用户管理
	private boolean checkUser; // 用户审核
	private boolean userMsg; // 用户信息
	private boolean userAdvice; // 用户建议
	private boolean userATM; // 用户提现
	private boolean orderManage;  // 订单管理
	private boolean adminManage; // 管理员管理
	private boolean msgManage; // 消息管理
	private boolean activitySet; // 活动设置
	private String city;//城市
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isCheckUser() {
		return checkUser;
	}
	public void setCheckUser(boolean checkUser) {
		this.checkUser = checkUser;
	}
	public boolean isUserMsg() {
		return userMsg;
	}
	public void setUserMsg(boolean userMsg) {
		this.userMsg = userMsg;
	}
	public boolean isUserAdvice() {
		return userAdvice;
	}
	public void setUserAdvice(boolean userAdvice) {
		this.userAdvice = userAdvice;
	}
	public boolean isUserATM() {
		return userATM;
	}
	public void setUserATM(boolean userATM) {
		this.userATM = userATM;
	}
	public boolean isOrderManage() {
		return orderManage;
	}
	public void setOrderManage(boolean orderManage) {
		this.orderManage = orderManage;
	}
	public boolean isAdminManage() {
		return adminManage;
	}
	public void setAdminManage(boolean adminManage) {
		this.adminManage = adminManage;
	}
	public boolean isMsgManage() {
		return msgManage;
	}
	public void setMsgManage(boolean msgManage) {
		this.msgManage = msgManage;
	}
	public boolean isActivitySet() {
		return activitySet;
	}
	public void setActivitySet(boolean activitySet) {
		this.activitySet = activitySet;
	}
	public boolean isUserManage() {
		return userManage;
	}
	public void setUserManage(boolean userManage) {
		this.userManage = userManage;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
}
