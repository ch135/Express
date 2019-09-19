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
	<div class="notice">
		<form action="">
			<div class="checkbox">
  				<label>
    				<input type="checkbox" value="">
   					 通知普通用户App有新的更新
  				</label>
			</div>
			<div class="checkbox">
  				<label>
    				<input type="checkbox" value="">
   					 通知快递用户App有新的更新
  				</label>
			</div>
		</form>
	</div>
</body>
</html>
