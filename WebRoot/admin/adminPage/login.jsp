<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<link href="admin/css/bootstrap.min.css" rel="stylesheet">
<link href="admin/css/style.css" rel="stylesheet">
<script src="admin/js/jquery-2.1.1.js"></script>
<script src="admin/js/bootstrap.min.js"></script>
</head>
<body>
	<div id="formbackground" style="position:absolute; z-index:-1;">
		<img src="admin/images/city001.jpg" height="100%" width="100%" />
		</div>
		<div class="loginheader"><strong style="margin-left: 120px;"> TC同城快递</strong></div>
		<br><br>
		<div class="form1">
		<form action="./admin/Admin_login" method="post">
			<input type="text" class="input1" name="username" placeholder="用户名">
			<br>
			<input type="password" class="input1" name="password" placeholder="密码">
			<button type="submit" class="btn btn-primary" style="margin-top:40px; width: 270px;height: 50px">登 录</button>
		</form>
		</div>
</body>
</html>
