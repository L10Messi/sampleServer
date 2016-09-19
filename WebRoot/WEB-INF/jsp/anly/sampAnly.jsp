<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>E charts Analyze</title>
<jsp:include page="../inc/incJs.jsp"></jsp:include>
<script type="text/javascript"
	src="<%=basePath%>/js/echarts/echarts-all.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/echarts/echarts.js"></script>
<script type="text/javascript">
$(function(){
	$('#ProductID').combobox({
		url : '<%=basePath%>/sampinspected/getProductName.do',
		editable:false, //不可编辑状态  
        cache: false,  
        panelHeight: '150',
		valueField:'id',
		textField:'text',
		onHidePanel: function(){  
			$("#QualChrtID").combobox("setValue",'');//清空文本框 
			$("#ClientID").combobox("setValue",'');
            var record = $('#ProductID').combobox('getValue');
            $.ajax({
		        type : "POST",
		        url : '<%=basePath%>/sampinspected/getQualChrtName.do?ProductID='+record,
		        cache: false,  
	            dataType : "json", 
		        success : function(data) {
		        	$("#QualChrtID").combobox("loadData",data);
		        } 
		   }); 
		}
		});
	
	$('#QualChrtID').combobox({  
		url : '<%=basePath%>/sampinspected/getQualChrtName.do?ProductID= ',
        editable:false, //不可编辑状态  
        cache: false,  
        panelHeight: '150',//自动高度适合  
        valueField:'id',     
        textField:'text',
        onHidePanel: function(){  
            var record = $('#QualChrtID').combobox('getValue');
            $.ajax({
		        type : "POST",
		        url : '<%=basePath%>/sampinspected/getClientID.do?QualChrt='+record,
		        cache: false,  
	            dataType : "json", 
		        success : function(data) {
		        	$("#ClientID").combobox("loadData",data);
		        } 
		   }); 
        }
       });    	
        
	$('#ClientID').combobox({
		url : '<%=basePath%>/sampinspected/getClientID.do?QualChrt= ',
		editable:false, //不可编辑状态  
        cache: false,  
        panelHeight: '150',
		valueField:'id',
		textField:'text'
		});
});



function query(){
	var ProductID = $('#queryForm [name=ProductID]').val();
	var QualChrtID = $('#queryForm [name=QualChrtID]').val();
	var ClientID = $('#queryForm [name=ClientID]').val();	
	
	$.ajax({
		url : '<%=basePath%>/sampAnly/queryJson.do',
		type : 'post',
		data : {"ProductID":ProductID,"QualChrtID":QualChrtID,"ClientID":ClientID},
		dataType : 'json',
		success : function(data) {
			var myChart = echarts.init(document.getElementById('main'));
			var options = {
		            title : {
		                text: '',
		                subtext: ''
		            },
		            tooltip : {
		                trigger: 'axis'
		            },
		            legend: {
		                data:[]
		            },
		            toolbox: {
		                show : false
		            },
		            calculable : true,
		            xAxis : [
		                {
		                    type : 'category',
		                    boundaryGap : false,
		                    data : []
		                }
		            ],
		            yAxis : [
		                {
		                    type : 'value',
		                    axisLabel : {
		                        formatter: '{value}'
		                    }
		                }
		            ],
		            series : []
		        };
			
			options.xAxis[0].data = data.category;
            options.series = eval(data.series);
            options.legend.data = eval(data.legend); 
			
			myChart.setOption(options);
		},
		error : function(errorMsg) {
			alert("图表请求数据失败啦！");
		}

	});

}

/* var option = {
		title:{
			text : '检测结果'
		},
		tooltip:{
			trigger:'axis'
		},
		legend:{
			data : [ '检测值','检测']
		},
		xAxis:[{
			type:'category',
			data: data[0].categories
		}],
		yAxis:[{
			
		}],
		series:[{
			name: '第一批',
            type: 'line',
            data: data[0].data
		},{
			name: '第二批',
            type: 'line',
            data: data[1].data
		}]
	};  */
</script>

</head>
<body>
	<div data-options="region:'center'" style="border: 0px; height: 100%;">
		<div id="p" class="easyui-panel" title='查询条件' style="height: 20%;" data-options="collapsible:true">
			<div style="height: 15px;"></div>
			<form name="queryForm" id="queryForm" method="post">
				<table style="width: 100%">
					<tr>
					<td align="right">产品名称:</td>
					<td align="left">
						<select class="easyui-combobox"  style="width: 150px;height: 20px;" ID="ProductID" name="ProductID" >
							<!-- <option  selected>请选择</option> -->
						</select>
					</td>
					<td align="right">质量特性名称:</td>
					<td align="left">
						<select class="easyui-combobox" style="width: 150px;height: 20px;" ID="QualChrtID" name="QualChrtID" >
							<!-- <option  selected>请选择</option> -->
						</select>
					</td>
					<td align="right">客户端:</td>
					<td align="left">
						<select class="easyui-combobox" style="width: 100px;height: 20px;" id="ClientID" name="ClientID" >
						</select>
					</td>
					<td align="center"><a href="javascript:void(0)" onclick="query();" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:80px">查询</a></td>
					<td align="center"><a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset();" style="width:80px">重置</a></td>
				</tr>
				</table>
				<input id="res" name="res" type="reset" style="display: none;" />
			</form>
		</div>
		<div id="main" style="width: 600px; height: 400px;"></div>
	</div>
	<!-- <script type="text/javascript">
		var myChart = echarts.init(document.getElementById('main'));
		var option = {
			title:{
				text : '样本记录显示'
			},
			tooltip:{
				trigger:'axis'
			},
			legend:{
				data : [ '检测结果' ]
			},
			xAxis:[{
				type:'category',
				 data: []
			}],
			yAxis:[{
				
			}],
			series:[{
				name: '检测结果',
	            type: 'bar',
	            data: []
			}]
		}; 
		myChart.setOption(option);
	</script> -->
</body>
</html>