//发送推送请求
function putMessage(){
	closed();
	var formData = new FormData($('.form')[0]);
	$.ajax({
	    url: "/Express/admin/Admin_pushNotice",
	    type: "post",
	    cache: true,
	    data:formData,
        contentType: false,
        processData: false,
        success:function(){
			var prompt = new _Prompt("推送成功!",flash);
			prompt.show_content();						
	    },
	    error:function(){
			var prompt = new _Prompt("抱歉上传过程出现了一点问题,请稍后再试！",reflash);
			prompt.show_content();
	    }
	});	
}
$(function(){
	$('.clientcheck').labelauty();
	//分页
	$('.go_page').click(function(event) {
		var first = changePage($(this));
		$.getJSON('/Express/admin/OrderManage_finshOrder', {first:first}, function(data){
			$('.announcement_table tbody').empty();
			for(var i = 0;i<data.length;i++){
					var html = 
					"<tr>" +
						"<td>" + data[i].name + "</td>" +
						"<td style='max-width:350px;padding-left:30px;padding-right:30px;'>" + data[i].content + "</td>" +
						"<td>" + data[i].date + "</td>" +
						"<td>" + data[i].client + "</td>" +
					"</tr>";
					$('.announcement_table tbody').append(html); 
			}
		});
	});
	//推送公告
	$('.sub').click(function(){
		var APP = "";
		var username = $(".name").text().split("\n")[0];
		$('.input_username').val(username);
		if($('input:radio:checked').val() == "uapp")
			APP = "普通用户";
		else
			APP = "快递员";
		if($('.text').val() == ""){
			var prompt_1 = new _Prompt("请先填写公告内容！");
			prompt_1.show_content();
		}else{
			var prompt = new _Prompt("是否推送公告至 "+APP+"客户端？",putMessage);
			prompt.show_content(); 
		}
	});	
})