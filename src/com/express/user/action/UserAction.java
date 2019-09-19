package com.express.user.action;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.logicalcobwebs.concurrent.FJTask.Par;

import com.express.database.dao.OrderDao;
import com.express.database.dao.impl.OrderDaoImpl;
import com.express.model.CouponUtil;
import com.express.model.Expense;
import com.express.model.Order;
import com.express.model.ShareCoupon;
import com.express.model.ShareWeiXin;
import com.express.model.User;
import com.express.model.UserAdvice;
import com.express.model.UserIDcard;
import com.express.user.service.APPService;
import com.express.user.service.UserServiceDAO;
import com.express.user.service.impl.APPServiceImpl;
import com.express.user.service.impl.UserServiceImpl;
import com.express.util.Constant;
import com.express.util.CookiesUtil;
import com.express.util.IdentityCheck;
import com.express.util.JsonUtil;
import com.express.util.MD5Util;
import com.express.util.MsgPush;
import com.express.util.MsgUtil;
import com.express.util.Struts2Util;
import com.express.util.SuperAction;
import com.express.util.ValueUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 用户业务逻辑处理类
 * 
 * @author LK
 *
 */
public class UserAction extends SuperAction {
	private UserServiceDAO dao = new UserServiceImpl();
	private OrderDao orderDao = new OrderDaoImpl();
	private APPService appService = new APPServiceImpl();
	private User user = new User();
	private Order order = new Order();
	private Map<String, Object> dataMap = new HashMap<String, Object>();
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

	/**
	 * @Title: code
	 * @Description: 获取注册验证码
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * @throws Exception
	 */
	public void registerCode() throws JsonGenerationException, JsonMappingException, IOException {
		// 获得注册的手机号
		String mobile = request.getParameter("mobile");
		dataMap.put("sessionId", session.getId());
		if (ValueUtil.isNull(mobile)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
			return;
		}
		System.out.println("传过来的手机号：" + mobile);
		if (dao.findUserByPhone(mobile) == null) {
			// 获得验证码
			String code = MsgUtil.msg(mobile, "您的验证码是：");

			// 将验证码和手机号保存在session中
			session.setAttribute("code", code);
			session.setAttribute("mobile", mobile);
			dataMap.put("status", "已发送验证码");
			JsonUtil.writeToResponse(dataMap);
		} else {
			dataMap.put("status", "用户已存在");
			JsonUtil.writeToResponse(dataMap);
		}
	}

	/**
	 * 普通验证码
	 * 
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void code() throws JsonGenerationException, JsonMappingException, IOException {
		dataMap.put("sessionId", session.getId());
		String mobile = request.getParameter("mobile");
		//System.out.println("传过来的手机号：" + mobile);
		// 获得验证码
		String code = MsgUtil.msg(mobile, "您的验证码是：");
		if (ValueUtil.isNull(mobile, code)) {
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
	 * @Title: register
	 * @Description: 用户注册
	 * @return
	 * @throws Exception
	 */
	public void register() throws JsonGenerationException, JsonMappingException, IOException {
		String code = null;
		String mobile = null;
		System.out.println("获取到的密码：" + request.getParameter("loginPassword"));

		if (!session.getId().isEmpty()) {
			// 取出验证码
			code = (String) session.getAttribute("code");
			// 取出手机号
			mobile = (String) session.getAttribute("mobile");
		} else {
			dataMap.put("status", "验证码不正确");
			JsonUtil.writeToResponse(dataMap);
			return;
		}
		System.out.println("传进来的code--------" + code + mobile);
		String code2 = request.getParameter("code");
		String mobile2 = request.getParameter("mobile");

		if (ValueUtil.isNull(mobile2, code2)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
		}
		if (!code2.equals(code) && !mobile2.equals(mobile)) {
			dataMap.put("status", "验证码不正确");
			JsonUtil.writeToResponse(dataMap);
		} else if (dao.findUserByPhone(mobile) != null) {
			dataMap.put("status", "用户已存在");
			JsonUtil.writeToResponse(dataMap);
		} else {
			User user = new User();
			String userId = MD5Util.getUUID();
			user.setId(userId);
			user.setMobile(mobile);
			user.setLoginPassword(request.getParameter("loginPassword"));
			user.setPath(Constant.PATH_DEFAULT_ICON);
			user.setCredit(100);
			user.setPingfen(5f);
			user.setState("0");
			user.setLoginStatus("on");
			dao.userAddCoupon(mobile);
			dao.insertUser(user);
			dataMap.put("status", "注册成功");
			JsonUtil.writeToResponse(dataMap);
		}
	}

	/**
	 * 微信登录
	 * 
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void weiChatLogin() throws JsonGenerationException, JsonMappingException, IOException {
		String code = request.getParameter("code");
		String role = request.getParameter("role");
		if (role == null) {
			role = "common";
		}
		if (ValueUtil.isNull(code)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
		}
		Map<String, String> map = appService.getWeixinJsonForUser(code, role);
		String unionId = map.get("unionid");
		String openId = map.get("openid");
		User userLogin = (User) request.getSession().getAttribute("user");
		if (unionId == null) {
			dataMap.put("status", "系统错误");
		} else if (userLogin != null && userLogin.getMobile() != null) {
			User weiChatUser = dao.findUserByPhone(userLogin.getMobile());
			weiChatUser.setOpenId(openId);
			weiChatUser.setUnionid(unionId);
			dao.updataUser(weiChatUser);
			dataMap.put("user", weiChatUser);
			dataMap.put("status", "登录成功");
			session.setAttribute("user", weiChatUser);
			CookiesUtil.saveCookie(weiChatUser, response);
			JsonUtil.writeToResponse(dataMap);
		} else if (dao.findUnionIdByPhone(unionId) != null) {
			User user = dao.findUnionIdByPhone(unionId);
			dataMap.put("status", "登录成功");
			dataMap.put("user", JsonUtil.beanToJson(user));
			if (user.getOpenId() == null) {
				user.setOpenId(openId);
				user.setUnionid(unionId);
				dao.updataUser(user);
			}
			session.setAttribute("user", user);
			CookiesUtil.saveCookie(user, response);
			JsonUtil.writeToResponse(dataMap);
		} else {
			User user = new User();
			String userId = MD5Util.getUUID();
			user.setId(userId);
			user.setRole(request.getParameter("role"));
			user.setPath(Constant.PATH_DEFAULT_ICON);
			user.setCredit(100);
			user.setLoginPassword(100000 + new Random().nextInt(100000) + "");
			user.setPingfen(5f);
			user.setState("0");
			user.setUnionid(unionId);
			user.setOpenId(openId);
			user.setLoginStatus("on");
			dao.insertUser(user);
			User user2 = dao.findUnionIdByPhone(unionId);
			session.setAttribute("user", user2);
			CookiesUtil.saveCookie(user2, response);
			dataMap.put("user", JsonUtil.beanToJson(user));
			dataMap.put("status", "登录成功");
			JsonUtil.writeToResponse(dataMap);
		}
		System.out.println(session.getId());
	}

	/**
	 * @Title: auth
	 * @Description: 快递员上传认证信息
	 * @return
	 * @throws Exception
	 */
	public void auth() throws JsonGenerationException, JsonMappingException, IOException {
		System.out.println("开始上传");
		String keepPath = "C:/Express/images/identity";
		String iconsPath = "C:/Express/images/icon";
		File file = new File(keepPath);
		File file2 = new File(iconsPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		if (!file2.exists()) {
			file2.mkdirs();
		}
		if (upload == null) {
			System.out.println("没有接收到照片");
			dataMap.put("status", "请添加三张身份证照片");
			JsonUtil.writeToResponse(dataMap);
			return;
		}
		System.out.println("上传中-------");
		for (int i = 0; i < upload.size(); i++) {
			if (i == upload.size() - 1) {
				FileUtils.copyFile(upload.get(i), new File(file2, request.getParameter("mobile") + i + ".jpg"));
				break;
			}
			FileUtils.copyFile(upload.get(i), new File(file, request.getParameter("mobile") + i + ".jpg"));
		}

		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String identity = request.getParameter("identity");
		String mobile = request.getParameter("mobile");

		User courier = dao.getUser(id);
		if (courier.getOpenId() != null) {
			User userMobile = dao.findUserByPhone(mobile);
			if (userMobile != null) {
				if (!courier.getId().equals(userMobile.getId())) {
					dao.deleteUser(courier.getId());
				}
				userMobile.setOpenId(courier.getOpenId());
				userMobile.setUnionid(courier.getUnionid());
				courier = userMobile;
			}
		}

		if (courier.getMobile() == null || "".equals(courier.getMobile())) {
			String code = request.getParameter("code");
			String msgCode = (String) session.getAttribute("code");
			if (!code.equals(msgCode)) {
				dataMap.put("result", "验证码错误");
				JsonUtil.writeToResponse(dataMap);
				return;
			}
		}

		if (ValueUtil.isNull(id, name, identity)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
			return;
		}

		// 三张图片的绝对路径
		String path1 = Constant.PATH_PREFIX + mobile + "0.jpg";
		String path2 = Constant.PATH_PREFIX + mobile + "1.jpg";
		String path3 = Constant.PATH_PREFIX + mobile + "2.jpg";
		// 头像的绝对路径
		String iconPath = Constant.PATH_ICON + mobile + "3.jpg";

		if ("".equals(name) && "".equals(identity)) {
			dataMap.put("status", "姓名和身份证号不得为空");
			JsonUtil.writeToResponse(dataMap);
		} else if (upload.size() < 3) {
			System.out.println("请添加三张身份证照片");
			dataMap.put("status", "请添加三张身份证照片");
			JsonUtil.writeToResponse(dataMap);
		} else {
			System.out.println("上传成功");
			String sex = IdentityCheck.Check(identity);
			UserIDcard userIDcard = new UserIDcard();
			userIDcard.setId(id);
			userIDcard.setName(name);
			userIDcard.setIcon(iconPath);
			userIDcard.setIdentity(identity);
			userIDcard.setMobile(mobile);
			userIDcard.setPath1(path1);
			userIDcard.setPath2(path2);
			userIDcard.setPath3(path3);
			if(!courier.getState().equals("3"))
			dao.idAuth(userIDcard);
			courier.setMobile(mobile);
			courier.setName(name);
			courier.setSex(sex);
			courier.setState("3");
			dao.updataUser(courier);
			// session.invalidate();
			session.setAttribute("user", courier);
			dataMap.put("status", "上传成功");
			dataMap.put("data", courier);
			CookiesUtil.saveCookie(courier, response);
			JsonUtil.writeToResponse(dataMap);
		}
	}

	/**
	 * @Title: loginByPwd
	 * @Description: 用户通过密码登录
	 * @return
	 * @throws Exception
	 */
	public void loginByPwd() throws JsonGenerationException, JsonMappingException, IOException {
		String mobile = request.getParameter("mobile");
		String loginPassword = request.getParameter("loginPassword");
		dataMap.put("sessionId", session.getId());
		System.out.println(session.getId());
		if (Struts2Util.getSession("user") != null) {
			System.out.println("用户自动登录");
			dataMap.put("status", "登录成功");
			dataMap.put("user", Struts2Util.getSession("user"));
			JsonUtil.writeToResponse(dataMap);
		} else if (dao.findUserByPhone(mobile) == null) {
			dataMap.put("status", "用户不存在");
			JsonUtil.writeToResponse(dataMap);
		} else if (dao.findUser(mobile, loginPassword)) {
			User user = dao.findUserByPhone(mobile);
			CookiesUtil.saveCookie(user, response);
			dataMap.put("status", "登录成功");
			dataMap.put("user", JsonUtil.beanToJson(user));
			JsonUtil.writeToResponse(dataMap);
		} else {
			dataMap.put("status", "密码错误");
			JsonUtil.writeToResponse(dataMap);
		}
	}

	/**
	 * @Title: loginByPwd
	 * @Description: 用户通过密码登录
	 * @return
	 * @throws Exception
	 */
	public void loginByWeChat() throws JsonGenerationException, JsonMappingException, IOException {

		String mobile = request.getParameter("mobile");
		String loginPassword = request.getParameter("loginPassword");
		dataMap.put("sessionId", session.getId());
		if (Struts2Util.getSession("user") != null) {
			System.out.println("用户自动登录");
			dataMap.put("status", "登录成功");
			dataMap.put("user", Struts2Util.getSession("user"));
			JsonUtil.writeToResponse(dataMap);
		} else if (dao.findUserByPhone(mobile) == null) {
			dataMap.put("status", "用户不存在");
			JsonUtil.writeToResponse(dataMap);
		} else if (dao.findUser(mobile, loginPassword)) {
			User user = dao.findUserByPhone(mobile);
			CookiesUtil.saveCookie(user, response);
			dataMap.put("status", "登录成功");
			dataMap.put("user", JsonUtil.beanToJson(user));
			JsonUtil.writeToResponse(dataMap);
		} else {
			dataMap.put("status", "密码错误");
			JsonUtil.writeToResponse(dataMap);
		}
	}

	/**
	 * @Title: loginByPhone
	 * @Description: 用户通过验证码登录
	 * @return
	 * @throws Exception
	 */
	public void loginByPhone() throws JsonGenerationException, JsonMappingException, IOException {

		String code = null;
		String mobile = null;
		dataMap.put("sessionId", session.getId());
		if (session.getId().isEmpty()) {
			// 取出验证码
			code = (String) session.getAttribute("code");
			// 取出手机号
			mobile = (String) session.getAttribute("mobile");
		}
		String code2 = request.getParameter("code");
		String mobile2 = request.getParameter("mobile");

		if (ValueUtil.isNull(code2, mobile2)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
			return;
		}

		if (!code2.equals(code) || !mobile2.equals(mobile)) {
			dataMap.put("status", "验证码不正确");
			JsonUtil.writeToResponse(dataMap);
		} else if (dao.findUserByPhone(mobile) != null) {
			user = dao.findUserByPhone(mobile);
			session.setAttribute("user", user);
			dataMap.put("user", JsonUtil.beanToJson(user));
			dataMap.put("status", "登录成功");
			JsonUtil.writeToResponse(dataMap);
		} else {
			dataMap.put("status", "用户不存在，请先注册");
			JsonUtil.writeToResponse(dataMap);
		}

	}

	/**
	 * @Title: logout
	 * @Description: 用户注销
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public void logout() throws JsonGenerationException, JsonMappingException, IOException {
		session.invalidate();
		CookiesUtil.clearCookie(Struts2Util.getResponse());
		dataMap.put("status", "注销成功");
		JsonUtil.writeToResponse(dataMap);
	}

	/**
	 * @throws IOException
	 *             用户修改头像
	 * 
	 */
	public void reIcon() throws IOException {

		String userId = request.getParameter("userId");
		String mobile = user.getMobile();
		// String role = user.getRole();
		// System.out.println("role----------"+role+"------"+userId);
		// if (role.equals("courier")) {
		// dataMap.put("status", "你已注册快递员，无法修改头像");
		// JsonUtil.writeToResponse(dataMap);
		// return null;
		// }

		if (ValueUtil.isNull(userId, mobile)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
			return;
		}

		user = dao.getUser(userId);
		String oldIconPath = user.getPath();
		try {
			String keepPath = "C:/Express/images/icon";
			File file = new File(keepPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			if (0 < upload.size()) {
				long date = new Date().getTime();
				FileUtils.copyFile(upload.get(0), new File(file, mobile + date + ".jpg"));
				String path = Constant.PATH_ICON + mobile + date + ".jpg";
				user.setPath(path);
			}
			dao.changeIcon(user);
			if (oldIconPath != null) {
				String[] filename = oldIconPath.split("/");
				String Icon = "C:/Express/images/icon/" + filename[filename.length - 1];
				File oldFile = new File(Icon);
				if (oldFile.exists()) {
					oldFile.delete();
				}
			}
			dataMap.put("user", JsonUtil.beanToJson(user));
			dataMap.put("status", "修改成功");
			JsonUtil.writeToResponse(dataMap);
		} catch (Exception e) {
			dataMap.put("status", "修改失败");
			JsonUtil.writeToResponse(dataMap);
		}
	}

	/**
	 * 用户建议
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public void advice() throws JsonGenerationException, JsonMappingException, IOException {
		String path = request.getParameter("path");
		String mobile = request.getParameter("mobile");
		String advice = request.getParameter("advice");
		String name = request.getParameter("name");

		if (ValueUtil.isNull(path, mobile, advice, name)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
			return;
		}

		UserAdvice userAdvice = new UserAdvice();
		userAdvice.setPath(path);
		userAdvice.setMobile(mobile);
		userAdvice.setAdvice(advice);
		userAdvice.setUsername(name);
		userAdvice.setDate(new Date());
		dao.saveAdvice(userAdvice);
		dataMap.put("status", "提交成功");
		JsonUtil.writeToResponse(dataMap);
	}

	/**
	 * 设置支付密码
	 * 
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws NoSuchFieldException
	 */
	public void setPayPwd() throws JsonGenerationException, JsonMappingException, IOException, NoSuchFieldException {
		User user = new User();
		String payPwd = request.getParameter("payPwd");
		String id = request.getParameter("id");

		if (ValueUtil.isNull(payPwd, id)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
			return;
		}

		user.setId(id);
		user.setPayPassword(payPwd);
		if (request.getParameter("payPwd").equals(null)) {
			dataMap.put("status", "密码不能为空");
			JsonUtil.writeToResponse(dataMap);
		} else {
			dao.rePayPwd(user);
			dataMap.put("status", "设置成功");
			JsonUtil.writeToResponse(dataMap);
		}

	}

	/**
	 * 通过原密码修改密码
	 * 
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws NoSuchFieldException
	 */
	public void rePwdByPwd() throws JsonGenerationException, JsonMappingException, IOException, NoSuchFieldException {
		String oldPwd = request.getParameter("oldPwd");
		String newPwd = request.getParameter("newPwd");
		String flag = request.getParameter("flag");
		String id = request.getParameter("id");
		if (ValueUtil.isNull(oldPwd, newPwd, flag, id)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
			return;
		}

		user.setId(id);
		// 0为修改登录密码否则为修改支付密码
		if (flag.equals("0")) {
			user.setLoginPassword(oldPwd);
		} else {
			user.setPayPassword(oldPwd);
		}
		if (newPwd.equals(null)) {
			dataMap.put("status", "新密码不能为空");
			JsonUtil.writeToResponse(dataMap);
		} else if (dao.checkUser(user) != null) {
			if (flag.equals("0")) {
				user.setLoginPassword(newPwd);
			} else {
				user.setPayPassword(newPwd);
			}
			dao.rePayPwd(user);
			dataMap.put("status", "修改成功");
			System.out.println("修改密码成功");
			session.invalidate();
			JsonUtil.writeToResponse(dataMap);
		} else {
			dataMap.put("status", "原密码错误");
			System.out.println("修改密码失败");
			JsonUtil.writeToResponse(dataMap);
		}
	}

	/**
	 * 通过验证码修改密码
	 * 
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws NoSuchFieldException
	 * @throws IOException
	 */
	public void rePwdByPhone() throws JsonGenerationException, JsonMappingException, NoSuchFieldException, IOException {
		String code = null;
		String mobile = null;
		if (!session.getId().isEmpty()) {
			// 取出验证码
			code = (String) session.getAttribute("code");
			// 取出手机号
			mobile = (String) session.getAttribute("mobile");
		}
		String flag = request.getParameter("flag");
		String newPwd = request.getParameter("newPwd");
		String code2 = request.getParameter("code");
		String mobile2 = request.getParameter("mobile");
		System.out.println(flag + ":" + newPwd + ":" + code2 + ":" + mobile2 + ":" + code + ":" + mobile + "---------------------");
		if (ValueUtil.isNull(flag, newPwd, code2, mobile2)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
			return;
		}

		if (!(code2.equals(code) && mobile2.equals(mobile))) {
			dataMap.put("status", "验证码错误");
			JsonUtil.writeToResponse(dataMap);
		} else if (newPwd.equals(null)) {
			dataMap.put("status", "新密码不能为空");
			JsonUtil.writeToResponse(dataMap);
		} else {
			user.setMobile(mobile);
			if (flag.equals("0")) {
				user.setLoginPassword(newPwd);
			} else {
				user.setPayPassword(newPwd);
			}
			dao.rePayPwdByPhone(user);
			dataMap.put("status", "修改成功");
			session.invalidate();
			User user2 = dao.findUserByPhone(mobile);
			CookiesUtil.saveCookie(user2, response);
			JsonUtil.writeToResponse(dataMap);
		}
	}

	/**
	 * 用户查看账户明细
	 * 
	 * @throws NoSuchFieldException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void findExpense() throws NoSuchFieldException, JsonGenerationException, JsonMappingException, IOException {
		String id = request.getParameter("userId");
		int first = Integer.valueOf(request.getParameter("first"));

		if (ValueUtil.isNull(id)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
			return;
		}

		Expense expense = new Expense();
		expense.setUserid(id);
		List<Expense> list = dao.findExpenses(expense, first);
		if (first == 0) {
			user = dao.getUser(id);
		} else {
			user = null;
		}
		dataMap.put("user", JsonUtil.beanToJson(user));
		if (list != null) {
			dataMap.put("expense", JsonUtil.beanToJson(list));
			dataMap.put("status", "查找成功");
			JsonUtil.writeToResponse(dataMap);
		} else {
			dataMap.put("status", "查无记录");
			JsonUtil.writeToResponse(dataMap);
		}
	}

	/**
	 * 通过用户id获取订单
	 * 
	 * @throws IOException
	 */
	public void getOrder() throws IOException {
		int first = Integer.valueOf(request.getParameter("first"));
		String id = request.getParameter("id");
		if (ValueUtil.isNull(id)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
			return;
		}
		List<Order> list = orderDao.getOrderList(id, first);
		dataMap.put("order", JsonUtil.beanToJson(list));
		JsonUtil.writeToResponse(dataMap);
	}

	/**
	 * 普通用户认证
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public void authIdentity() throws JsonGenerationException, JsonMappingException, IOException {
		String userId = request.getParameter("userId");
		String identity = request.getParameter("identity");
		String name = request.getParameter("name");
		String telephone = request.getParameter("telephone");
		String code = request.getParameter("code");
		if (ValueUtil.isNull(userId, identity, name)) {
			dataMap.put("result", "缺少参数");
			JsonUtil.writeToResponse(dataMap);
			return;
		}
		String sex = IdentityCheck.Check(identity);
		if (telephone != null && !"".equals(telephone) && code != null && !code.equals(session.getAttribute("code"))) {
			System.out.println(session.getAttribute("code"));
			dataMap.put("status", "验证错误");
			JsonUtil.writeToResponse(dataMap);
		} else if (sex != null) {
			user.setId(userId);
			user.setIdentity(identity);
			if (telephone != null && !"".equals(telephone)) {
				User usered = dao.findUserByPhone(telephone);
				if (usered != null&&!user.getId().equals(userId)) {
					User weiChatUser = dao.getUser(userId);
					dao.deleteUser(userId);
					userId = usered.getId();
					user.setId(userId);
					user.setUnionid(weiChatUser.getUnionid());
					user.setOpenId(weiChatUser.getOpenId());
				}
				user.setMobile(telephone);
			}
			user.setSex(sex);
			user.setRole(request.getParameter("role"));
			dataMap.put("sex", sex);
			user.setState("1");
			user.setSend_able(true);
			user.setName(name);
			try {
				dao.updataUser(user);
			} catch (Exception e) {
				e.printStackTrace();
			}
			User userNew = dao.getUser(userId);
			dataMap.put("data", userNew);
			dataMap.put("status", "认证成功");
			CookiesUtil.saveCookie(userNew, response);
			JsonUtil.writeToResponse(dataMap);
			session.invalidate();
			String msg = "您获得了一张优惠券，请点击查看详情！";
			MsgPush.push(userId, msg, Constant.JPUSH_UCMASTER_SECRET, Constant.JPUSH_UAPPKEY, "test", 5, "获得优惠券");
		} else {
			dataMap.put("status", "认证失败");
			JsonUtil.writeToResponse(dataMap);
		}
		System.out.println(session.getId());
	}

	/**
	 * 用户上传定位信息
	 */
	public void address() {
		String address = request.getParameter("address");
		double longitude = Double.valueOf(request.getParameter("longitude"));
		double latitude = Double.valueOf(request.getParameter("latitude"));
		String userId = request.getParameter("userId");

		if (ValueUtil.isNull(address, userId)) {
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

		user.setId(userId);
		user.setAddress(address);
		user.setLongitude(longitude);
		user.setLatitude(latitude);
		dao.updataUser(user);
		dataMap.put("status", "上传成功");
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

	public void getUserSession() {
		String userId = request.getParameter("userId");

		if (ValueUtil.isNull(userId)) {
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

		System.out.println("userId========:" + userId);
		User user = dao.getUser(userId);
		List<Order> list = dao.getTwoOrder(userId);
		try {
			dataMap.put("order", JsonUtil.beanToJson(list));
			dataMap.put("user", user);
			JsonUtil.writeToResponse(dataMap);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		session.setAttribute("user", user);
	}

	/**
	 * 获取 新的session
	 */
	public void userGetSession() {
		String userId = request.getParameter("userId");
		User user = dao.getUser(userId);
		try {
			dataMap.put("user", user);
			JsonUtil.writeToResponse(dataMap);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		session.setAttribute("user", user);
	}

	/**
	 * 获取封面
	 */
	public void getCover() {
		dataMap.put("path", dao.getCover());
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
	 * 获取优惠券
	 */
	public void getCoupon() {
		String mobile = request.getParameter("mobile");

		if (ValueUtil.isNull(mobile)) {
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

		dataMap.put("coupon", dao.getCoupon(mobile));
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
	 * 获取分享链接
	 */
	public void shareUrl() {
		ShareWeiXin swx = dao.getShareWeiXin();
		try {
			dataMap.put("data", JsonUtil.beanToJson(swx));
			JsonUtil.writeToResponse(dataMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 跳转到红包页面
	 * 
	 * @return
	 */
	public String toSharePage() {
		String orderId = request.getParameter("orderId");
		request.setAttribute("orderId", orderId);
		return "hongbao";
	}

	/**
	 * 分享链接获取红包
	 */
	public void mobileGetCoupon() {
		System.out.println("测试领取红包");
		String mobile = request.getParameter("mobile");
		String orderId = request.getParameter("orderId");

		if (ValueUtil.isNull(mobile, orderId)) {
			System.out.println("缺少参数");
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

		System.out.println(orderId + "||||" + mobile);
		if (mobile != null) {
			User user = dao.findUserByPhone(mobile);
			if (user != null) {
				ShareCoupon shareCoupon = dao.checkShare(mobile, orderId);
				if (shareCoupon == null) {
					double value = CouponUtil.getCouponValue();
					System.out.println("红包金额" + value);
					dao.insertShareCoupon(orderId, mobile, value);
					dao.userGetCoupon(mobile, value);
					dataMap.put("value", value);
					dataMap.put("result", "success");
				} else {
					dataMap.put("value", shareCoupon.getValue());
					dataMap.put("result", "aleary");
				}
			} else {
				dataMap.put("result", "noUser");
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
	}

	/**
	 * 绑定微信
	 * 
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void weiChatBind() {
		String code = request.getParameter("code");
		User user = (User) session.getAttribute("user");
		String role = user.getRole();
		Map<String, String> map = appService.getWeixinJsonForUser(code, role);
		String unionId = map.get("unionid");
		String openId = map.get("openid");
		User user2 = dao.findUserByPhone(user.getMobile());
		user2.setUnionid(unionId);
		user2.setOpenId(openId);
		dao.updataUser(user2);
		try {
			dataMap.put("result", "success");
			JsonUtil.writeToResponse(dataMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void offLine() {
		User user = (User) session.getAttribute("user");
		User loginUser = dao.getUser(user.getId());
		String status = request.getParameter("status");
		loginUser.setLoginStatus(status);
		dao.updataUser(loginUser);
		dataMap.put("result", "success");
		try {
			session.setAttribute("user", loginUser);
			JsonUtil.writeToResponse(dataMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
