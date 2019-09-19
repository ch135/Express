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
		<form action="./admin/Admin_pushMsg" methdo="post">
			<textarea class="form-control" rows="3" name="msg" style="height: 200px"
				placeholder="请输入公告内容..."></textarea>
				<div style="margin-top: 10px;margin-bottom: 15px">
			<label class="checkbox-inline"> 
			<input type="checkbox" id="uApp" value="option1" name="uApp">用户APP
			</label> 
			<label class="checkbox-inline"> 
			<input type="checkbox" id="cApp" value="option2" name="cApp">快递APP
			</label>
			</div>
			<button type="submit" class="btn btn-success" style="margin-left: 220px">确认推送</button>
			<button type="reset" class="btn btn-danger" style="margin-left: 10px">取消推送</button>
		</form>
	</div>
</body>
</html>
