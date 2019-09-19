<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>同城管理--首页</title>
        <link rel="stylesheet" href="/Express/css/inc_style.css">
        <link rel="stylesheet" href="/Express/css/index.css">
    </head>
    <body>
	<jsp:include page="header_inc.jsp">
	<jsp:param name="username" value= '<%=session.getAttribute("username") %>'/>
	</jsp:include> 
	<!-- 主干区 -->
	<div class="main clearfix">
	
	<jsp:include page="menu.jsp">
	<jsp:param name="clazz" value= "null"/>
	</jsp:include>

		<div class="main_content">
			<h2 class="main_content_title">后台首页</h2>
			<div class="user_tongji">
				<span class="div_title">订单和用户统计</span>
				<div class="user_table">
					<div class="user_table_box">
						<a href="" class="dingdan_number user_table_a"><i class="iconfont">&#xe622;</i>订单数:<%=request.getAttribute("orderCount") %></a>
						<a href="" class="user_number user_table_a"><i class="iconfont">&#xe610;</i>注册用户:<%=request.getAttribute("userCount") %></a>
						<a href="" class="kuaidiyuan_number user_table_a"><i class="iconfont">&#xe6ae;</i>注册快递员:<%=request.getAttribute("cuserCount") %></a>
					</div>
				</div>
			<div class="user_hometown_tongji">
				<span class="div_title">用户地域统计</span>
				<h4 style="padding:30px 120px;">
					<span>选择城市:</span>
					<select id="city-select">
						<option value="" >选择城市</option>
						<c:forEach var="l" items="${cityList}">
							<option value="">${l}</option>
						</c:forEach>						
					</select>			
				</h4>
				<div class="user_hometown_table"></div>
				<h4 style="padding:30px 120px;">
					<span>选择县区:</span>
					<select id="area-select">
						<option value="" >选择县区</option>
					</select>	
					<span style="margin-left:20px">选择日期</span>	
					<select id="year-select">
						<option value="" >选择年份</option>
					</select>		
					<button class="btn" id="JYTJbtn">查询</button>				
				</h4>
				<div id="jyje_table"></div>
			</div>
		</div>
	</div>
	</div>
	<!-- 底部 -->
	<jsp:include page="footer_inc.jsp"/>
	<script src="/Express/js/jquery-2.1.1.js"></script>
	<script src="/Express/js/inc_action.js"></script>
	<script src="/Express/js/highcharts.js"></script>
	<script src="/Express/js/data.js"></script>
	<script src="/Express/js/index.js"></script>
    </body>
</html>