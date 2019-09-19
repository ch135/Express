package com.express.model;

import java.util.Date;

/**
 * 用户明细表
 * @author user
 *
 */
public class Expense {
	private String id;
	private String userid;
	private double money;//支出
	private Date date;
	private String record; //支出或收入的原因
	private int spend;//0表示支出，1表示收入
	private String orderId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getRecord() {
		return record;
	}
	public void setRecord(String record) {
		this.record = record;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public int getSpend() {
		return spend;
	}
	public void setSpend(int spend) {
		this.spend = spend;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
