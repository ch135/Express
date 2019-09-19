package com.express.admin.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.struts2.ServletActionContext;

import com.express.admin.service.OrderService;
import com.express.admin.service.UserService;
import com.express.admin.service.impl.OrderServiceImpl;
import com.express.admin.service.impl.UserServiceImpl;
import com.express.model.Admin;
import com.express.model.AreaNorm;
import com.express.model.Order;
import com.express.model.User;
import com.express.model.UserAdvice;
import com.express.model.UserChargeMoney;
import com.express.model.UserIDcard;
import com.express.util.CityUtil;
import com.express.util.IdentityCheck;
import com.express.util.JsonUtil;
import com.express.util.SuperAction;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class UserManageAction extends SuperAction {
	private static final long serialVersionUID = 1L;
	
	UserService service = new UserServiceImpl();
	OrderService orderService = new OrderServiceImpl();
	UserService userService = new UserServiceImpl();
	private Map<String, Object> dataMap = new HashMap<String, Object>();
	User user = new User();
	Order order = new Order();
	UserIDcard userIDcard = new UserIDcard();

	/**
	 * 用户建议
	 * 
	 * @return
	 */
	public String lookAdvice() {
		int first = Integer.valueOf(request.getParameter("first"));
		List<UserAdvice> list = service.findAdvice(first);
		int pageNum = (int) Math.ceil(Double.valueOf(service.getAllAdvice().size()) / 10.0);
		if (list.size() > 0) {
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("advice", list);
			return "advice";
		} else {
			request.setAttribute("pageNum", 0);
			System.out.println("没有找到建议");
			return "advice";
		}
	}

	/**
	 * 审核用户信息
	 * 
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public String pass() throws JsonGenerationException, JsonMappingException, IOException {
		String flag = request.getParameter("flag");
		String id = request.getParameter("id");
		String reason = request.getParameter("reason");
		userIDcard.setId(id);
		userIDcard.setReason(reason);
		if (flag.equals("pass")) {
			// 2代表通过二级认证
			userIDcard.setResults("yes");
			service.updateIdentity(userIDcard);
		} else {
			userIDcard.setResults("no");
			service.updateIdentity(userIDcard);
		}
		Admin admin = (Admin) session.getAttribute("adminSession");
		String area = admin.getCity();
		// 0代表未审核
		List<UserIDcard> needCheck = service.findAllUserIdentity(0, "0", area);
		int needCheckPageNum = service.findAllUserIdentity("0", area) / 10;
		// 1代表已审核
		List<UserIDcard> finshCheck = service.findAllUserIdentity(0, "1", area);
		int finshCheckPageNum = service.findAllUserIdentity("1", area) / 10;

		request.setAttribute("needCheckPageNum", needCheckPageNum);
		request.setAttribute("needCheck", needCheck);
		request.setAttribute("finshCheckPageNum", finshCheckPageNum);
		request.setAttribute("finshCheck", finshCheck);
		return "Identity";
	}

	/**
	 * 查找所有需要检查的信息
	 * 
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String getAllUserIdentity() throws ServletException, IOException {
		int first = Integer.valueOf(request.getParameter("first"));
		Admin admin = (Admin) session.getAttribute("adminSession");
		String area = admin.getCity();
		// 0代表未审核
		List<UserIDcard> needCheck = service.findAllUserIdentity(first, "0", area);
		int needCheckPageNum = service.findAllUserIdentity("0", area) / 10;
		// 1代表已审核
		List<UserIDcard> finshCheck = service.findAllUserIdentity(first, "1", area);
		int finshCheckPageNum = service.findAllUserIdentity("1", area) / 10;

		request.setAttribute("needCheckPageNum", needCheckPageNum);
		request.setAttribute("needCheck", needCheck);
		request.setAttribute("finshCheckPageNum", finshCheckPageNum);
		request.setAttribute("finshCheck", finshCheck);
		return "Identity";
	}

	/**
	 * 获取已（未）审批的详细信息
	 */
	public void getAllUserIdentity2() {
		int first = Integer.valueOf(request.getParameter("first"));
		String type = request.getParameter("type");
		Admin admin = (Admin) session.getAttribute("adminSession");
		String area = admin.getCity();
		if ("unfinsh".equals(type)) {
			// 0代表未审核
			try {
				List<UserIDcard> needCheck = service.findAllUserIdentity(first, "0", area);
				JsonUtil.writeToResponse(needCheck);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// 1代表已审核
			try {
				List<UserIDcard> finshCheck = service.findAllUserIdentity(first, "1", area);
				JsonUtil.writeToResponse(finshCheck);
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
	 * 检测身份证是否合法
	 * 
	 * @throws IOException
	 */
	public void identityCheck() throws IOException {
		String contentType = "text/html;charset=utf-8";
		// 指定输出内容类型和编码
		ServletActionContext.getResponse().setContentType(contentType);
		// 获取输出流，然后使用
		PrintWriter out = ServletActionContext.getResponse().getWriter();
		String identity = request.getParameter("identity");
		String sex = IdentityCheck.Check(identity);
		if (sex != null) {
			session.setAttribute("sex", sex);
			out.print("身份证合法");
		} else {
			out.print("身份证不合法");
		}
	}

	/**
	 * 地区用户数量统计
	 */
	public void areaPeople() {
		String proName = request.getParameter("proName");
		Map<String, Integer> map;
		try {
			map = service.statistic(proName);
			JsonUtil.writeToResponse(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 第一次获取所有用户
	 */
	public String getAllUser1() {
		Admin admin = (Admin) session.getAttribute("adminSession");
		int first = Integer.valueOf(request.getParameter("first"));
		List<User> list = service.getAllUser(first, admin.getCity());
		int pageNum = (int) Math.ceil(Double.valueOf(service.getAllUser(admin.getCity()).size()) / 10.0);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("user", list);
		return "allUser";
	}

	/**
	 * 获取所有用户
	 * 
	 * @throws JsonProcessingException
	 */
	public void getAllUser() throws JsonProcessingException {
		Admin admin = (Admin) session.getAttribute("adminSession");
		System.out.println("传过来的值：" + request.getParameter("first"));
		int first = Integer.valueOf(request.getParameter("first"));
		List<User> list = service.getAllUser(first, admin.getCity());
		try {
			JsonUtil.writeToResponse(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void getUser() {
		String mobile = request.getParameter("mobile");
		User user = service.getUser(mobile);
		try {
			JsonUtil.writeToResponse(user);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getIdentity() {
		String identity = request.getParameter("identity");
		UserIDcard uDcard = service.getIdentity(identity);
		dataMap.put("idCard", uDcard);
		String sex = IdentityCheck.Check(uDcard.getIdentity());
		if (sex != null) {
			uDcard.setSex(sex);
			service.updateIdentity(uDcard);
			dataMap.put("results", "1");
		} else {
			dataMap.put("results", "0");
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

	public void changeAble() {
		String mobile = request.getParameter("mobile");
		String flag = request.getParameter("flag");
		boolean able;
		if (flag.equals("true")) {
			able = true;
		} else {
			able = false;
		}
		service.changeUserAble(mobile, able);
		dataMap.put("results", "success");
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

	public void searchUser() {
		String value = request.getParameter("value").trim().replace(" ", "");
		List<User> list = service.searchUser(value);
		if (list != null) {
			try {
				JsonUtil.writeToResponse(list);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			dataMap.put("results", "error");
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
	 * 设置免押金权限
	 */
	public void setPass() {
		String mobile = request.getParameter("mobile");
		String pass = request.getParameter("pass");

		if (pass != null && mobile != null) {
			if (pass.equals("true")) {
				service.setUserPass(mobile, true);
				dataMap.put("result", "success");
			} else if (pass.equals("false")) {
				service.setUserPass(mobile, false);
				dataMap.put("result", "success");
			} else {
				System.out.println("参数不全");
				dataMap.put("result", "valueError");
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
			return;
		}
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

	/**
	 * 获取省份
	 */
	public void getProvince() {
		try {
			List<String> list = CityUtil.getProList();
			try {
				JsonUtil.writeToResponse(list);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取城市列表
	 * 
	 * @throws FileNotFoundException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 */
	public void getCity() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		String provinceName = request.getParameter("provinceName");
		List<String> list = CityUtil.getCityList(provinceName);
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

	/**
	 * 获取县区
	 */
	public void getCounty() {
		String provinceName = request.getParameter("provinceName");
		String cityName = request.getParameter("cityName");
		try {
			List<String> list = CityUtil.getCountyList(provinceName, cityName);
			try {
				JsonUtil.writeToResponse(list);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取地区和起步价
	 * 
	 * @throws FileNotFoundException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 * @throws JsonProcessingException 
	 */
	public void getAreaList() throws JsonIOException, JsonSyntaxException, FileNotFoundException, JsonProcessingException {
		String cityName = request.getParameter("cityName");
		List<AreaNorm> areaNorm = service.getAreaPrice(cityName+"市");
		List<String> areaList = CityUtil.getCountyList("广东", cityName);
		Map<String,Object> map=new HashMap<String,Object>();
		ObjectMapper object=new ObjectMapper();
		for(int i=0;i<areaNorm.size();i++){
			AreaNorm area=areaNorm.get(i);
			map.put("time"+(i*2), area.getStart_time());
			map.put("time"+(i*2+1), area.getEnd_time());
			map.put("value"+i,area.getValue());
		}
		String result=object.writeValueAsString(map);
		dataMap.put("list", areaList);
		dataMap.put("value", result);
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

	/**
	 * 获取起步价
	 * @throws JsonProcessingException 
	 */
	public void getAreaPrice() throws JsonIOException, JsonSyntaxException, FileNotFoundException, JsonProcessingException {
		String areaName = request.getParameter("areaName");
		List<AreaNorm> areaNorm = service.getAreaPrice(areaName);
		Map<String,Object> map=new HashMap<String,Object>();
		ObjectMapper object=new ObjectMapper();
		for(int i=0;i<areaNorm.size();i++){
			AreaNorm area=areaNorm.get(i);
			map.put("time"+(i*2), area.getStart_time());
			map.put("time"+(i*2+1), area.getEnd_time());
			map.put("value"+i,area.getValue());
		}
		String result=object.writeValueAsString(map);
		dataMap.put("value", result);
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

	/**
	 * 设置起步价
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws ServletException 
	 * @throws IllegalStateException 
	 */
	public void setAreaPrice() throws JsonParseException, JsonMappingException, IOException, IllegalStateException, ServletException {
		String name=request.getParameter("name");
		List<Time> start=new ArrayList<Time>();
		List<Time> end=new ArrayList<Time>();
		double[] value=new double[3];
		List<AreaNorm> result = new ArrayList<AreaNorm>();
		AreaNorm area=new AreaNorm();
		for(int i=1;i<=3;i++){
			start.add(Time.valueOf(request.getParameter("start_time"+i)));
			end.add(Time.valueOf(request.getParameter("end_time"+i)));
			value[i-1]=Double.parseDouble(request.getParameter("value"+i));
		}
		if("".equals(name)){
			name="default";
		}
		result=service.getAreaNorms(name);
		for(int i=0;i<3;i++){
			area.setId(result.get(i).getId());
			area.setName(name);
			area.setStart_time(start.get(i));
			area.setEnd_time(end.get(i));
			area.setValue(value[i]);
			if (service.setAreaPrice(area)) {
				dataMap.put("result"+i, "success");
			} else {
				dataMap.put("result"+i, "fail");
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

	public String toChargePage() {
		String times = request.getParameter("times");
		Admin admin = (Admin) session.getAttribute("adminSession");
		String area = admin.getCity();
		if (times != null) {
			List<UserChargeMoney> list = service.getChargeRecord(0, area);
			int pageNum = (int) Math.ceil(service.getChargeRecordNum(area) / 10.0f);
			request.setAttribute("list", list);
			request.setAttribute("PageNum", pageNum);
			return "charge";
		} else {
			int first = Integer.valueOf(request.getParameter("first"));
			List<UserChargeMoney> list = service.getChargeRecord(first, area);
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

	public void findUserCharge() {
		String mobile = request.getParameter("mobile");
		List<UserChargeMoney> list = service.findUserCharge(mobile);
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

	public void getCourierOrder() throws JsonGenerationException, JsonMappingException, IOException {
		String userId = request.getParameter("userId");
		Integer page = 0;
		String pageStr = request.getParameter("page");
		if (pageStr != null) {
			page=Integer.parseInt(pageStr)-1;
		}
		List<Order> list = userService.findOrder(userId, page);
		Integer count = userService.findOrderCount(userId);
		dataMap.put("data", list);
		dataMap.put("pageNum", count/10+1);
		JsonUtil.writeToResponse(dataMap);
	}

}
