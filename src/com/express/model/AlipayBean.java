package com.express.model;

public class AlipayBean {
	private String body;//对一笔交易的具体描述信息
	private String subject;//交易标题
	private String out_trade_no;//商户网站唯一订单号
	private String timeout_express;//该笔订单允许的最晚付款时间，逾期将关闭交易
	private String total_amount;//订单总金额，单位为元，精确到小数点后两位
	private String seller_id;//收款支付宝用户ID。 如果该值为空，则默认为商户签约账号对应的支付宝用户ID
	private String product_code;//销售产品码，商家和支付宝签约的产品码
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getTimeout_express() {
		return timeout_express;
	}
	public void setTimeout_express(String timeout_express) {
		this.timeout_express = timeout_express;
	}
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

}
