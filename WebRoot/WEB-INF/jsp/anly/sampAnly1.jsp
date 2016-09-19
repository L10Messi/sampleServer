<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<jsp:include page="../inc/incJs.jsp"></jsp:include>
<script type="text/javascript"
	src="<%=basePath%>/js/highcharts/highcharts.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/js/highcharts//modules/exporting.js"></script>
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
		        url : '<%=basePath%>/sampinspected/getQualChrtName.do',
		        data : {"ProductID":record},
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
		url : '<%=basePath%>/sampAnly/qualified.do',
		data : {"ProductID" : ProductID,"QualChrtID" : QualChrtID,"ClientID" : ClientID},
		type : 'post',
		dataType : 'json',
		success : function(data) {
		$('#container').highcharts({
			chart : {type : 'column'},
			title : {text : '产品检测统计',
					 x : -20},
			xAxis : {categories : data.categories},
			yAxis : {min : 0,
					 title : {
							text : '抽检样本总数'
							},
					stackLabels : {
						enabled : true,
						style : {
							fontWeight : 'bold',
							color : (Highcharts.theme && Highcharts.theme.textColor)|| 'gray'
								}
						}
					},
			tooltip : {
					headerFormat : '<b>{point.x}</b><br/>',
					pointFormat : '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
					},
			legend : {
					align : 'right',
					x : -30,
					verticalAlign : 'top',
					y : 25,
					floating : true,
					backgroundColor : (Highcharts.theme && Highcharts.theme.background2)|| 'white',
					borderColor : '#CCC',
					borderWidth : 1,
					shadow : false
					},
			plotOptions : {
					column : {
					stacking : 'normal',
					dataLabels : {
							enabled : true,
							color : (Highcharts.theme && Highcharts.theme.dataLabelsColor)|| 'white',
							style : {
									textShadow : '0 0 3px black'
									}
							}
						}
					},
			series : data.series
			});
		}
	});
};
</script>
</head>
<body>
	<div data-options="region:'center'" style="border: 0px; height: 100%;">
		<div id="p" class="easyui-panel" title='查询条件' style="height: 20%;"
			data-options="collapsible:true">
			<div style="height: 15px;"></div>
			<form name="queryForm" id="queryForm" method="post">
				<table style="width: 100%">
					<tr>
						<td align="right">产品名称:</td>
						<td align="left"><select class="easyui-combobox"
							style="width: 150px; height: 20px;" ID="ProductID"
							name="ProductID">
								<!-- <option  selected>请选择</option> -->
						</select></td>
						<td align="right">质量特性名称:</td>
						<td align="left"><select class="easyui-combobox"
							style="width: 150px; height: 20px;" ID="QualChrtID"
							name="QualChrtID">
								<!-- <option  selected>请选择</option> -->
						</select></td>
						<td align="right">客户端:</td>
						<td align="left"><select class="easyui-combobox"
							style="width: 100px; height: 20px;" id="ClientID" name="ClientID">
						</select></td>
						<td align="center"><a href="javascript:void(0)"
							onclick="query();" class="easyui-linkbutton"
							data-options="iconCls:'icon-search'" style="width: 80px">查询</a></td>
						<td align="center"><a href="javascript:void(0)"
							class="easyui-linkbutton" onclick="reset();" style="width: 80px">重置</a></td>
					</tr>
				</table>
				<input id="res" name="res" type="reset" style="display: none;" />
			</form>
		</div>
		<div id="container"
			style="min-width: 310px; height: 400px; margin: 0 auto"></div>
	</div>
</body>
</html>