var AdminInfo = {
	//存放当前管理员的权限信息
	/*
	 	string name;			//管理员名称
		boolean userManage; 	//用户管理
		boolean checkUser; 		// 用户审核
		boolean userMsg; 		// 用户信息
		boolean userAdvice; 	// 用户建议
		boolean userATM; 		// 用户提现
		boolean orderManage;  	// 订单管理
		boolean adminManage; 	// 管理员管理
		boolean msgManage; 		// 消息管理
		boolean activitySet; 	// 活动设置
	*/
};
//删除管理员
function deletAdmin(userName){
	closed();
	ajaxFun({
		url : '/Express/admin/Admin_deleteAdmin',
		data : {'userName' : userName},
		sucFun : function(data){
			if(data.result == 'success'){
		    	var prompt = new _Prompt("删除成功！",flash);		
		    	prompt.show_content();			
			}else if(data.result == 'forbid'){
		    	var prompt = new _Prompt("无法删除超级管理员");		
		    	prompt.show_content();				
			}else{
		    	var prompt = new _Prompt("出错了，请重试",flash);		
		    	prompt.show_content();				
			}
		}
	});
}
//删除盒子
function removeBgd(){
	$('.background').remove();
}
// 查看大图
$('body').on('click','.photo',function() {
	createPicShow(this);
});
//确认删除管理员操作
$('body').on('click','.delet_btn',function(){
	var userName = $(this).attr('data-name');
	var prompt = new _Prompt("您确定要删除此管理员吗?",deletAdmin,userName);
	prompt.show_content();
});
//渲染模板
function showTpl(data){
	var tpl =  $('#changeAut').html();
	var html = juicer(tpl,data);
	var bgdHtml = '	<div class="background"></div>'
	$('body').append(bgdHtml);
	$('.background').html(html);
	$('.background').fadeIn(400);
	$('.list-box :input').labelauty();
	if(data.username == 'admin'){
		$('.list-box input').attr('disabled',true);
	}
}
//获取管理员的权限信息
function getAdminInfo(Data){
	ajaxFun({
		url : '/Express/admin/Admin_getAdminByName',
		data : Data,
		sucFun : function(data){
			console.log(data);
			if(data != 'error'){
				console.log(data);
				AdminInfo = data;
				showTpl(AdminInfo);				
			}else{
				var prompt = new _Prompt("出了点问题,请刷新重试",flash);
				prompt.show_content();				
			}
		}
	});
}
//发送修改权限请求
function submitForm(){
	var _data = ($('.changeAutForm').serialize());
	closed();
	ajaxFun({
		url : '/Express/admin/Admin_changeQX',
		data : _data,
		sucFun : function(data){
			if(data.result == 'success'){
				var prompt = new _Prompt("修改成功！",flash);
				prompt.show_content();	
			}else if(data.result == 'forbid'){
				var prompt = new _Prompt("无法更改超级管理员的权限",closed);
				prompt.show_content();				
			}
			else{
				var prompt = new _Prompt("修改失败，请重试！",flash);
				prompt.show_content();				
			}
		}
	});
}
function useCheckBoxFun(){
	var inputArr = $('.useCB');
	var i = 0;
	for(i = 0;i<inputArr.length;i++){
		if(inputArr[i].checked){
			document.getElementById('useOpt').checked = true;
			break;
		}
	}
	if(i == inputArr.length){
		document.getElementById('useOpt').checked = false;
	}
}

$(document).ready(function(){
	//更改juicer.js引擎配置,避免与jsp语法冲突
	juicer.set({
		'tag::operationOpen': '{@',
		'tag::operationClose': '}',
		'tag::interpolateOpen': '&{',
		'tag::interpolateClose': '}',
		'tag::noneencodeOpen': '$${',
		'tag::noneencodeClose': '}',
		'tag::commentOpen': '{#',
		'tag::commentClose': '}'
	});	
	//修改权限
	$('body').on('click','.change_btn',function(){
		var name = $(this).parent().prev().prev().text();
		getAdminInfo({'username':name});
	});
});

