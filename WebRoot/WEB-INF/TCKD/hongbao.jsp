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
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=0.5,maximum-scale=0.5,user-scalable=yes">
	<link rel="stylesheet" href="/Express/css/red_bag.css">
	<title>优惠券</title>
</head>
<body>
	<div class="title-img-box">
		<img src="/Express/images/coupon/ico_splash_5.png"  class="title-img">
	</div>
	<div class="content-box">
		<!-- 透明层盒子 -->
		<div class="list-box"></div>
		<div class="activity_rule">
			<h1>活动规则</h1>
			<ul class="rule-list">
				<li>
					<div class="circle">1</div>
					<span>使用优惠券时的下单手机号需为抢优惠券时使用的手机号</span>
				</li>
				<li>
					<div class="circle">2</div>
					<span>发放至手机号的优惠券需要在App用手机号注册才能使用</span>
				</li>
				<li>
					<div class="circle">3</div>
					<span>发放至快快送账户的优惠券登陆后即可使用</span>
				</li>
				<li>
					<div class="circle">4</div>
					<span>每张订单仅限使用一张优惠券，优惠券不找零</span>
				</li>
				<li>
					<div class="circle">5</div>
					<span>快快送保留法律范围内允许的对活动的解释权</span>
				</li>
			</ul>
		</div>
		<img src="/Express/images/coupon/icon_name.png"  class="foot-image">
	</div>
	<script src="/Express/js/jquery-2.1.1.js"></script>
	<script src="/Express/js/inc_action.js"></script>
	<script type="text/template" id="wchatMessageTpl">
			{@if flag == 1}
				{@if bindPhone == 0}
					<div class="phoneNum">
						<input class="phoneNumInput" type="text" placeholder="输入您的手机号">
						<button onclick="getRedBag()">领取优惠券</button>
					</div>
				{@/if}
			{@else}
				<h1 style="text-align:center;padding:20px 10px;font-size:28px;">授权失败，请在微信内重新打开并授权</h1>
			{@/if}	
	</script>
	<script type="text/template" id="redBagTpl">
		<div style="width:100%;">
			{@if haveBag == 1}
				<div class="redBag" style="">
					<div class="money">
						<h1>￥&{money}</h1>
					</div>
					<div class="useRule">
						<ul>
							<li>满&{minMoneyUse}元可用</li>
							<li>&{deadLine}</li>
						</ul>
					</div>
				</div>
				<span class="tishi">优惠券已放至账户&{phoneNum} 登录APP即可使用</span>
				<button class="btn"><a href="http://m.anzhi.com/info_2748061.html">立即使用</a></button>
			{@else}
				<h1 class="tooLate">来晚了,优惠券已经被抢光啦！</h1>
			{@/if}
		</div>
	</script>
	<script src="/Express/js/jquery-2.1.1.js"></script>
	<script src="/Express/js/Prompt_Message.js"></script>
	<script src="http://cdn.bootcss.com/juicer/0.6.14/juicer-min.js"></script>
	<script src="/Express/js/hongbao.js"></script>
	<script src="/Express/js/inc_action.js"></script>
</body>
</html>