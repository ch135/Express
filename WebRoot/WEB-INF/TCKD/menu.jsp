<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

		<!-- 左侧菜单栏 -->
		<div class="left_menu">
			<div class="menu_box">
				<dl>
				    <c:if test="${adminSession.userManage}">
					<dt class = "menu_title"><i class = "icon_menu_title" style = "background:url(/Express/img/yonghu.png) no-repeat;"></i>用户管理</dt>
				    </c:if>
					<c:if test="${adminSession.checkUser}">
					<a href="/Express/admin/UserManage_getAllUserIdentity?first=0"><dd class = "menu_item" name="shenqing">申请审核</dd></a>
					</c:if>
					<c:if test="${adminSession.userMsg}">
					<a href="/Express/admin/UserManage_getAllUser1?first=0"><dd class = "menu_item" name="message">用户信息</dd></a>
					</c:if>
					<c:if test="${adminSession.userAdvice}">
					<a href="/Express/admin/UserManage_lookAdvice?first=0"><dd class = "menu_item" name="suggest">用户建议</dd></a>
					</c:if>
					<c:if test="${adminSession.userATM}">
					<a href="/Express/admin/Admin_getInCashRecord?times=first"><dd class = "menu_item" name="withdraw_cash">用户提现</dd></a>
					<a href="/Express/admin/UserManage_toChargePage?times=first"><dd class = "menu_item" name="rechargeInquiry">充值记录</dd></a>
					</c:if>
				</dl>
				<c:if test="${adminSession.orderManage}">
				<dl>
					<dt class = "menu_title"><i class = "icon_menu_title" style = "background:url(/Express/img/dingdan.png) no-repeat;"></i>订单管理</dt>
					<a href="/Express/admin/Admin_toCPDpage?times=first"><dd class = "menu_item" name="transactionDetails">收支明细</dd></a>
					<a href="/Express/admin/OrderManage_unfinshOrder1?first=0"><dd class = "menu_item" name="unfinish_order">未完成订单</dd></a>
					<a href="/Express/admin/OrderManage_overtimeOrder?first=0"><dd class = "menu_item" name="overtime_order">超时订单</dd></a>
					<a href="/Express/admin/OrderManage_finshOrder?times=first"><dd class = "menu_item" name="finish_order">已完成订单</dd></a>
				</dl>
				</c:if>
				<c:if test="${adminSession.adminManage }">
				<dl>
					<dt class = "menu_title"><i class = "icon_menu_title" style = "background:url(/Express/img/guanli.png) no-repeat;"></i>管理员管理</dt>
					<a href="/Express/admin/Admin_toCreateAdmin"><dd class = "menu_item" name="add_admin">创建管理员</dd></a>
					<c:if test="${adminSession.username=='admin'}">
					<a href="/Express/admin/Admin_toCreateCityAdmin"><dd class = "menu_item" name="add_city_admin">创建地区管理员</dd></a>
					</c:if>
					<a href="/Express/admin/Admin_toChangeQX"><dd class = "menu_item" name = 'change_authority'>修改权限</dd></a>
				</dl>
				</c:if>
				<c:if test="${adminSession.msgManage }">
				<dl>
					<dt class = "menu_title"><i class = "icon_menu_title" style = "background:url(/Express/img/xiaoxi.png) no-repeat;"></i>消息管理</dt>
					<a href="/Express/admin/Admin_toSysMsg?first=0"><dd class = "menu_item" name="announment">系统公告</dd></a>
					<a href="/Express/admin/Admin_toAppUpdate"><dd class = "menu_item" name="app_upload">应用更新</dd></a>
				</dl>
				</c:if>
				<c:if test="${adminSession.activitySet }">
				<dl>
					<dt class = "menu_title"><i class = "icon_menu_title" style = "background:url(/Express/img/xiaoxi.png) no-repeat;"></i>其他</dt>
					<a href="/Express/admin/Admin_toCouponPage"><dd class = "menu_item" name="activity">活动设置</dd></a>
				</dl>
				</c:if>
				<dl></dl>
			</div>
		</div>
	<script src="/Express/js/jquery-2.1.1.js"></script>
    <script>
    $(function(){

    	function getUrlParam(name)
		{
			var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
			var r = window.location.search.substr(1).match(reg);  //匹配目标参数
			if (r!=null) return unescape(r[2]); return null; //返回参数值
		} 
    	var Name = '<%=request.getParameter("clazz")%>';
    	var obj = $("dd[name = " + Name + "]");
    	obj.removeClass('menu_item');
    	obj.addClass('here');

    })
    </script>
