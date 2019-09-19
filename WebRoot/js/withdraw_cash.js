$(function() {
	bindWeiChuLi();
	$("#fragment-2").hide();
	// 点击分页
	$('body').on('click','#fragment-3 .go_page',function(event) {
		var first = ChangePage.getFirst($(this));
		ajaxFun({
			url : '/Express/admin/Admin_getInCashRecord',
			data :{first:first,times:"noFirst"},
			sucFun : function(data){
				console.log(data);
				$('#fragment-3 .user_table tbody').empty();
				for (var i = 0; i < data.length; i++) {
					var date = new Date(data[i].date);
					var zidong='';
					date = date.Format('yyyy-MM-dd hh:mm:ss');
					if(data[i].results==true)deal='已处理';
					else{
						deal='<a href="javascript:void(0)"  class="weichuli" data="'+data[i].id+'">未处理</a>';
						if(data[i].inCashType=='WeiXin')
						zidong='<a href="javascript:void(0)"  class="zidong" data="'+data[i].id+'">提现</a>';
					}
					var html = '<tr><td>' + data[i].name + '</td><td>'
							+ data[i].mobile + '</td><td>'
							+ data[i].balance + '</td><td>'
							+ data[i].inCashType + '</td><td>'
							+ data[i].account + '</td><td>'
							+ zidong+ '</td><td>'
							+ date+ '</td><td>'
							+ deal+ '</td></tr>';
					$('#fragment-3 .user_table tbody').append(html);
				}
				bindWeiChuLi();
				ChangePage.start();					
			}
		});
		
	});
	
	
//	$('#fragment-3 .go_page').click(
//	function(event) {
//		var first = ChangePage.getFirst($(this));
//		$.getJSON('/Express/admin/Admin_getInCashRecord', {
//			first : first,
//			times : "noFirst"
//		}, function(data) {
//			$('#fragment-3 .user_table tbody').empty();
//			for (var i = 0; i < data.length; i++) {
//				var date = new Date(data[i].date);
//				date = date.Format('yyyy-MM-dd hh:mm:ss');
//				if(data[i].result==true)deal='已处理';
//				else{
//					if(data[i].inCashType=='WeiXin')deal='<a href="#" class="weichuli" data="'+data[i].id+'">未处理</a>';
//					else deal='未处理';
//				}
//				var html = '<tr><td>' + data[i].name + '</td><td>'
//						+ data[i].mobile + '</td><td>'
//						+ data[i].balance + '</td><td>'
//						+ data[i].inCashType + '</td><td>'
//						+ data[i].account + '</td><td>' + date
//						+ '</td><td>'
//						+ deal
//						+ '</td></tr>';
//				$('#fragment-3 .user_table tbody').append(html);
//			}
//			bindWeiChuLi();
//			ChangePage.start();
//		});
//	});
	$(".daying").click(function() {
		$("#fragment-3").hide();
		$("#fragment-2").show();
		$.getJSON('/Express/admin/Admin_getInCashRecordTime', {
			times : 0
		}, function(data) {
			$("#div1").html("");
			page({
	            id:'div1',
	            nowNum:1,
	            allNum:data.pageNum,
	            callback:function(now,all){
	            	$.getJSON('/Express/admin/Admin_getInCashRecordTime', {
						times :now
					}, function(data) {
						console.log(data);
						$('#fragment-2 .user_table tbody').empty();
						for (var i = 0; i < data.data.length; i++) {
							var date = new Date(data.data[i]);
							date = date.Format('yyyy-MM-dd hh:mm:ss');
							var html = '<tr><td  style="padding: 5px">' + date
									+ '</td><td  style="padding: 5px;width:50%;">'
									+ '<a href="/Express/admin/Admin_printExcelByDate?date='+date+'">下载</a>' + '</td><td>' + '</td></tr>';
							$('#fragment-2 .user_table tbody').append(html);
						}
					});
	            	bindWeiChuLi();
	            }
	        })
		});
	})
	$(".tixian").click(function() {
		$("#fragment-2").hide();
		$("#fragment-3").show();
	});

});

function bindWeiChuLi(){
	$(".weichuli").bind("click", function(){
		var id=$(this).attr("data");
		function pass(){
			var inCashId=id;
			$.ajax({
				url:"/Express/admin/Admin_charge2Money",
				data:"inCashId="+inCashId,
				dataType: "json",
				async: true,
				success:function(data){
					$('body .cancle_btn').click();
					var prompt = new _Prompt2(data.result,function(){
						location.reload();
					});
					prompt.show_content();
				}
			});
			closed();
		}
		var prompt = new _Prompt("确认已处理？",pass);
		prompt.show_content();
	});
	
	
	
	$(".zidong").bind("click", function(){
		var id=$(this).attr("data");
		function pass(){
			var inCashId=id;
			$.ajax({
				url:"/Express/admin/Admin_chargeMoney",
				data:"inCashId="+inCashId,
				dataType: "json",
				async: true,
				success:function(data){
					$('body .cancle_btn').click();
					var prompt = new _Prompt2(data.result,function(){
						location.reload();
					});
					prompt.show_content();
				}
			});
			closed();
		}
		var prompt = new _Prompt("自动提现？",pass);
		prompt.show_content();
	});
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




/*--------------- 提示框类 -------------------*/
var _Prompt2 = function(txt,fun_1,argument){
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
    "<button class='ok btn yes_btn' style='margin-left:160px'>确定</button>"+
    "</div>"+
    "</div>";
}

_Prompt2.prototype = {
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

