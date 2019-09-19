package com.express.model;

import java.util.Date;

public class Order {
//	private long id;
	private String orderId;//订单编号
	private String userid;//订单所属用户
	private Date requestDate;//下单时间
	private Date receiveDate;//快递员接单时间
	private Date finshDate;//订单完成时间
	private String sendName;//寄件人名字
	private double sendLongitude;//寄件人经度
	private double sendLatitude;//寄件人纬度
	private String sendAddress;//寄件人地址
	private String sendMobile;//寄件人手机
	private String receiveName;//收件人名字
	private double receiveLongitude;//收件人经度
	private double receiveLatitude;//收件人纬度
	private String receiveAddress;//收件人地址
	private String receiveMobile;//收件人手机
	private String goodsDetail;//物品描述
	private int goodsWeight;//物品重量
	private String goodsPic;//物品图片
	private int supportValue;//保价金
	private double orderFare;//订单价格
	private double payFare;//支付价格
	private String orderRemark;//订单备注
	//0:未支付 1:未接单 2:已接单 3:派送中 4:已完成 5:已评价 -1:用户已取消 -2:快递员取消 -3:订单自动过期 -4:管理员关闭订单
	private int orderStaus;//订单状态(包括失效)        
	private String courierid;//接单快递员
	private String cname;//快递员姓名
	private String cmobile;//快递员手机
	private String cpath;//快递员头像
	private float cgrade;//快递员评分
	private float orderGrade;//订单评分
	private String couponId;//优惠券id
	private double coupon;//优惠券
	private double goodsPrice;//物品价格 普通订单不用
	private String orderType; //订单类型  普通订单：general 代收费：toll
	private Double addition=0d;//附加费
	private Double sendDistance;//快递到目的地的距离
	
	public Double getSendDistance() {
		return sendDistance;
	}
	public void setSendDistance(Double sendDistance) {
		this.sendDistance = sendDistance;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getSendName() {
		return sendName;
	}
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	public double getSendLongitude() {
		return sendLongitude;
	}
	public void setSendLongitude(double sendLongitude) {
		this.sendLongitude = sendLongitude;
	}
	public double getSendLatitude() {
		return sendLatitude;
	}
	public void setSendLatitude(double sendLatitude) {
		this.sendLatitude = sendLatitude;
	}
	public String getSendAddress() {
		return sendAddress;
	}
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
	public String getSendMobile() {
		return sendMobile;
	}
	public void setSendMobile(String sendMobile) {
		this.sendMobile = sendMobile;
	}
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	public double getReceiveLongitude() {
		return receiveLongitude;
	}
	public void setReceiveLongitude(double receiveLongitude) {
		this.receiveLongitude = receiveLongitude;
	}
	public double getReceiveLatitude() {
		return receiveLatitude;
	}
	public void setReceiveLatitude(double receiveLatitude) {
		this.receiveLatitude = receiveLatitude;
	}
	public String getReceiveAddress() {
		return receiveAddress;
	}
	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}
	public String getReceiveMobile() {
		return receiveMobile;
	}
	public void setReceiveMobile(String receiveMobile) {
		this.receiveMobile = receiveMobile;
	}
	public String getGoodsDetail() {
		return goodsDetail;
	}
	public void setGoodsDetail(String goodsDetail) {
		this.goodsDetail = goodsDetail;
	}
	public int getGoodsWeight() {
		return goodsWeight;
	}
	public void setGoodsWeight(int goodsWeight) {
		this.goodsWeight = goodsWeight;
	}
	public String getGoodsPic() {
		return goodsPic;
	}
	public void setGoodsPic(String goodsPic) {
		this.goodsPic = goodsPic;
	}
	public int getSupportValue() {
		return supportValue;
	}
	public void setSupportValue(int supportValue) {
		this.supportValue = supportValue;
	}
	public double getOrderFare() {
		return orderFare;
	}
	public void setOrderFare(double orderFare) {
		this.orderFare = orderFare;
	}
	public String getOrderRemark() {
		return orderRemark;
	}
	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}
	public int getOrderStaus() {
		return orderStaus;
	}
	public void setOrderStaus(int orderStaus) {
		this.orderStaus = orderStaus;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public Date getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}
	public Date getFinshDate() {
		return finshDate;
	}
	public void setFinshDate(Date finshDate) {
		this.finshDate = finshDate;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getCourierid() {
		return courierid;
	}
	public void setCourierid(String courierid) {
		this.courierid = courierid;
	}
	public float getOrderGrade() {
		return orderGrade;
	}
	public void setOrderGrade(float orderGrade) {
		this.orderGrade = orderGrade;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getCmobile() {
		return cmobile;
	}
	public void setCmobile(String cmobile) {
		this.cmobile = cmobile;
	}
	public String getCpath() {
		return cpath;
	}
	public void setCpath(String cpath) {
		this.cpath = cpath;
	}
	public float getCgrade() {
		return cgrade;
	}
	public void setCgrade(float cgrade) {
		this.cgrade = cgrade;
	}
	public double getCoupon() {
		return coupon;
	}
	public void setCoupon(double coupon) {
		this.coupon = coupon;
	}
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	public double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public Double getAddition() {
		return addition;
	}
	public void setAddition(Double addition) {
		this.addition = addition;
	}
	public double getPayFare() {
		return payFare;
	}
	public void setPayFare(double payFare) {
		this.payFare = payFare;
	}
}
