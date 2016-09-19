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
<title>抽样计划管理</title>
<jsp:include page="../inc/incJs.jsp"></jsp:include>
<script type="text/javascript">
$(function() {
	$('#datagrid').datagrid({
			url : '<%=basePath%>/returnSampPlan/queryList.do',
			idField : 'RSPid',
			pageSize : '20',
			title : 'Samplign Program',
			pagination : true,
			fit : true,
			rownumbers : true,
			/* toolbar : [ {
				text : '导出Excel',
				iconCls : 'icon-add',
				handler : function() {
					toExcel();
				}
			} ], */
			columns : [ [ /* {
				field : 'RSPid',
				title : '流水号',
				resizable : true,
				align : 'center',
				sortable : true
			}, */{
				field : 'sampTask0',
				title : 'Sampling Task ID',
				resizable : true,
				align : 'center',
				sortable : true,
				formatter:function(value,rows,index){
					var sampTask=rows.sampTask;
					return sampTask.sampTaskID;
				}
			},{
				field : 'sampTask1',
				title : 'Samping Scheme ID',
				resizable : true,
				align : 'center',
				sortable : true,
				formatter:function(value,rows,index){
					var sampTask=rows.sampTask;
					return sampTask.sampScheme.sampSchemeID;
				}
			},{
				field : 'sampTask2',
				title : 'Product ID',
				resizable : true,
				align : 'center',
				sortable : true,
				formatter:function(value,rows,index){
					var sampTask=rows.sampTask;
					return sampTask.sampScheme.product.productName;
				}
			},{
				field : 'sampTask3',
				title : 'Quality Name',
				resizable : true,
				align : 'center',
				sortable : true,
				formatter:function(value,rows,index){
					var sampTask=rows.sampTask;
					return sampTask.sampScheme.qualChrt.qualChrtName;
				}
			},{
				field : 'sampTask4',
				title : 'Client ID',
				resizable : true,
				align : 'center',
				sortable : true,
				formatter:function(value,rows,index){
					var sampTask=rows.sampTask;
					return sampTask.client.clientID;
				}
			},{
				field : 'lotNo',
				title : 'Lot Number',
				resizable : true,
				align : 'center',
				sortable : true
			},{
				field : 'sampleSize',
				title : 'Sample Size',
				resizable : true,
				align : 'center',
				sortable : true
			}, {
				field : 'inspectedCount',
				title : 'Count of Inspected',
				resizable : true,
				align : 'center',
				sortable : true
			},{
				field : 'acceptCount',
				title : 'Count of Accepted',
				resizable : true,
				align : 'center',
				sortable : true
			},{
				field : 'refuseCount',
				title : 'Count of Refused',
				resizable : true,
				align : 'center',
				sortable : true
			},{
				field : 'inspectionResult',
				title : 'Result of Inspection',
				resizable : true,
				align : 'center',
				sortable : true,
				formatter:function(value,rows,index){
					if(value == 0){
						return "不合格";
					}else if(value ==1){
						return "合格";
					}else{
						return "还未检测";
					}
				}
			}] ]
		});

	});
	
<%-- function toExcel(){
	alert("hello");
	$.ajax({
		url:'<%=basePath%>/returnSampPlan/toExcel.do',
		/* success : function(response) {
			if (response.success) {
				showSuccess();
			} else {
				$.messager.alert('提示',response.msg, 'error');
			}
		} */
	});
} --%>
</script>
</head>

<body class="easyui-layout">
	<div data-options="region:'center'" style="border: 0px; height: 100%;">
		<div style="width: 100%; height: 100%;">
			<table id="datagrid"></table>
		</div>
	</div>
</body>
</html>