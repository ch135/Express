<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
<head>
<meta charset="UTF-8">
<title>同城管理--用户信息</title>
<link rel="stylesheet" href="/Express/css/inc_style.css">
<link rel="stylesheet" href="/Express/css/user_message.css">
</head>
 <style type="text/css">
            *{ margin: 0; padding: 0;}
            a{ text-decoration: none; margin: 0 5px; color: #333;}
            #ul1{ width: 600px; height: 240px; list-style: none; margin: 50px auto; overflow: hidden; position: relative;}
            #ul1 li{ width: 100px; height: 100px; background: olive; float: left; margin: 10px;}
            #div1{ margin-top:15px; text-align: center;}
        </style>
<body>
	<!-- 头部 -->
	<jsp:include page="header_inc.jsp">
		<jsp:param name="username" value='<%=session.getAttribute("username")%>' />
	</jsp:include>
	<!-- 主干区 -->
	<div class="main clearfix">

		<jsp:include page="menu.jsp">
			<jsp:param name="clazz" value="message" />
		</jsp:include>

		<!-- 主内容 -->
		<div class="main_content" id="tabs">
			<h2 class="main_content_title">用户信息</h2>
			<ul class="title_option">
				<li><a href="#fragment-2" class="select userList">用户列表</a></li>
				<li><a href="#fragment-1" class="show">权限编辑</a></li>
				<li><a href="#fragment-3" class="showOrderTag">订单记录</a></li>
				<li class="user-search-li"><input type="text" class="user-search-input" placeholder="输入手机号或姓名查询"><span class='user-search_btn' title='搜索'><i class="iconfont">&#xe662;</i></span></li>
			</ul>

			<div class="user_message_box" id="fragment-2">
				<div class="user_message_table">
					<table class="user_table tablesorter" cellspacing="0">
						<thead>
							<tr class="Title">
								<th>头像</th>
								<th>用户名</th>
								<th class="canSort">性别<img src="/Express/img/sort.png" alt="" name="0"></th>
								<th class="canSort">手机号<img src="/Express/img/sort.png" alt="" name="0"></th>
								<th class="canSort">身份证号<img src="/Express/img/sort.png" alt="" name="0"></th>
								<th>评分</th>
								<th>账户余额</th>
								<th>信誉值</th>
								<th>可否接单</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="p" items="${user}">
								<tr title="">
									<td><img src="${p.path}" alt=""></td>
									<td class='name'>${p.name}</td>
									<td class='sex'>${p.sex }</td>
									<td class='mobile'>${p.mobile }</td>
									<td class='identity'>${p.identity }</td>
									<td class='pingfen'>${p.pingfen }</td>
									<td class='balance'>${p.balance }</td>
									<td class='credit'>${p.credit }</td>
									<td class='accept_able'>${p.accept_able }</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<c:if test="${PageNum==0}">
						<div style='width:100%;height:100px;'>
							<h1 style="text-align:center;font-size:20px;line-height:200px;color:#8C8C8C;">
								暂无数据
								<h1>
						</div>
					</c:if>
				</div>
				<c:if test="${pageNum>5}">
					<div class="next_page_box">
						<span class="go_page last_page" name="${pageNum}">尾页</span> <span class="next_page go_page"><i class="iconfont">&#xe66c;</i></span> <span class="go_page">5</span> <span class="go_page">4</span> <span class="go_page">3</span> <span class="go_page">2</span> <span class="go_page here_page">1</span>
						<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
					</div>
				</c:if>
				<c:if test="${pageNum<=5}">
					<div class="next_page_box">
						<c:if test="${pageNum>1}">
							<span class="last_page go_page" name="${pageNum}">尾页</span>
							<span class="next_page go_page"><i class="iconfont">&#xe66c;</i></span>
						</c:if>
						<c:choose>
							<c:when test="${pageNum==1}">
								<span class="go_page here_page">1</span>
								<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
							</c:when>
							<c:when test="${pageNum==2}">
								<span class="go_page">2</span>
								<span class="go_page here_page">1</span>
								<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
							</c:when>
							<c:when test="${pageNum==3}">
								<span class="go_page">3</span>
								<span class="go_page">2</span>
								<span class="go_page here_page">1</span>
								<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
							</c:when>
							<c:when test="${pageNum==4}">
								<span class="go_page">4</span>
								<span class="go_page">3</span>
								<span class="go_page">2</span>
								<span class="go_page here_page">1</span>
								<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
							</c:when>
							<c:when test="${pageNum==5}">
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

			<div class="user_message_box" id="fragment-1" >
				<div class="information_box">
					<div style='width:100%;height:200px;'>
						<h1 style="text-align:center;font-size:20px;line-height:200px;color:#8C8C8C;">
							您尚未选择用户
							<h1>
					</div>
				</div>
			</div>


			<div class="finish_order_box" id="fragment-3" style="display:none ">
				<div class="user_message_table">
					<table class="finish_order_firstTable tablesorter" cellspacing="0">
						<thead>
							<tr class="Title">
								<th>订单编号</th>
								<th>订单价格</th>
								<th>物品描述</th>
								<th>订单状态</th>
								<th class="canSort">下单时间<img src="/Express/img/sort.png" alt="" name="0"></th>
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
							<h1 style="text-align:center;font-size:20px;line-height:200px;color:#8C8C8C;">
								抱歉,暂无数据 ~
								<h1>
						</div>
					</c:if>
				</div>
				<div id="div1"></div>
			</div>
		</div>
	</div>

	<!-- 底部 -->
	<div class="footer">
		<span>©拓嵘科技 2016 </span>
	</div>


	<script src="/Express/js/jquery-2.1.1.js"></script>
	<script src="/Express/js/jquery-ui.min.js"></script>
	<script src="/Express/js/user_message.js"></script>
	<script src="/Express/js/inc_action.js"></script>
</body>
</html>