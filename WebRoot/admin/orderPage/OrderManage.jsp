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
				<label for="exampleInputName2">订单号</label> <input type="text"
					class="form-control"  name="orderId" id="orderId" placeholder="请输入订单号">
			</div>
			<button type="submit" class="btn btn-default">查询订单</button>
		</form>
	</div>
</body>
</html>
