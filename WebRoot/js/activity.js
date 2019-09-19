var cityData = [];
function submit(url,data,successfun){
	$.ajax({
        cache: true,
        type: "POST",
        url:url,
        data:data,
        dataType : "json",
        async: false,
        error: function() {
			var prompt = new _Prompt("出错了！请重试！",flash);
			prompt.show_content();  
        },
        success: successfun
    });
}
//select选中城市查询起步价
function SelectCityPrice(name){
	var i = 0
	for(;i<cityData.length;i++){
		if(cityData[i].name == name)
			return cityData[i].value;
	}
	return 0;
}
//修改起步价
function changeCityPrice(data){
	$.ajax({
        type: "POST",
        dataType : 'json',
        url:"/Express/admin/UserManage_setAreaPrice",
        data:data,
        error: function(request) {
			var prompt = new _Prompt("出错了！请重试！",flash);
			prompt.show_content();  
        },
        success: function(data) {
        	var result0=data.result0;
        	var result1=data.result1;
        	var result2=data.result2;
        	if(result0 == "success" && result1 == "success" && result2 == "success"){
				var prompt = new _Prompt("修改成功！",flash);
				prompt.show_content();  
        	}else{
				var prompt = new _Prompt("修改出错了,请重试!",flash);
				prompt.show_content();         		
        	}
        }
    });	
}
function close(){
	$('.background').remove();
}
$(function(){
	$('.city-select').change(function(){
		var name = $(this).val();
		var value = SelectCityPrice(name);
		$('.city-value').text('当前起步价: ' + value + '元');
	});
	
	//更改起步价
	$('body').on('click','.changeCityValue-btn',function(){
		var cityName = $("#city-select").find("option:selected").text();
		var areaName = $("#area-select").find("option:selected").text();
		var times=new Array();
		var values=new Array();
		var name="";
		$(".time").each(function(key,value){
			var value=$(this).val();
			if(value==""){
				var prompt = new _Prompt("请正确填写表单",closed);
				prompt.show_content(); 	
				return false;
			}else{
				var num=value.split(':').length;
				if(num==2){
					if(key==0||key==5){
						times[key]=value+":00";
					}else{
						times[key]=value+":30";
					}
				}else{
					times[key]=value;
				}
			}
		});
		$(".price").each(function(key,value){
			var value=$(this).val();
			if(value==""){
				var prompt = new _Prompt("请正确填写表单",closed);
				prompt.show_content(); 	
				return false;
			}else{
				var res=value.split('元');
				value=res[0];
				values[key]=value;
			}
		});
		if(cityName!="选择城市"){
			if(areaName=="选择地区"){
				name=cityName+"市";
			}else{
				name=areaName;
			}
		}
		var data = {
			'name' : name,
			'start_time1':times[0],
			'end_time1' : times[1],
			'value1' : values[0],
			'start_time2':times[2],
			'end_time2' : times[3],
			'value2' : values[1],
			'start_time3':times[4],
			'end_time3' : times[5],
			'value3' : values[2]
		};
		changeCityPrice(data);
	});
	
	
	//选择城市查看起步价
	$('body').on('change',"#city-select",function(){
		var cityName = $("#city-select").find("option:selected").text();
	    $.ajax({
	        url: '/Express/admin/UserManage_getAreaList',
	        type: 'POST',
	        dataType: 'json',
	        data: {cityName: cityName},
	      })
	      .done(function(data) {
	    	  	var areaList = data.list;
	    	  	var result=JSON.parse(data.value);
				$("#area-select").empty();
				$("#area-select").append("<option value='Value'>选择地区</option>");
				for(var i = 0;i < areaList.length;i++){
					$("#area-select").append("<option value='Value'>" + areaList[i] + "</option>");  
				}
				$(".time").each(function(key,value){
					var time='time'+key;
					$(this).val(result[time]);
				});
				$(".price").each(function(key,value){
					var value='value'+key;
					$(this).val(result[value]+'元');
				});
	      })
	      .fail(function() {
	        console.log("出问题了！");
	      })		
	});
	
	//选择地区查看起步价
	$('body').on('change',"#area-select",function(){
		var areaName = $("#area-select").find("option:selected").text();
	    $.ajax({
	        url: '/Express/admin/UserManage_getAreaPrice',
	        type: 'POST',
	        dataType: 'json',
	        data: {areaName: areaName},
	      })
	      .done(function(data) {
	    	  var result=JSON.parse(data.value);
	    	  $(".time").each(function(key,value){
					var time='time'+key;
					$(this).val(result[time]);
				});
				$(".price").each(function(key,value){
					var value='value'+key;
					$(this).val(result[value]+'元');
				});
	      })
	      .fail(function() {
	        console.log("出问题了！");
	      })		
	});
	
	$('body').on('click','.coupon-btn',function(){
		if($(this).hasClass('registerCoupon-btn'))
			submit('/Express/admin/Admin_changeCouponValue',$('.registerCoupon-form').serialize(),function(data){
				if(data.result == 'valueError'){
					var prompt = new _Prompt("请输入正确的数值！",flash);
					prompt.show_content(); 
				}else if(data.result == 'setError'){
					var prompt = new _Prompt("参数错误！",flash);
					prompt.show_content(); 				
				}else{
					var prompt = new _Prompt("设置成功！",flash);
					prompt.show_content(); 					
				}	
			});
		else if($(this).hasClass('awardCoupon-btn'))
			submit('/Express/admin/Admin_changeCouponValue',$('.awardCoupon-form').serialize(),function(data){
				if(data.result == 'valueError'){
					var prompt = new _Prompt("请输入正确的数值！",flash);
					prompt.show_content(); 
				}else if(data.result == 'setError'){
					var prompt = new _Prompt("参数错误！",flash);
					prompt.show_content(); 				
				}else{
					var prompt = new _Prompt("设置成功！",flash);
					prompt.show_content(); 					
				}	
			});
		else
			submit('/Express/admin/Admin_changeCouponValue',$('.toll-form').serialize(),function(data){
				if(data.result == 'valueError'){
					var prompt = new _Prompt("请输入正确的数值！",flash);
					prompt.show_content(); 
				}else if(data.result == 'setError'){
					var prompt = new _Prompt("参数错误！",flash);
					prompt.show_content(); 				
				}else{
					var prompt = new _Prompt("设置成功！",flash);
					prompt.show_content(); 					
				}				
			});
	});
	
	//图片上传
	$('.hongbao_upload').click(function(){
		if($('.add_pic').val() == "" || $('.hbTitle').val() == "" || $('.hbContext').val() == ""){
			var prompt = new _Prompt("请先填写完整表单！",close);
			prompt.show_content();	
			return false;
		}
		else{
			var formData_Pic = new FormData($('.upload_hongbao_form')[0]);
			console.log(formData_Pic);
			$.ajax({
			    url: "/Express/admin/Upload_changeHB",
			    type: "post",
			    cache: true,
			    data:formData_Pic,
		        contentType: false,
		        processData: false,
		        dataType:"json",
		        success:function(data){
		        	var reflush = function(){
		        		window.location.reload();
		        	};
		        	if(data.status == "success"){
						var prompt = new _Prompt("上传成功！",reflush);
						prompt.show_content();					        		
		        	}else if(data.status == "fail"){
						var prompt = new _Prompt("出错了，请刷新重试！",reflush);
						prompt.show_content();						        		
		        	}

			    },
			    error:function(){
					var prompt = new _Prompt("出错了，请刷新重试！",close);
					prompt.show_content();
			    	return false;
			    }
			});	
		}
	});
	
	$.imageFileVisible = function(options) {     

		var defaults = {    
			wrapSelector: null,    
			fileSelector:  null ,
			width : '100%',
			height: 'auto',
			errorMessage: "抱歉,您选择的不是图片！"
		};      
		var opts = $.extend(defaults, options);     
		$(opts.fileSelector).on("change",function(){
			var file = this.files[0];
			var imageType = /image.*/;
			if (file.type.match(imageType)) {
				var reader = new FileReader();
				reader.onload = function(){
					var img = new Image();
					img.src = reader.result;
					$(img).css('border', '1px solid #44B549');
					$(img).width( opts.width);
					$(img).height( opts.height);
					$(opts.wrapSelector).empty();
					$(opts.wrapSelector).css('border','none');
					$(opts.wrapSelector).append(img);	
				};
				reader.readAsDataURL(file);
			}else{
				alert(defaults.errorMessage);
			}
		});
	};


	//图片显示插件
	$.imageFileVisible({
		wrapSelector: ".add_pic_div",   
		fileSelector: ".add_pic",
		width: 140,
		height:240
	});   

	$('.add_pic_div').click(function(){
		$('.add_pic').val("");
		$('.add_pic').click();
	})
	
	//更改附加价
	$('body').on('click','.changeAddtion-btn',function(){
		var data={};
		var startDate="";
		var endDate="";
		var price="";
//		$(".startDate").each(function(key, value) {
//			startDate+=$(this).val()+",";
//		});
//		data.startDate=startDate.substring(0,startDate.length-1);
//		$(".endDate").each(function(key, value) {
//			endDate+=$(this).val()+",";
//		});
//		data.endDate=endDate.substring(0,endDate.length-1);
		
//		$(".addtion_price").each(function(key, value) {
//			price+=$(this).val()+",";
//		});
//		data.addtion=price.substring(0,price.length-1);
		var pattern=/[0-9]{4}-[0-9]+-[0-9]+/;
		for(var i=0;i<3;i++){
			var start_date=$(".startDate").eq(i).val();
			var end_date=$(".endDate").eq(i).val();
			var addtion_price=$(".addtion_price").eq(i).val();
			if(start_date!=''||end_date!=''){
				if(!pattern.test(start_date)||!pattern.test(end_date)){
					var prompt = new _Prompt("请填写日期！",close);
					prompt.show_content();
					return;
				}else if(isNaN(addtion_price)){
					var prompt = new _Prompt("请填写附加价！",close);
					prompt.show_content();
					return;
				}else if(addtion_price<0){
					var prompt = new _Prompt("附加价需大于0！",close);
					prompt.show_content();
					return;
				}
			}
			startDate+=start_date+",";
			data.startDate=startDate.substring(0,startDate.length-1);
			endDate+=end_date+",";
			data.endDate=endDate.substring(0,endDate.length-1);
			price+=addtion_price+",";
			data.addtion=price.substring(0,price.length-1);
		}
		$.ajax({
	        type: "POST",
	        dataType : 'json',
	        url:"/Express/admin/Admin_updateAddtion",
	        data:data,
	        error: function(request) {
	        	console.log(request);
				var prompt = new _Prompt("出错了！请重试！",flash);
				prompt.show_content();  
	        },
	        success: function(data) {
	        	if(data.result == "success"){
					var prompt = new _Prompt("修改成功！",flash);
					prompt.show_content();  
	        	}else{
					var prompt = new _Prompt("修改出错了,请重试!",flash);
					prompt.show_content();         		
	        	}
	        }
	    });	
	});
	
	$(".time").hunterTimePicker();
	
});