package com.express.admin.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.express.admin.service.AdminService;
import com.express.admin.service.OrderService;
import com.express.admin.service.UserService;
import com.express.admin.service.impl.AdminServiceImpl;
import com.express.admin.service.impl.OrderServiceImpl;
import com.express.admin.service.impl.UserServiceImpl;
import com.express.model.Admin;
import com.express.model.CompanyPaymentDetails;
import com.express.model.CouponType;
import com.express.model.InCash;
import com.express.model.Order;
import com.express.model.SysNotice;
import com.express.model.User;
import com.express.model.UserIDcard;
import com.express.util.CityUtil;
import com.express.util.Constant;
import com.express.util.CookiesUtil;
import com.express.util.DateUtil;
import com.express.util.ExcelUtil;
import com.express.util.JsonUtil;
import com.express.util.MD5Util;
import com.express.util.MsgPush;
import com.express.util.Struts2Util;
import com.express.util.SuperAction;
import com.express.util.WeiXinPublicPayUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class AdminAction extends SuperAction {
	private static boolean couponType;
	AdminService dao = new AdminServiceImpl();
	OrderService orderService = new OrderServiceImpl();
	UserService userService = new UserServiceImpl();
	private Map<String, Object> dataMap = new HashMap<String, Object>();
	User user = new User();
	Admin admin = new Admin();
	Order order = new Order();
	UserIDcard userTemp = new UserIDcard();
	SysNotice notice = new SysNotice();

	/**
	 * 到登录页面
	 */
	@SkipValidation
	public String gologin() {
		System.out.println("跳转登录页面");
		return "goLogin";
	}

	/**
	 * 管理员登录 Express/
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 */
	@SkipValidation
	public String login() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		System.out.println("用户登录中···");
		AdminService adminDAO = new AdminServiceImpl();
		// 获取管理员登录名和密码
		String username = request.getParameter("username");
		String password = MD5Util.LoginEncryption(request.getParameter("password"));
		admin = dao.getAdmin(username, password);

		if (admin != null) {
			int orderCount = (int) dao.getOrderCount(admin.getCity());
			int userCount = (int) dao.getUserCount("common", admin.getCity());
			int cuserCount = (int) dao.getUserCount("courier", admin.getCity());
			request.setAttribute("orderCount", orderCount);
			request.setAttribute("userCount", userCount);
			request.setAttribute("cuserCount", cuserCount);
			session.setAttribute("adminSession", admin);
			session.setAttribute("username", username);
			List<String> list = CityUtil.getCityList("广东");
			request.setAttribute("cityList", list);
			return "login_success";
		} else if ("admin".equals(username) && "admin".equals(password)) {
			adminDAO.addAdmin(admin);
			int orderCount = (int) dao.getOrderCount(admin.getCity());
			int userCount = (int) dao.getUserCount("common", admin.getCity());
			int cuserCount = (int) dao.getUserCount("courier", admin.getCity());
			request.setAttribute("orderCount", orderCount);
			request.setAttribute("userCount", userCount);
			request.setAttribute("cuserCount", cuserCount);
			session.setAttribute("username", admin.getUsername());
			request.setAttribute("cityList", CityUtil.getCityList("广东"));
			return "login_success";
		} else {
			System.out.println("验证失败");
			return "login_failure";
		}

	}

	/**
	 * 跳转到主页
	 * 
	 * @throws FileNotFoundException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 */
	public String toIndex() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		Admin admin = (Admin) session.getAttribute("adminSession");
		String area = admin.getCity();
		int orderCount = (int) dao.getOrderCount(area);
		int userCount = (int) dao.getUserCount("common", area);
		int cuserCount = (int) dao.getUserCount("courier", area);
		request.setAttribute("orderCount", orderCount);
		request.setAttribute("userCount", userCount);
		request.setAttribute("cuserCount", cuserCount);
		request.setAttribute("cityList", CityUtil.getCityList("广东"));
		return "login_success";
	}

	/**
	 * 创建普通管理员
	 * 
	 * @return
	 * @throws NoSuchFieldException
	 */
	@SkipValidation
	public String createAdmin() throws NoSuchFieldException {
		if (session.getAttribute("username") == null) {
			return "login_lose";
		}
		String adminName = (String) session.getAttribute("username");
		String newUserName = request.getParameter("newUserName");
		String newUserPwd = request.getParameter("newUserPwd");
		String userManage = request.getParameter("userManage");
		String checkUser = request.getParameter("checkUser");
		String userMsg = request.getParameter("userMsg");
		String userAdvice = request.getParameter("userAdvice");
		String userATM = request.getParameter("userATM");
		String orderManage = request.getParameter("orderManage");
		String adminManage = request.getParameter("adminManage");
		String msgManage = request.getParameter("msgManage");
		String activitySet = request.getParameter("activitySet");

		if (dao.findAdmin(newUserName) != null) {
			dataMap.put("result", "repetition");
			try {
				JsonUtil.writeToResponse(dataMap);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		Admin adminLogin = (Admin) session.getAttribute("adminSession");
		String role = adminLogin.getRole();
		System.out.println(role);
		if (role.equals("超级管理员") || role.equals("地区管理员")) {
			admin.setUsername(newUserName);
			admin.setPassword(MD5Util.LoginEncryption(newUserPwd));
			admin.setRole("普通管理员");
			if ("on".equals(userManage)) {
				admin.setUserManage(true);
			}
			if ("on".equals(checkUser)) {
				admin.setCheckUser(true);
			}
			if ("on".equals(userMsg)) {
				admin.setUserMsg(true);
			}
			if ("on".equals(userAdvice)) {
				admin.setUserAdvice(true);
			}
			if ("on".equals(userATM)) {
				admin.setUserATM(true);
			}
			if ("on".equals(orderManage)) {
				admin.setOrderManage(true);
			}
			if ("on".equals(adminManage)) {
				admin.setAdminManage(true);
			}
			if ("on".equals(msgManage)) {
				admin.setMsgManage(true);
			}
			if ("on".equals(activitySet)) {
				admin.setActivitySet(true);
			}
			admin.setCity(adminLogin.getCity());
			dao.addAdmin(admin);
			dataMap.put("result", "success");
		} else {
			System.out.println("不是超级管理员权限不能新建管理员");
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
		return null;
	}

	public String deleteAdmin() throws NoSuchFieldException {
		String username = request.getParameter("userName");
		if (session.getAttribute("username") == null) {
			return "login_lose";
		}
		String adminName = (String) session.getAttribute("username");
		System.out.println(adminName);
		if ("admin".equals(username)) {
			dataMap.put("result", "forbid");
			try {
				JsonUtil.writeToResponse(dataMap);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		if ("admin".equals(adminName)) {
			dao.deleteAdmin(username);
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
		return null;
	}

	/**
	 * 创建地区管理员
	 * 
	 * @return
	 * @throws NoSuchFieldException
	 */
	@SkipValidation
	public String createCityAdmin() throws NoSuchFieldException {
		if (session.getAttribute("username") == null) {
			return "login_lose";
		}
		String adminName = (String) session.getAttribute("username");
		String newUserName = request.getParameter("newUserName");
		String newUserPwd = request.getParameter("newUserPwd");

		String userManage = request.getParameter("userManage");
		String checkUser = request.getParameter("checkUser");
		String userMsg = request.getParameter("userMsg");
		String userAdvice = request.getParameter("userAdvice");
		String userATM = request.getParameter("userATM");
		String orderManage = request.getParameter("orderManage");
		String adminManage = request.getParameter("adminManage");
		String msgManage = request.getParameter("msgManage");
		String activitySet = request.getParameter("activitySet");
		String city = request.getParameter("city");
		if (dao.findAdmin(newUserName) != null) {
			dataMap.put("result", "repetition");
			try {
				JsonUtil.writeToResponse(dataMap);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		if (city == null || "".equals(city)) {
			dataMap.put("result", "cityIsNull");
			try {
				JsonUtil.writeToResponse(dataMap);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		Admin adminLogin = (Admin) session.getAttribute("adminSession");

		if ("超级管理员".equals(adminLogin.getRole()) && adminLogin.getCity() == null) {
			admin.setUsername(newUserName);
			admin.setPassword(MD5Util.LoginEncryption(newUserPwd));
			admin.setRole("地区管理员");
			System.out.println(userAdvice + ":" + userManage);
			if ("on".equals(userManage)) {
				admin.setUserManage(true);
			}
			if ("on".equals(checkUser)) {
				admin.setCheckUser(true);
			}
			if ("on".equals(userMsg)) {
				admin.setUserMsg(true);
			}
			if ("on".equals(userAdvice)) {
				admin.setUserAdvice(true);
			}
			if ("on".equals(userATM)) {
				admin.setUserATM(true);
			}
			if ("on".equals(orderManage)) {
				admin.setOrderManage(true);
			}
			if ("on".equals(adminManage)) {
				admin.setAdminManage(true);
			}
			if ("on".equals(msgManage)) {
				admin.setMsgManage(true);
			}
			if ("on".equals(activitySet)) {
				admin.setActivitySet(true);
			}
			admin.setCity(city);
			System.out.println(admin.isAdminManage() + "----------------");
			dao.addAdmin(admin);
			dataMap.put("result", "success");
		} else {
			System.out.println("不是超级管理员权限不能新建管理员");
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
		return null;
	}

	/**
	 * 注销登录
	 * 
	 * @return
	 */
	@SkipValidation
	public String loginOut() {
		session.invalidate();
		CookiesUtil.clearCookie(Struts2Util.getResponse());
		return "login_out";
	}

	/**
	 * 修改密码
	 * 
	 * @return
	 * @throws NoSuchFieldException
	 */
	@SkipValidation
	public void reLoginPwd() throws NoSuchFieldException {
		admin.setUsername(request.getParameter("username"));
		admin.setPassword(MD5Util.LoginEncryption(request.getParameter("oldPwd")));
		String newPwd = MD5Util.LoginEncryption(request.getParameter("newPwd"));
		if (dao.checkAdmin(admin)) {
			admin.setPassword(newPwd);
			dao.rePwd(admin);
			dataMap.put("status", "success");
		} else {
			System.out.println("查无此人");
			dataMap.put("status", "fail");
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

	@SkipValidation
	public String getAllAdmin() {
		int first = Integer.valueOf(request.getParameter("first"));
		List<Admin> list = dao.getAllAdmin(first);
		request.setAttribute("admin", list);
		return "admin";
	}

	// @SkipValidation
	// public String pushMsg(){
	// String msg = request.getParameter("msg");
	// String cApp = request.getParameter("cApp");
	// String uApp = request.getParameter("uApp");
	// if(cApp!=null){
	// MsgPush.push(msg, Constant.JPUSH_CCMASTER_SECRET,
	// Constant.JPUSH_CAPPKEY);
	// }
	// if(uApp!=null){
	// MsgPush.push(msg, Constant.JPUSH_UCMASTER_SECRET,
	// Constant.JPUSH_UAPPKEY);
	// }
	//
	// return "systemMsg";
	// }

	/**
	 * 跳转到系统公告页面
	 */
	@SkipValidation
	public String toSysMsg() {
		int first = Integer.valueOf(request.getParameter("first"));
		List<SysNotice> list = dao.getNotice(first);
		int pageNum = (int) Math.ceil(Double.valueOf(dao.getAllNotice().size()) / 10.0);
		request.setAttribute("PageNum", pageNum);
		request.setAttribute("notice", list);
		return "announcement";
	}

	public void getSysMsg() {
		int first = Integer.valueOf(request.getParameter("first"));
		List<SysNotice> list = dao.getNotice(first);
		try {
			JsonUtil.writeToResponse(list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String toAppUpdate() {
		String cAppVersion = dao.getAPPVersion("capp");
		String uAppVersion = dao.getAPPVersion("uapp");
		request.setAttribute("cappv", cAppVersion);
		request.setAttribute("uappv", uAppVersion);
		return "app_upload";
	}

	// public String toInCash(){
	// return "inCash";
	// }

	public String getInCashRecord() {
		String times = request.getParameter("times");
		if (times.equals("first")) {
			List<InCash> list = dao.getIncashRecord(0);
			int pageNum = (int) Math.ceil(dao.getInCashNum() / 10.0f);
			request.setAttribute("PageNum", pageNum);
			request.setAttribute("list", list);
		} else {
			int first = Integer.valueOf(request.getParameter("first"));
			List<InCash> list = dao.getIncashRecord(first);
			try {
				JsonUtil.writeToResponse(list);
				return null;
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "inCash";
	}

	public void getInCashRecordTime() {
		String times = request.getParameter("times");
		int curPage = Integer.parseInt(times) - 1;
		Integer count = dao.getIncashRecordTimeCount();
		List<Date> list = dao.getIncashRecordTime(curPage);
		int pageNum = (int) Math.ceil(count / 10.0f);
		dataMap.put("pageNum", pageNum);
		dataMap.put("data", list);
		try {
			JsonUtil.writeToResponse(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String printExcel() {
		try {
			List<InCash> list = dao.getIncashRecord();
			if (list != null) {
				OutputStream os = response.getOutputStream();
				String date = DateUtil.getDateFormat(new Date(), Constant.YYYY_MM_DD);
				String filename = URLEncoder.encode("同城快递提现报表", "utf-8");
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition", "attachment;filename=" + filename + date + ".xls");
				ExcelUtil.exportEmployeeByPoi(os, list);
				// dao.setRecord2finsh();
				os.flush();
				os.close();
				return null;
			} else {
				request.setAttribute("PageNum", 0);
				return "toinCash";
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String printExcelByDate() throws ParseException {
		String dateTime = request.getParameter("date");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date2 = format.parse(dateTime);
		try {
			List<InCash> list = dao.getIncashRecord(date2);
			if (list != null) {
				OutputStream os = response.getOutputStream();
				String date = DateUtil.getDateFormat(new Date(), Constant.YYYY_MM_DD);
				String filename = URLEncoder.encode("同城快递提现报表", "utf-8");
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition", "attachment;filename=" + filename + date + ".xls");
				ExcelUtil.exportEmployeeByPoi(os, list);
				dao.setRecord2finsh();
				os.flush();
				os.close();
				return null;
			} else {
				request.setAttribute("PageNum", 0);
				return "toinCash";
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String toCreateAdmin() {
		return "add_admin";
	}

	public String toCreateCityAdmin() {
		try {
			request.setAttribute("cityList", CityUtil.getCityList("广东"));
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
		return "add_city_admin";
	}

	/**
	 * 跳转修改管理员权限页面
	 * 
	 * @return
	 */
	public String toChangeQX() {
		Admin admin = (Admin) session.getAttribute("adminSession");
		List<Admin> admins = dao.getAdmin(admin.getCity());
		request.setAttribute("admins", admins);
		return "change";
	}

	/**
	 * 修改管理员权限
	 */
	public void changeQX() {
		String username = request.getParameter("username");
		String userManage = request.getParameter("userManage");
		String checkUser = request.getParameter("checkUser");
		String userMsg = request.getParameter("userMsg");
		String userAdvice = request.getParameter("userAdvice");
		String userATM = request.getParameter("userATM");
		String orderManage = request.getParameter("orderManage");
		String adminManage = request.getParameter("adminManage");
		String msgManage = request.getParameter("msgManage");
		String activitySet = request.getParameter("activitySet");

		if (username == null || "admin".equals(username)) {
			dataMap.put("result", "forbid");
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
		System.out.println("---------" + username + "-------");
		boolean result = dao.updateUserQX(username, userManage, checkUser, userMsg, userAdvice, userATM, orderManage, adminManage, msgManage, activitySet);

		if (result) {
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

	/**
	 * 通过管理员名字获取管理员详细
	 */
	public void getAdminByName() {
		String username = request.getParameter("username");
		System.out.println("---------" + username + "---------");
		if (username != null) {
			Admin admin = dao.findAdmin(username);
			if (admin != null) {
				try {
					JsonUtil.writeToResponse(admin);
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
			dataMap.put("result", "error");
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
	 * 推送公告
	 * 
	 * @return
	 */
	public String pushNotice() {
		String msg = request.getParameter("msg");
		String username = request.getParameter("username");
		String client = request.getParameter("client");
		System.out.println("===================" + msg + "===================");
		if ("capp".equals(client)) {
			notice.setClient("快递员APP");
			MsgPush.push(msg, Constant.JPUSH_CCMASTER_SECRET, Constant.JPUSH_CAPPKEY, "公告");
		} else if ("uapp".equals(client)) {
			notice.setClient("普通用户APP");
			MsgPush.push(msg, Constant.JPUSH_UCMASTER_SECRET, Constant.JPUSH_UAPPKEY, "公告");

		}
		notice.setName(username);
		notice.setContent(msg);
		notice.setDate(DateUtil.getDateFormat(new Date(), Constant.YYYY_MM_DD_HH_MM_SS));
		dao.save(notice);
		List<SysNotice> list = dao.getNotice(0);
		int pageNum = (int) Math.ceil(Double.valueOf(dao.getAllNotice().size()) / 10.0);
		request.setAttribute("PageNum", pageNum);
		request.setAttribute("notice", list);
		return "announcement";
	}

	/**
	 * 获取公告
	 */
	public String getPushNotice() {
		int first = Integer.valueOf(request.getParameter("first"));
		List<SysNotice> list = dao.getNotice(first);
		try {
			JsonUtil.writeToResponse(list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "announcement";
	}

	public String toCouponPage() {
		if (!couponType) {
			if (dao.getCouponType() == null) {
				dao.setCouponType();
			}
			couponType = true;
		}
		List<CouponType> list = dao.getCouponType();
		request.setAttribute("coupon", list);
		List<String> cityList = null;
		try {
			cityList = CityUtil.getCityList("广东");
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		request.setAttribute("cityList", cityList);
		return "couponPage";
	}

	public void changeCouponValue() {
		String couponType = request.getParameter("couponType");
		String valueString = request.getParameter("value");
		if (couponType == null) {
			List<CouponType> list = dao.getCouponType();
			request.setAttribute("coupon", list);
			dataMap.put("result", "valueError");
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
		if (couponType.equals("registerCoupon") || couponType.equals("toll")) {
			double value = Double.valueOf(valueString);
			if (value < 0 || value > 100) {
				dataMap.put("result", "valueError");
				return;
			}
			if (couponType.equals("toll")) {
				value = value / 100;
			}
			dao.changeCouponValue(couponType, value);
			Constant.TOLL = 0;
			dataMap.put("result", "success");
		} else if (couponType.equals("awardCoupon")) {
			double max = Double.valueOf(request.getParameter("max"));
			double min = Double.valueOf(request.getParameter("min"));
			if (max >= min & min >= 0) {
				dao.changeCouponValue("max", max);
				dao.changeCouponValue("min", min);
				Constant.MAX = -1;
				Constant.MIN = -1;
				dataMap.put("result", "success");
			} else {
				dataMap.put("result", "valueError");
			}
		} else {
			dataMap.put("result", "setError");
		}
		List<CouponType> list = dao.getCouponType();
		request.setAttribute("coupon", list);
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
	 * 跳转到交易明细页面
	 * 
	 * @return
	 */
	public String toCPDpage() {
		String times = request.getParameter("times");
		String flag = request.getParameter("flag");
		Admin admin = (Admin) session.getAttribute("adminSession");
		String area = admin.getCity();
		if (times != null) {
			List<CompanyPaymentDetails> list = dao.getCPD(0, area);
			int pageNum = (int) Math.ceil(dao.getCPDNUM(area) / 10.0f);
			double value = dao.getTollValue(area);
			double payValue = dao.getPayValue(area);
			double couponValue = dao.getCouponValue(area);
			request.setAttribute("couponValue", couponValue);
			request.setAttribute("payValue", -payValue);
			request.setAttribute("value", value);
			request.setAttribute("list", list);
			request.setAttribute("PageNum", pageNum);
			request.setAttribute("years", "不限");
			request.setAttribute("months", "不限");
			request.setAttribute("days", "不限");
			return "details";
		} else if (flag != null) {
			String year = request.getParameter("year");
			String month = request.getParameter("month");
			String day = request.getParameter("day");
			String twice = request.getParameter("twice");
			if (year == null) {
				year = "不限";
			}
			if (month == null) {
				month = "不限";
			}
			if (day == null) {
				day = "不限";
			}
			if (twice == null) {
				System.out.println("条件查询第一次");
				List<CompanyPaymentDetails> list = dao.getCPD(0, year, month, day, area);
				int pageNum = (int) Math.ceil(dao.getCPDNUM(year, month, day, area) / 10.0f);
				double value = dao.getTollValue(year, month, day, area);
				double payValue = dao.getPayValue(year, month, day, area);
				double couponValue = dao.getCouponValue(year, month, day, area);
				request.setAttribute("couponValue", couponValue);
				request.setAttribute("payValue", -payValue);
				request.setAttribute("value", value);
				request.setAttribute("list", list);
				request.setAttribute("PageNum", pageNum);
				request.setAttribute("years", year);
				request.setAttribute("months", month);
				request.setAttribute("days", day);
				return "details";
			} else {
				System.out.println("条件查询分页查询");
				int first = Integer.valueOf(request.getParameter("first"));
				List<CompanyPaymentDetails> list = dao.getCPD(first, year, month, day, area);
				try {
					JsonUtil.writeToResponse(list);
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		} else {
			int first = Integer.valueOf(request.getParameter("first"));
			List<CompanyPaymentDetails> list = dao.getCPD(first, area);
			try {
				JsonUtil.writeToResponse(list);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * 获取地区会员数
	 * 
	 * @throws FileNotFoundException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 */
	public void getAreaNum() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		System.out.println("测试地区");
		Admin admin = (Admin) session.getAttribute("adminSession");
		String area = admin.getCity();
		if (area == null)
			area = "惠州";
		List<String> list = CityUtil.getCountyList("广东", area);
		if (list != null)
			for (int i = 0; i < list.size(); i++) {
				dataMap.put(list.get(i), (int) dao.getMemberNum(list.get(i)));
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

	public void getOrderCount() {
		String area = request.getParameter("area");
		String year = request.getParameter("year");
		List<Long> list = dao.getOrderCountList(area, year);
		for (int i = 0; i < list.size(); i++) {
			dataMap.put(i + 1 + "", list.get(i));
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

	public void no() {

	}

	public void chargeMoney() throws Exception {
		String inCashId = request.getParameter("inCashId");
		InCash inCash = dao.getIncashRecord(inCashId);
		User inCashUser = userService.getUserByMoBile(inCash.getMobile());
		if (inCashUser.getOpenId() == null || "".equals(inCashUser.getOpenId())) {
			dataMap.put("result", "提现失败");
		} else if (inCash.isResults()) {
			dataMap.put("result", "该订单已提现");
		} else {
			Map<String, String> map = WeiXinPublicPayUtil.ATM(inCashUser.getOpenId(), inCash.getBalance(), request.getRemoteHost(), inCash.getId(), inCashUser.getRole());
			String result = map.get("result_code");
			if (result != null && "SUCCESS".equals(result)) {
				inCash.setResults(true);
				dao.updateIncash(inCash);
				dataMap.put("result", "提现完成");
			} else {
				dataMap.put("result", map.get("return_msg"));
			}
		}
		JsonUtil.writeToResponse(dataMap);
	}

	public void charge2Money() throws Exception {
		String inCashId = request.getParameter("inCashId");
		InCash inCash = dao.getIncashRecord(inCashId);
		if (inCash.isResults()) {
			dataMap.put("result", "该订单已提现");
		} else {
			inCash.setResults(true);
			dao.updateIncash(inCash);
			dataMap.put("result", "提现完成");
		}
		JsonUtil.writeToResponse(dataMap);
	}

}
