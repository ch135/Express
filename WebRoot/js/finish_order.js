//异步加载订单详情
function details(that){
	var orderId =  $(that).parent().parent().find('.orderId').text();
	$.getJSON('/Express/admin/OrderManage_findOrder', {orderId:orderId}, function(data){
		//订单状态
		var status = '';
		switch(data.orderStaus){
			case 4: status = "已完成";break;
			case 5: status = "已评价";break;
			default: status = "未知";break;
		}
		//下单时间
		var requestDate = new Date(data.requestDate);
		//接单时间
		var receiveDate = new Date(data.receiveDate);
		var url = data.goodsPic.split(";");
		var html = 
		'<div class="basic_message">' +
			"<span class='basic_message_title'>订单号: </span>" +
			'<span>' + data.orderId + '</span>' +
			"<span class='basic_message_title'>下单时间: </span>" +
			"<span>" + requestDate.Format("yyyy-MM-dd hh:mm:ss") + "</span>" +
			"<span class='basic_message_title'>接单时间: </span>" +
			"<span>" + receiveDate.Format("yyyy-MM-dd hh:mm:ss") + "</span>" +
		'</div>' +
		'<div class="basic_message">' + 
			"<span class='basic_message_title'>订单价格: </span>" +
			"<span>" + data.orderFare + "元</span>" +
			"<span class='basic_message_title'>订单评分: </span>" +
			"<span>" +  data.orderGrade  + "</span>" +
			"<span class='basic_message_title'>订单状态: </span>" +
			"<span>" +  status  + "</span>" +
		"</div>" +
			'<span class="div_title">快递员信息</span>' +
			'<div class="basic_message">' +
				"<span class='finish_order_title'>姓名: </span>" +
				'<span>' + data.cname +'</span>' +
				"<span class='finish_order_title'>手机号: </span>" +
				"<span>" + data.cmobile + "</span>" +
			'</div>' +
		'<span class="div_title">寄件人信息</span>' +
		'<div class="basic_message">' +
			"<span class='finish_order_title'>姓名: </span>" +
			'<span>' + data.sendName + '</span>' +
			"<span class='finish_order_title'>手机号: </span>" +
			"<span>" + data.sendMobile + "</span>" +
		"</div>" +
		'<div class="basic_message">' +
			"<span class='finish_order_title'>地址: </span>" +
			"<span>" + data.sendAddress + "</span>" +	
		" </div> " +
		'<span class="div_title">收件人信息</span>' +
		'<div class="basic_message">' +
			"<span class='finish_order_title'>姓名: </span>" +
			"<span>" + data.receiveName + "</span>" +
			"<span class='finish_order_title'>手机号: </span>" +
			"<span>" + data.receiveMobile + "</span>" +
		"</div>" +
		'<div class="basic_message">' +
			"<span class='finish_order_title'>地址: </span>" +
			"<span>" + data.receiveAddress + "</span>" +	
		"</div>" +
		'<span class="div_title">物品信息</span>'+
		'<div class="basic_message">'+
			"<span class='basic_message_title'>商品描述: </span>" +
			"<span>" + data.goodsDetail + "</span>" +
			"<span class='basic_message_title'>物品重量: </span>" +
			"<span>" + data.goodsWeight + "</span>" +
			"<span class='basic_message_title'>保价金: </span>" +
			"<span>" + data.supportValue + "元</span>" +
		"</div>" +
		'<div class="basic_message picture_show">';
		for(var i = 0;i<url.length;i++){
			html += '<img class="picture photo" src="' + url[i] +  '" alt="">';
		}
		html += "</div>";				
		$('.Order_details').empty();
		$('.Order_details').append(html);
		$('.photo').click(function(event) {
			createPicShow(this);
		});
		$(".show").click();
	});
}

//根据日期查找快递员订单
function timeorder(number){
	var name=$("#username").val();
	var start=$("#start").val();
	var end=$("#end").val();
	if(name!=""&&start!=""&&end!=""){
		if((new Date(start)).getTime()<=(new Date(end)).getTime()){
			$.ajax({
				"type":"post",
				"url":"/Express/admin/OrderManage_timefinshOrder",
				"data":{
					"username":name,
					"start":start,
					"end":end,
					"number":number
				},
				success:function(date){
					var result=JSON.parse(date);
					var order=result.order;
					var pagenum=result.pagenum;
					var num=result.num;
					var tb=$("tbody");
					var t__ = parseInt(Math.random() * 1000);
					tb[0].id = "tb_" + t__;
					var short=$(".next_page_box:last");
					var title=$("h5");
					if(pagenum==0){
						tb.empty();
						title.empty();
						if(short!=null){
							short.empty();
						}
						tb.append("<div style='width:100%;height:100px;'>" +
									"<h1 style='text-align:center;font-size:20px;line-height:200px;color:#8C8C8C;'>抱歉,暂无数据 ~<h1>"+
								  "</div>"
								);
					}else if(pagenum==undefined){
						tb.empty();
						for(var i=0;i<order.length;i++){
							result+="<tr>" +
							"<td class='orderId'>"+order[i].orderId+"</td>" +
							"<td class='orderFare'>"+order[i].orderFare+"</td>" +
							"<td class='goodsDetail'>"+order[i].goodsDetail+"</td>" +
							"<td class='requestDate'>"+changetime(order[i].requestDate)+"</td>" +
							"<td><button class='btn ok details'>详细资料</button></td>" +
							"</tr>";
						}
						tb.append(result);
					}else if(order!=null){
						title.empty();
						title.html("查询订单数为："+num);
						tb.empty();
						for(var i=0;i<order.length;i++){
							result+="<tr>" +
							"<td class='orderId'>"+order[i].orderId+"</td>" +
							"<td class='orderFare'>"+order[i].orderFare+"</td>" +
							"<td class='goodsDetail'>"+order[i].goodsDetail+"</td>" +
							"<td class='requestDate'>"+changetime(order[i].requestDate)+"</td>" +
							"<td><button class='btn ok details'>详细资料</button></td>" +
							"</tr>";
						}
						tb.append(result);
						if(number==0){
							short.html("");
							if(pagenum>5){
								short.append("<span class='last_page go_timepage' name="+pagenum+">尾页</span>" +
										"<span class='next_page go_timepage'><i class='iconfont'>&#xe66c;</i></span>" +
										"<span class='go_timepage'>5</span>" +
										"<span class='go_timepage'>4</span>" +
										"<span class='go_timepage'>3</span>" +
										"<span class='go_timepage'>2</span>" +
										"<span class='go_timepage here_page'>1</span>" +
										"<span class='prev_page go_timepage hidden'><i class='iconfont'>&#xe66b;</i></span>"
										);
							}else if(pagenum<=5){
								if(pagenum>1){
									short.append("<span class='last_page go_timepage' name="+pagenum+">尾页</span>" +
											"<span class='next_page go_timepage'><i class='iconfont'>&#xe66c;</i></span>");
								}
								if(pagenum==1){
									short.append("<span class='go_timepage here_page'>1</span>" +
											"<span class='prev_page go_timepage hidden'><i class='iconfont'>&#xe66b;</i></span>");
								}
								if(pagenum==2){
									short.append("<span class='go_timepage'>2</span>"+
											"<span class='go_timepage here_page'>1</span>" +
											"<span class='prev_page go_timepage hidden'><i class='iconfont'>&#xe66b;</i></span>");
								}
								if(pagenum==3){
									short.append("<span class='go_timepage'>3</span>"+
											"<span class='go_timepage'>2</span>"+
											"<span class='go_timepage here_page'>1</span>" +
											"<span class='prev_page go_timepage hidden'><i class='iconfont'>&#xe66b;</i></span>");
								}
								if(pagenum==4){
									short.append("<span class='go_timepage'>4</span>"+
											"<span class='go_timepage'>3</span>"+
											"<span class='go_timepage'>2</span>"+
											"<span class='go_timepage here_page'>1</span>" +
											"<span class='prev_page go_timepage hidden'><i class='iconfont'>&#xe66b;</i></span>");
								}
								if(pagenum==5){
									short.append("<span class='go_timepage'>5</span>"+
											"<span class='go_timepage'>4</span>"+
											"<span class='go_timepage'>3</span>"+
											"<span class='go_timepage'>2</span>"+
											"<span class='go_timepage here_page'>1</span>" +
											"<span class='prev_page go_timepage hidden'><i class='iconfont'>&#xe66b;</i></span>");
								}
							}
							//根据时间分页
							$(".go_timepage").click(function(event){
								var first = ChangePage.getFirst($(this));
								timeorder(first);
							});
						}else{
							ChangePage.start();
							$('.user_table tbody tr').mousemove(function(event) {
								tr_mousemove($(this));
							});

							$('.user_table tbody tr').mouseout(function(event) {
								tr_mouseout($(this));
							});
							
							$('.details').click(function(){
								details(this);
							});
							
							$('.btn').click(function(event) {
								$('.show').click();
							});
						}
					}
					$("#tb_" + t__).find('.details').click(function(){
						details(this);
					});
				}
			});
		}else{
			var prompt = new _Prompt("请输入正确日期！");
			prompt.show_content();
		}
	}else{
		var prompt = new _Prompt("请填写完整信息！");
		prompt.show_content();
	}
}

//更改日期格式
function changetime(time){
	var time=new Date(time);
	var result=time.getFullYear()+"/"+(time.getMonth()+1)+"/"+time.getDate()+" "+time.getHours()+":"+time.getMinutes()+":"+time.getSeconds();
	return result;
}

$(function(){
	$("#tabs").tabs();

	// 表格样式设置
	$('.user_table tbody tr').mousemove(function(event) {
		$(this).css('background-color', '#FBFBFB');
	});

	$('.user_table tbody tr').mouseout(function(event) {
		$(this).css('background-color', 'white');
	});

	//异步获取数据
	$('.details').click(function(){
		details(this);
	});

	// 查看大图
	$('.photo').click(function(event) {
		createPicShow();
	});
	
	
	//分页
	$('.go_page').click(function(event) {
		var first = ChangePage.getFirst($(this));
		$.getJSON('/Express/admin/OrderManage_finshOrder', {first:first,'times':'nofirst'}, function(data){
			$('.finish_order_firstTable tbody').empty();
			for(var i = 0;i<data.length;i++){
				
				var requestDate = new Date(data[i].requestDate);
				
					var html = 
					"<tr>" +
						"<td class='orderId'>" + data[i].orderId + "</td>" +
						"<td class='orderFare'>" + data[i].orderFare + "</td>" +
						"<td class='goodsDetail'>" + data[i].goodsDetail + "</td>" +
						"<td class='requestDate'>" +requestDate.Format("yyyy-MM-dd hh:mm:ss") + "</td>" +
						'<td><button class="btn ok details">详细资料</button></td>' +
					"</tr>";
					$('.finish_order_firstTable tbody').append(html); 
			}
			ChangePage.start();
			$('.user_table tbody tr').mousemove(function(event) {
				tr_mousemove($(this));
			});

			$('.user_table tbody tr').mouseout(function(event) {
				tr_mouseout($(this));
			});
			
			$('.details').click(function(){
				details(this);
			});
			
			$('.btn').click(function(event) {
				$('.show').click();
			});
		});
		
	});
	
	//根据时间查找订单
	$(".timeselect").click(function(){
		timeorder(0);
	});
})