<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>同城管理--登录jsp</title>
	<link rel="stylesheet" href="/Express/css/inc_style.css">
	<link rel="stylesheet" href="/Express/css/login.css">
</head>
<body>
	<!-- 头部 -->
	<nav class = "header">
		<div class="header_box">
			<div class="logo">
				<img src="" alt="">
			</div>
		</div>
	</nav>
	<form action= "<%=path%>/admin/Admin_login" id="commentForm" method="post">
	<div class="login_main">
		<div class="login_box">
			<div class="login">
				<span class="title">管理员登录</span>
				<div class="account_div login_row">
					<i class="iconfont">&#xe605;</i><input type="text" name="username" class="account input" placeholder="账号">
				</div>
				<div class="password_div login_row">
					<i class="iconfont">&#xe629;</i><input type="password" name="password" class="password input" placeholder="密码">
				</div>
				<ul>
					<input type="checkbox"><span>记住我</span>
				</ul>
				<div class="login_row denglu">
					<input type="submit" class="submit btn ok" value="登录">
				</div>
			</div>
		</div>
	</div>
	</form>

	<script src="/Express/js/jquery-2.1.1.js"></script>
	<script src="/Express/js/login_validate.js"></script>
</body>
</html>
