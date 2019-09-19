<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>  
    <title>My JSP 'register.jsp' starting page</title>
  </head>
  <body>
     <table border="2" bgcolor="ccceee" width="650">
         <tr bgcolor="CCCCCC" align="center">
              <td>姓名</td>
              <td>身份证号</td>
         </tr>
         
         <c:forEach var="p" items="${user}">
         <tr>
         <form action="<%=path %>/admin/UserManage_identityCheck.action?identity=${p.identity}" name="form1" class="form1" method="post">
              <td>${p.name}</td>
              <td>${p.identity}<input type="submit" value="检测合法性"></td>
         </form>
              <td><img src="${p.path1}" whidth="150" height="150"></td>
              <td><img src="${p.path2}" whidth="150" height="150"></td>
              <td><img src="${p.path3}" whidth="150" height="150"></td>
         
         </tr>
         </c:forEach>
  </table>
  </body>
</html>
