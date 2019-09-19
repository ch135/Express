package com.express.order.action;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.junit.Test;

import com.express.model.Coupon;
import com.express.model.CuserComment;
import com.express.model.Expense;
import com.express.model.Order;
import com.express.model.User;
import com.express.order.service.OrderService;
import com.express.order.service.impl.OrderServiceImpl;
import com.express.transaction.service.PayService;
import com.express.transaction.service.impl.PayServiceImpl;
import com.express.util.Constant;
import com.express.util.DateUtil;
import com.express.util.JsonUtil;
import com.express.util.MsgPush;
import com.express.util.PriceUtil;
import com.express.util.RandomUtil;
import com.express.util.SuperAction;
import com.express.util.ValueUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class OrderAction extends SuperAction {
	private Map<String, Object> dataMap = new HashMap<String, Object>();
	OrderService dao = new OrderServiceImpl();
	Expense expense = new Expense();
	Order order = new Order();
	User user = new User();

	private List<File> upload;
	private List<String> uploadContentType;
	private List<String> uploadFileName;

	public List<File> getUpload() {
		return upload;
	}

	public void setUpload(List<File> upload) {
		this.upload = upload;
	}

	public List<String> getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(List<String> uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public List<String> getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(List<String> uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	@Test
	public void test1() {
		List<String> userIds = new ArrayList<String>();
		userIds.add("f3e60bbf79314d358d543894fb081fd1");
		MsgPush.push(userIds, "有新订单可接！", Constant.JPUSH_CCMASTER_SECRET, Constant.JPUSH_CAPPKEY, "", 5, "");
	}

	/**
	 * 创建订单
	 * 
	 * @param <T>
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ParseException 
	 */
	public void createOrder() throws IOException, InterruptedException, ParseException {
		if (request.getParameter("order") == null) {
			System.out.println("没有收到数据，死掉了");
			dataMap.put("status", "下单失败");
			JsonUtil.writeToResponse(dataMap);
			return;
		}
		String area = request.getParameter("area");
		if (area == null) {
			area = "default";
		}
		Order order = JsonUtil.jsonToBean(request.getParameter("order"), Order.class);
		if (!dao.checkUserAble(order.getUserid())) {
			dataMap.put("status", "权限不足");
			JsonUtil.writeToResponse(dataMap);
			return;
		}
		double orderFace;
		Coupon coupon = dao.checkCoupon(order.getCouponId());
		if (coupon != null) {
			orderFace = PriceUtil.orderPrice(order.getSendLatitude(), order.getSendLongitude(), order.getReceiveLatitude(), order.getReceiveLongitude(), coupon.getValue(), area);
			dao.deleteCoupon(order.getCouponId());
		} else {
			orderFace = PriceUtil.orderPrice(order.getSendLatitude(), order.getSendLongitude(), order.getReceiveLatitude(), order.getReceiveLongitude(), 0, area);
		}

		Long time = System.currentTimeMillis();
		if (orderFace == 0.0) {
			order.setOrderStaus(1);
			List<User> list = dao.getCUser(order.getSendLatitude(), order.getSendLongitude());
			List<String> userIds = new ArrayList<String>();
			if (list != null) {
				for (User user : list) {
					if ("on".equals(user.getLoginStatus()))
						userIds.add(user.getId());
				}
				MsgPush.push(userIds, "有新订单可接！", Constant.JPUSH_CCMASTER_SECRET, Constant.JPUSH_CAPPKEY, "", 5, "");
			}
		} else {
			order.setOrderStaus(0);
		}
		order.setRequestDate(new Date());
		double addition = order.getAddition();
		double couponValue = 0;
		if (coupon != null) {
			couponValue = coupon.getValue();
		}
		order.setOrderFare(orderFace + addition + couponValue);
		order.setPayFare(orderFace + addition);
		order.setOrderId(RandomUtil.getRandomNum() + time.toString() + RandomUtil.getRandomNum());
		String keepPath = "C:/Express/images/goodspic";
		File file = new File(keepPath);
		String path = "";
		if (!file.exists()) {
			file.mkdir();
		}
		for (int i = 0; i < upload.size(); i++) {
			FileUtils.copyFile(upload.get(i), new File(file, order.getOrderId() + i + ".jpg"));
		}
		for (int j = 0; j < upload.size(); j++) {
			path = path + Constant.PATH_GPIC + order.getOrderId() + j + ".jpg";
			if (j == upload.size() - 1) {
				break;
			}
			path = path + ",";
		}
		order.setGoodsPic(path);
		dao.create(order);
		dataMap.put("status", "下单成功");
		dataMap.put("order", JsonUtil.beanToJson(order));
		JsonUtil.writeToResponse(dataMap);

	}

	/**
	 * 浏览可接订单
	 * 
	 * @throws IOException
	 */
	public void browseOrder() throws IOException {
		User user = (User) session.getAttribute("user");
		String status = "";
		if (user != null)
			status = user.getLoginStatus();
		if ("off".equals(status)) {
			dataMap.put("order", JsonUtil.beanToJson(new ArrayList()));
		} else {
			Double receiveLongitude = Double.valueOf(request.getParameter("receiveLongitude"));
			Double receiveLatitude = Double.valueOf(request.getParameter("receiveLatitude"));
			int first = Integer.valueOf(request.getParameter("first"));
			List<Order> order = dao.rangeFindOrder(first, receiveLongitude, receiveLatitude);
			Iterator<Order> iterator = order.iterator();
			List<Order> orders=new ArrayList<Order>();
			while (iterator.hasNext()) {
				Order order2 = iterator.next();
				if (checked(order2)) {
					iterator.remove();
				}else{
					double sendDistance=PriceUtil.getDistant(order2.getSendLatitude(),order2.getSendLongitude(),order2.getReceiveLatitude(),order2.getReceiveLongitude());
					System.out.println("快递距离为"+sendDistance);
					order2.setSendDistance(sendDistance);
					orders.add(order2);
				}
			}
			dataMap.put("order", JsonUtil.beanToJson(orders));
		}
		JsonUtil.writeToResponse(dataMap);
	}

	public Boolean checked(Order order) {
		Boolean flag = false;
		Long orderRquestTime = DateUtil.date2Long(order.getRequestDate());
		Long newDate = System.currentTimeMillis();
		Long min = newDate - orderRquestTime;
		if (min > 1800000) {
			flag = true;
			if (order.getOrderStaus() == 0) {
				Order order1 = new Order();
				order1.setOrderId(order.getOrderId());
				order1.setOrderStaus(-3);
				dao.updateOrderStatus(order1);
			} else if (order.getOrderStaus() == 1) {
				Order order1 = new Order();
				order1.setOrderId(order.getOrderId());
				order1.setOrderStaus(-3);
				dao.updateOrderStatus(order1);
				PayService payService = new PayServiceImpl();
				if (order.getOrderStaus() == 1) {
					expense.setUserid(order.getUserid());
					expense.setMoney(order.getOrderFare());
					expense.setRecord("取消订单回款");
					expense.setSpend(1);
					expense.setDate(new Date());
					dao.saveRecord(expense);
					dao.rollBack(order.getUserid(), order.getOrderFare());
					payService.createRecord(order.getOrderId(), order.getUserid(), order.getSendName(), -order.getOrderFare(), "订单自动取消，回退订单手续费");
				}
			}
		}
		return flag;
	}

	/**
	 * 用户通过id查找所有订单
	 * 
	 * @throws IOException
	 */
	public void findAllOrder() throws IOException {
		int first = Integer.valueOf(request.getParameter("first"));
		String flag = request.getParameter("flag");
		String userId = request.getParameter("id");

		if (ValueUtil.isNull(flag, userId)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
			return;
		}

		System.out.println("查询的用户id：" + userId);
		String paramKey = null;
		// 0表示普通用户，1表示快递员
		if (flag.equals("0")) {
			paramKey = "userid";
		} else {
			paramKey = "courierid";
		}
		List<Order> order = dao.findOrderById(first, userId, paramKey);
		dataMap.put("order", JsonUtil.beanToJson(order));
		JsonUtil.writeToResponse(dataMap);
	}

	/**
	 * 快递员接单
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws NoSuchFieldException
	 */
	public void receiveOrder() throws JsonParseException, JsonMappingException, IOException, NoSuchFieldException {

		String userId = request.getParameter("userId");
		String orderId = request.getParameter("orderId");
		String courierid = request.getParameter("courierid");
		String cMobile = request.getParameter("cMobile");
		String cGrade = request.getParameter("cGrade");
		String cName = request.getParameter("cName");
		String cPath = request.getParameter("cPath");

		if (ValueUtil.isNull(userId, courierid, orderId, cMobile, cGrade, cName, cPath)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
			return;
		}

		if (!dao.checkCUserAble(courierid)) {
			if (dao.checkTime(courierid)) {
				dao.updateCUserAble(courierid);
			} else {
				dataMap.put("status", "权限不足");
				JsonUtil.writeToResponse(dataMap);
				return;
			}
		}

		order.setCourierid(courierid);
		order.setOrderId(orderId);
		order.setCmobile(cMobile);
		order.setCgrade(Float.valueOf(cGrade));
		order.setCname(cName);
		order.setCpath(cPath);

		Order order1 = dao.findOrder(order);
		User user = dao.findUser(courierid);
		if (!user.getPass()) {
			if (user.getBalance() < order1.getOrderFare() + order.getGoodsPrice()) {
				dataMap.put("status", "押金不足");
				JsonUtil.writeToResponse(dataMap);
				return;
			}
		}
		if (dao.receive(order)) {
			String msg = "您的订单【" + order.getOrderId() + "】已被接，请查看！";
			MsgPush.push(userId, msg, Constant.JPUSH_UCMASTER_SECRET, Constant.JPUSH_UAPPKEY, order.getOrderId(), 0, "快递员正在派送");
			System.out.println("接单成功");
			dataMap.put("status", "接单成功");
			JsonUtil.writeToResponse(dataMap);
		} else {
			System.out.println("失败？");
			dataMap.put("status", "接单失败");
			JsonUtil.writeToResponse(dataMap);
		}

	}

	/**
	 * 查找订单
	 * 
	 * @return
	 * @throws IOException
	 */
	@SkipValidation
	public void findOrder() throws IOException {
		String orderId = request.getParameter("orderId");

		if (ValueUtil.isNull(orderId)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
			return;
		}

		order.setOrderId(orderId);
		if (dao.findOrder(order) != null) {
			order = dao.findOrder(order);
			System.out.println("进来了1");
			Long orderRquestTime = DateUtil.date2Long(order.getRequestDate());
			Long newDate = System.currentTimeMillis();
			Long min = newDate - orderRquestTime;
			if (min > 1800000) {
				System.out.println("进来了");
				if (order.getOrderStaus() == 0) {
					Order order1 = new Order();
					order1.setOrderId(orderId);
					order1.setOrderStaus(-3);
					dao.updateOrderStatus(order1);
				} else if (order.getOrderStaus() == 1) {
					System.out.println("进来了2");
					Order order1 = new Order();
					order1.setOrderId(orderId);
					order1.setOrderStaus(-3);
					dao.updateOrderStatus(order1);
					PayService payService = new PayServiceImpl();
					if (order.getOrderStaus() == 1) {
						expense.setUserid(order.getUserid());
						expense.setMoney(order.getOrderFare());
						expense.setRecord("取消订单回款");
						expense.setSpend(1);
						expense.setDate(new Date());
						dao.saveRecord(expense);
						dao.rollBack(order.getUserid(), order.getOrderFare());
						payService.createRecord(order.getOrderId(), order.getUserid(), order.getSendName(), -order.getOrderFare(), "订单自动取消，回退订单手续费");
					}
				}
			}
			dataMap.put("order", JsonUtil.beanToJson(dao.findOrder(order)));
			dataMap.put("status", "查询成功");
			JsonUtil.writeToResponse(dataMap);
		} else {
			dataMap.put("status", "查询失败");
			JsonUtil.writeToResponse(dataMap);
		}
	}

	/**
	 * 通过id和订单状态查询订单
	 * 
	 * @throws NoSuchFieldException
	 * @throws IOException
	 */
	public void getOrder() throws NoSuchFieldException, IOException {
		String id = request.getParameter("id");
		if (ValueUtil.isNull(id)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
			return;
		}
		int first = Integer.valueOf(request.getParameter("first"));
		order.setCourierid(id);
		List<Order> list = dao.getOrder(order, first);
		List<Order> orders=new ArrayList<Order>();
		if (list != null) {
			for(Order order:list){
				double sendDistance=PriceUtil.getDistant(order.getSendLatitude(),order.getSendLongitude(),order.getReceiveLatitude(),order.getReceiveLongitude());
				System.out.println("快递距离为"+sendDistance);
				order.setSendDistance(sendDistance);
				orders.add(order);
			}
			dataMap.put("order", JsonUtil.beanToJson(orders));
			dataMap.put("status", "查询成功");
			JsonUtil.writeToResponse(dataMap);
		} else {
			dataMap.put("order", JsonUtil.beanToJson(list));
			dataMap.put("status", "查无订单");
			JsonUtil.writeToResponse(dataMap);
		}
	}

	/**
	 * 用户评分
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void grade() throws JsonParseException, JsonMappingException, IOException {
		String comment = request.getParameter("comment");

		if (ValueUtil.isNull(comment)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
			return;
		}

		CuserComment c = JsonUtil.jsonToBean(comment, CuserComment.class);
		order.setOrderId(request.getParameter("orderId"));
		order.setOrderStaus(5);
		order.setOrderGrade(c.getGrade());
		dao.updateOrderStatus(order);
		dao.saveGrade(c);
		dataMap.put("status", "评价成功");
		JsonUtil.writeToResponse(dataMap);
	}

	/**
	 * 普通用户取消订单
	 */
	public void uCancelOrder() {
		String orderId = request.getParameter("orderId");

		if (ValueUtil.isNull(orderId)) {
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

		order.setOrderId(orderId);
		Order order1 = dao.findOrder(order);
		if (order1 != null) {
			PayService payService = new PayServiceImpl();
			String msg = "订单号为【" + orderId + "】已被用户取消，请点击查看详情！";
			if (order1.getOrderStaus() == 0 || order1.getOrderStaus() == 1) {
				// 0:未支付 1:未接单
				order.setOrderStaus(-1);
				dao.updateOrderStatus(order);
				if (order1.getOrderStaus() == 1) {
					expense.setUserid(order1.getUserid());
					expense.setMoney(order1.getPayFare());
					expense.setRecord("取消订单回款");
					expense.setSpend(1);
					expense.setDate(new Date());
					dao.saveRecord(expense);
					dao.rollBack(order1.getUserid(), order1.getPayFare());
					payService.createRecord(order1.getOrderId(), order1.getUserid(), order1.getSendName(), -order1.getPayFare(), "用户取消订单，回退订单手续费");
				}
				dataMap.put("status", "订单取消成功");
			} else if (order1.getOrderStaus() == 2) {
				order.setOrderStaus(-1);
				expense.setUserid(order1.getUserid());
				expense.setMoney(order1.getPayFare() * 0.8);
				expense.setRecord("取消订单回款");
				expense.setSpend(1);
				expense.setDate(new Date());
				dao.saveRecord(expense);
				dao.updateOrderStatus(order);
				// 2:已接单
				// 退换金额给用户
				dao.rollBack(order1.getUserid(), order1.getPayFare() * 0.8);
				// 补偿快递员
				dao.rollBack(order1.getCourierid(), order1.getOrderFare() * 0.2);
				Expense expense2 = new Expense();
				expense2.setUserid(order1.getCourierid());
				expense2.setMoney(order1.getPayFare() * 0.2);
				expense2.setRecord("用户取消订单赔款");
				expense2.setSpend(1);
				expense2.setDate(new Date());
				dao.saveRecord(expense2);
				payService.createRecord(order1.getOrderId(), order1.getUserid(), order1.getSendName(), -order1.getPayFare(), "用户取消订单，回退订单手续费");
				MsgPush.push(order1.getCourierid(), msg, Constant.JPUSH_CCMASTER_SECRET, Constant.JPUSH_CAPPKEY, orderId, 2, "订单被取消");
				dataMap.put("status", "订单取消成功");
			} else {
				try {
					dataMap.put("status", "订单取消失败");
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
	 * 快递员取消订单
	 */
	public void cCancelOrder() {
		String orderId = request.getParameter("orderId");

		if (ValueUtil.isNull(orderId)) {
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

		order.setOrderId(orderId);
		Order order1 = dao.findOrder(order);
		if (order1 != null) {
			String msg = "订单号为【" + orderId + "】已被送件人取消，请点击查看详情！";
			PayService payService = new PayServiceImpl();
			// 已接单，未揽件
			if (order1.getOrderStaus() == 2) {
				order.setOrderStaus(1);
				dao.updateOrderStatus(order);
//				dao.rollBack(order1.getUserid(), order1.getOrderFare());
				dao.reduce(order1.getCourierid(), 10);
				// 已经揽件
				MsgPush.push(order1.getUserid(), msg, Constant.JPUSH_UCMASTER_SECRET, Constant.JPUSH_UAPPKEY, orderId, 2, "订单被取消，等待重新接收");
				dataMap.put("status", "订单取消成功");
			} else if (order1.getOrderStaus() == 3) {
				if (session.getAttribute("code").equals(request.getParameter("code"))) {
					order.setOrderStaus(1);
					dao.updateOrderStatus(order);
//					dao.rollBack(order1.getUserid(), order1.getOrderFare());
					dao.reduce(order1.getCourierid(), 20);
					MsgPush.push(order1.getUserid(), msg, Constant.JPUSH_UCMASTER_SECRET, Constant.JPUSH_UAPPKEY, orderId, 2, "订单被取消");
					dataMap.put("status", "订单取消成功");
				} else {
					dataMap.put("status", "验证码错误");
				}
				expense.setUserid(order1.getUserid());
				expense.setMoney(order1.getOrderFare());
				expense.setRecord("取消订单回款");
				expense.setSpend(1);
				expense.setDate(new Date());
				dao.saveRecord(expense);
				payService.createRecord(order1.getOrderId(), order1.getUserid(), order1.getSendName(), -order1.getOrderFare(), "用户取消订单，回退订单手续费");
			}
		} else {
			dataMap.put("status", "订单取消失败");
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

	public void checkTimeout() {
		String orderId = request.getParameter("orderId");

		if (ValueUtil.isNull(orderId)) {
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

		order.setOrderId(orderId);
		order = dao.findOrder(order);
		Long orderRquestTime = DateUtil.date2Long(order.getRequestDate());
		Long newDate = System.currentTimeMillis();
		Long min = newDate - orderRquestTime;
		if (order.getOrderStaus() == 0 && min > 1800000) {
			dataMap.put("status", "订单已超时");
			Order order1 = new Order();
			order1.setOrderId(orderId);
			order1.setOrderStaus(-3);
			dao.updateOrderStatus(order1);
		} else if (order.getOrderStaus() == 1 && min > 3600000) {
			Order order1 = new Order();
			order1.setOrderId(orderId);
			order1.setOrderStaus(-3);
			dao.updateOrderStatus(order1);
			PayService payService = new PayServiceImpl();
			if (order.getOrderStaus() == 1) {
				expense.setUserid(order.getUserid());
				expense.setMoney(order.getPayFare());
				expense.setRecord("取消订单回款");
				expense.setSpend(1);
				expense.setDate(new Date());
				dao.saveRecord(expense);
				dao.rollBack(order.getUserid(), order.getPayFare());
				payService.createRecord(order.getOrderId(), order.getUserid(), order.getSendName(), -order.getPayFare(), "订单自动取消，回退订单手续费");
			}
		} else {
			dataMap.put("status", "订单未超时");
		}
		try {
			System.out.println(dataMap);
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
	 * 获取订单价格
	 * @throws ParseException 
	 */
	public void getOrderPrice() throws ParseException {
		String area = request.getParameter("area");
		System.out.println("用户下单area=========="+area);
		double sendLatitude = Double.valueOf(request.getParameter("sendLatitude"));
		double sendLongitude = Double.valueOf(request.getParameter("sendLongitude"));
		double receiveLatitude = Double.valueOf(request.getParameter("receiveLatitude"));
		double receiveLongitude = Double.valueOf(request.getParameter("receiveLongitude"));

		if (ValueUtil.isNull(area)) {
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
		double value = PriceUtil.orderPrice(sendLatitude, sendLongitude, receiveLatitude, receiveLongitude, 0, area);
		dataMap.put("result", "success");
		dataMap.put("value", value);
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
}
