//刷新页面
function flash(){
	location.reload(true);   
}	
//查看大图
function createPicShow(that){
	var html = "<div class = 'background'><a href='javascript:;' onclick = 'closed()' title = '关闭大图'><img class='close' src='/Express/img/close.png' alt=''></a></div>";
	var box = $(html).appendTo($('body'));
	var img = $("<img class='show'/>").attr("src",$(that).attr('src')).load(function(){
		var WinHeight = document.body.clientHeight;
		var realHeight = this.height;
		var marginVal = (WinHeight - realHeight)/2;
		if(marginVal > 0)
			$(this).css({'margin-top' : marginVal});
		$(this).appendTo($('.background'));
		$('.background').fadeIn('400');	
	});	
}
// 关闭大图
function closed(){
	$('.background').remove();
	return false;
}
//修改密码
function changePw(){
	var text = $('.name').text();
	var username = text.split("\n")[0];
	var html = "<div class='background'><div class='change' style='background-color:#EEEFF0;height:300px'><a href='javascript:;' onclick = 'closed()' title = '关闭'><img class='close' src='/Express/img/close.png' alt='' style='width:30px;height:30px'></a><ul class='change_ul'><li><img class='change_img' src='/Express/img/test.png' alt='' /><span class = 'change_name'  name = 'username'>" + username + "</span></li><li><span>原始密码</span><input type='password' class='oldPwd'/></li><li><span>新的密码</span><input type='password' class='newPwd'/></li><li><span class='confirm_pw'>确认新密码</span><input type='password' class='confirm_pwd'/></li></ul><ul><button class='ok change_pw_button' >确认</button></ul></div></div>";
	var box = $(html).appendTo($('body')); 
	$('.change_pw_button').click(function(){change_pw_button();})
	$('.background').fadeIn('400');	

}
//修改密码确认提交
function change_pw_button(){
	if($('.newPwd').val() == ""||$('.confirm_pwd').val() == ""|| $('.oldPwd').val() == ""){
		alert("请把资料填写完整哦！");
		return false;
	}
	else if($('.newPwd').val() != $('.confirm_pwd').val() ){
		alert("您输入的新密码不一致哦！");
		return flase;
	}else{
         $.ajax({
             type: "POST",
             url: "/Express/admin/Admin_reLoginPwd",
             dataType: "json",
             data: {username:$('.name').text().split("\n")[0], oldPwd:$(".oldPwd").val(),newPwd:$(".newPwd").val()},
             success: function(data){
            	 if(data['status'] == 'success'){
            		 closed();
            		 alert("修改密码成功！");
            	 }else if(data['status'] == 'fail'){
            		 alert("您输入的原始密码有误！");
            	 }
             }
         });
	}
}
//向后台提交请求的方法
function ajaxFun(Data){
/*
 *	Data参数:
 *	Data.data:向后台传递的参数
 *	Data.sucFun:回调成功函数
 *	Data.errFun:回调失败函数	 
 */
	var errFun = Data.errFun || function(){
		var prompt = new _Prompt("出错了！请重试！",closed);
		prompt.show_content();
	};
    $.ajax({
        type: "POST",
        url: Data.url,
        dataType: "json",
        data: Data.data,
        success:Data.sucFun,
        error:Data.errFun
    });
}
//Tab转换内容
function showTabContent(){
	$('.show').click();
}
$(document).ready(function() { 
// 左侧菜单栏固定
//	menuFixed('.menu_box','no');

	// tab转换
	$('.title_option a').on('click',function(event) {
		$('.title_option a').removeClass('select');
		$(this).addClass('select');
	});

    $('.change_pw').on('click',function(){
        changePw();
    });
	
}); 



/*---------------------------------分页类------------------------------------------*/

var ChangePage = (function(){
	var obj,
		objParent,
		lastPage,
		herePage,
		herePageObj,
		pageNum = 5;   //分页栏一次显示页码个数

	return {
		setObjAndObjParent : function(that){
			obj = that;
			objParent = that.parent();
			return this;
		},
		setLastPage : function(){
			lastPage = parseInt(objParent.find('.last_page').attr('name'));
			return this;
		},
		setHerePage : function(){
			herePage = parseInt(objParent.find('.here_page').text());
			herePageObj = objParent.find('.here_page');
			return this;
		},
		getFirst : function(that){
			this.setObjAndObjParent(that);
			//如果是尾页
			if(obj.hasClass('last_page')){
				this.setLastPage();
				return (lastPage - 1) * 10;
			}
			//如果是下一页
			else if(obj.hasClass('next_page')){
				return ( parseInt(objParent.find('.here_page').text()) ) * 10;
			}
			//如果是上一页
			else if(obj.hasClass('prev_page')){
				return ( parseInt(objParent.find('.here_page').text()) - 2) * 10;
			}
			else
				return  (parseInt(obj.text()) - 1)* 10;
		},
		removeHidden : function(){
			objParent.find('.here_page').removeClass('hidden');
			objParent.find('.prev_page').removeClass('hidden');		
			return this;
		},
		toLastPage : function(){
			var num = lastPage%pageNum;
			for(var j = 2,i = 1; i <= num; i++,j++){
				objParent.find('.go_page').eq(j).text( (lastPage + 1 - i));
	        	if( i == 1)
	        		objParent.find('.go_page').eq(j).addClass('here_page');
	        	objParent.find('.go_page').eq(j).removeClass('hidden');
			}
		},
		toNextPage : function(){
			//如果当前页码为分页栏最后一页，即下一页需要翻页时
			if( herePage % pageNum == 0 ){
				//如果翻页后还可显示5页以上
				if(herePage + pageNum <= lastPage){
					objParent.find('.go_page').eq(2).text((herePage + 5));
					objParent.find('.go_page').eq(3).text((herePage + 4));
					objParent.find('.go_page').eq(4).text((herePage + 3));
					objParent.find('.go_page').eq(5).text((herePage + 2));
					objParent.find('.go_page').eq(6).text((herePage + 1));
					objParent.find('.go_page').eq(6).addClass('here_page');
				//如果翻页后不够显示五页
				}else if( (herePage + pageNum) > lastPage){
					//先隐藏所有按钮
					objParent.find('.go_page').addClass('hidden');
					//从第j个按钮开始赋值
			        for(var j = 2,i = 1; i <= (lastPage - herePage); i++,j++){
			        	if( i ==  1)
			        		$('.go_page').eq(j).addClass('here_page');
			        	$('.go_page').eq(j).removeClass('hidden');
			        	$('.go_page').eq(j).text( (herePage + i));
			        }		        
				}
			}
			//否则只需要找到上一个子兄弟
			else{	
				objParent.find('.go_page').removeClass('hidden');
				herePageObj.prev().addClass('here_page');
			}			
		},
		toPrevPage : function(){
			//如果当前页为第一个页,即下一页需要分页
			if( herePage % pageNum == 1 && herePage != 1){
				objParent.find('.go_page').removeClass('hidden');
				objParent.find('.go_page').eq(6).text(herePage - 5);
				objParent.find('.go_page').eq(5).text(herePage - 4);
				objParent.find('.go_page').eq(4).text(herePage - 3);
				objParent.find('.go_page').eq(3).text(herePage - 2);
				objParent.find('.go_page').eq(2).text(herePage - 1);
				objParent.find('.go_page').eq(2).addClass('here_page');
			}
			//否则只需要找到当前页按钮的下一个子兄弟
			else{
				herePageObj.next().addClass('here_page');	
			}			
		},
		_changePage : function(){
			//先除去.here_page属性
			objParent.find('.go_page').removeClass('here_page');
			//如果点击的是尾页
			if(obj.hasClass('last_page')){
				this.toLastPage();
			}
			//如果点击是下一页按钮
			else if(obj.hasClass('next_page')){
				this.toNextPage();
			}
			//如果点击的是上一页按钮
			else if(obj.hasClass('prev_page')){
				this.toPrevPage();
			}
			//如果点击的为具体页数
			else{
				objParent.find('.go_page').removeClass('hidden');
				obj.addClass('here_page');
			}
			objParent.find('.prev_page').addClass('hidden');
			objParent.find('.next_page').addClass('hidden');
			objParent.find('.last_page').addClass('hidden');
			//如果当前页不为 1 ，显示上一页按钮
			if(parseInt(objParent.find(".here_page").text()) != 1)
				objParent.find('.prev_page').removeClass('hidden');
			//如果当前页不为最后一页，显示下一页按钮和尾页按钮
			if(parseInt(objParent.find(".here_page").text()) != lastPage){
				objParent.find('.next_page').removeClass('hidden');
				objParent.find('.last_page').removeClass('hidden');
			}
		},
		start : function(){
			if(arguments.length != 0){
				return this.setObjAndObjParent(arguments[0]).setLastPage().setHerePage().removeHidden()._changePage();
			}
			else;
				return this.setLastPage().setHerePage().removeHidden()._changePage();
		}
	}

})();

/*--------------- 提示框类 -------------------*/
var _Prompt = function(txt,fun_1,argument){
	//参数
	this.argument = argument;
	//提示框内容
	this._content = txt; 
	//点击确定执行函数
	this._okFunc = fun_1 || closed;
	//html样式
	this.html = "<div class='background'>"+
    "<div class='change' style='background-color:#EEEFF0'>"+
    "<a href='javascript:;' onclick = 'closed()' title = '关闭'><img class='close' src='/Express/img/close.png' alt='' style='width:30px;height:30px'></a>"+
    "<span class='tishi'>"+ this._content +"</span>"+
    "<button class='ok btn yes_btn' style='margin-left:120px'>确定</button><button class='error btn cancle_btn' style='margin-left:5px'>取消</button>"+
    "</div>"+
    "</div>";
}

_Prompt.prototype = {
	//弹出提示框
	show_content:function(){
        var flag = this;
        $(flag.html).appendTo($('body'));
        $('.background').fadeIn('400');
        $('body .cancle_btn').on('click',function(){
        	closed();
        });
        $('body .yes_btn').click(function(){
        	flag._okFunc(flag.argument);
        });		
	}
}







