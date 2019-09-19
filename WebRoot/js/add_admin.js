//合法性检查
function check(){
	var inputarr = $('.baseMesUl input');
	for(var i = 0;i<inputarr.length;i++) {
		if($(inputarr[i]).val() == ''){
			var prompt = new _Prompt("请将信息填写完整！");
			prompt.show_content();  
			return false;
		}
	}
	if($('.pwdinput').val() != $('.pwdinputAgain').val()){
		var prompt = new _Prompt("密码不一致！");
		prompt.show_content();  
		return false;
	}	
	return true;
}
//提交表单
function formSubmit(){
	if(check())
		$.ajax({
	        cache: true,
	        type: "POST",
	        url:"/Express/admin/Admin_createAdmin",
	        data:$('#addAdminForm').serialize(),
	        async: false,
	        error: function(request) {
				var prompt = new _Prompt("出错了！请重试！",flash);
				prompt.show_content();  
	        },
	        success: function(data) {
	        	if(data.result = 'success'){
					var prompt = new _Prompt("创建成功！",flash);
					prompt.show_content();            		
	        	}else{
					var prompt = new _Prompt("出了点问题，请重试！",flash);
					prompt.show_content();   	        		
	        	}        	
	        }
	    });
	return false;
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
	$('body').on('click','.add_btn',function(){
		formSubmit();
	});
	$('body').on('click','.useCB',function(){
		useCheckBoxFun();
	});
	$('.check-authority :input').labelauty();
});
