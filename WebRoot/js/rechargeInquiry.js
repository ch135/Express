$(function(){
	//点击分页
	$('.go_page').click(function(event){
		var first = ChangePage.getFirst($(this));
		$.getJSON('/Express/admin/UserManage_toChargePage', {first:first}, function(data){
			console.log(data);
			$('.user_table tbody').empty();
			for(var i = 0;i<data.length;i++){
					var date = new Date(data[i].date);
					date = date.Format('yyyy-MM-dd hh:mm:ss');
					var html = 
					'<tr><td>' + data[i].id +'</td><td>' + data[i].mobile + '</td><td>' + data[i].money + '</td><td>' + data[i].chargeType +'</td><td>'+ date + '</td>'; 
					$('.user_table tbody').append(html); 
			}
			ChangePage.start();
		});		
	});
	//点击查询
	$("#mobile-search").click(function(){
		var mobile = $('.search-input').val();
		 $.getJSON('/Express/admin/UserManage_findUserCharge', {mobile:mobile}, function(data){

			$('.user_table tbody').empty();
			$('.next_page_box').css('display','none');
			if(data == null){
				var html = 
				"<div style='width:100%;height:100px;'>" + 
					'<h1 style="text-align:center;font-size:20px;line-height:200px;color:#8C8C8C;">暂无数据 <h1>' +
				"</div>";
				$('.user_message_table').append(html);
				return false;
			}
			for(var i = 0;i<data.length;i++){
				var date = new Date(data[i].date);
				date = date.Format('yyyy-MM-dd hh:mm:ss');
				var html = 
				'<tr><td>' + data[i].id +'</td><td>' + data[i].mobile + '</td><td>' + data[i].money + '</td><td>' + data[i].chargeType +'</td><td>'+ date + '</td>'; 
				$('.user_table tbody').append(html); 
			}
		 });			
	});
});