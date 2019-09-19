<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta charset="UTF-8">
<title>同城管理--用户提现</title>
<link rel="stylesheet" href="/Express/css/inc_style.css">
<link rel="stylesheet" href="/Express/css/withdraw_cash.css">
</head>
 <style type="text/css">
            *{ margin: 0; padding: 0;}
            a{ text-decoration: none; margin: 0 5px; color: #333;}
            #ul1{ width: 600px; height: 240px; list-style: none; margin: 50px auto; overflow: hidden; position: relative;}
            #ul1 li{ width: 100px; height: 100px; background: olive; float: left; margin: 10px;}
            #div1{ width: 380px; margin: 40px auto;}
        </style>
<body>
	<jsp:include page="header_inc.jsp">
		<jsp:param name="username" value='<%=session.getAttribute("username")%>' />
	</jsp:include>
	<!-- 主干区 -->
	<div class="main clearfix">

		<jsp:include page="menu.jsp">
			<jsp:param name="clazz" value="withdraw_cash" />
		</jsp:include>
		<!-- 主内容 -->
		<div class="main_content" id="tabs">
			<h2 class="main_content_title">用户提现</h2>
			<ul class="title_option">
				<li><a href="#fragment-3" class="select tixian">用户提现记录</a></li>
				<li><a href="#fragment-2" class="daying">打印记录</a></li>
				<a href="/Express/admin/Admin_printExcel" class="ok withdraw_cash_btn">打印报表</a>
			</ul>

			<div class="user_message_box" id="fragment-3">
				<div class="user_message_table">
					<table class="user_table tablesorter" cellspacing="0">
						<thead>
							<tr class="Title">
								<th>用户姓名</th>
								<th>手机号</th>
								<th>提现金额</th>
								<th>提现方式</th>
								<th>提现账号</th>
								<th>自动提现</th>
								<th>提现时间</th>
								<th>处理</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="l" items="${list}">
								<tr>
									<td>${l.name }</td>
									<td>${l.mobile }</td>
									<td>${l.balance }</td>
									<td>${l.inCashType }</td>
									<td>${l.account }</td>
									<td>
										<c:if test="${l.results==false&&l.inCashType=='WeiXin'}">
											<a href="javascript:void(0)" class="zidong" data="${l.id }" >提现</a>
										</c:if>
										<c:if test="${l.results!=false}">
										</c:if>
									</td>
									<td><fmt:formatDate value="${l.date}" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
									<td>
										<c:if test="${l.results==false}">
											<a href="#" class="weichuli" data="${l.id }">未处理</a>
										</c:if>
										<c:if test="${l.results!=false}">
											已处理
										</c:if>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<c:if test="${PageNum==0}">
					<div style='width:100%;height:100px;'>
						<h1 style="text-align:center;font-size:20px;line-height:200px;color:#8C8C8C;">
							暂无数据
							<h1>
					</div>
				</c:if>

				<c:if test="${PageNum>5}">
					<div class="next_page_box">
						<span class="last_page go_page" name="${PageNum}">尾页</span> <span class="next_page go_page"><i class="iconfont">&#xe66c;</i></span> <span class="go_page">5</span> <span class="go_page">4</span> <span class="go_page">3</span> <span class="go_page">2</span> <span class="go_page here_page">1</span>
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



			<div class="user_message_box" id="fragment-2" style="">
				<div class="user_message_table">
					<table class="user_table tablesorter" cellspacing="0">
						<thead>
							<tr class="Title">
								<th>打印时间</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>

				<div id="div1">
				
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
	<script src="/Express/js/tool_inc.js"></script>
	<script src="/Express/js/withdraw_cash.js"></script>
</body>
</html>
