<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>抽样任务管理</title>
<jsp:include page="../inc/incJs.jsp"></jsp:include>
<script type="text/javascript">
$(function() {
	$('#datagrid').datagrid({
		url : '<%=basePath%>/samptask/queryList.do',
		idField : 'STid',
		pageSize:'20',
		title:'抽样任务列表',
		pagination : true,
		fit : true,
		rownumbers:true,
		toolbar : [
					{
						text : '添加',
						iconCls : 'icon-add',
						handler : function() {
							$('#SampTaskForm').form('clear');
							$('#SampTaskEdit').window('open');
						}
					},
					'-',
					{
						text : '编辑',
						iconCls : 'icon-edit',
						handler : function() {
							var rows = $('#datagrid').datagrid('getSelections');
							if (rows.length < 1) {
								$.messager.alert('提示', '请选择一行','error');
							} else if (rows.length > 1) {
								$.messager.alert('提示','只能选择一行进行修改', 'error');
							} else {
								if(rows[0].sampTaskStatus == 0){
									$('#SampTaskForm').form('clear');
									$('#SampTaskForm').form('load',{
										STid:rows[0].STid,
										SSid:rows[0].sampScheme.SSid,
										cid:rows[0].client.cid
									}); 
									$('#SampTaskEdit').window('open');
								}else{
									$.messager.alert('提示','当前任务不处于就绪状态不能修改', 'error');
								}
							}
						},
					},
					'-',
					{
						text : '删除',
						iconCls : 'icon-remove',
						handler : function() {
							var ids = [];
							var rows = $('#datagrid').datagrid('getSelections');
							if (rows.length > 0) {
								$.messager.confirm('请确认','您要执行当前所选项目？',function(r) {
									if (r) {
										for ( var i = 0; i < rows.length; i++) {
											ids.push(rows[i].STid);
										}
										$.ajax({
												url : '<%=basePath%>/samptask/delete.do',
												data : {
													'ids' : ids.join(',')
												},
												type: 'post',
												dataType:'json',
												success : function(response) {
													if (response.success) {
														showSuccess();
													} else {
														$.messager.alert('提示',response.msg, 'error');
													}
													$('#datagrid').datagrid('reload');
													$('#datagrid').datagrid('unselectAll');
												}
										});
									}
								});
							} else {
								$.messager.alert('提示','请选择要删除的记录', 'error');
						}
					}
				},
				'-',
					{
						text : '开始任务',
						iconCls : 'icon-edit',
						handler : function() {
							var rows = $('#datagrid').datagrid('getSelections');
							if (rows.length < 1) {
								$.messager.alert('提示', '请选择一行','error');
							} else if (rows.length > 1) {
								$.messager.alert('提示','只能开始一个抽样任务', 'error');
							} else {
								var ids=rows[0].STid;
								$.ajax({
									url : '<%=basePath%>/samptask/begin.do',
									data : {'ids' : ids},
									type: 'post',
									dataType:'json',
									success : function(response) {
										if (response.success) {
											showSuccess();
										} else {
											$.messager.alert('提示',response.msg, 'error');
										}
										$('#datagrid').datagrid('reload');
										$('#datagrid').datagrid('unselectAll');
									}
							});
							}
						},
					}
					],
	    columns : [[{
			field : 'STid',
			title : '序号',
			sortable : true,
			checkbox : true 
		},{
			field : 'sampScheme',
			title : '抽样计划ID',
			width : 200,
			align:'center',
			sortable : true,
			formatter:function(value,rows,index){
				return rows.sampScheme.sampSchemeID;
			}
		},{
			field : 'client',
			title : '客户端ID',
			width : 200,
			align:'center',
			sortable : true,
			formatter:function(value,rows,index){
				return value.clientID;
			}
		},{
			field : 'sampTaskID',
			title : '任务ID',
			width : 200,
			align:'center',
			sortable : true
		},{
			field : 'lotNo',
			title : '当前批次',
			width : 200,
			align:'center',
			sortable : true
		},{
			field : 'sampTaskStatus',
			title : '当前状态',
			width : 200,
			align:'center',
			sortable : true,
			formatter:function(value,row,index){ 
			    if(value == 0){ 
			        return "就绪"; 
			    }
			    if(value == 1){
			    	return "开始";
			    }
			    if(value == 2){
			    	return "运行";
			    }
			    if(value == 3){
			    	return "暂停";
			    }
			    if(value == 4){
			    	return "终止";
			    }
			    if(value == 5){
			    	return "结束";
			    }
			}
		},{
			field : 'createDate',
			title : '创建日期',
			align:'center',
			width : 200,
			sortable : true,
			formatter: function (value, row, index) {
				var year = value.year + 1900;
				var month = value.month + 1;
				var date = value.date + 1;
				return year+"-"+month+"-"+date;
			 }
		}]]
	});
});


function save(){
	var flag = $('#SampTaskForm').form('validate');
	if(!flag){
		return false;
	}
   $.ajax({
		url : '<%=basePath%>/samptask/save.do',
		data : $('#SampTaskForm').serialize(),
		type: 'post',
		dataType:'json',
		success : function(data) {
			if (data.success) {
				$('#datagrid').datagrid('reload');
				$('#datagrid').datagrid('unselectAll');
				$('#SampTaskEdit').window('close');
				showSuccess();
			} else {
				showError();
			}
		}
	});
};

</script>
</head>
<body class="easyui-layout">
<div data-options="region:'center'" style="border: 0px;height: 100%;">	
		<table id="datagrid"></table>
</div>
	<div id="SampTaskEdit" class="easyui-window" data-options="modal:true, closed:true, collapsible:true, minimizable:false, maximizable:false, closable:true" title="产品新增" style="padding: 20px 70px 0px 70px;">
		<form id="SampTaskForm" method="post" enctype="multipart/form-data">
			<input type="hidden" name="STid"/>
			<table>
				<tr>
					<td align="right">抽样计划信息：</td>
					<td align="left">
						<select class="easyui-combogrid" name="SSid" id="SSid" style="width:200px" required data-options="
									panelWidth: 420,
									idField: 'SSid',
									textField: 'sampSchemeID',
									url: '<%=basePath%>/sampscheme/queryList.do',
									columns : [[{field : 'sampSchemeID',title : '抽样计划ID',width : 200,sortable : true},
									{field : 'product',title : '产品',width : 100,align:'center',sortable : true,
											formatter:function(value,rows,index){return value.productName;}},
									{field : 'qualChrt',title : '质量特性',width : 100,align:'center',sortable : true,
											formatter:function(value,rows,index){return value.qualChrtName;}},
									{field : 'lotSize',title : '批量',width : 60,sortable : true},
									{field : 'batch',title : '批次',width : 40,sortable : true},
									{field : 'IL',title : '检验水平',width : 100,sortable : true},
									{field : 'AQL',title : '接收质量限',width : 100,sortable : true},
									{field : 'severityLevel',title : '严重程度',width : 100,sortable : true}
									]],
									fitColumns: true,
									rownumbers:true,
									pageSize:10,
									pagination:true
							">
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">客户端信息：</td>
					<td align="left">
						<select class="easyui-combogrid" name="cid" id="cid" style="width:200px" required data-options="
									panelWidth: 420,
									idField: 'cid',
									textField: 'clientID',
									url: '<%=basePath%>/client/queryList.do',
									columns : [[
									{field : 'clientID',title : '客户端ID',width : 200,sortable : true},
									{field : 'clientType',title : '客户端类型',width : 200,sortable : true},
									{field : 'macAddress',title : 'Mac地址',width : 200,sortable : true},
									{field : 'remark',title : '客户端说明',width : 200,sortable : true}
											]],
									multiple:true,
									fitColumns: true,
									rownumbers:true,
									pageSize:10,
									pagination:true
							">
						</select>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="2" height="50">
						<a class="easyui-linkbutton" href="javascript:void(0);" onclick="save();" style="width:60px">保存</a>
						<a class="easyui-linkbutton" href="javascript:void(0);" onclick="$('#SampTaskEdit').window('close');" style="width:60px">取消</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>