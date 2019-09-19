<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
	<title>同城管理--交易明细</title>
	<link rel="stylesheet" href="/Express/css/inc_style.css">
	<link rel="stylesheet" href="/Express/css/transactionDetails.css">
</head>
<body>
	<jsp:include page="header_inc.jsp">
	<jsp:param name="username" value= '<%=session.getAttribute("username") %>'/>
	</jsp:include>
	<!-- 主干区 -->
	<div class="main clearfix">
	
	<jsp:include page="menu.jsp">
	<jsp:param name="clazz" value= "transactionDetails"/>
	</jsp:include>
		<!-- 主内容 -->
		<div class="main_content" id="tabs">
			<h2 class="main_content_title">交易明细</h2>
			<ul class="title_option">
				<li><a href="#fragment-2" class="select">平台交易明细</a></li>
			</ul>

			<div class="user_message_box" id="fragment-2">
				<div class="user_message_table">
					<h4 class="select-box">
						<span>选择年份:</span>
						<select id="year-select">
							<option value="" >${years}</option>
						</select>
						年	
						<span style="margin-left:20px">选择月份:</span>	
						<select id="month-select">
							<option value="" >${months}</option>
						</select>
						月
						<span style="margin-left:20px">选择日期:</span>	
						<select id="day-select">
							<option value="" >${days }</option>
						</select>
						日
						<button class="btn" id="TJbtn">按条件查询</button>
					</h4>	
					<h2 class="TJ">统计: <span>支出: ${payValue }元</span><span>收入: ${value}元</span><span>红包支出金额: ${couponValue }元</span></h2>				
					<table class="user_table tablesorter" cellspacing = "0">
					<thead>
						<tr class="Title">
							<th>用户订单号</th>
							<th>交易人</th>
							<th>金额</th>
							<th>交易日期</th>	
							<th>交易记录</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach var="l" items="${list}">
						<tr>
							<td style="max-width:180px">${l.orderId}</td>
							<td style="min-width:100px">${l.name }</td>
							<td>${l.money }</td>
							<td>${l.date }</td>
							<td>${l.record }</td>
						</tr>
					</c:forEach>
					</tbody>
					</table>
				</div>
					<c:if test="${PageNum==0}">
						<div style='width:100%;height:100px;'>
							<h1 style="text-align:center;font-size:20px;line-height:200px;color:#8C8C8C;">暂无数据 <h1>
						</div>
					</c:if>
				</div>
				<c:if test="${PageNum>5}">
				<div class="next_page_box">
					<span class="last_page go_page" name="${PageNum}">尾页</span>
					<span class="next_page go_page"><i class="iconfont">&#xe66c;</i></span>
					<span class="go_page">5</span>
					<span class="go_page">4</span>
					<span class="go_page">3</span>
					<span class="go_page">2</span>
					<span class="go_page here_page">1</span>
					<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
				</div>
				</c:if>
				<c:if test="${PageNum<=5}">
				
				<div class="next_page_box">
					<c:if test="${PageNum>1}">
						<span class="last_page go_page" name="${PageNum}">尾页</span>
						<span class="next_page go_page"><i class="iconfont">&#xe66c;</i></span>
					</c:if>
					<c:choose>
       					<c:when test="${PageNum==1}">
             				<span class="go_page here_page">1</span>
             				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
       					<c:when test="${PageNum==2}">
              				<span class="go_page">2</span>
              				<span class="go_page here_page">1</span>
              				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
       					<c:when test="${PageNum==3}">
              				<span class="go_page">3</span>
              				<span class="go_page">2</span>
              				<span class="go_page here_page">1</span>
              				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
       					<c:when test="${PageNum==4}">
              				<span class="go_page">4</span>
              				<span class="go_page">3</span>
              				<span class="go_page">2</span>
              				<span class="go_page here_page">1</span>
              				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
       					<c:when test="${PageNum==5}">
              				<span class="go_page">5</span>
              				<span class="go_page">4</span>
              				<span class="go_page">3</span>
              				<span class="go_page">2</span>
              				<span class="go_page here_page">1</span>
              				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
					</c:choose>			
				</div>
				</c:if>		
			</div>
		</div>
	</div>

	<!-- 底部 -->
	<div class="footer">
		<span>©拓嵘科技 2016 </span>
	</div>
	
	<script src="/Express/js/jquery-2.1.1.js"></script>
	<script src="/Express/js/inc_action.js"></script>
	<script src="/Express/js/tool_inc.js"></script>
	<script src="/Express/js/transactionDetails.js"></script>
	<script type="text/javascript">
	$(function(){
		//点击分页
		$('.go_page').click(function(event){
			var first = ChangePage.getFirst($(this));
			$.getJSON('', {first:first,times:"noFirst"}, function(data){
				$('.user_table tbody').empty();
				for(var i = 0;i<data.length;i++){
						var date = new Date(data[i].date);
						date = date.Format('yyyy-MM-dd hh:mm:ss');
						var html = 
						'<tr><td>' + data[i].name +'</td><td>' + data[i].mobile + '</td><td>' + data[i].balance + '</td><td>' + data[i].inCashType +'</td>'; 
						$('.user_table tbody').append(html); 
				}
				ChangePage.start();
			});		
		});	
	});
	</script>
</body>
</html>