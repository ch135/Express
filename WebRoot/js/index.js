/*
* @Author: WG
* @Date:   2016-09-20 21:52:17
* @Last Modified by:   WG
* @Last Modified time: 2016-09-20 21:58:49
*/

'use strict';
var _checkData = {
	city : '惠州市',
	area : '惠城区',
	year : ''
}
//加载用户地域统计表格
/*
 * @param : _DYdata : 各地区的相关数据
 */
 function setYHDYTJ(_DYdata){
   $('.user_hometown_table').highcharts({
        chart: {
            type: 'column'
        },
        title: {
            text: '注册用户地域分布统计表'
        },
        subtitle: {
            text: '当前城市:' + _checkData.city
        },
        xAxis: {
            categories:_DYdata.area,
            title: {
                text:_checkData.city
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: '注册用户数量（人）',
                align: 'high'
            },
            labels: {
                overflow: 'justify'
            }
        },
        tooltip: {
            valueSuffix: '人'
        },
        plotOptions: {
            bar: {
                dataLabels: {
                    enabled: true
                }
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -40,
            y: 100,
            floating: true,
            borderWidth: 1,
            backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
            shadow: true
        },
        credits: {
            enabled: false
        },
        series: [{
            name: '人数',
            data:_DYdata.num
        }]
    }); 
 }
 
 //加载交易量统计图
 function setJYLTJ(_JYdata){
	 Highcharts.chart('jyje_table', {
		    chart: {
		        type: 'line'
		    },
		    title: {
		        text: _checkData.area +'交易量统计'
		    },
		    subtitle: {
		        text: _checkData.year + "年"
		    },
		    xAxis: {
		        categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
		    },
		    yAxis: {
		        title: {
		            text: '交易量(k)'
		        }
		    },
		    plotOptions: {
		        line: {
		            dataLabels: {
		                enabled: true
		            },
		            enableMouseTracking: false
		        }
		    },
		    series: [{
		        name: _checkData.area,
		        data: _JYdata
		    }]
		});	 
 }
 //异步请求用户地域统计图表数据
 function getYHDYTJData(city){
		$.ajax({
			url: '/Express/admin/Admin_getAreaNum',
			type: 'POST',
			dataType: 'json',
			data: {cityName : city},
		})
		.done(function(data) {
			var _DYdata = {
				area:[],
				num:[]
			};
			for(var i in data){
				_DYdata.area.push(i);
				_DYdata.num.push(data[i]);
			}
			setYHDYTJ(_DYdata);
		  $("#area-select").empty();
		  $("#area-select").append("<option value='Value'>选择地区</option>");
			for(var i = 0;i < _DYdata.area.length;i++){
				$("#area-select").append("<option value='Value'>" + _DYdata.area[i] + "</option>");  
			}
		})
		.fail(function() {
			alert("出错了:用户地域统计图表无法加载");
		})
		.always(function() {
			console.log("complete");
		});	 
 }
 //异步请求交易量统计图表数据
 function getJYLTJData(_JYLData){
		$.ajax({
			url: '/Express/admin/Admin_getOrderCount',
			type: 'POST',
			dataType: 'json',
			data: _JYLData,
		})
		.done(function(data) {
			var _JYdata = [];
			for(var i in data){
				_JYdata.push(data[i]);
			}
			setJYLTJ(_JYdata);
		})
		.fail(function() {
			alert("出错了:交易量图表数据无法加载")
		})
		.always(function() {
			console.log("complete");
		});		 
 }
$(function(){
	//第一次加载获取数据
	getYHDYTJData("惠州");
	var tody = new Date();
	_checkData.year = tody.getFullYear();
	getJYLTJData({'area':_checkData.area,'year':_checkData.year})
	for(var i = 0;i <= 82;i++){
		var year = i + 2017;
		$("#year-select").append("<option value='Value'>" + year + "</option>");  
	}
	
	//选择城市加载用户地域统计图表数据
	$('body').on('change','#city-select',function(){
		var city = $("#city-select").find("option:selected").text();
		if(city != '选择城市' || city != ''){
			_checkData.city = city;
			getYHDYTJData(_checkData.city);	
		}
	})
	
	//选择年份加载交易量统计数据
	$('body').on('click','#JYTJbtn',function(){
		var area = $("#area-select").find("option:selected").text();
		var year = $("#year-select").find("option:selected").text();
		if( area == '选择地区' || area == ''){
			alert('请先选择县区！');
			return false;
		}
		if(year == '选择年份' || year == ''){
			alert("请先选择年份！");
			return false;
		}
		var date = new Date();
		if( year > date.getFullYear()){
			alert("所选年份尚未开始!");
			return false;
		}
		_checkData.area = area;
		_checkData.year = year;
		getJYLTJData({'area':area,'year':year})
	});
});
