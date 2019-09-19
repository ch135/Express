<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@page language="java" import=" java.text.SimpleDateFormat" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<link href="admin/css/bootstrap.min.css" rel="stylesheet">
<link href="admin/css/style.css" rel="stylesheet">
<script src="admin/js/jquery-2.1.1.js"></script>
<script src="admin/js/bootstrap.min.js"></script>
<%SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); %>
</head>
<body>
	<div>
		<table class="table table-striped">
			<tr class="info">
				<td>用户名</td>
				<td>手机号</td>
				<td>用户建议</td>
				<td>日期</td>
				<td>操作</td>
			</tr>

			<c:forEach var="p" items="${advice}">
				<tr>
					<td>${p.username}</td>
					<td>${p.mobile}</td>
					<td>${p.advice}</td>
					<td>${p.date}</td>
					<td><a href="">删除</a></td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>
