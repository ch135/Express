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
		});
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
		var first = changePage($(this));
		$.getJSON('/Express/admin/OrderManage_finshOrder', {first:first}, function(data){
			$('.finish_order_firstTable tbody').empty();
			for(var i = 0;i<data.length;i++){
					var html = 
					"<tr>" +
						"<td class='orderId'>" + data[i].orderId + "</td>" +
						"<td class='orderFare'>" + data[i].orderFare + "</td>" +
						"<td class='goodsDetail'>" + data[i].goodsDetail + "</td>" +
						"<td class='requestDate'>" + data[i].requestDate + "</td>" +
						'<td><button class="btn ok details">详细资料</button></td>' +
					"</tr>";
					$('.finish_order_firstTable tbody').append(html); 
			}
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
})