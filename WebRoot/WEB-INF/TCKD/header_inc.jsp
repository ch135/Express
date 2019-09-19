<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
	<!-- 头部 -->
	<nav class = "header">
		<!-- logo区 -->
		<div class="header_box">
		<a href="/Express/admin/Admin_toIndex">
			<div class="logo">
				<span class="logo_name">同城快递·管理平台</span>
			</div>
		</a>
		<!-- 右侧 -->
		<ul class="right user">
			<li class="user_name">
				<span class = "welcome">欢迎你,管理员</span>
				<span name = "user_name" class = "name"><%=request.getParameter("username") %>
				<ul class="dropdown_content">
					<a href="javascript:;" class = 'change_pw'>修改密码</a>
					<a href="/Express/admin/Admin_loginOut">退出</a>
				</ul>
				</span>
			</li>
		</ul>
		<ul class="right search_ul"> 
			<form action="/Express/admin/OrderManage_findOrder">
			<li class="search_li">
				<input type="text" class="search" name="orderId" placeholder = "订单查询"><span class='search_btn' title='搜索'><i class="iconfont">&#xe662;</i></span>
			</li>
			</form>
		</ul>
		</div>
	</nav>
