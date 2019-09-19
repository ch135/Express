package com.express.transaction.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.express.alipay.util.AlipayCore;
import com.express.model.CompanyPaymentDetails;
import com.express.model.Expense;
import com.express.model.Order;
import com.express.model.User;
import com.express.model.UserChargeMoney;
import com.express.order.service.OrderService;
import com.express.order.service.impl.OrderServiceImpl;
import com.express.transaction.service.PayService;
import com.express.transaction.service.ReceiveService;
import com.express.transaction.service.impl.PayServiceImpl;
import com.express.transaction.service.impl.ReceiveServiceImpl;
import com.express.util.Constant;
import com.express.util.DateUtil;
import com.express.util.MD5Util;
import com.express.util.MsgPush;
import com.express.util.SuperAction;
import com.express.util.WeiXinPayUtil;
import com.express.util.WeiXinPublicPayUtil;
import com.express.util.XMLUtil;

public class ReceiveAction extends SuperAction{

	ReceiveService service = new ReceiveServiceImpl();
	PayService payService = new PayServiceImpl();
	private Expense expense = new Expense();
	OrderService orderService = new OrderServiceImpl();
	public void notifyCheck(){
		Map<String, String> paramsMap = new HashMap<String, String>();
		//遍历request中的参数		
		Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
		String paraName=(String)enu.nextElement(); 
		paramsMap.put(paraName, request.getParameter(paraName));
		}  
		
		String orderId = request.getParameter("out_trade_no");//订单id
		String total_amount = request.getParameter("total_amount");//交易金额
		String seller_id = request.getParameter("seller_id");
		String app_id = request.getParameter("app_id");
		String gmt_payment = request.getParameter("gmt_payment");//支付日期
		String content = AlipayCore.createLinkString(paramsMap);
		boolean signVerified;
		try {
			signVerified = AlipaySignature.rsaCheckV1(paramsMap, Constant.ALIPAY_PUBLIC_KEY, "UTF-8");
			if(signVerified){
				Order order = service.getOrder(orderId);
					if(order!=null){
						String orderFare = String.valueOf(order.getOrderFare()-order.getCoupon());
						//System.out.println("orderFare++=="+orderFare);
						double orderFareValue = Double.parseDouble(orderFare);
						double totalAmountValue=Double.parseDouble(total_amount);
						if(orderFareValue==totalAmountValue&&seller_id.equals(Constant.PID)&&app_id.equals(Constant.APP_ID)){
							try {
								BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
								bf.write("success");
								bf.flush();
								service.reOrderStatus(order);
								expense.setMoney(Double.valueOf(total_amount));
								expense.setUserid(order.getUserid());
								System.out.println("消费时间："+DateUtil.stringToDate(gmt_payment, "yyyy-MM-dd HH:mm:ss"));
								expense.setDate(DateUtil.stringToDate(gmt_payment, "yyyy-MM-dd HH:mm:ss"));
								expense.setId(MD5Util.getUUID());
								expense.setRecord("支付宝支付订单");
								expense.setSpend(0);
								payService.createRecord(order.getOrderId(),order.getUserid(),order.getSendName(),order.getOrderFare(),"收取订单手续费");
								service.record(expense);
								List<User> list = orderService.getCUser(order.getSendLatitude(),order.getSendLongitude());
								List<String> userIds = new ArrayList<String>();
								if (list!=null) {
									for (User user : list) {
										userIds.add(user.getId());
									}
									MsgPush.push(userIds, "有新订单可接！", Constant.JPUSH_CCMASTER_SECRET, Constant.JPUSH_CAPPKEY, "", 5, "");
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}else {
							try {
								BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
								bf.write("failure");
								bf.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}else{
					try {
						BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
						bf.write("failure");
						bf.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		} //调用SDK验证签名
	}
	
	public void wXnotifyCheck(){
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(request.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String str = null;
			while ((str = bf.readLine())!=null) {
				sb.append(str);
			}
			Map<String, String> map = XMLUtil.readStringXmlOut(sb.toString());
			String sign = map.get("sign");
			if(map.get("return_code").toString().equals("SUCCESS")){
				System.out.println("第1层");
				map.remove("sign");
				if(sign.equals(WeiXinPayUtil.getSign(map))){
					Order order = service.getOrder(map.get("out_trade_no").toString());
						if(order!=null){
							service.reOrderStatus(order);
							expense.setMoney(Double.valueOf(map.get("total_fee").toString())/100);
							expense.setUserid(order.getUserid());
							System.out.println("消费时间："+DateUtil.stringToDate(map.get("time_end").toString(), "yyyyMMddHHmmss"));
							expense.setDate(DateUtil.stringToDate(map.get("time_end").toString(), "yyyyMMddHHmmss"));
							expense.setId(MD5Util.getUUID());
							expense.setRecord("微信支付订单");
							expense.setSpend(0);
							payService.createRecord(order.getOrderId(),order.getUserid(),order.getSendName(),order.getOrderFare(),"收取订单手续费");
							service.record(expense);
							List<User> list = orderService.getCUser(order.getSendLatitude(),order.getSendLongitude());
							List<String> userIds = new ArrayList<String>();
							if (list!=null) {
								for (User user : list) {
									userIds.add(user.getId());
								}
								MsgPush.push(userIds, "有新订单可接！", Constant.JPUSH_CCMASTER_SECRET, Constant.JPUSH_CAPPKEY, "", 5, "");
							}
							Map<String, String> map2 = new HashMap<String, String>();
							map2.put("return_code", "SUCCESS");
							map2.put("return_msg", "OK");
							String xmlStr = XMLUtil.map2XmlString(map2);
							BufferedWriter bf2 = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
							bf2.write(xmlStr);
							bf2.flush();
						}else {
							Map<String, String> map2 = new HashMap<String, String>();
							map2.put("return_code", "FAIL");
							map2.put("return_msg", "错误");
							String xmlStr = XMLUtil.map2XmlString(map2);
							BufferedWriter bf2 = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
							bf2.write(xmlStr);
							bf2.flush();
						}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//微信公众号支付回调函数
	public void wXPublicNotifyCheck(){
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(request.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String str = null;
			while ((str = bf.readLine())!=null) {
				sb.append(str);
			}
			Map<String, String> map = XMLUtil.readStringXmlOut(sb.toString());
			String sign = map.get("sign");
			if(map.get("return_code").toString().equals("SUCCESS")){
				System.out.println("第1层");
				map.remove("sign");
				if(sign.equals(WeiXinPublicPayUtil.getSign(map))){
					Order order = service.getOrder(map.get("out_trade_no").toString());
						if(order!=null){
							service.reOrderStatus(order);
							expense.setMoney(Double.valueOf(map.get("total_fee").toString())/100);
							expense.setUserid(order.getUserid());
							System.out.println("消费时间："+DateUtil.stringToDate(map.get("time_end").toString(), "yyyyMMddHHmmss"));
							expense.setDate(DateUtil.stringToDate(map.get("time_end").toString(), "yyyyMMddHHmmss"));
							expense.setId(MD5Util.getUUID());
							expense.setRecord("微信小程序支付订单");
							expense.setSpend(0);
							payService.createRecord(order.getOrderId(),order.getUserid(),order.getSendName(),order.getOrderFare(),"收取订单手续费");
							service.record(expense);
							List<User> list = orderService.getCUser(order.getSendLatitude(),order.getSendLongitude());
							List<String> userIds = new ArrayList<String>();
							if (list!=null) {
								for (User user : list) {
									userIds.add(user.getId());
								}
								MsgPush.push(userIds, "有新订单可接！", Constant.JPUSH_CCMASTER_SECRET, Constant.JPUSH_CAPPKEY, "", 5, "");
							}
							
							Map<String, String> map2 = new HashMap<String, String>();
							map2.put("return_code", "SUCCESS");
							map2.put("return_msg", "OK");
							String xmlStr = XMLUtil.map2XmlString(map2);
							BufferedWriter bf2 = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
							bf2.write(xmlStr);
							bf2.flush();
						}else {
							Map<String, String> map2 = new HashMap<String, String>();
							map2.put("return_code", "FAIL");
							map2.put("return_msg", "错误");
							String xmlStr = XMLUtil.map2XmlString(map2);
							BufferedWriter bf2 = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
							bf2.write(xmlStr);
							bf2.flush();
						}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void aliPayNotify(){
		//遍历request中的参数	
		Map<String, String> paramsMap = new HashMap<String, String>();
		Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
		String paraName=(String)enu.nextElement();  
		paramsMap.put(paraName, request.getParameter(paraName));
		}
		String orderId = request.getParameter("out_trade_no");//订单id
		String total_amount = request.getParameter("total_amount");//交易金额
		String seller_id = request.getParameter("seller_id");
		String app_id = request.getParameter("app_id");
		String gmt_payment = request.getParameter("gmt_payment");//支付日期
		System.out.println(paramsMap);
		boolean signVerified;
		try {
			signVerified = AlipaySignature.rsaCheckV1(paramsMap, Constant.ALIPAY_PUBLIC_KEY, "UTF-8");
			System.out.println(signVerified);
			if(signVerified){
				System.out.println("-------------------1");
				UserChargeMoney user = service.getUser(orderId);
					if(user!=null){
						System.out.println("-------------------2");
						String orderFare = String.valueOf(user.getMoney());
						System.out.println("orderFare++=="+orderFare);
						System.out.println(orderFare+":"+total_amount+":"+seller_id+":"+Constant.PID+":"+app_id+":"+Constant.APP_ID);
						System.out.println(orderFare.equals(total_amount));
						System.out.println(seller_id.equals(Constant.PID));
						System.out.println(app_id.equals(Constant.APP_ID));
						double orderFareValue = Double.parseDouble(orderFare);
						double totalValue = Double.parseDouble(orderFare);
						if(orderFareValue==totalValue&&seller_id.equals(Constant.PID)&&app_id.equals(Constant.APP_ID)){
							try {
								System.out.println("-------------------3");
								BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
								bf.write("success");
								bf.flush();
								service.addBlance(user.getUserId(),Double.valueOf(total_amount));
								expense.setMoney(Double.valueOf(total_amount));
								expense.setUserid(user.getUserId());
								System.out.println("消费时间："+DateUtil.stringToDate(gmt_payment, "yyyy-MM-dd HH:mm:ss"));
								expense.setDate(DateUtil.stringToDate(gmt_payment, "yyyy-MM-dd HH:mm:ss"));
								expense.setId(MD5Util.getUUID());
								expense.setRecord("支付宝充值");
								expense.setSpend(1);
								service.record(expense);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}else {
							try {
								BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
								bf.write("failure");
								bf.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}else{
					try {
						BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
						bf.write("failure");
						bf.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		} //调用SDK验证签名
	}
	
	
	public void wxNotify(){
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(request.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String str = null;
			while ((str = bf.readLine())!=null) {
				sb.append(str);
			}
			Map<String, String> map = XMLUtil.readStringXmlOut(sb.toString());
			String sign = map.get("sign");
			if(map.get("return_code").toString().equals("SUCCESS")){
				System.out.println("第1层");
				map.remove("sign");
				if(sign.equals(WeiXinPayUtil.getSign(map))){
					UserChargeMoney user = service.getUser(map.get("out_trade_no").toString());
						if(user!=null){
							service.addBlance(user.getUserId(),Double.valueOf(map.get("total_fee").toString())/100);
							expense.setMoney(Double.valueOf(map.get("total_fee").toString())/100);
							expense.setUserid(user.getUserId());
							expense.setDate(DateUtil.stringToDate(map.get("time_end").toString(), "yyyyMMddHHmmss"));
							expense.setId(MD5Util.getUUID());
							expense.setRecord("微信充值");
							expense.setSpend(1);
							service.record(expense);
							
							Map<String, String> map2 = new HashMap<String, String>();
							map2.put("return_code", "SUCCESS");
							map2.put("return_msg", "OK");
							String xmlStr = XMLUtil.map2XmlString(map2);
							BufferedWriter bf2 = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
							bf2.write(xmlStr);
							bf2.flush();
						}else {
							Map<String, String> map2 = new HashMap<String, String>();
							map2.put("return_code", "FAIL");
							map2.put("return_msg", "未知错误");
							String xmlStr = XMLUtil.map2XmlString(map2);
							BufferedWriter bf2 = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
							bf2.write(xmlStr);
							bf2.flush();
						}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void wxcNotify(){
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(request.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String str = null;
			while ((str = bf.readLine())!=null) {
				sb.append(str);
			}
			Map<String, String> map = XMLUtil.readStringXmlOut(sb.toString());
			String sign = map.get("sign");
			System.out.println("回调的信息："+map.get("return_msg"));
			if(map.get("return_code").toString().equals("SUCCESS")){
				map.remove("sign");
				if(sign.equals(WeiXinPayUtil.getcSign(map))){
					UserChargeMoney user = service.getUser(map.get("out_trade_no").toString());
						if(user!=null){
							service.addBlance(user.getUserId(),Double.valueOf(map.get("total_fee").toString())/100);
							expense.setMoney(Double.valueOf(map.get("total_fee").toString())/100);
							expense.setUserid(user.getUserId());
							expense.setDate(DateUtil.stringToDate(map.get("time_end").toString(), "yyyyMMddHHmmss"));
							expense.setId(MD5Util.getUUID());
							expense.setRecord("微信充值");
							expense.setSpend(1);
							service.record(expense);
							
							Map<String, String> map2 = new HashMap<String, String>();
							map2.put("return_code", "SUCCESS");
							map2.put("return_msg", "OK");
							String xmlStr = XMLUtil.map2XmlString(map2);
							BufferedWriter bf2 = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
							bf2.write(xmlStr);
							bf2.flush();
						}else {
							Map<String, String> map2 = new HashMap<String, String>();
							map2.put("return_code", "FAIL");
							map2.put("return_msg", "位置错误");
							String xmlStr = XMLUtil.map2XmlString(map2);
							BufferedWriter bf2 = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
							bf2.write(xmlStr);
							bf2.flush();
						}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
