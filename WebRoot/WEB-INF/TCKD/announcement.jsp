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
	<title>同城管理--系统公告</title>
	<link rel="stylesheet" href="/Express/css/inc_style.css">
	<link rel="stylesheet" href="/Express/css/announcement.css">
	<link rel="stylesheet" href="/Express/css/jquery-labelauty.css">
</head>
<body>
		<jsp:include page="header_inc.jsp">
	<jsp:param name="username" value= '<%=session.getAttribute("username") %>'/>
	</jsp:include>
	<!-- 主干区 -->
	<div class="main clearfix">
	
	<jsp:include page="menu.jsp">
	<jsp:param name="clazz" value= "announment"/>
	</jsp:include>

		<div class="main_content" id="tabs">
			<h2 class="main_content_title">系统公告</h2>
			<ul class="title_option">
				<li><a href="javascript:;" class="select">历史公告</a></li>
			</ul>

			<div class="main_content_text">
				<div class="information_box">
					<table class="announcement_table" cellspacing = "0">
					<thead>
						<tr>
							<th>管理员</th>
							<th>公告内容</th>
							<th>推送日期</th>
							<th>推送对象</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach var="p" items="${notice}">
						<tr>
							<td>${p.name}</td>
							<td style="max-width:350px;padding-left:30px;padding-right:30px;">${p.content}</td>
							<td>${p.date}<br/></td>
							<td>${p.client}</td>
						</tr>
					</c:forEach>
					</tbody>
					</table>
					<c:if test="${PageNum==0}">
						<div style='width:100%;height:100px;'>
							<h1 style="text-align:center;font-size:20px;line-height:200px;color:#8C8C8C;">抱歉,暂无数据 ~<h1>
						</div>
					</c:if>
				</div>
				<c:if test="${PageNum>5}">
				<div class="next_page_box">
					<span class="last_page" name="${PageNum}">尾页</span>
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
						<span class="last_page" name="${PageNum}">尾页</span>
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

			<ul class="title_option">
				<li><a href="javascript:;" class="select">推送公告</a></li>
			</ul>

			<form class="form">
			<div class="main_content_text">
				<div class="information_box">
					<textarea name="msg" class="text" cols="115" rows="5" placeholder="在这里输入公告内容:"></textarea>
				</div>
				<ul class="clientcheck-ul">
					<li>
						<input class="clientcheck" type="radio" checked name="client" value="capp" data-labelauty="快递员APP">
					</li>
					<li>
						<input class="clientcheck" type="radio" name="client" value="uapp" data-labelauty="普通用户APP">
					</li>
				</ul>
				<input type="text" name="username" class="input_username" style="display:none">
			</div>
			</form>
			<ul class="sub_ul">
				<button type="button" class="sub ok">发布公告</button>
			</ul>

		</div>

	</div>

	<!-- 底部 -->
	<div class="footer">
		<span>©拓嵘科技 2016 </span>
	</div>

	
	<script src="/Express/js/jquery-2.1.1.js"></script>
	<script src="/Express/js/inc_action.js"></script>
	<script src="/Express/js/jquery-labelauty.js"></script>
	<script src="/Express/js/announcement.js"></script>
</body>
</html>