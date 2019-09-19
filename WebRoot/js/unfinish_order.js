/*
* @Author: WG
* @Date:   2016-10-13 21:45:30
* @Last Modified by:   WG
* @Last Modified time: 2016-10-13 22:16:21
*/
	//异步加载订单详情
	function details(that){
		var orderId =  $(that).parent().parent().find('.orderId').text();
		$.getJSON('/Express/admin/OrderManage_findOrder', {orderId:orderId}, function(data){
			//订单状态
			var status = '';
			switch(data.orderStaus){
				case 0: status = "未支付";break;
				case 1: status = "未接单";break;
				case 2: status = "已接单";break;
				case 3: status = "派送中";break;
				case -1: status = "用户已取消";break;
				case -2: status = "快递员取消";break;
				case -3: status = "订单过期自动取消";break;
				case -4: status = "管理员关闭订单";break;
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
				"<span class='basic_message_title'>订单状态: </span>" +
				"<span>" +  status  + "</span>" +
			"</div>" ;
			if(data.cname != "null" && data.cname != null){
				html +=
				'<span class="div_title">快递员信息</span>' +
				'<div class="basic_message">' +
					"<span class='unfinish_order_title'>姓名: </span>" +
					'<span>' + data.cname +'</span>' +
					"<span class='unfinish_order_title'>手机号: </span>" +
					"<span>" + data.cmobile + "</span>" +
				'</div>';
			}
			html +=
			'<span class="div_title">寄件人信息</span>' +
			'<div class="basic_message">' +
				"<span class='unfinish_order_title'>姓名: </span>" +
				'<span>' + data.sendName + '</span>' +
				"<span class='unfinish_order_title'>手机号: </span>" +
				"<span>" + data.sendMobile + "</span>" +
			"</div>" +
			'<div class="basic_message">' +
				"<span class='unfinish_order_title'>地址: </span>" +
				"<span>" + data.sendAddress + "</span>" +	
			" </div> " +
			'<span class="div_title">收件人信息</span>' +
			'<div class="basic_message">' +
				"<span class='unfinish_order_title'>姓名: </span>" +
				"<span>" + data.receiveName + "</span>" +
				"<span class='unfinish_order_title'>手机号: </span>" +
				"<span>" + data.receiveMobile + "</span>" +
			"</div>" +
			'<div class="basic_message">' +
				"<span class='unfinish_order_title'>地址: </span>" +
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
			$('.show').click();
			$('.photo').click(function(event) {
				createPicShow(this);
			});
		});
	}
	
	//方法绑定
	function resetFun(){
		// 表格样式设置
		$('.user_table tbody tr').mousemove(function(event) {
			$(this).css('background-color', '#FBFBFB');
		});

		$('.user_table tbody tr').mouseout(function(event) {
			$(this).css('background-color', 'white');
		});
		
		//异步获取数据
		$('body').on('click','.details',function(){
			details(this);
		});


		// 查看大图
		$('body').on('click','.photo',function(){
			createPicShow();
		});	
	}

$(function(){
	$("#tabs").tabs();

	resetFun();
	//分页
	$('.go_page').click(function(event) {
		var first = ChangePage.getFirst($(this));
		$.getJSON('/Express/admin/OrderManage_unfinshOrder', {first:first}, function(data){
			$('.unfinish_order_firstTable tbody').empty();
			for(var i = 0;i<data.length;i++){
					var requestDate = new Date(data[i].requestDate);
					var html = 
					"<tr>" +
						"<td class='orderId'>" + data[i].orderId + "</td>" +
						"<td class='orderFare'>" + data[i].orderFare + "</td>" +
						"<td class='goodsDetail'>" + data[i].goodsDetail + "</td>" +
						"<td class='requestDate'>" + requestDate.Format("yyyy-MM-dd hh:mm:ss") + "</td>" +
						'<td><button class="btn ok details">详细资料</button></td>' +
					"</tr>";
					$('.unfinish_order_firstTable tbody').append(html); 
			}
			ChangePage.start();
		});
		
	});

})