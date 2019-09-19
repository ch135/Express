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
<link href="/Express/css/app_upload.css" rel="stylesheet">

</head>
<body>
	<!-- 头部 -->
	<jsp:include page="header_inc.jsp">
	<jsp:param name="username" value= '<%=session.getAttribute("username") %>'/>
	</jsp:include> 
	<!-- 主干区 -->
	<div class="main clearfix">
	
	<jsp:include page="menu.jsp">
	<jsp:param name="clazz" value= "app_upload"/>
	</jsp:include>
	
		<div class="main_content" id="tabs">
			<h2 class="main_content_title">应用更新</h2>
			<ul class="title_option">
				<li><a href="javascript:;" class="select">版本信息</a></li>
			</ul>
			<div class="main_content_text">
				<span class="app_title">普通用户APP当前版本:${uappv }</span>
				<span class="app_title">快递员APP当前版本:${cappv }</span>
			</div>
			<ul class="title_option">
				<li><a href="javascript:;" class="select">APP更新包推送</a></li>
			</ul>
			<div class="main_content_text">
			<form method="post"  class="upload_apk_form"  enctype="multipart/form-data">
				<span class="app_title">选择APP更新包文件:</span>
				<div class="add_file_div" style="width:100px;height:100px;display:block;" title="点击上传APK文件">
					<i class="iconfont" style="line-height:100px;">&#xe640;</i>
				</div>
				<span class="upload_apk_name"></span>
				<div class="show_son"><div class="son"></div></div>
				<span class="app_title">APP更新包推送对象:</span>
				<select name='appType' class="select_appType">
					<option name='appType' value='-1'>请选择</option>
					<option name='appType' value='1'>快递员方</option>
					<option name='appType' value='0'>用户方</option>
				</select>
				<span class="app_title">APP更新包版本:</span>
				<input type="text" class="input_appVersion" name = "appVersion">
				<input type = "text" name = "operator" class="operator" style="display:none">
				<input type="file" name = "upload" style="display:none" class='upload_apk'>
				</form>	
				<button class='ok add_btn apk_upload_btn' style="margin-top:10px;display:none">确认上传</button>
			</div>

			<ul class="title_option">
				<li><a href="javascript:;" class="select">APP封面推送</a></li>
			</ul>
			<div class="main_content_text">
				<div style="width:100%;min-height:300px;">
					<div class="add_pic_div" style="" title="点击上传图片">
						<i class="iconfont">&#xe7cf;</i>
					</div>
				</div>
				<div class="hidden_div">
					<span class="prompt">提示:上传的图片请尽可能按照16:9的比例</span>
					<form action="" enctype="multipart/form-data" class="upload_pic_form">
						<input type="file" name='upload' class="add_pic" style="display:none">
					</form>	
						<button class='ok add_btn pic_upload'>确认上传</button>				
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
	<script src="/Express/js/inc_action.js"></script>
	<script src="/Express/js/app_upload.js"></script>
</body>
</html>
