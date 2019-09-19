<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>同城管理--创建管理员</title>
	<link rel="stylesheet" href="/Express/css/inc_style.css">
	<link rel="stylesheet" href="/Express/css/add_admin.css">
	<link rel="stylesheet" href="/Express/css/jquery-labelauty.css">
</head>
<body>
	<jsp:include page="header_inc.jsp">
	<jsp:param name="username" value= '<%=session.getAttribute("username") %>'/>
	</jsp:include>
	<!-- 主干区 -->
	<div class="main clearfix">
	
	<jsp:include page="menu.jsp">
	<jsp:param name="clazz" value= "add_city_admin"/>
	</jsp:include>

		<div class="main_content" id="tabs">
			<h2 class="main_content_title">创建地区管理员</h2>
			<ul class="title_option">
				<li><a href="javascript:;" class="select">创建地区管理员</a></li>
			</ul>
			<div class="main_content_text">
				<div class="register_div">
				<form action="" id="addAdminForm" onSubmit="return formSubmit();">
					<ul class="baseMesUl">
						<li>
							<label class='li_title'>管理员昵称:</label>  
							<input class='addAdminFormInput' name='newUserName' type="text" placeholder="长度不要大于6个字" required> 
						</li>
						<li>
							<label class='li_title'>密码:</label>  
							<input class="addAdminFormInput pwdinput" name="newUserPwd" type="password"  placeholder="输入密码" required/> 
						</li>
						<li>
							<label class='li_title'>确认密码:</label>  
							<input class="addAdminFormInput pwdinputAgain" type="password" placeholder="确认你的密码" required/> 
						</li>
						<li>
							<label class='li_title'>地区:</label>  
				 			<select id="city"  name="city"   style="line-height: 38px;height: 38px">
								<option value="" >选择城市</option>
								<c:forEach var="l" items="${cityList}">
									<option value="${l}">${l}</option>
								</c:forEach>						
							</select>	
						</li>
						<li>
							<label class='li_title'>管理员权限:</label>  
							<input type="checkbox" id='useOpt' name="userManage" style="display:none"/>
							<div class="check-authority">
								<ul class="checkul">
									<input type="checkbox" class="useCB checkinput" name="checkUser" onclick="useCheckBoxFun(this)" data-labelauty="审核申请">
									<input type="checkbox" class="useCB checkinput" name="userMsg" onclick="useCheckBoxFun(this)" data-labelauty="查看用户信息">
									<!-- <input type="checkbox" class="useCB checkinput" name="userATM" onclick="useCheckBoxFun(this)" data-labelauty="查看提现记录"> -->
								</ul>
								<ul class="checkul" style="position: absolute;">
									<input class="checkinput" type="checkbox" name="orderManage"  data-labelauty="订单管理">
									<!-- <input class="checkinput" type="checkbox" name="adminManage" data-labelauty="管理员管理">
									<input class="checkinput" type="checkbox" name="msgManage"  data-labelauty="消息管理">
									<input class="checkinput" type="checkbox" name="activitySet" data-labelauty="活动设置">		 -->					
									
								</ul>
							</div>
						</li>	
					</ul>
					</form>
					<button class='ok add_btn'>创建管理员</button>
				</div>			
			</div>
		</div>
	</div>

	<!-- 底部 -->
	<div class="footer">
		<span>©拓嵘科技 2016 </span>
	</div>
	<script src="/Express/js/jquery-2.1.1.js"></script>
	<script src="/Express/js/inc_action.js"></script>
	<script src="/Express/js/add_city_admin.js"></script>
	<script src="/Express/js/jquery-labelauty.js"></script>
</body>
</html>