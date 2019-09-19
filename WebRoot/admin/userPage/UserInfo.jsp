<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
    <head>
	<meta http-equiv = "Content-Type" Content = "text/html;charset = utf-8" >
	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
	<style>
		tr td,tr th{
			text-align: center;
		}

		tr td img{
			width: 60px;
			height: 60px;
		}

		tr td button{
			margin-top:10px;
		}

		tr td .no_pass,tr td .pass{
			margin-left: 10px;
		}

		#next_page{
			float: right;
		}
	</style>
    </head>
    <body>
    <div class="container">
		<table class="table table-bordered table-hover ">
			<thead id="table_head">
				<tr>
					<th>姓名</th>
					<th>身份证号</th>
					<th>图片1</th>
					<th>图片2</th>
					<th>图片3</th>
					<th>操作</th>
					<th>审查结果</th>
				</tr>
			</thead>
			<tbody id="table_body">
				<tr>
					<td style="vertical-align: middle;">小明</td>
					<td style="vertical-align: middle;">1234567890123</td>
					<td><img src="images/chrome-logo-small.jpg" alt="photo1"></td>
					<td><img src="images/chrome-logo-small.jpg" alt="photo2"></td>
					<td><img src="images/chrome-logo-small.jpg" alt="photo3"></td>
					<td><button class="btn btn-default btn-primary check_button" type="button">检查</button><button class="btn btn-default btn-success pass">审查通过</button><button class="btn btn-default btn-danger no_pass">审查不通过</button></td>
					<td style="vertical-align: middle;">未审批</td>
				</tr>
				<tr>
					<td style="vertical-align: middle;">小明</td>
					<td style="vertical-align: middle;">1234567890123</td>
					<td><img src="images/chrome-logo-small.jpg" alt="photo1"></td>
					<td><img src="images/chrome-logo-small.jpg" alt="photo2"></td>
					<td><img src="images/chrome-logo-small.jpg" alt="photo3"></td>
					<td><button class="btn btn-default btn-primary check_button" type="button">检查</button><button class="btn btn-default btn-success pass">审查通过</button><button class="btn btn-default btn-danger no_pass">审查不通过</button></td>
					<td style="vertical-align: middle;">未审批</td>
				</tr>
				<tr>
					<td style="vertical-align: middle;">小明</td>
					<td style="vertical-align: middle;">1234567890123</td>
					<td><img src="images/chrome-logo-small.jpg" alt="photo1"></td>
					<td><img src="images/chrome-logo-small.jpg" alt="photo2"></td>
					<td><img src="images/chrome-logo-small.jpg" alt="photo3"></td>
					<td><button class="btn btn-default btn-primary check_button" type="button">检查</button><button class="btn btn-default btn-success pass">审查通过</button><button class="btn btn-default btn-danger no_pass">审查不通过</button></td>
					<td style="vertical-align: middle;">未审批</td>
				</tr>
			</tbody>
		</table>
		<nav id="next_page">
		  <ul class="pagination">
		    <li>
		      <a href="#" aria-label="Previous" id="prevPage">
		        <span aria-hidden="true">&laquo;</span>
		      </a>
		    </li>
		    <li class="active"><a href="#">1</a></li>
		    <li><a href="#">2</a></li>
		    <li><a href="#">3</a></li>
		    <li><a href="#">4</a></li>
		    <li>
		      <a href="#" aria-label="Next" id="nextPage">
		        <span aria-hidden="true">&raquo;</span>
		      </a>
		    </li>
		  </ul>
		</nav>
	</div>
	
	<!-- 点击"检查"后的弹出框 -->
	<div class="modal fade" id="check_result">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">检查结果</h4>
	      </div>
	      <div class="modal-body">
	        <p id="check_Message"></p>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default btn-primary" data-dismiss="modal">知道了</button>
	      </div>
	    </div>
	  </div>
	</div>
	<script type="text/javascript" src = "admin/js/jquery-2.1.1.js"></script>
	<script type="text/javascript" src = "admin/js/bootstrap.min.js"></script>
	<script type="text/javascript" src = "admin/js/check.js"></script>
    </body>
</html>
