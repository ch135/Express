var sUserAgent = navigator.userAgent.toLowerCase();
var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
var bIsMidp = sUserAgent.match(/midp/i) == "midp";
var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
var bIsAndroid = sUserAgent.match(/android/i) == "android";
var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
var Wxdata = {
	bindPhone : 0,			//有无绑定手机
	flag : 1,  				//是否授权
	Imgurl : '',			//头像路径
	userName : ''			//昵称
};
var redbagData = {
	geted : 0, 					//是否是领过的红包
	haveBag : 1, 				//是否还有红包
	money : 1.6,				//金额
	minMoneyUse : 0.1,			//满几可用
	deadLine : '长期有效',			//到期时间
	phoneNum : ''				//手机号
};
//手机端判断各个平台浏览器及操作系统平台
function browserRedirect(){
	if (bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM){
		//判断在手机打开网页
		var ua = navigator.userAgent.toLowerCase();//获取判断用的对象
		if (ua.match(/MicroMessenger/i) == "micromessenger"){//判断在微信中打开
			console.log('微信手机版打开');
		}else{
			console.log('手机浏览器打开');
		}
	}else{//判断为电脑登录
		var ua = navigator.userAgent.toLowerCase();//获取判断用的对象信
		if (ua.match(/MicroMessenger/i) == "micromessenger"){//判断在微信中打开
			console.log("微信电脑版打开");
		}else{
			console.log("电脑浏览器打开");
		}
	}
}
//获取参数
function GetQueryString(name){
   var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
   var r = window.location.search.substr(1).match(reg);
   if(r!=null)
   	return  unescape(r[2]); 
   return null;
}
//请求红包
function getRedBag(){
	var orderId = GetQueryString('orderId');
	redbagData.phoneNum = $('.phoneNumInput').val();
	if(redbagData.phoneNum == null || redbagData.phoneNum == ''){
		setPromptMsg('请先填写你的手机号');
		return false;
	}
	ajaxFun({
		url:'Express/user/User_mobileGetCoupon',
		data : {
			orderId : orderId,
			mobile :redbagData.phoneNum
		},
		sucFun : function(data){
			if(data.result == 'success' || data.result == 'aleary'){
				Wxdata.bindPhone = 1;
				redbagData.money = data.value;
				redbagData.geted = ((data.result == 'success')?0:1);
			}
			else if(data.result == 'noUser'){
				//没有注册
				setPromptMsg('该手机号尚未注册,下载快快送APP注册后才能领哦~');
				return false;
			}	
			showRedBag();
		}
	});
}
//初始化
function begin(){
	var tpl =  document.getElementById('wchatMessageTpl').innerHTML;
	var html = juicer(tpl,Wxdata);
	$('.list-box').html(html);	
}
//显示结果
function showRedBag(){
	//绑定手机且已经授权(默认已授权且已绑定手机)
	if(Wxdata.flag == 1 && Wxdata.bindPhone == 1){
		$('.list-box').empty();
		var tpl =  document.getElementById('redBagTpl').innerHTML;
		var html = juicer(tpl,redbagData);
		$('.list-box').append(html);
		//已经领过该红包
		if(redbagData.geted){
			setPromptMsg('这个红包已经领过了哦~');
		}
	}		
}
$(document).ready(function($){
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
	begin();
});

/*
 * 备注:
 * 显示微信信息（头像、昵称）html:
 * 		<div class="wchat-message">
 *			<img src="${headimgurl}" alt="" class="userImg">
 *			<span>${nickname}</span>
 *		</div>	
 */