package com.express.model;

import java.util.Date;


/**
 * �û���
 * @author Cbs
 *
 */
public class User {
	
	private String id;//�û�id
	private String loginPassword;//��¼����
	private String payPassword;//֧������
	private String mobile;//�ֻ���
	private String name;//��ʵ����
	private String sex;//�Ա�
	private String identity;//���֤��
	private String path;//�û�ͷ��λ��
	private double balance;//���
	private int jifen;//����
	private String pingjia;//����
	private float pingfen;//ƽ������
	private int order_count;//��ʷ�ӵ���
	private int credit;//����ֵ
	private boolean accept_able;//�Ƿ�ɽӵ�
	private boolean send_able;//�Ƿ�ɷ���
	private double longitude;//����
	private double latitude;//γ��
	private String address;//�û���ǰ��λ��ַ
	private String role;//�û���ɫ common:��ͨ�û� courier:���Ա 
	private String state;//�û���֤״̬  0:δ��֤, 1:һ����֤ ,2:������֤,3:��֤�У�-1:��֤ʧ��
	private boolean pass;//�Ƿ��ⵣ��
	private Date forbidTime;//��ֹ�ӵ�����
	private String unionid;//΢��unionid
	private String openId;//΢��id
	private String loginStatus;//��¼״̬
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public String getPayPassword() {
		return payPassword;
	}
	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public int getJifen() {
		return jifen;
	}
	public void setJifen(int jifen) {
		this.jifen = jifen;
	}
	public String getPingjia() {
		return pingjia;
	}
	public void setPingjia(String pingjia) {
		this.pingjia = pingjia;
	}
	public float getPingfen() {
		return pingfen;
	}
	public void setPingfen(float pingfen) {
		this.pingfen = pingfen;
	}
	public int getOrder_count() {
		return order_count;
	}
	public void setOrder_count(int order_count) {
		this.order_count = order_count;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean getAccept_able() {
		return accept_able;
	}
	public void setAccept_able(boolean accept_able) {
		this.accept_able = accept_able;
	}
	public boolean getSend_able() {
		return send_able;
	}
	public void setSend_able(boolean send_able) {
		this.send_able = send_able;
	}
	public boolean getPass() {
		return pass;
	}
	public void setPass(boolean pass) {
		this.pass = pass;
	}
	public Date getForbidTime() {
		return forbidTime;
	}
	public void setForbidTime(Date forbidTime) {
		this.forbidTime = forbidTime;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}
	
	

}
