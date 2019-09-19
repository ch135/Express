<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta charset="UTF-8">
	<title>同城后台-订单详情</title>
	<link rel="shortcut icon" type="/Express/image/x-icon" href="/Express/img/Search.png" />
	<link rel="stylesheet" href="/Express/css/inc_style.css">
	<link rel="stylesheet" href="/Express/css/unfinish_order.css">
</head>
<body>
	<!-- 头部 -->
	<jsp:include page="header_inc.jsp">
	<jsp:param name="username" value= '<%=session.getAttribute("username") %>'/>
	</jsp:include> 
	<!-- 主干区 -->
	<div class="main clearfix">
	
	<jsp:include page="menu.jsp">
	<jsp:param name="clazz" value= "unfinish_order"/>
	</jsp:include>
	
	<!-- 主内容 -->
		<div class="main_content" id="tabs">
			<h2 class="main_content_title">未完成订单</h2>
			<ul class="title_option">
				<li><a href="#fragment-1" class="select">订单概况</a></li>
				<li><a href="#fragment-2" class="show">订单详情</a></li>
			</ul>

			<div class="unfinish_order_box" id="fragment-1">
				<div class="user_message_table">
					<table class="unfinish_order_firstTable tablesorter" cellspacing = "0">
					<thead>
						<tr class="Title">
							<th>订单编号</th>
							<th>订单价格</th>
							<th>物品描述</th>
							<th class="canSort">下单时间<img src="/Express/img/sort.png" alt="" name = "0"></th>	

							<th></th>		
						</tr>
					</thead>
					<tbody>
					<c:forEach var="o" items="${order}">
						<tr>
							<td class='orderId'>${o.orderId}</td>
							<td class='orderFare'>${o.orderFare}</td>
							<td class='goodsDetail'>${o.goodsDetail}</td>
							<td class='requestDate'><fmt:formatDate value="${o.requestDate}" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
							<td><button class="btn ok details">详细资料</button></td>
						</tr>
					</c:forEach>
					</tbody>
					</table>
					<c:if test="${PageNum==0}">
						<div style='width:100%;height:100px;'>
							<h1 style="text-align:center;font-size:20px;line-height:200px;color:#8C8C8C;">抱歉,暂无数据 ~<h1>
						</div>
					</c:if>
				</div>
				<c:if test="${PageNum>5}">
				<div class="next_page_box">
					<span class="last_page go_page" name="${PageNum}">尾页</span>
					<span class="next_page go_page"><i class="iconfont">&#xe66c;</i></span>
					<span class="go_page">5</span>
					<span class="go_page">4</span>
					<span class="go_page">3</span>
					<span class="go_page">2</span>
					<span class="go_page here_page">1</span>
					<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
				</div>
				</c:if>
				<c:if test="${PageNum<=5}">
				<div class="next_page_box">
					<c:if test="${PageNum>1}">
						<span class="last_page go_page" name="${PageNum}">尾页</span>
						<span class="next_page go_page"><i class="iconfont">&#xe66c;</i></span>
					</c:if>
					<c:choose>
       					<c:when test="${PageNum==1}">
             				<span class="go_page here_page">1</span>
             				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
       					<c:when test="${PageNum==2}">
              				<span class="go_page">2</span>
              				<span class="go_page here_page">1</span>
              				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
       					<c:when test="${PageNum==3}">
              				<span class="go_page">3</span>
              				<span class="go_page">2</span>
              				<span class="go_page here_page">1</span>
              				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
       					<c:when test="${PageNum==4}">
              				<span class="go_page">4</span>
              				<span class="go_page">3</span>
              				<span class="go_page">2</span>
              				<span class="go_page here_page">1</span>
              				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
       					<c:when test="${PageNum==5}">
              				<span class="go_page">5</span>
              				<span class="go_page">4</span>
              				<span class="go_page">3</span>
              				<span class="go_page">2</span>
              				<span class="go_page here_page">1</span>
              				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
					</c:choose>			
				</div>
				</c:if>
			</div>

			<div class="unfinish_order_box Order_details" id="fragment-2">
				<div style='width:100%;height:200px;'>
					<h1 style="text-align:center;font-size:20px;line-height:200px;color:#8C8C8C;">您尚未选择订单 ~<h1>
				</div>
			</div>	

		</div>	
	</div>
	
	<!-- 底部 -->
	<div class="footer">
		<span>©拓嵘科技 2016 </span>
	</div>

	<script src="/Express/js/tool_inc.js"></script>	
	<script src="/Express/js/jquery-2.1.1.js"></script>
	<script src="/Express/js/jquery-ui.min.js"></script>
	<script src="/Express/js/unfinish_order.js"></script>
	<script src="/Express/js/inc_action.js"></script>
</body>
</html>
