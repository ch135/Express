		(function($) {  
			var close = function(){
				$('.background').remove();
			};

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

			$('.add_file_div').click(function(){
				$('.upload_apk').val("");
				$('.upload_apk').click();
			})

			$('.upload_apk').on("change",function(){
				var name = $('.upload_apk').val();
				var check = name.split(".")[(name.split(".").length-1)];
				if(check != 'apk'){
					var prompt = new _Prompt("抱歉，您选择的不是APK格式文件！",close);	
					prompt.show_content();
					$('.add_file_div').css("border","2px dashed #a9b7b7");
					$('.add_file_div i').css("color","#a9b7b7");
					$('.apk_upload_btn').fadeOut('200');
					$('.upload_apk_name').val("");
				}
				else{
					$('.add_file_div').css("border","2px solid #44B549");
					$('.add_file_div i').css("color","#44B549");
					$('.upload_apk_name').text(name);
					$('.apk_upload_btn').fadeIn('200');
				}
			});
			
			 function onprogress(evt){
				  var loaded = evt.loaded;     //已经上传大小情况 
				  var tot = evt.total;        //附件总大小 
				  var per = Math.floor(100*loaded/tot);  //已经上传的百分比 
				  $(".son").css("width" , per +"%");
			}
			 

			//安装包上传
			$('.apk_upload_btn').click(function(){
				var operator = $('.name').text().split("\n")[0];
				$('.operator').val(operator);
				if($('.select_appType').val()!= 1 && $('.select_appType').val()!= 0){
					var prompt = new _Prompt("请选择APP推送对象！",close);
					prompt.show_content();
					return false;
				}else if($('.input_appVersion').val() == '' || $('.input_appVersion').val() == null){
					var prompt = new _Prompt("请填写APP更新包版本号！",close);	
					prompt.show_content();
					return false;
				}else{
					var formData = new FormData($('.upload_apk_form')[0]);
						$.ajax({
						    url: "/Express/admin/Upload_updateApp",
						    type: "post",
						    cache: true,
						    data:formData,
					        contentType: false,
					        processData: false,
					        dataType:"json",
					        xhr: function(){
					            var xhr = $.ajaxSettings.xhr();
					            if(onprogress && xhr.upload) {
					            	$('.show_son').fadeIn('fast');
					            	xhr.upload.addEventListener("progress" , onprogress, false);
					            	return xhr;
					            }
					         },
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
								var prompt = new _Prompt("抱歉上传过程出现了一点问题,请稍后再试！",close);
								prompt.show_content();
						    }
						});
					}
			});
			
			//图片上传
			$('.pic_upload').click(function(){
				if($('.add_pic').val() == ""){
					var prompt = new _Prompt("请先选择图片！",close);
					prompt.show_content();	
					return false;
				}
				else{
					var formData_Pic = new FormData($('.upload_pic_form')[0]);
					$.ajax({
					    url: "/Express/admin/Upload_uploadPic",
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
		})(jQuery); 
