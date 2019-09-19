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
	<div class="search">
		<form class="form-inline" action="./admin/Admin_reLoginPwd"
			method="post">
			<div class="form-group">
				&nbsp;&nbsp;&nbsp;&nbsp;<label for="exampleInputName2">旧密码 </label> <input type="text"
					class="form-control" name="newUserPwd" id="newUserPwd"
					placeholder="请输入旧密码">
			</div>
			<br />
			<div class="form-group">
				&nbsp;&nbsp;&nbsp;&nbsp;<label for="exampleInputName2">新密码 </label> <input type="text"
					class="form-control" name="newUserPwd1" id="newUserPwd1"
					placeholder="请输入新密码">
			</div>
				<br />
			<div class="form-group">
				<label for="exampleInputName2"> 确认密码</label> <input type="text"
					class="form-control" name="newUserPwd1" id="newUserPwd1"
					placeholder="确认密码">
			</div>
			<div style="margin-left:31%">
				<button type="submit" class="btn btn-success">创建</button>
				&nbsp;&nbsp;
				<button type="reset" class="btn btn-danger">取消</button>
			</div>
		</form>
	</div>
</body>
</html>
