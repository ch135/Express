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
	<meta charset="UTF-8">
	<title>同城管理--修改权限</title>
	<link rel="stylesheet" href="/Express/css/inc_style.css">
	<link rel="stylesheet" href="/Express/css/jquery-labelauty.css">
	<link rel="stylesheet" href="/Express/css/change_authority.css">
</head>
<body>
	<jsp:include page="header_inc.jsp">
	<jsp:param name="username" value= '<%=session.getAttribute("username") %>'/>
	</jsp:include>
	<!-- 主干区 -->
	<div class="main clearfix">
	
	<jsp:include page="menu.jsp">
	<jsp:param name="clazz" value= "change_authority"/>
	</jsp:include>
		<!-- 主内容 -->
		<div class="main_content" id="tabs">
			<h2 class="main_content_title">修改权限</h2>
			<ul class="title_option">
				<li><a href="#" class="select">管理员列表</a></li>
			</ul>
			
			<div class="main_content_text" id="fragment-3">
				<div class="unapproved_table_box ">
					<table class="unapproved_table tablesorter" cellspacing = "0">
					<thead>
						<tr class="Title">
							<th>账号</th>
							<th>权限范围</th>	
							<th></th>		
						</tr>
					</thead>
					<tbody>
					<c:forEach var="a" items="${admins}">
						<tr>
							<td class="adminName">${a.username}</td>
							<td class='grade'>${a.role}</td>
							<td><button class="btn ok change_btn">权限修改</button><button data-name=${a.username} class="btn error delet_btn">删除</button></td>
						</tr>
					</c:forEach>
					</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- 底部 -->
	<div class="footer">
		<span>©拓嵘科技 2016 </span>
	</div>
	<!-- 修改权限盒子 -->
	<script type="text/template" id="changeAut">
		<div class="changeAuthority-box">
			<center>
				<div class="changeAuthority-box-title">
					<img src="../img/guanli.png" alt="">
					<h3>&{username}</h3>
				</div>
				<div style="width:100%;">
				<form class='changeAutForm' method="POST" action='/Express/admin/Admin_changeQX'>
					<input name='username' value=&{username}  style="display:none" />
					<input type="checkbox" id='useOpt' name="userManage" &{userManage?'checked':''} style="display:none" />
					<div class="list-box">
						<input type="checkbox" class="useCB" name="checkUser" &{checkUser?'checked':''} onclick="useCheckBoxFun(this)" data-labelauty="审核申请">
						<input type="checkbox" class="useCB" name="userMsg" &{userMsg?'checked':''} onclick="useCheckBoxFun(this)" data-labelauty="查看用户信息">
						<input type="checkbox" class="useCB" name="userAdvice" &{userAdvice?'checked':''} onclick="useCheckBoxFun(this)" data-labelauty="查看用户建议">
						<input type="checkbox" class="useCB" name="userATM" &{userATM?'checked':''} onclick="useCheckBoxFun(this)" data-labelauty="查看提现记录">
					</div>
					<div class="list-box">
						<input type="checkbox" name="orderManage" &{orderManage?'checked':''}  data-labelauty="订单管理">
						<input type="checkbox" name="adminManage" &{adminManage?'checked':''}  data-labelauty="管理员管理">
						<input type="checkbox" name="msgManage" &{msgManage?'checked':''}  data-labelauty="消息管理">
						<input type="checkbox" name="activitySet" &{activitySet?'checked':''}  data-labelauty="活动设置">
					</div>	
				</div>
			</center>
		</div>
		</form>	
		<div style="width:100%;text-align:center">
			<button class='ok btn yes_btn' onclick="submitForm()">修改</button>
			<button class='error btn cancle_btn' style='margin-left:5px' onclick="closed()">取消</button>
		</div>
	</script>		
	<script src="/Express/js/jquery-2.1.1.js"></script>
	<script src="http://cdn.bootcss.com/juicer/0.6.14/juicer-min.js"></script>
	<script src="/Express/js/inc_action.js"></script>
	<script src="/Express/js/change_authority.js"></script>
	<script src="/Express/js/jquery-labelauty.js"></script>
</body>
</html>
