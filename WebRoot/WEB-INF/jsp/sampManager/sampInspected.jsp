<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
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
		url : '<%=basePath%>/sampinspected/queryList.do',
		idField : 'SIid',
		pageSize:'20',
		title:'样本检测记录',
		pagination : true,
		fit : true,
		rownumbers:true,
		toolbar:[{
			text : '导出Excel',
			iconCls : 'icon-add',
			handler : function() {
				$.ajax({
					url:'<%=basePath%>/sampinspected/toExcel.do',
					dataType:'json',
					success : function(response) {
						if (response.success) {
							showSuccess();
						} else {
							$.messager.alert('提示',response.msg, 'error');
						}
					}
				});
			}
		}],
	    columns : [[
		 {	field : 'serialID',
			title : '流水号',
			resizable:true,
			align:'center',
			sortable : true
		}, {
			field : 'sampSchemeID',
			title : '抽样计划编号',
			resizable:true,
			align:'center',
			sortable : true
		},{
			field : 'productName',
			title : '产品名称',
			resizable:true,
			align:'center',
			sortable : true
		},{
			field : 'qualChrtName',
			title : '质量特性',
			resizable:true,
			align:'center',
			sortable : true
		},{
			field : 'sampTaskID',
			title : '抽样任务编号',
			resizable:true,
			sortable : true
		},{
			field : 'clientID',
			title : '检测的客户端编号',
			resizable:true,
			align:'center',
			sortable : true
		},{
			field : 'lotNo',
			title : '批次号',
			resizable:true,
			align:'center',
			sortable : true
		},{
			field : 'sampleNo',
			title : '样本号',
			resizable:true,
			align:'center',
			sortable : true
		},{
			field : 'inspectionStatus',
			title : '检测状态',
			resizable:true,
			formatter:function(value,row,index){ 
				 if(value == 0){ 
				        return "未检测"; 
				    }
				 if(value == 1){ 
				        return "检测结束"; 
				    }
			},
			align:'center',
			sortable : true
		},{
			field : 'inspectionSequence',
			title : '检测序列',
			resizable:true,
			align:'center',
			sortable : true
		}, {
			field : 'inspectionResult',
			title : '检测结果',
			resizable:true,
			formatter:function(value,row,index){ 
				 if(value == 0){ 
				        return "未检测"; 
				    }
				 if(value == 1){ 
				        return "合格"; 
				    }
				 if(value == 2){
				    	return "不合格"
				    }
			},
			align:'center',
			sortable : true
		},{
			field : 'inspectionResultDetail',
			title : '检测结果详情',
			resizable:true,
			align:'center',
			sortable : true
		}]]
	});
	
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
	var ProductID = $('#queryForm [name=ProductID]');
	var QualChrtID = $('#queryForm [name=QualChrtID]');
	var ClientID = $('#queryForm [name=ClientID]'); 
	
	var queryParams = $('#datagrid').datagrid('options').queryParams;
	queryParams.ProductID = ProductID.val();
	queryParams.QualChrtID = QualChrtID.val();
	queryParams.ClientID = ClientID.val();
	$('#datagrid').datagrid('options').queryParams = queryParams;
	$("#datagrid").datagrid('load');
}

function reset(){
	$('#queryForm').form('clear');
}
</script>
</head>

<body class="easyui-layout">
<div data-options="region:'center'" style="border: 0px;height: 100%;">	
	<div id="p" class="easyui-panel" title='查询条件' style="height:20%;" data-options="collapsible:true">
	<div style="height: 15px;"></div>
		<form name="queryForm" id="queryForm" method="post" >
			<table style="width:100%"  >
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
							<!-- <option  selected>请选择</option> -->
						</select>
					</td>
					<td align="center"><a href="javascript:void(0)" onclick="query();" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:80px">查询</a></td>
					<td align="center"><a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset();" style="width:80px">重置</a></td>
				</tr>
			</table>
			<input id="res" name="res" type="reset" style="display: none;" />
		</form>
	</div>
	<div style="width: 100%;height: 80%;">
		<table id="datagrid"></table>
	</div>
</div>
</body>
</html>