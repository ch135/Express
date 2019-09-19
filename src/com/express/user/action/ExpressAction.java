package com.express.user.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.express.model.AreaNorm;
import com.express.model.Coupon;
import com.express.model.Expense;
import com.express.model.Order;
import com.express.model.User;
import com.express.user.service.ExpressService;
import com.express.user.service.impl.ExpressServiceImpl;
import com.express.util.Constant;
import com.express.util.JsonUtil;
import com.express.util.MsgPush;
import com.express.util.MsgUtil;
import com.express.util.SuperAction;
import com.express.util.ValueUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ExpressAction extends SuperAction {
	private Map<String, Object> dataMap = new HashMap<String, Object>();
	ExpressService dao = new ExpressServiceImpl();
	Order order = new Order();
	
	/**
	 * 快递员确认拿到货物
	 */
	public void cuserGetGoods(){
		String orderId = request.getParameter("orderId");
		order.setOrderId(orderId);
		order.setOrderStaus(3);
		String code = request.getParameter("code");
		String code2 = (String) session.getAttribute("code");
		
		if(ValueUtil.isNull(orderId,code)){
			dataMap.put("result", "缺少参数");
			try {
				JsonUtil.writeToResponse(dataMap);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		if(!session.getId().isEmpty()&&code!=null&&code.equals(code2)){
			dao.updateOrderStatus(order);
			MsgUtil.msg(request.getParameter("mobile"), "您有快递正在出发，请留意接收，订单号为："+orderId);
			dataMap.put("status", "确认揽件");
		}else{
			dataMap.put("status", "验证码错误");
		}
		try {
			JsonUtil.writeToResponse(dataMap);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送收货码
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
public void goodsCode() throws JsonGenerationException, JsonMappingException,
		IOException {
	String mobile = request.getParameter("mobile");
	System.out.println("传过来的手机号：" + mobile);
	// 获得验证码
	String code = MsgUtil.msg(mobile,"您的签收码是：");

	if(ValueUtil.isNull(mobile,code)){
		dataMap.put("result", "缺少参数");
		JsonUtil.writeToResponse(dataMap);
		return;
	}
	
	// 将验证码和手机号保存在session中
	session.setAttribute("code", code);
	session.setAttribute("mobile", mobile);
	dataMap.put("status", "已发送验证码");
	JsonUtil.writeToResponse(dataMap);
}
	
	/**
	 * 验证收货码
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void complete() throws JsonGenerationException, JsonMappingException, IOException{
		String code = request.getParameter("code");
		String code2 = (String) session.getAttribute("code");
		String orderId = request.getParameter("orderId");
		String cuserId = request.getParameter("cuserId");
		String userId = request.getParameter("userId");
		
		if(ValueUtil.isNull(code,orderId,cuserId,userId)){
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
			return;
		}
		order = dao.findOrder(orderId);
		
		if(!session.getId().isEmpty()&&code!=null&&code.equals(code2)){
			
			if(Constant.TOLL<=0){
				Constant.TOLL = dao.getToll();
			}
			
			dao.complete(orderId);
			dao.updateUserOrderCount(cuserId);
			Expense expense = new Expense();
			Expense expense2 = new Expense();
			expense2.setUserid(order.getUserid());
			expense.setUserid(cuserId);
			BigDecimal money = new BigDecimal((order.getOrderFare())*(1-Constant.TOLL));
			dao.cuserIncome(cuserId,money.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
			expense.setMoney(money.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
			expense.setRecord("完成订单获取的收入");
			expense.setSpend(1);
			expense.setDate(new Date());
			dao.saveRecord(expense);
			if(order.getOrderType().equals("toll")){
				BigDecimal money2 = new BigDecimal(order.getGoodsPrice());
				dao.cuserIncome(cuserId,-money2.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
				dao.cuserIncome(order.getUserid(), order.getGoodsPrice());
				expense.setMoney(money2.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
				expense.setRecord("扣除代收费物品的费用");
				expense.setSpend(0);
				expense2.setMoney(order.getGoodsPrice());
				expense2.setRecord("代收费订单回款");
				expense2.setSpend(1);
				expense2.setDate(new Date());
				dao.saveRecord(expense);
				dao.saveRecord(expense2);
			}
//			dao.userAddCoupon(order.getSendMobile());
//			MsgUtil.msg(request.getParameter("mobile"), "您的订单已确认送达，订单号为：",orderId);
			String msg = "您的订单已确认送达，订单号为："+orderId+"，点击查看详情！";
			MsgPush.push(userId, msg, Constant.JPUSH_UCMASTER_SECRET, Constant.JPUSH_UAPPKEY,orderId,1,"订单已完成");
//			String msg2 = "您获得了一张优惠券，请点击查看详情！";
//			MsgPush.push(userId, msg2, Constant.JPUSH_UCMASTER_SECRET, Constant.JPUSH_UAPPKEY,orderId,5,"获得优惠券");
			dataMap.put("status", "货物送达");
			JsonUtil.writeToResponse(dataMap);
		}else{
			dataMap.put("status", "验证码错误");
			JsonUtil.writeToResponse(dataMap);
		}
	}
	
	public void rePublishOrder(){
		String orderId = request.getParameter("orderId");
		String newOrderId = dao.createNewOrder(orderId);
		
		if(ValueUtil.isNull(orderId)){
			dataMap.put("result", "缺少参数");
			try {
				JsonUtil.writeToResponse(dataMap);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		dataMap.put("orderId", newOrderId);
		dataMap.put("status", "下单成功");
		try {
			JsonUtil.writeToResponse(dataMap);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getAllOrder(){
		String userId = request.getParameter("userId");
		int first = Integer.valueOf(request.getParameter("first"));

		if(ValueUtil.isNull(userId)){
			dataMap.put("result", "缺少参数");
			try {
				JsonUtil.writeToResponse(dataMap);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		List<Order> list = dao.getAllOrder(userId,first);
		
		try {
			dataMap.put("order", list);
			JsonUtil.writeToResponse(dataMap);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//获取快递员当前位置
	public void getCuserAddress(){
		String id = request.getParameter("cuserId");
		User user = dao.getUser(id);
		dataMap.put("lat",user.getLatitude());
		dataMap.put("lng",user.getLongitude());
		try {
			JsonUtil.writeToResponse(dataMap);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 投诉订单
	 */
	public void complaint(){
		String userId = request.getParameter("userId");
		String name = request.getParameter("name");
		String cuserId = request.getParameter("cuserId");
		String cname = request.getParameter("cname");
		String complaint = request.getParameter("complaint");
		String orderId = request.getParameter("orderId");
		
		if(ValueUtil.isNull(userId,name,cuserId,cname,complaint,orderId)){
			dataMap.put("result", "缺少参数");
			try {
				JsonUtil.writeToResponse(dataMap);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		if(dao.findOrder(orderId)!=null){
			dao.saveComplaint(userId,name,cuserId,cname,orderId,complaint);
			dataMap.put("status","success");
		}else{
			dataMap.put("status","fail");
		}
		try {
			JsonUtil.writeToResponse(dataMap);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取地区和起步价的列表
	 * @throws ParseException 
	 */
	public void getAreaPrice() throws ParseException{
		String areaName = request.getParameter("areaName");
		List<AreaNorm> areas = dao.getAreaPrice(areaName);
		double areaPrice=5.0;
    	SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
    	String now=sdf.format(new Date());
    	long ns=sdf.parse(now).getTime();
    	for(AreaNorm ar :areas){
    		long start=ar.getStart_time().getTime();
    		long end=ar.getEnd_time().getTime();
    		if(start<=ns&&end>ns){
    			areaPrice=ar.getValue();
    			break;
    		}
    	}
		dataMap.put("value", areaPrice);
		try {
			JsonUtil.writeToResponse(dataMap);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
