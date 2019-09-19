//设置提示信息
function setPromptMsg(text){
	var promptMsg = '<div class="promptMessage"><h1>'+text+'</h1></div>';
	$("body").append(promptMsg);
	var msgLength = $(".promptMessage").length-1;//计算提示信息的位置index
	var width = $(".promptMessage").eq(msgLength).width();//锁定第n个信息的宽度
	setTimeout('$(".promptMessage").eq('+msgLength+').fadeOut(200);',2000);//两秒后隐藏
}
//清除多余提示信息
function clearMsg(){
	var hiddenMsg = $(".promptMessage:hidden").length;
	if(hiddenMsg>20){
		$(".promptMessage:hidden").remove();
	}
}