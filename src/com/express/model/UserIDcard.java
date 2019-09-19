package com.express.model;

/**
 * 用户认证提交的临时数据
 * @author Lwk
 *
 */
public class UserIDcard {
	
	private String id;//用户id
	private String mobile; //用户手机
	private String name; //用户真实姓名
	private String icon;//用户头像
	private String identity; //用户身份证号
	private String sex;//性别
	private String path1; //用户身份证照片路径
	private String path2;
	private String path3;
	private String results;//审核结果
	private String reason;//失敗原因
	
	
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
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPath1() {
		return path1;
	}
	public void setPath1(String path1) {
		this.path1 = path1;
	}
	public String getPath2() {
		return path2;
	}
	public void setPath2(String path2) {
		this.path2 = path2;
	}
	public String getPath3() {
		return path3;
	}
	public void setPath3(String path3) {
		this.path3 = path3;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getResults() {
		return results;
	}
	public void setResults(String results) {
		this.results = results;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}

	
}
