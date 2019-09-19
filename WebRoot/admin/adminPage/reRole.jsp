<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<title>My JSP 'findOrder.jsp' starting page</title>
<link href="admin/css/bootstrap.min.css" rel="stylesheet">
<link href="admin/css/style.css" rel="stylesheet">
<script src="admin/js/jquery-2.1.1.js"></script>
<script src="admin/js/bootstrap.min.js"></script>
</head>

<body>
	<div class="search">
		<form class="form-inline" action="./admin/OrderManage_findOrder.action" method="post">
			<div class="form-group">
				<label for="exampleInputName2">查找管理员</label> <input type="text"
					class="form-control"  name="orderId" id="orderId" placeholder="请输入管理员名">
			</div>
			<button type="submit" class="btn btn-default">查询</button>
		</form>
		<form action=""  style="padding-right:300px">
				<table class="table table-bordered"  >
						<tr>
							<td>管理员名</td>
							<td>管理员权限</td>
							<td>操作</td>
						</tr>
						<c:forEach var="p"  items="${admin}">
							<c:if test="${p.username!='admin' }">
						 		<tr>
									<td name="username">${p.username}</td>
									<td>
										<select name="role">
											<option value="2">二级管理员</option>
											<option value="3">三级管理员</option>
										</select>
									</td>
									<td><button class="btn btn-primary btn-sm"  type="submit" >修改权限</button></td>
								</tr>
							</c:if>
						</c:forEach>
				</table>
		</form>
	</div>
</body>
</html>
