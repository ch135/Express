var userid; 
var allNum;
//用户信息
//存放当前显示的用户的个人详细资料
var PersonMes = {
	/*
	 *	path : '头像路径'	
	 *	name : '用户名'
	 *	sex : '性别'
	 *	mobile : '手机号'
	 *	identity : '身份证'
	 *	pinfen : '评分'
	 *	balance : '账户余额'
	 *	credit : '信誉值'
	 *	accept_able : '可否接单'
	 *	courier : '用户角色'
	 *	pass '是否免押金'(快递员专有)
	 */	
};

//修改接单权限
function changeRole(Data){
	ajaxFun({
		url : '/Express/admin/UserManage_changeAble',
		data :	Data,
		sucFun : function(data){
			if(data.results == 'success'){
				PersonMes.accept_able = !PersonMes.accept_able;
				reflashMes(PersonMes);
				var prompt = new _Prompt("修改成功");
				prompt.show_content();
			}
			else{
				var prompt = new _Prompt("遇到了一点问题，请稍后再试",flash);
				prompt.show_content();
			} 					
		}
	});
}

//修改免押金设置
function changePass(Data){
	ajaxFun({
		url : '/Express/admin/UserManage_setPass',
		data : Data,
		sucFun : function(data){
			if(data.result == 'success'){
				PersonMes.pass = !PersonMes.pass;
				reflashMes(PersonMes);
				var prompt = new _Prompt("修改成功");
				prompt.show_content();				
			}else{
				var prompt = new _Prompt("遇到了一点问题，请稍后再试",flash);
				prompt.show_content();				
			}
		}
	});
}

//更新用户详细信息的表单
function reflashMes(data){
	$('.information_box').empty();
	var a=$('.information_box').parent();
	var table = '<table class="information" cellspacing = "0"></table>';
	$('.information_box').append("<div style='padding-left:140px;font-size:16px;color:#000;'><a class='showOrder' href='#' id='"+data.id+"'>查看订单记录</a></div>");
	$('.information_box').append(table);
	var html = '<tr><th>用户头像</th><td><img onclick = "createPicShow(this)" title="查看大图" class="photo" src=" ' + data.path +  '" alt=""></td></tr><tr><th>用户名</th><td>' + data.name + '</td></tr><tr><th>性别</th><td>' + data.sex + '</td></tr><tr><th>手机号</th><td class="mobile-td">' + data.mobile + '</td></tr><tr><th>身份证号</th><td>' + data.identity + '</td></tr><tr><th>评分</th><td>' + data.pinfen + '</td></tr><tr><th>账户余额</th><td>' + data.balance +'</td></tr><tr><th>信誉值</th><td>' + data.credit + '</td></tr><tr><th>用户角色</th><td>' + ((data.role == 'courier')?"快递人":"普通用户") + '</td></tr><tr><th>可否接单</th><td class="accept_able_td">' + (data.accept_able?'可接单':'不可接单') + '</td></tr>';
	//如果是快递员，添加另外的内容
	if(data.role == 'courier'){
		html +=  '<tr><th>修改权限</th><td class="btn-role"><button class="'+ ((data.accept_able)?'error':'ok') +'" onclick = "changeRole({mobile:'+ data.mobile +',flag:'+ !data.accept_able +'})">更改权限为'+ ((data.accept_able)?'不可接单':'可接单') +'</button></td></tr>'+
		'<tr><th>是否免押金</th><td>'+ ((data.pass)?'是':'否') +'</td></tr>' + 
		'<tr><th>免押金设置</th><td class="btn-role"><button class="'+ ((data.pass)?'error':'ok') +'" onclick = "changePass({pass:' + !data.pass+',mobile:' + data.mobile + '})">'+ ((data.pass)?'取消':'设置') +'免押金</button></td></tr>'
	}
	$('.information').append(html);
	$(".showOrder").click(function(){
		var userId=$(this).attr('id');
		userid=userId;
		ajaxFun({
			url : '/Express/admin/UserManage_getCourierOrder',
			data :{page:1,userId:userid},
			sucFun : function(data){
				$("#div1").html("");
				page({
			         id:'div1',
			         nowNum:1,
			         allNum:data.pageNum,
			         callback:function(now,all){
			         	showOrder(now);
			         }
			     })
			}
		});
	})
}
function showOrder(now){
	ajaxFun({
		url : '/Express/admin/UserManage_getCourierOrder',
		data :{page:now,userId:userid},
		sucFun : function(data){
			var html='';
			for(var i in data['data']){
				var time1 = new Date(data['data'][i].requestDate).Format("yyyy-MM-dd");
				var status=data['data'][i].orderStaus;
				//0:未支付 1:未接单 2:已接单 3:派送中 4:已完成 5:已评价 -1:用户已取消 -2:快递员取消 -3:订单自动过期 -4:管理员关闭订单
				console.log(status<0);
				if(status<=0)status='订单取消';
				else if(status<=3)status='派送中';
				else if(status>=4)status="订单完成";
				html+='<tr>'
					+'<td class="orderId">'+data['data'][i].orderId+'</td>'
					+'<td class="orderFare">'+data['data'][i].orderFare+'</td>'
					+'<td class="goodsDetail">'+data['data'][i].goodsDetail+'</td>'
					+'<td class="goodsDetail">'+status+'</td>'
					+'<td class="requestDate">'+time1+'</td>'
					+'</tr>';
			}
			allNum=data['data'].pageNum;
			$("#fragment-3 tbody").html("");
			$("#fragment-3 tbody").append(html);  
			$(".showOrderTag").click();
		}
	});
}

$(function(){

/*------------------------权限编辑加载数据----------------------------*/
var _LoadData = (function(){
	var data = {};

	return {
		//获取所需数据
		getData:function(that){
			data = {
				'url':'/Express/admin/UserManage_getUser',
				'postData':{
					'mobile':that.find(".mobile").text()
				}
			};
			return this;
		},
		//发送请求
		sendData:function(){
			var _flag = this;
			$.getJSON(data.url, data.postData , function(data){
				_flag.result(data);
			});				
		},
		//回调函数
		result:function(data){
			PersonMes = data;
			for(i in PersonMes)
				console.log(i+':'+PersonMes[i]);
			showTabContent();
			$('.show').click();
			reflashMes(data);
		},
		//启动
		start:function(that){
			this.getData(that).sendData();
		}
	}
})();
/*------------------------------------------------------------------*/


//触发加载tab页
	function tr_click(that){
		_LoadData.start(that);
	}

	$("#tabs").tabs();
	
	// 表格样式设置
	$('body').on('click','#fragment-2 .user_table tbody tr',function(){
		tr_click($(this));
	});
	//页面渲染
	function fenye_monochrome(data){
		$('#fragment-2 .user_table tbody').empty();
		for(var i = 0;i<data.length;i++){
			var html = '<tr title="点击编辑权限"><td><img src= " ' + data[i].path + ' "></td><td class="name">' +data[i].name + 
			'</td><td class="sex">' + data[i].sex + '</td><td class="mobile">' + data[i].mobile + '</td><td class="identity">' + data[i].identity + 
			'</td><td class="pingfen">' + data[i].pingfen + '</td><td class="balance">' + data[i].balance+ '</td><td class="credit">' + data[i].credit +
			'</td><td class="accept_able">' +  data[i].accept_able + '</td></tr>'; 
			$('.user_table tbody').append(html); 
		}	
	}
	

/****************************分页栏**********************************/
$('body').on('click','#fragment-2 .go_page',function(event) {
	var first = ChangePage.getFirst($(this));
	ajaxFun({
		url : '/Express/admin/UserManage_getAllUser',
		data :{first:first},
		sucFun : function(data){
			console.log(data);
			fenye_monochrome(data);
			ChangePage.start();					
		}
	});
});
/*******************************************************************/
	

/*--------------------------用户查询----------------------------------*/
$('body').on('click','.user-search_btn',function(){
	$('.userList').click();
	var value = $('.user-search-input').val();
	if(value == ""){
		var prompt = new _Prompt("内容不能为空!");
		prompt.show_content();
		return false;
	}	
	ajaxFun({
		url : '/Express/admin/UserManage_searchUser',
		data : {value:value},
		sucFun : function(data){
			fenye_monochrome(data);
			$('.last_page').attr('name',data.pageNum);
			var obj = $('.next_page_box span:last-child').prev('span');
			$('.go_page').css('display','none');				
		}
	});
});

/*------------------------------------------------------------------*/
});

Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}


function page(opt){

    if (!opt.id) {
        return false;
    }

    var obj=document.getElementById(opt.id);
    var nowNum=opt.nowNum || 1;
    var allNum=opt.allNum || 5;

    //看回调函数是否存在，如果存在就直接赋值，不存在则默认空
    var callback=opt.callback || function(){};

    //首页
    if (nowNum>=4 && allNum>=6) {
        var oA=document.createElement('a');
        oA.href='#1';
        oA.innerHTML='首页';
        obj.appendChild(oA);
    }

    //上一页
    if (nowNum>=2) {
        var oA=document.createElement('a');
        oA.href='#'+(nowNum-1);
        oA.innerHTML='上一页';
        obj.appendChild(oA);
    }

    //5个页码
    if (allNum<=5) {

        for (var i=1; i<=allNum; i++) {
            var oA=document.createElement('a');
            oA.href='#'+i;
            if (nowNum==i) {
                oA.innerHTML=i;
            }else{
                oA.innerHTML='['+ i +']';
            }

            obj.appendChild(oA);
        }

    }else{
        for (var i=1; i<=5; i++) {

            var oA=document.createElement('a');

            if (nowNum<3) {

                oA.href='#'+i;
                if (nowNum==i) {
                    oA.innerHTML=i;
                }else{
                    oA.innerHTML='['+ i +']';
                }

            }else if (nowNum>(allNum-2)) {

                oA.href='#'+(allNum+i-5);

                if (nowNum==(allNum+i-5)) {
                    oA.innerHTML=(allNum+i-5);
                }else{
                    oA.innerHTML='['+ (allNum+i-5) +']';
                }

            }else{

                oA.href='#'+(nowNum-3+i);
                if (i==3) {
                    oA.innerHTML=(nowNum-3+i);
                }else{
                    oA.innerHTML='['+ (nowNum-3+i) +']';
                }

            }

            obj.appendChild(oA);
        }
    }

    //下一页
    if ((allNum-nowNum)>=1) {
        var oA=document.createElement('a');
        oA.href='#'+(nowNum+1);
        oA.innerHTML='下一页';
        obj.appendChild(oA);
    }

    //尾页
    if ((allNum-nowNum)>=3 && allNum>=6) {
        var oA=document.createElement('a');
        oA.href='#'+allNum;
        oA.innerHTML='尾页';
        obj.appendChild(oA);
    }

    //总共页码
    var oSpan=document.createElement('span');
    oSpan.innerHTML='总共 '+ allNum +' 页';
    obj.appendChild(oSpan);

    //执行回调函数
    callback(nowNum,allNum);

    //点击切换
    var aA=obj.getElementsByTagName('a');
    for (var i=0; i<aA.length; i++) {
        aA[i].onclick=function(){
            var nowNum=parseInt(this.getAttribute('href').substring(1));
            obj.innerHTML='';

            page({
                id:opt.id,
                nowNum:nowNum,
                allNum:allNum,
                callback:callback
            })

            return false;
        }
    }

}
