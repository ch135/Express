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
<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
<META HTTP-EQUIV="Expires" CONTENT="0"> 
<base href="<%=basePath%>">
<link href="/Express/css/inc_style.css" rel="stylesheet">
<link href="/Express/css/activity.css" rel="stylesheet">
<link href="/Express/css/timePicker.css" rel="stylesheet">
</head>
<body>
	<!-- 头部 -->
	<jsp:include page="header_inc.jsp">
	<jsp:param name="username" value= '<%=session.getAttribute("username") %>'/>
	</jsp:include> 
	<!-- 主干区 -->
	<div class="main clearfix">
	
	<jsp:include page="menu.jsp">
	<jsp:param name="clazz" value= "activity"/>
	</jsp:include>
	
		<div class="main_content" id="tabs">
			<h2 class="main_content_title">活动设置</h2>
			<ul class="title_option">
				<li><a href="javascript:;" class="select">平台收费设置</a></li>
			</ul>
			<div class="main_content_text">
			<c:forEach var="p" items="${coupon}">
			<c:if test="${p.type=='toll'}">
				<strong><span class="app_title">当前平台收费标准: ${p.value*100} %</span></strong>
				<div class="box">
					<form action="/Express/admin/Admin_changeCouponValue" method="post" class="toll-form">
						<input class="coupon-input" type="text" name="value"> %
						<input  class="coupon-input" type="text" name="couponType" value='toll' style='display:none'>
					</form>
				<button class="ok coupon-btn toll-btn">确认修改</button>
				</div>
			</div>
			<ul class="title_option">
				<li><a href="javascript:;" class="select">优惠券设置</a></li>
			</ul>
			</c:if>
			<c:if test="${p.type=='registerCoupon'}">
				<strong><span class="app_title">当前注册优惠券额度: ${p.value} 元</span></strong>
				<span class="app_title">额度修改：</span>
				<div class="box">
					<form action="/Express/admin/Admin_changeCouponValue" method="post" class="registerCoupon-form">
						<input class="coupon-input" type="text" name="value"> 元
						<input class="coupon-input" type="text" name="couponType" value="registerCoupon" style='display:none'>					
					</form>	
					<button class="ok coupon-btn registerCoupon-btn">确认修改</button>			
				</div>
			</c:if>
			<c:if test="${p.type=='min'}">
				<strong><span class="app_title">当前下单奖励优惠券额度下限:${p.value}元</span></strong>
			</c:if>
			<c:if test="${p.type=='max'}">
				<strong><span class="app_title">当前下单奖励优惠券额度上限:${p.value}元</span></strong>
				<span class="app_title">额度修改：</span>
				<div class="box">
					<form action="/Express/admin/Admin_changeCouponValue" method="post" class="awardCoupon-form">
						额度下限: <input  class="coupon-input" type="text" name="min"> 元
							  <input  class="coupon-input" type="text" name="couponType" value='awardCoupon' style='display:none'>
						额度上限: <input  class="coupon-input" type="text" name="max"> 元
					</form>
					<button class="ok coupon-btn awardCoupon-btn">确认修改</button>
				</div>
			</c:if>
			</c:forEach>
			<ul class="title_option">
				<li><a href="javascript:;" class="select">起步价设置</a></li>
			</ul>
			<div class="main_content_text">
				<select id="city-select">
						<option value= >选择城市</option>
						<c:forEach var="l" items="${cityList}">
							<option value=>${l}</option>
						</c:forEach>						
					</select>
				<select id="area-select">
						<option value= >选择地区</option>					
					</select>
				<h3 style="padding:20px 50px">
					<span class="city-value" style="font-size:16px">当前起步价为: </span>
					<table>
						<thead><th>起始时间</th><th>结束时间</th><th>价格</th></thead>
						<tbody>
							<tr><td><input type="text" class="time" value="00:00:00"/></td><td ><input type="text" class="time" value="12:00:30"/></td><td><input type="text" class="price" value="5.0元"/></td></tr>
							<tr><td><input type="text" class="time" value="12:00:30"/></td><td ><input type="text" class="time" value="18:00:30"/></td><td><input type="text" class="price" value="5.0元"/></td></tr>
							<tr><td><input type="text" class="time" value="18:00:30"/></td><td ><input type="text" class="time" value="00:00:00"/></td><td><input type="text" class="price" value="5.0元"/></td></tr>
						</tbody>
					</table>
				</h3>
				<h4 style="padding-left:50px;padding-bottom:15px;font-size:16px">
					<button class="ok changeCityValue-btn" style="display:inline-block;margin-left:10px" >确认修改</button>		
				</h4>
			</div>
			

			<ul class="title_option">
				<li><a href="javascript:;" class="select">红包设置</a></li>
			</ul>
			<div class="main_content_text">
				<div style="width:100%;min-height:220px;">
					<div class="add_pic_div" style= title="点击上传图片">
						<h4 style="margin-top:60px;text-align:center">点击上传红包链接图片</h4>
					</div>
				</div>
				<form action= enctype="multipart/form-data" class="upload_hongbao_form">
					<input type="file" name='upload' class="add_pic" style="display:none">
					<h3 class="hb-title">标题:<input name="title" class="hongbao-input hbTitle" type="text" placeholder="输入红包链接标题"/></h3>
					<h3  class="hb-title">内容:<input name="content" class="hongbao-input  hbContext" type="text" placeholder="输入红包链接宣传内容"/></h3>
				</form>	
				<div class="hidden_div">
					<span class="prompt">提示:上传的图片请尽可能按照1:1的比例</span>
					<button class='ok add_btn hongbao_upload'>确认上传</button>				
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
	<script src="/Express/js/activity.js"></script>
	<script src="/Express/js/jquery-timepicker.js"></script>
</body>
</html>
