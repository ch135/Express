//申请审核
$(function(){
	var close = function(){
		$(".background").remove();
	};
	

	$( "#tabs" ).tabs();


	// 表格初始化
	$("table").tablesorter({ 
        sortList: [[0,0],[2,0]],
        headers:{
        	0:{sorter:false},
        	1:{sorter:false}
        }
	}); 



	// 表格排序图标转换
    $("table thead tr th.canSort").click(function(event) {
    	var Table = $(this).parent().parent();
    	var Img = $(this).find('img');
    	var name = Img.attr('name');

    	Table.find('img').each(function() {
    		$(this).attr('src', 'img/sort.png');
    		$(this).attr('name', '0');
    	});

    	if(name == "0"){
    		Img.attr('src', 'img/sortUp.png');
    		Img.attr('name', '1');
    	}
    	else if(name == "1"){
    		Img.attr('src', 'img/sortDown.png');
    		Img.attr('name', '2');	    
    	}else{
    		Img.attr('src', 'img/sortUp.png');
    		Img.attr('name', '1');		    		
    	}
    });

    //未审批点击下一页
	$('#fragment-3 .go_page').click(function(event) {
		var first = ChangePage.getFirst($(this));
		$.getJSON('/Express/admin/UserManage_getAllUserIdentity2', {first:first,type:'unfinsh'}, function(data){
			$('.unapproved_table tbody').empty();
			for(var i = 0;i<data.length;i++){
					var html = 
					'<tr><td class="path"><img src="' + data[i].path +'" alt=""></td><td class="name>' + data[i].name + '</td><td class="sex">' + data[i].sex + '</td><td class="identity">' + data[i].identity +'</td><td><button class="btn ok detail">详细资料</button></td></tr>'; 
					$('.unapproved_table tbody').append(html); 
			}
			changePage.start();
		});
		$('.unapproved_table button.btn').click(function(event) {
			$('.show').click();
		});
	});
	
	//已审批点击下一页
	$('#fragment-2 .go_page').click(function(event) {
		var first = ChangePage.getFirst($(this));
		$.getJSON('/Express/admin/UserManage_getAllUserIdentity2', {first:first,type:'finish'}, function(data){
			$('.approved_table tbody').empty();
			for(var i = 0;i<data.length;i++){
					var html = 
					'<tr><td class = "icon"><img src="' + data[i].icon +'" alt=""></td><td class="name" name="'+ data[i].identity +'">' + data[i].name + '</td><td class="sex">' + data[i].sex + '</td><td class="mobile">' + data[i].mobile +'</td><td class="shenpi">'+ (data[i].results =='yes'?'通过':'不通过')+'</td><td class="caozuo"><button class="btn ok">详细资料</button></td></tr>'; 
					$('.approved_table tbody').append(html); 
			}
			ChangePage.start();
			
			$('.approved_table button.btn').click(function(event) {
				$('.show').click();
			});
		});
	});
	
	
	//动态创建表单提交审核结果
	function Form(action,method,argument){
		var form = $('<form></form>');
		form.attr('action',action);
		form.attr("method",method);
		form.css('display', 'none');
		for(var i=0;i< argument.length;i++){
			var input = $('<input type="text" name=' + argument[i].name +' />');
			input.val(argument[i].value);
			form.append(input);
		}
		$('body').append(form);
		return form;
	}
	
	//未审批点击详细资料
	$('body').on('click','.unapproved_table .btn',function(){
		var identity = $(this).parent().parent().find('.identity').text();
		var id = $(this).parent().parent().attr("id");
		$.getJSON('/Express/admin/UserManage_getIdentity', {identity:identity}, function(data){
			console.log(data);
			var JC = {
				className:"",
				content:""
			};
			if(data.results == 1){
				JC.classname = "ok";
				JC.context = "检测通过";
			}else{
				JC.classname = "error";
				JC.context = "检测不通过";			
			}
			
			$('.information_box').empty();
			var table = '<table class="information" cellspacing = "0"></table>';
			$('.information_box').append(table);
			var html = '<tr><th>姓名</th><td>' + data.idCard.name + '</td></tr><tr><th>手机号</th><td>' + data.idCard.mobile + '</td></tr><tr><th>身份证号</th><td class="identity_td">' + data.idCard.identity + '<button class="' + JC.classname + ' check_identity">' + JC.context + '</button>' + '</td></tr><tr><th>身份证照</th><td><img src="' + data.idCard.path1 + '" alt="" title="查看大图" class="photo"><img src="'+ data.idCard.path2 + '" alt="" title="查看大图" class="photo"><img src="' + data.idCard.path3 + '" alt="" title="查看大图" class="photo"></td></tr><tr class="results_tr"><th>审批</th><td><button class="results_agree_btn ok">同意</button> <button class="results_refuse_btn error">拒绝</button></td></tr>';   
			$('.information').append(html);
			$(".show").click();
			$('.photo').click(function(event) {
				createPicShow(this);
			});
			//审核结果点击事件
			$('.information .results_agree_btn').click(function(){
				function pass(){
					var form = Form("/Express/admin/UserManage_pass","post",[{name:"flag",value:"pass"},{name:"reason",value:"通过"},{name:"id",value:id}]);
					form.submit();					
				}
				var prompt = new _Prompt("确认通过审核？",pass);
				prompt.show_content();
			});
			//审核不通过点击事件
			$('.information .results_refuse_btn').click(function(){
				$('.results_tr').attr("hidden","hidden");
				var html = "<tr class='reason_tr'><th>拒绝理由</th><td><input type='text' name='reason' class='refuse_reason_input'/><button class='ok results_refuse'>提交</button><button class='error cansle'>取消</button></td></tr>";
				$('.information').append(html);
				//确认拒绝
				$('.results_refuse').click(function(){
					function noPass(){
						var reason = $('.refuse_reason_input').val();
						var form = Form("/Express/admin/UserManage_pass","post",[{name:"flag",value:"不同意"},{name:"reason",value:reason},{name:"id",value:id}]);
						form.submit();					
					}
					if($('.refuse_reason_input').val() == ""){
						var prompt = new _Prompt("请先填写拒绝理由",close);
						prompt.show_content();						
					}
					else{
						var prompt = new _Prompt("确认拒绝审核？",noPass);
						prompt.show_content();					
					}
				});
				//取消
				$('.cansle').click(function(){
					$('.reason_tr').remove();
					$('.results_tr').removeAttr("hidden");
				});
			});	
		});		
	});
	
	
	//已审批点击详细资料
	$('body').on('click','.approved_table .btn',function(){
		var identity = $(this).parent().parent().find('.name').attr('name');
		$.getJSON('/Express/admin/UserManage_getIdentity', {identity:identity}, function(data){
			console.log(data);
			var results;
			if(data.idCard.results == 'yes')
				results = '通过';
			else
				results = '不通过';
			$('.information_box').empty();
			var table = '<table class="information" cellspacing = "0"></table>';
			$('.information_box').append(table);
			var html = '<tr><th>姓名</th><td>' 
				+ data.idCard.name + '</td></tr><tr><th>性别</th><td>' 
				+ data.idCard.sex + '</td></tr><tr><th>手机号</th><td>' 
				+ data.idCard.mobile + '</td></tr><tr><th>身份证号</th><td>' 
				+ data.idCard.identity + '</td></tr><tr><th>身份证照</th><td><img src="' 
				+ data.idCard.path1 + '" alt="" title="查看大图" class="photo"><img src="'
				+ data.idCard.path2 + '" alt="" title="查看大图" class="photo"><img src="' 
				+ data.idCard.path3 + '" alt="" title="查看大图" class="photo"></td></tr>'
				+ '<tr><th>审批结果</th><td>' + results; 
			if(results == '不通过')
				html += '<tr><th>拒绝理由</th><td>' +  data.idCard.reason + '</td></tr>';
			$('.information').append(html);
			$('.photo').click(function(event) {
				createPicShow(this);
			});
			$(".show").click();
		});		
	});
	
	
	
	//未审批点击详细资料
//	$('.unapproved_table .btn').click(function(){
//		var identity = $(this).parent().parent().find('.identity').text();
//		var id = $(this).parent().parent().attr("id");
//		$.getJSON('/Express/admin/UserManage_getIdentity', {identity:identity}, function(data){
//			var JC = {
//				className:"",
//				content:""
//			};
//			if(data.results == 1){
//				JC.classname = "ok";
//				JC.context = "检测通过";
//			}else{
//				JC.classname = "error";
//				JC.context = "检测不通过";			
//			}
//			
//			$('.information_box').empty();
//			var table = '<table class="information" cellspacing = "0"></table>';
//			$('.information_box').append(table);
//			var html = '<tr><th>姓名</th><td>' + data.idCard.name + '</td></tr><tr><th>手机号</th><td>' + data.idCard.mobile + '</td></tr><tr><th>身份证号</th><td class="identity_td">' + data.idCard.identity + '<button class="' + JC.classname + ' check_identity">' + JC.context + '</button>' + '</td></tr><tr><th>身份证照</th><td><img src="' + data.idCard.path1 + '" alt="" title="查看大图" class="photo"><img src="'+ data.idCard.path2 + '" alt="" title="查看大图" class="photo"><img src="' + data.idCard.path3 + '" alt="" title="查看大图" class="photo"></td></tr><tr class="results_tr"><th>审批</th><td><button class="results_agree_btn ok">同意</button> <button class="results_refuse_btn error">拒绝</button></td></tr>';   
//			$('.information').append(html);
//			$('.photo').click(function(event) {
//				createPicShow(this);
//			});
//			//审核结果点击事件
//			$('.information .results_agree_btn').click(function(){
//				function pass(){
//					var form = Form("/Express/admin/UserManage_pass","post",[{name:"flag",value:"pass"},{name:"reason",value:"通过"},{name:"id",value:id}]);
//					form.submit();					
//				}
//				var prompt = new _Prompt("确认通过审核？",pass);
//				prompt.show_content();
//			});
//			//审核不通过点击事件
//			$('.information .results_refuse_btn').click(function(){
//				$('.results_tr').attr("hidden","hidden");
//				var html = "<tr class='reason_tr'><th>拒绝理由</th><td><input type='text' name='reason' class='refuse_reason_input'/><button class='ok results_refuse'>提交</button><button class='error cansle'>取消</button></td></tr>";
//				$('.information').append(html);
//				//确认拒绝
//				$('.results_refuse').click(function(){
//					function noPass(){
//						var reason = $('.refuse_reason_input').val();
//						var form = Form("/Express/admin/UserManage_pass","post",[{name:"flag",value:"不同意"},{name:"reason",value:reason},{name:"id",value:id}]);
//						form.submit();					
//					}
//					if($('.refuse_reason_input').val() == ""){
//						var prompt = new _Prompt("请先填写拒绝理由",close);
//						prompt.show_content();						
//					}
//					else{
//						var prompt = new _Prompt("确认拒绝审核？",noPass);
//						prompt.show_content();					
//					}
//				});
//				//取消
//				$('.cansle').click(function(){
//					$('.reason_tr').remove();
//					$('.results_tr').removeAttr("hidden");
//				});
//			});	
//		});
//	});
	
	//已审批点击详细资料
//	$('.approved_table button.btn').click(function(){
//		var identity = $(this).parent().parent().find('.name').attr('name');
//		$.getJSON('/Express/admin/UserManage_getIdentity', {identity:identity}, function(data){
//			var results;
//			if(data.idCard.results == 'yes')
//				results = '通过';
//			else
//				results = '不通过';
//			$('.information_box').empty();
//			var table = '<table class="information" cellspacing = "0"></table>';
//			$('.information_box').append(table);
//			var html = '<tr><th>姓名</th><td>' 
//				+ data.idCard.name + '</td></tr><tr><th>性别</th><td>' 
//				+ data.idCard.sex + '</td></tr><tr><th>手机号</th><td>' 
//				+ data.idCard.mobile + '</td></tr><tr><th>身份证号</th><td>' 
//				+ data.idCard.identity + '</td></tr><tr><th>身份证照</th><td><img src="' 
//				+ data.idCard.path1 + '" alt="" title="查看大图" class="photo"><img src="'
//				+ data.idCard.path2 + '" alt="" title="查看大图" class="photo"><img src="' 
//				+ data.idCard.path3 + '" alt="" title="查看大图" class="photo"></td></tr>'
//				+ '<tr><th>审批结果</th><td>' + results; 
//			if(results == '不通过')
//				html += '<tr><th>拒绝理由</th><td>' +  data.idCard.reason + '</td></tr>';
//			$('.information').append(html);
//			$('.photo').click(function(event) {
//				createPicShow(this);
//			});
//		});
//	});
	
});
	
