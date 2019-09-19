var _data = {
	year : '不限',
	month : '不限',
	day : '不限'
}
//生成查询请求连接
function ReqUrl(){
	for(var i in _data){
		if(_data.month != '不限' || _data.day != '不限')
			if(_data.year == '不限' || _data.year == ''){
				alert("请先选择具体年份!");
				return false;
			}
		if(_data.day != '不限' && _data.month == '不限'){
			alert("请先选择具体月份!");
			return false;
		}
		var url = '/Express/admin/Admin_toCPDpage?flag=1';
		for( i in _data){
			if(_data[i] != '不限' && _data[i] != ''){
				url += '&' + i + "=" +  _data[i];
			}
		}
		return url;
	}
}
$(document).ready(function($) {
	//加载下拉框
	$("#year-select").append("<option value='Value'>不限</option>");  
	$("#month-select").append("<option value='Value'>不限</option>");
	$("#day-select").append("<option value='Value'>不限</option>");
	for(var i = 0;i <= 82;i++){
		var year = i + 2017;
		$("#year-select").append("<option value='Value'>" + year + "</option>");  
	}
	for(var i = 1;i<=12;i++){
		$("#month-select").append("<option value='Value'>" + i + "</option>");
	}
	for(var i = 1;i<=31;i++){
		$("#day-select").append("<option value='Value'>" + i + "</option>");
	}
	//点击分页
	$('.go_page').click(function(event){
		var first = ChangePage.getFirst($(this));
		$.getJSON('/Express/admin/Admin_toCPDpage', {first:first,twice:true}, function(data){
			$('.user_table tbody').empty();
			for(var i = 0;i<data.length;i++){
					var date = new Date(data[i].date);
					date = date.Format('yyyy-MM-dd hh:mm:ss');
					var html = 
					'<tr><td style="max-width:180px">' + data[i].orderId +'</td><td style="max-width:180px">' + data[i].name + '</td><td>' + data[i].money + '</td><td>' + date +'</td><td>'+ data[i].record + '</td>'; 
					$('.user_table tbody').append(html); 
			}
			ChangePage.start();
		});		
	});	
	//点击按条件查询
	$("body").on('click','#TJbtn',function(){
		_data.year = $("#year-select").find("option:selected").text();
		_data.month = $("#month-select").find("option:selected").text();
		_data.day = $("#day-select").find("option:selected").text();
		var url = ReqUrl();
		 if(url != false){
		 	window.location.href = url;
		 }
	});
});