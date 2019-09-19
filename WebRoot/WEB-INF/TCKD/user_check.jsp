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
	<title>同城后台-申请审核</title>
	<link rel="shortcut icon" type="image/x-icon" href="/Express/img/Search.png" />
	<link rel="stylesheet" href="/Express/css/inc_style.css">
	<link rel="stylesheet" href="/Express/css/message_auditing.css">
</head>
<body>
	<jsp:include page="header_inc.jsp">
	<jsp:param name="username" value= '<%=session.getAttribute("username") %>'/>
	</jsp:include>
	<!-- 主干区 -->
	<div class="main clearfix">
	
	<jsp:include page="menu.jsp">
	<jsp:param name="clazz" value= "shenqing"/>
	</jsp:include>

		<!-- 主内容 -->
		<div class="main_content" id="tabs">
			<h2 class="main_content_title">申请审核</h2>
			<ul class="title_option">
				<li><a href="#fragment-3" class="select">未审批</a></li>
				<li><a href="#fragment-2">已审批</a></li>
				<li><a href="#fragment-1" class="show">审批详情</a></li>
			</ul>
			
			<!-- 未审批页 -->
			<div class="main_content_text" id="fragment-3">
				<div class="unapproved_table_box ">
					<table class="unapproved_table tablesorter" cellspacing = "0">
					<thead>
						<tr class="Title">
							<th>照片</th>
							<th>姓名</th>
							<th class="canSort">身份证号<img src="/Express/img/sort.png" alt="" name = "0"></th>
							<th class="canSort">手机号<img src="/Express/img/sort.png" alt="" name="0"></th>	
							<th></th>		
						</tr>
					</thead>
					<tbody>
					<c:forEach var="p" items="${needCheck}">
						<tr id="${p.id}">
							<td><img src="${p.icon}" alt=""></td>
							<td class="name">${p.name}</td>
							<td class="identity">${p.identity}</td>
							<td class="mobile">${p.mobile}</td>
							<td><button class="btn ok detail">详细资料</button></td>
						</tr>
					</c:forEach>
					</tbody>
					</table>
					<c:if test="${needCheckPageNum==0}">
						<div style='width:100%;height:100px;'>
							<h1 style="text-align:center;font-size:20px;line-height:200px;color:#8C8C8C;">暂无数据<h1>
						</div>
					</c:if>
				<c:if test="${needCheckPageNum>5}">
				<div class="next_page_box fragment-3-goPage">
					<span class="last_page go_page" name="${needCheckPageNum}">尾页</span>
					<span class="next_page go_page"><i class="iconfont">&#xe66c;</i></span>
					<span class="go_page">5</span>
					<span class="go_page">4</span>
					<span class="go_page">3</span>
					<span class="go_page">2</span>
					<span class="go_page here_page">1</span>
					<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
				</div>
				</c:if>  
				<c:if test="${needCheckPageNum<=5}">
				<div class="next_page_box fragment-3-goPage">
					<c:if test="${PageNum>1}">
						<span class="last_page go_page" name="${PageNum}">尾页</span>
						<span class="next_page go_page"><i class="iconfont">&#xe66c;</i></span>
					</c:if>
					<c:choose>
       					<c:when test="${needCheckPageNum==1}">
             				<span class="go_page here_page">1</span>
             				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
       					<c:when test="${needCheckPageNum==2}">
              				<span class="go_page">2</span>
              				<span class="go_page here_page">1</span>
              				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
       					<c:when test="${needCheckPageNum==3}">
              				<span class="go_page">3</span>
              				<span class="go_page">2</span>
              				<span class="go_page here_page">1</span>
              				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
       					<c:when test="${needCheckPageNum==4}">
              				<span class="go_page">4</span>
              				<span class="go_page">3</span>
              				<span class="go_page">2</span>
              				<span class="go_page here_page">1</span>
              				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
       					<c:when test="${needCheckPageNum==5}">
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
			
			<!-- 已审批页 -->
			<div class="main_content_text" id="fragment-2">
				<div class="a  pproved_table_box">
					<table class="approved_table" cellspacing = "0">
					<thead>
						<tr class="Title">
							<th>照片</th>
							<th>姓名</th>
							<th class="canSort">性别<img src="/Express/img/sort.png" alt="" name = "0"></th>
							<th class="canSort">手机号<img src="/Express/img/sort.png" alt="" name = "0"></th>	
							<th class="canSort">审批结果<img src="/Express/img/sort.png" alt="" name = "0"></th>
							<th></th>		
						</tr>
					</thead>
					<tbody>
					
						<c:forEach var="p" items="${finshCheck}">
						<tr>
							<td class="icon"><img src="${p.icon}" alt=""></td>
							<td name="${p.identity}" class="name">${p.name}</td>
							<td class="sex">${p.sex}</td>
							<td class="mobile">${p.mobile }</td>
							<td class="shenpi">
							<c:if test="${p.results=='yes'}">
								通过
							</c:if>
							<c:if test="${p.results=='no'}">
								未通过
							</c:if>
							</td>
							<td class="caozuo">
								<button class="btn ok">详细资料</button>
							</td>
						</tr>
						</c:forEach>
					</tbody>
					</table>	
					<c:if test="${finshCheckPageNum==0}">
						<div style='width:100%;height:100px;'>
							<h1 style="text-align:center;font-size:20px;line-height:200px;color:#8C8C8C;">暂无数据<h1>
						</div>
					</c:if>			
				</div>
				<c:if test="${finshCheckPageNum>5}">
				<div class="next_page_box fragment-2-goPage">
					<span class="last_page go_page" name="${finshCheckPageNum}">尾页</span>
					<span class="next_page go_page"><i class="iconfont">&#xe66c;</i></span>
					<span class="go_page">5</span>
					<span class="go_page">4</span>
					<span class="go_page">3</span>
					<span class="go_page">2</span>
					<span class="go_page here_page">1</span>
					<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
				</div>
				</c:if>
				<c:if test="${finshCheckPageNum<=5}">
				<div class="next_page_box fragment-2-goPage">
					<c:if test="${finshCheckPageNum>1}">
						<span class="last_page go_page" name="${finshCheckPageNum}">尾页</span>
						<span class="next_page go_page"><i class="iconfont">&#xe66c;</i></span>
					</c:if>
					<c:choose>
       					<c:when test="${finshCheckPageNum==1}">
             				<span class="go_page here_page">1</span>
             				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
       					<c:when test="${finshCheckPageNum==2}">
              				<span class="go_page">2</span>
              				<span class="go_page here_page">1</span>
              				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
       					<c:when test="${finshCheckPageNum==3}">
              				<span class="go_page">3</span>
              				<span class="go_page">2</span>
              				<span class="go_page here_page">1</span>
              				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
       					<c:when test="${finshCheckPageNum==4}">
              				<span class="go_page">4</span>
              				<span class="go_page">3</span>
              				<span class="go_page">2</span>
              				<span class="go_page here_page">1</span>
              				<span class="prev_page go_page hidden"><i class="iconfont">&#xe66b;</i></span>
       					</c:when>
       					<c:when test="${finshCheckPageNum==5}">
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
			
			<div class="main_content_text" id="fragment-1">
				<div class="information_box">
					<div style='width:100%;height:200px;' >
						<h1 style="text-align:center;font-size:20px;line-height:200px;color:#8C8C8C;">您尚未选择订单<h1>
					</div>
				</div>
			</div>
			</div>
		</div>



	<!-- 底部 -->
	<div class="footer">
		<span>©拓嵘科技 2016 </span>
	</div>

 <script src="/Express/js/jquery-2.1.1.js"></script>
 <script src="/Express/js/jquery-ui.min.js"></script>
 <script src="/Express/js/jquery.tablesorter.js"></script>
 <script src="/Express/js/inc_action.js"></script>
 <script src="/Express/js/message_auditing.js"></script>
</body>
</html>