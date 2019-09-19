		$(document).ready(function(){
			$(".check_button").click(function(){
				var ID = $(this).parent().prev().prev().prev().prev().html();
				$.ajax({  
				        type : "post",  
				        url : "register/RegisterState",  
				        data : ID,  
				        async : true,  
				        success : function(data){  
				        	if(data == 0){
				        		$("#check_Message").html("信息格式不正确！");
				        	}else if(data == 1){
				        		$("#check_Message").html("信息格式正确！");
				        	}else{
				        		$("#check_Message").html("出错了！请稍后再试！");
				        	}
							$("#check_result").modal();					 
				    	}  
			    }); 
			})


			$(".pass").click(function(){
				var ID = $(this).parent().prev().prev().prev().prev().html();
				$.ajax({  
				        type : "post",  
				        url : "register/RegisterState",  
				        data : ID,  
				        async : true,  
				        success : function(data){  
							if(data == 1){
								$(this).parent().next().html("已通过");
							}				 
				    	}  
			    }); 				
			});


			$(".no_pass").click(function(){
				var ID = $(this).parent().prev().prev().prev().prev().html();
				$.ajax({  
				        type : "post",  
				        url : "register/RegisterState",  
				        data : ID,  
				        async : true,  
				        success : function(data){  
							if(data == 1){
								$(this).parent().next().html("未通过");
							}				 
				    	}  
			    }); 				
			});
		});