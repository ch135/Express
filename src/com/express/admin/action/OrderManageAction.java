package com.express.admin.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;






import java.util.Map;

import com.express.admin.service.OrderService;
import com.express.admin.service.impl.OrderServiceImpl;
import com.express.model.Admin;
import com.express.model.Order;
import com.express.util.JsonUtil;
import com.express.util.SuperAction;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderManageAction extends SuperAction {
	OrderService dao = new OrderServiceImpl();
	Order order = new Order();
	
	/**
	 * 关闭订单
	 */
	public String closeOrder() {
		System.out.println("我进来了，不知道你看到没有");
		String orderId = request.getParameter("orderId");
		order.setOrderId(orderId);
		dao.closeOrder(order);
		return "close_success";
	}
	
	/**
	 * 查找订单
	 * 
	 * @return
	 */
	public String findOrder() {
		System.out.println("我在查询？");
		String orderId = request.getParameter("orderId");
		order.setOrderId(orderId);
		Order order1 = dao.findOrder(order);
		if (order1 != null) {
			System.out.println("我返回了东西了");
			try {
				JsonUtil.writeToResponse(order1);
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		} else {
			return null;
		}
	}  
	
	/**
	 * 根据状态浏览订单第一次查询
	 */
	public String unfinshOrder1(){
		Admin admin = (Admin)session.getAttribute("adminSession");
		String area = admin.getCity();
		int first = Integer.valueOf(request.getParameter("first"));
		List<Order> list = dao.getUnfinshOrder(first,area);
		int pageNum = (int) Math.ceil(dao.getUnfinshOrderNum(area)/10.0f);
		System.out.println("pageNum-----------"+pageNum);
		request.setAttribute("order", list);
		request.setAttribute("PageNum", pageNum);
		return "unfinshOrder";
	}
	
	/**
	 * 根据状态浏览超时订单
	 * @author chenhao
	 * @data 2017/5/23 19:49
	 */
	public String overtimeOrder(){
		Admin admin = (Admin)session.getAttribute("adminSession");
		String area = admin.getCity();
		int first = Integer.valueOf(request.getParameter("first"));
		List<Order> list = dao.getOvertimeOrder(first,area);
		int pageNum = (int) Math.ceil(dao.getOvertimeOrderNum(area)/10.0f);
		System.out.println("超时pageNum-----------"+pageNum);
		request.setAttribute("order", list);
		request.setAttribute("PageNum", pageNum);
		return "overtimeOrder";
	}
	/**
	 * 根据页数查询超时订单
	 * @author chenhao
	 * @time 2017/5/23 21:39
	 */
	public void overtimeOrders(){
		Admin admin = (Admin)session.getAttribute("adminSession");
		String area = admin.getCity();
		int first = Integer.valueOf(request.getParameter("first"));
		List<Order> list = dao.getOvertimeOrder(first,area);
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
	 * 根据状态浏览订单查询
	 */
	public void unfinshOrder(){
		Admin admin = (Admin)session.getAttribute("adminSession");
		String area = admin.getCity();
		int first = Integer.valueOf(request.getParameter("first"));
		List<Order> list = dao.getUnfinshOrder(first,area);
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
	
	
	public String finshOrder(){
		Admin admin = (Admin)session.getAttribute("adminSession");
		String area = admin.getCity();
		String times = request.getParameter("times");
		List<Order> list = null;
		if(times.equals("first")){
			list = dao.getFinshOrder(0,area);
			int pageNum = (int) Math.ceil(dao.getFinshOrderNum(area)/10.0f);
			System.out.println("pageNum-----------"+pageNum);
			request.setAttribute("order", list);
			request.setAttribute("PageNum", pageNum);
		}else{
			int first = Integer.valueOf(request.getParameter("first"));
			list = dao.getFinshOrder(first,area);
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
		return "finshOrder";
	}
	
	public void timefinshOrder() throws ParseException, IOException{
		Admin admin = (Admin)session.getAttribute("adminSession");
		Map<String,Object> map=new HashMap<String,Object>();
		Map<String,Object> result=new HashMap<String,Object>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		ObjectMapper object=new ObjectMapper();
		PrintWriter out=response.getWriter();
		Date start=sdf.parse(request.getParameter("start"));
		String endtime=request.getParameter("end");
		String[] tis=endtime.split("-");
		int ti=Integer.parseInt(tis[2])+1;
		if(ti>10){
			tis[2]=ti+"";
		}else{
			tis[2]="0"+ti;
		};
		endtime=tis[0]+"-"+tis[1]+"-"+tis[2];
		Date end=sdf.parse(endtime);
		map.put("area", admin.getCity());
		map.put("name", request.getParameter("username"));
		map.put("start", start);
		map.put("end", end);
		String number=request.getParameter("number");
		List<Order> list = null;
		if(number.equals("0")){
			map.put("number", 0);
			list = dao.gettimefinshOrder(map);
			long num=dao.gettimeFinshOrderNum(map);
			int pageNum = (int) Math.ceil(num/10.0f);
			System.out.println("pageNum-----------"+pageNum);
			result.put("num", num);
			result.put("order",list);
			result.put("pagenum", pageNum);
			String st=object.writeValueAsString(result);
			out.print(st);
		}else{
			int first = Integer.valueOf(request.getParameter("number"));
			map.put("number", first);
			list = dao.gettimefinshOrder(map);
			result.put("order",list);
			String st=object.writeValueAsString(result);
			out.print(st);
		}
		if(out!=null){
			out.close();
		}
	}
	
}
