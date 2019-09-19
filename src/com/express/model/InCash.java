package com.express.model;

import java.util.Date;

public class InCash {
	private String id;
	private String mobile;
	private String name;
	private String inCashType;// 提现方式 WeiXin：微信提现；AliPay 支付宝；Bank：银行
	private double balance;// 金额
	private Date date;// 日期
	private String account;// 账号
	private boolean results;// 是否已处理
	private Date dealDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInCashType() {
		return inCashType;
	}

	public void setInCashType(String inCashType) {
		this.inCashType = inCashType;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public boolean isResults() {
		return results;
	}

	public void setResults(boolean results) {
		this.results = results;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getDealDate() {
		return dealDate;
	}

	public void setDealDate(Date dealDate) {
		this.dealDate = dealDate;
	}

}
