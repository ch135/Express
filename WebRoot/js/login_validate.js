$(document).ready(function() {
	
	$('.submit').click(function(event) {
		removeError();
		var account = $('.account').val();
		var password = $('.password').val();
		var flag = 0;
		if(account == ""){
			var account_html = "<span class = 'error_message'>账号不能为空！</span>";
			$('.account_div').after(account_html);
			flag = 1;
		}
		if(password == ""){
			var password_html ="<span class = 'error_message'>密码不能为空！</span>" ;
			$('.password_div').after(password_html);
			flag = 1;
		}
		if(flag)
			return false;
	});


	$('.password').focus(function(event) {
		removeError();
	});

	$('.account').focus(function(event) {
		removeError();
	});

	function removeError(){
		$('.error_message').remove();
	}



});