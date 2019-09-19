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
<form action="/Express/admin/Upload_updateApp" method="post" enctype="multipart/form-data">
<input type="file" name="upload"/>
appType<input type="text" name = "appType"/>
operator<input type="text" name = "operator"/>
appVersion<input type="text" name = "appVersion"/>
<input type="submit"/ value="1111">
</form>
</body>
</html>
