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
<script>
	$(document).ready(function() {
		dropdownOpen();//调用
	});
	/**
	 * 鼠标划过就展开子菜单，免得需要点击才能展开
	 */
	function dropdownOpen() {

		var $dropdownLi = $('li.dropdown');

		$dropdownLi.mouseover(function() {
			$(this).addClass('open');
		}).mouseout(function() {
			$(this).removeClass('open');
		});
	}
</script>
</head>
<body>
	<div id="formbackground" style="position:absolute; z-index:-1;">
		<img src="admin/images/city001.jpg" height="100%" width="100%" />
	</div>
	<div class="header">
		<ul class="nav nav-tabs" style="float: left">
			<li><a href="admin/main.jsp">主页</a></li>
			<li><a href="admin/orderPage/OrderManage.jsp" target="main">订单查询</a></li>
			<li><a href="./admin/userPage/UserManage_lookAdvice?first=0"
				method="post" target="main">用户建议</a></li>
			<li class="dropdown"><a id="drop1" href="#"
				class="dropdown-toggle" data-toggle="dropdown" role="button"
				aria-haspopup="true" aria-expanded="false"> 用户管理 <span
					class="caret"></span>
			</a>
				<ul class="dropdown-menu" aria-labelledby="drop1">
					<li><a
						href="./admin/UserManage_getAllUserIdentity.action?first=0"
						target="main">审核信息</a></li>
					<li><a href="admin/userPage/UserManage.jsp" target="main">用户管理</a></li>
					<li><a href="admin/userPage/UserInfo.jsp" target="main">用户信息</a></li>
				</ul></li>
			<li class="dropdown"><a id="drop1" href="#"
				class="dropdown-toggle" data-toggle="dropdown" role="button"
				aria-haspopup="true" aria-expanded="false"> 推送消息 <span
					class="caret"></span>
			</a>
				<ul class="dropdown-menu" aria-labelledby="drop1">
					<li><a href="admin/adminPage/systemMsg.jsp" target="main">系统公告</a></li>
					<li><a href="admin/adminPage/updateMsg.jsp" target="main">应用更新</a></li>
				</ul></li>
							<li class="dropdown"><a id="drop1" href="#"
				class="dropdown-toggle" data-toggle="dropdown" role="button"
				aria-haspopup="true" aria-expanded="false"> 管理员管理 <span
					class="caret"></span>
			</a>
				<ul class="dropdown-menu" aria-labelledby="drop1">
					<li><a href="admin/adminPage/creatAdmin.jsp" target="main">创建管理员</a></li>
					<li><a href="admin/adminPage/creatCityAdmin.jsp" target="main">创建地区管理员</a></li>
					<li><a href="./admin/Admin_getAllAdmin?first=0" target="main">修改权限</a></li>
				</ul></li>
		</ul>
		<ul class="nav nav-tabs" style="float: right">
			<li><a>欢迎你</a></li>
			<li class="dropdown"><a class="dropdown-toggle"
				data-toggle="dropdown" role="button" aria-haspopup="true"
				aria-expanded="false"> <%=session.getAttribute("username")%><span
					class="caret"></span>
			</a>
				<ul class="dropdown-menu" aria-labelledby="drop1">
					<li><a href="admin/adminPage/reLoginPwd.jsp" target="main">修改密码</a></li>
				</ul></li>
			</li>
			<li><a href="./admin/Admin_loginOut">退出</a></li>
		</ul>
	</div>
	<iframe class="main" name="main" style="border: 0px;opacity:0.9"> </iframe>
</body>
</html>
