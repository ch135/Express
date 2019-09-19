<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ page language="java" import="com.express.model.Order" %> 
<% Order order = (Order)request.getAttribute("order");%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>订单详情</title>
    <base href="<%=basePath%>">
<link href="admin/css/bootstrap.min.css" rel="stylesheet">
<link href="admin/css/style.css" rel="stylesheet">
<script src="admin/js/jquery-2.1.1.js"></script>
<script src="admin/js/bootstrap.min.js"></script>
  </head>
  
  <body>
  <br>
  <div><h1 style="margin-left:140px">订单：<%=order.getOrderId()%></h1></div>
  <div class="order">
  		<div class="order_bd">
  				<div class="send">
  						<li>寄件人信息：<%=order.getSendName() %>,<%=order.getSendMobile()%>, 地址：<%=order.getSendAddress() %></li>
  						<li>订单备注：<%=order.getOrderRemark() %></li>
  						<hr>
  				</div>
  				<div class="send">
  						<li>收件人信息：<%=order.getReceiveName() %>,<%=order.getReceiveMobile() %>, 地址：<%=order.getReceiveAddress() %></li>
  						<hr>
  				</div>
  				<div class="send">
  						<li>接单人信息：<%=order.getCname()%>,<%=order.getCmobile()%>,评分：<%=order.getCgrade() %></li>
  						<hr>
  				</div>
  				<div class="order_info">
  						<li>订单信息：下单时间：<%=order.getRequestDate()%>  接单时间：<%=order.getReceiveDate() %> 订单状态：<% if(order.getOrderStaus()==-4){out.print("已关闭");}else{out.print("未完成");}%></li>
  						<hr style="margin: 5px 5px 5px 5px">
  						<ul id="imgmove">
  						<c:forEach items="${order.goodsPic}" var="pic" >
  						<li><a href="${pic}"><img src="${pic}" style="width:380px;height:330px" 
  						                       onload="return imgzoom(this,600);"
  						                       onclick="javascript:window.open(this.src);" style="cursor:pointer;"></a></li>
  						</c:forEach>
  						</ul>
  				</div>
  		</div>
  </div>
    		<div class="button_1">
    				<a href="admin/orderPage/OrderManage.jsp"  class="btn btn-success">返回查询</a>
    				&nbsp;&nbsp;&nbsp;&nbsp;
    				<a href="./admin/OrderManage_closeOrder.action?orderId=<%=order.getOrderId()%>"  class="btn btn-danger">关闭订单</a>
			</div>
  </body>
</html>
