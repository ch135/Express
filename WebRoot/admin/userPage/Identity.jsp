<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
	<div>
		<table class="table table-striped">
			<tr class="info">
				<td>姓名</td>
				<td>手机号</td>
				<td>身份证号</td>
				<td>图一</td>
				<td>图二</td>
				<td>图三</td>
				<td>操作</td>
			</tr>

			<c:forEach var="p" items="${user}">
				<tr>
					<td>${p.name}</td>
					<td>${p.mobile}</td>
					<td>${p.identity}</td>
					<td>${p.path1	}</td>
					<td>${p.path2	}</td>
					<td>${p.path3	}</td>
					<td><a href="">通过</a></td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>
