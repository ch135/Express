package com.express.model;


public class CuserComment {
	private String cuserid;  //快递员id
	private float grade; //评分
	private int attitude; //服务态度
	private int speed; //速度
	private int integrity; //包裹完好
	private int good; //好评
	private int onTime; //准时
	private int timce;//接单次数
	
	public float getGrade() {
		return grade;
	}
	public void setGrade(float grade) {
		this.grade = grade;
	}
	public int getAttitude() {
		return attitude;
	}
	public void setAttitude(int attitude) {
		this.attitude = attitude;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getIntegrity() {
		return integrity;
	}
	public void setIntegrity(int integrity) {
		this.integrity = integrity;
	}
	public int getGood() {
		return good;
	}
	public void setGood(int good) {
		this.good = good;
	}
	public int getOnTime() {
		return onTime;
	}
	public void setOnTime(int onTime) {
		this.onTime = onTime;
	}
	public String getCuserid() {
		return cuserid;
	}
	public void setCuserid(String cuserid) {
		this.cuserid = cuserid;
	}
	public int getTimce() {
		return timce;
	}
	public void setTimce(int timce) {
		this.timce = timce;
	}
	
}
