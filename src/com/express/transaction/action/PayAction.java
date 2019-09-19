package com.express.transaction.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.httpclient.URIException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import com.express.alipay.util.AlipayCore;
import com.express.database.dao.BaseDao;
import com.express.database.dao.impl.BaseDaoImpl;
import com.express.model.CompanyPaymentDetails;
import com.express.model.Expense;
import com.express.model.Order;
import com.express.model.User;
import com.express.order.service.OrderService;
import com.express.order.service.impl.OrderServiceImpl;
import com.express.transaction.service.PayService;
import com.express.transaction.service.impl.PayServiceImpl;
import com.express.util.AlipayUtil;
import com.express.util.Constant;
import com.express.util.DateUtil;
import com.express.util.JsonUtil;
import com.express.util.MD5Util;
import com.express.util.MsgPush;
import com.express.util.RandomUtil;
import com.express.util.SuperAction;
import com.express.util.WeiXinPayUtil;
import com.express.util.WeiXinPublicPayUtil;
import com.express.util.XMLUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PayAction extends SuperAction {
	private Map<String, Object> dataMap = new HashMap<String, Object>();
	private Expense expense = new Expense();
	OrderService service = new OrderServiceImpl();
	PayService dao = new PayServiceImpl();
	User user = new User();
	Order order = new Order();

	/**
	 * 支付订单
	 * 
	 * @throws NoSuchFieldException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public void pay() throws NoSuchFieldException, JsonGenerationException, JsonMappingException, IOException {
		String orderId = request.getParameter("orderId");
		Order order1 = dao.getOrder(orderId);
		System.out.println(order1);
		String openid = request.getParameter("openid");
		String sessionId = request.getParameter("sessionId");
		System.out.println(session.getId() + ":" + orderId + ":" + openid);
		dataMap.put("sessionId", session.getId());
		Long orderRquestTime = DateUtil.date2Long(order1.getRequestDate());
		Long newDate = System.currentTimeMillis();
		Long min = newDate - orderRquestTime;
		if (min > 1800000) {
			order.setOrderId(orderId);
			order.setOrderStaus(-3);
			dao.reOrderStatus(order);
			dataMap.put("data", null);
			dataMap.put("status", "订单已超时");
			JsonUtil.writeToResponse(dataMap);
			return;
		}
		String payType = request.getParameter("payType");
		Double amount = order1.getPayFare();
		// 0表示余额支付
		if (payType != null && "0".equals(payType.trim())) {
			String token = session.getAttribute("token").toString();
			String tokenFromApp = request.getParameter("token");
			System.out.println(token + ":------------------:" + tokenFromApp);
			if (token == null || tokenFromApp == null || !token.equals(tokenFromApp)) {
				dataMap.put("status", "tokenError");
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

			user.setId(request.getParameter("id"));
			user.setPayPassword(request.getParameter("payPassword"));
			User user1 = dao.checkPwd(user);
			if (user1 != null) {
				if (user1.getBalance() >= amount) {
					BigDecimal money = new BigDecimal(user1.getBalance() - amount);
					user1.setBalance(money.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					order.setOrderId(orderId);
					order.setOrderStaus(1);
					dao.balancePay(user1);
					dao.reOrderStatus(order);
					session.setAttribute("user", user1);
					expense.setMoney(amount);
					expense.setUserid(user1.getId());
					System.out.println("消费时间：" + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
					expense.setDate(new Date());
					expense.setId(MD5Util.getUUID());
					expense.setRecord("余额支付订单");
					expense.setSpend(0);
					dao.createRecord(order1.getOrderId(), order1.getUserid(), order1.getSendName(), order1.getOrderFare(), "收取订单手续费");
					dao.record(expense);
					List<User> list = service.getCUser(order1.getSendLatitude(), order1.getSendLongitude());
					if (list != null) {
						List<String> userIds = new ArrayList<String>();
						for (User user : list) {
							if (user.getLoginStatus() == null || user.getLoginStatus().equals("on"))
								userIds.add(user.getId());
						}
						MsgPush.push(userIds, "有新订单可接！", Constant.JPUSH_CCMASTER_SECRET, Constant.JPUSH_CAPPKEY, "", 5, "");
					}
					dataMap.put("data", null);
					dataMap.put("status", "支付成功");
					JsonUtil.writeToResponse(dataMap);
				} else {
					dataMap.put("status", "余额不足");
					JsonUtil.writeToResponse(dataMap);
				}
			} else {
				dataMap.put("status", "密码错误");
				JsonUtil.writeToResponse(dataMap);
			}
		}

		// 1表示支付宝支付
		if (payType != null && "1".equals(payType.trim())) {
			try {
				System.out.println("支付宝正在支付中····");
				dataMap.put("data", AlipayUtil.pay(orderId, amount.toString(), Constant.ALIPAY_PAY_NOTIFY_URY));
				dataMap.put("status", "正在支付");
				JsonUtil.writeToResponse(dataMap);
			} catch (AlipayApiException e) {
				System.out.println("支付失败");
				e.printStackTrace();
			}
		}

		// 2表示微信支付
		if (payType != null && "2".equals(payType.trim())) {
			try {
				System.out.println("微信正在支付中····");
				String ip = request.getParameter("ip");
				System.out.println("支付端ip：" + ip);
				String nonce_str = MD5Util.getUUID().replace("-", "");
				String content_sign = WeiXinPayUtil.pay(orderId, amount, ip, nonce_str, Constant.WEIXIN_PAY_NOTIFY_URY, "0");
				if (content_sign != null) {

					Map<String, String> map2 = XMLUtil.readStringXmlOut(content_sign);
					if (map2.get("return_code").equals("SUCCESS")) {
						System.out.println("content_sign不为空");
						String prepay_id = map2.get("prepay_id");
						dataMap.put("data", WeiXinPayUtil.appPay(prepay_id, nonce_str, "0"));
						dataMap.put("status", "正在支付");
						JsonUtil.writeToResponse(dataMap);
					} else {
						System.out.println("验证失败");
					}
				} else {
					System.out.println("content_sign为空");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		// 3表示微信公众号支付
		if (payType != null && "3".equals(payType.trim())) {
			try {
				System.out.println("微信公众号正在支付中····");
				String ip = request.getParameter("ip");
				ip = request.getRemoteAddr();
				System.out.println("支付端ip：" + ip);
				String nonce_str = MD5Util.getUUID().replace("-", "");
				String content_sign = WeiXinPublicPayUtil.pay(orderId, amount, ip, nonce_str, Constant.WEIXIN_WEB_NOTIFY_USER, openid);
				if (content_sign != null) {
					Map<String, String> map2 = XMLUtil.readStringXmlOut(content_sign);
					if (map2.get("return_code").equals("SUCCESS")) {
						System.out.println("content_sign不为空");
						String prepay_id = map2.get("prepay_id");
						Long timeStamp = (System.currentTimeMillis() / 1000);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("appId", Constant.WEIXIN_WEB_ID_USER);
						map.put("timeStamp", timeStamp.toString());
						map.put("nonceStr", nonce_str);
						map.put("package", "prepay_id=" + prepay_id);
						map.put("signType", "MD5");
						String content = AlipayCore.createLinkString(map);
						String sign_encode = content + "&key=" + Constant.WEIXIN_WEB_KEY_USER;
						String sign = MD5Util.MD5Encode(sign_encode, "UTF-8");
						map.put("paySign", sign.toUpperCase());
						System.out.println(map);
						dataMap.put("data", map);
						dataMap.put("status", "正在支付");
						JsonUtil.writeToResponse(dataMap);
					} else {
						System.out.println("验证失败");
					}
				} else {
					System.out.println("content_sign为空");
				}
			} catch (Exception e) {
			}
		}

	}

	/**
	 * 用户提现
	 * 
	 * @throws NoSuchFieldException
	 */
	public void ATM() throws NoSuchFieldException {
		String token = session.getAttribute("token").toString();
		User user = (User) session.getAttribute("user");
		user = dao.getUser(user.getId());
		if (dao.checkInCash(user.getMobile())) {
			dataMap.put("result", "limit");
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
		String tokenFromApp = request.getParameter("token");
		dataMap.put("sessionId", session.getId());
		if (token == null || tokenFromApp == null || !token.equals(tokenFromApp)) {
			dataMap.put("result", "tokenError");
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
		String account = request.getParameter("account");
		String inCashType = request.getParameter("inCashType");
		String userId = request.getParameter("userId");
		String mobile = request.getParameter("mobile");
		String name = request.getParameter("name");
		String payPwd = request.getParameter("payPwd");
		user.setId(userId);
		user.setPayPassword(payPwd);
		if (dao.checkOrder(userId)) {
			dataMap.put("result", "freeze");
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
		if (dao.checkPwd(user) == null) {
			dataMap.put("result", "pwdError");
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
		double value = Double.valueOf(request.getParameter("value"));
		Expense withdraw = dao.withdraw(userId, value, inCashType);
		if (withdraw != null) {
			try {
				dao.creatRecord(mobile, account, inCashType, value, name, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			session.invalidate();
			dataMap.put("result", "success");
		} else {
			dataMap.put("result", "fail");
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

	public void chargeMoney() throws IOException {
		String userId = request.getParameter("userId");
		Long time = System.currentTimeMillis();
		String orderId = RandomUtil.getRandomNum() + time.toString() + RandomUtil.getRandomNum();
		Double money = Double.valueOf(request.getParameter("money"));
		String payType = request.getParameter("payType");
		user = dao.getUser(userId);
		// 1表示支付宝支付
		if (payType != null && "1".equals(payType.trim())) {
			try {
				System.out.println("支付宝正在支付中····");
				dataMap.put("data", AlipayUtil.pay(orderId, money.toString(), Constant.ALIPAY_ADD_NOTIFY_URY));
				dataMap.put("status", "正在支付");
				JsonUtil.writeToResponse(dataMap);
			} catch (AlipayApiException e) {
				System.out.println("支付失败");
				e.printStackTrace();
			}
			dao.createCMRecord(orderId, userId, user.getMobile(), money, "支付宝充值");
		}

		// 2表示微信支付
		if (payType != null && "2".equals(payType.trim())) {
			String userType = request.getParameter("userType");
			String content_sign = null;
			try {
				System.out.println("微信正在支付中····");
				String ip = request.getParameter("ip");
				System.out.println("支付端ip：" + ip);
				String nonce_str = MD5Util.getUUID().replace("-", "");
				if (userType.equals("0")) {
					content_sign = WeiXinPayUtil.pay(orderId, money, ip, nonce_str, Constant.WEIXIN_ADD_NOTIFY_URY, userType);
				} else {
					content_sign = WeiXinPayUtil.pay(orderId, money, ip, nonce_str, Constant.WEIXIN_ADD_CNOTIFY_URY, userType);
				}
				if (content_sign != null) {
					Map<String, String> map2 = XMLUtil.readStringXmlOut(content_sign);
					if (map2.get("return_code").equals("SUCCESS")) {
						System.out.println("content_sign不为空");
						String prepay_id = map2.get("prepay_id");
						dataMap.put("data", WeiXinPayUtil.appPay(prepay_id, nonce_str, userType));
						dataMap.put("status", "正在支付");
						JsonUtil.writeToResponse(dataMap);
					} else {
						System.out.println("验证失败");
					}
				} else {
					System.out.println("content_sign为空");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			dao.createCMRecord(orderId, userId, user.getMobile(), money, "微信充值");
		}
	}

	public void getToken() {
		Date date = new Date();
		String random = RandomUtil.getfindRandonNum() + date.getTime();
		String md5 = MD5Util.getPayWordMD5(random);
		session.setAttribute("token", md5);
		dataMap.put("token", random);
		dataMap.put("sessionId", session.getId());
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

	public void code2OpenId() {
		String code = request.getParameter("code");
		System.out.println("code:" + code);
		Map<String, String> map = WeiXinPublicPayUtil.codeToSession_key(code);
		try {
			JsonUtil.writeToResponse(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test1() {
		String openid = "oMKhzwc1LbYgEZQCK2taZKNrvWvI";
		double amount = 1;
		String spbill_create_ip = "59.33.240.11";
		String partner_trade_no = "2132131";
		try {
			Map<String, String> map = WeiXinPublicPayUtil.ATM(openid, amount, spbill_create_ip, partner_trade_no, "common");
			System.out.println(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
