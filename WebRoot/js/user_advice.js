$(function(){
	//分页
	$('.go_page').click(function(){
		var first = ChangePage.getFirst($(this));
		$.getJSON('/Express/admin/OrderManage_unfinshOrder', {first:first}, function(data){
			$('.user_suggest_table tbody').empty();
			for(var i = 0;i<data.length;i++){
					var requestDate = new Date(data[i].requestDate);
					var html = 
					"<tr>" +
						"<th><img src='" + ${p.path} + "' alt=''><span>" + ${p.username} + "</span></th>" +
						"<td>" + ${p.advice} "<br/><span class='time'>" + requestDate + "</span></td>";
					"</tr>";
					$('.user_suggest_table tbody').append(html); 
			}
			ChangePage.start();
		});
	});
		
});