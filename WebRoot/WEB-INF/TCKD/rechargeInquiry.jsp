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
	<title>同城管理--充值查询</title>
	<link rel="stylesheet" href="/Express/css/inc_style.css">
	<link rel="stylesheet" href="/Express/css/rechargeInquiry.css">
</head>
<body>
	<jsp:include page="header_inc.jsp">
	<jsp:param name="username" value= '<%=session.getAttribute("username") %>'/>
	</jsp:include>
	<!-- 主干区 -->
	<div class="main clearfix">
	
	<jsp:include page="menu.jsp">
	<jsp:param name="clazz" value= "rechargeInquiry"/>
	</jsp:include>
		<!-- 主内容 -->
		<div class="main_content" id="tabs">
			<h2 class="main_content_title">充值查询</h2>
			<ul class="title_option">
				<li><a href="#fragment-2" class="select">用户充值查询</a></li>
			</ul>
			<div class="user_message_box" id="fragment-2">
				<h4 class="search-box"><span>记录查询 :</span> <input type="text" class="search-input" placeholder="输入用户手机号进行查询"/> <button class="btn" id="mobile-search">查询</button> </h4>
				<div class="user_message_table">
					<table class="user_table tablesorter" cellspacing = "0">
					<thead>
						<tr class="Title">
							<th>充值单号</th>
							<th>手机号</th>
							<th>充值金额</th>
							<th>充值方式</th>	
							<th>充值时间</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach var="l" items="${list}">
						<tr>
							<td>${l.id }</td>
							<td>${l.mobile }</td>
							<td>${l.money }</td>
							<td>${l.chargeType }</td>
							<td><fmt:formatDate value="${l.date}" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
						</tr>
					</c:forEach>
					</tbody>
					</table>
				</div>
					<c:if test="${PageNum==0}">
						<div style='width:100%;height:100px;'>
							<h1 style="text-align:center;font-size:20px;line-height:200px;color:#8C8C8C;">暂无数据 <h1>
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
		</div>
	</div>

	<!-- 底部 -->
	<div class="footer">
		<span>©拓嵘科技 2016 </span>
	</div>
	
	<script src="/Express/js/jquery-2.1.1.js"></script>
	<script src="/Express/js/inc_action.js"></script>
	<script src="/Express/js/tool_inc.js"></script>
	<script src="/Express/js/rechargeInquiry.js"></script>
</body>
</html>
