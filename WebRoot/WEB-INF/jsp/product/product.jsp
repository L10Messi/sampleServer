<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>产品管理</title>
<jsp:include page="../inc/incJs.jsp"></jsp:include>

<script type="text/javascript">
$(function() {
	$('#datagrid').datagrid({
		url : '<%=basePath%>/product/queryList.do',
		idField : 'pid',
		pageSize:'20',
		title:'产品列表',
		pagination : true,
		fit : true,
		rownumbers:true,
		toolbar : [
					{
						text : '添加',
						iconCls : 'icon-add',
						handler : function() {
							$('#form_edit').form('clear');
							$('#product_edit').window('open');
						}
					},'-',{
						text : '编辑',
						iconCls : 'icon-edit',
						handler : function() {
							var rows = $('#datagrid').datagrid('getSelections');
							if (rows.length < 1) {
								$.messager.alert('提示', '请选择一行','error');
							} else if (rows.length > 1) {
								$.messager.alert('提示','只能选择一行进行修改', 'error');
							} else {
								$('#form_edit').form('clear');
								$('#form_edit').form('load',{
									pid:rows[0].pid,
									ProductID:rows[0].productID,
									ProductName:rows[0].productName									
								});
								$('#product_edit').window('open');
							}
						},
					},'-',{
						text : '删除',
						iconCls : 'icon-remove',
						handler : function() {
							var ids = [];
							var rows = $('#datagrid').datagrid('getSelections');
							if (rows.length > 0) {
								$.messager.confirm('请确认','您要删除当前所选项目？',function(r) {
									if (r) {
										for ( var i = 0; i < rows.length; i++) {
											ids.push(rows[i].pid);
										}
										$.ajax({
												url : '<%=basePath%>/product/delete.do',
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
					},{
						text : '导出Excel',
						iconCls : 'icon-add',
						handler : function() {
							$.ajax({
								url:'<%=basePath%>/product/toExcel.do',
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
	    columns : [[{
			field : 'pid',
			title : '序号',
			sortable : true,
			checkbox : true
		},{
			field : 'productID',
			title : '产品ID',
			align:'center',
			width : 200,
			sortable : true
		},{
			field : 'productName',
			title : '产品名称',
			align:'center',
			width : 200,
			sortable : true
		},{
			field : 'createdDate',
			title : '创建日期',
			align:'center',
			width : 200,
			sortable : true,
			formatter: function (value, row, index) {
				if(value != null){
					var year = value.year + 1900;
					var month = value.month + 1;
					var date = value.date + 1; 
					return year+"-"+month+"-"+date;
				}
				return value;
			 }
		}]]
	});
});

function save(){
	var flag = $('#form_edit').form('validate');
	if(!flag){
		return false;
	}
   $.ajax({
		url : '<%=basePath%>/product/save.do',
		data : $('#form_edit').serialize(),
		type: 'post',
		dataType:'json',
		success : function(data) {
			if (data.success) {
				$('#datagrid').datagrid('reload');
				$('#datagrid').datagrid('unselectAll');
				$('#product_edit').window('close');
				showSuccess();
			} else {
				showError();
			}
		}
	});
};

function query(){
	var ID = $('#queryForm [name=ProductID]');
	var name = $('#queryForm [name=ProductName]');
	
	var queryParams = $('#datagrid').datagrid('options').queryParams;
	queryParams.productID = ID.val();
	queryParams.productName = name.val();
	
	$('#datagrid').datagrid('options').queryParams = queryParams;
	$("#datagrid").datagrid('load');
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
					<td align="right">产品ID：</td>
					<td align="left"><input class="easyui-validatebox textbox" style="width: 200px;height: 20px;"  name="ProductID"  maxlength="32"></input></td>
					<td align="right">产品名称：</td>
					<td align="left"><input class="easyui-validatebox textbox" style="width: 200px;height: 20px;"  name="ProductName"  maxlength="32" style="width: 180px;"></input></td>
					<td align="center"><a href="javascript:void(0)" onclick="query();" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:80px">查询</a></td>
					<td align="center"><a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset(queryForm);" style="width:80px">重置</a></td>
				</tr>
			</table>
			<input id="res" name="res" type="reset" style="display: none;" />
		</form>
	</div>
	<div style="width: 100%;height: 80%;">
		<table id="datagrid"></table>
	</div>
</div>
	<div id="product_edit" class="easyui-window" data-options="modal:true, closed:true, collapsible:true, minimizable:false, maximizable:false, closable:true" title="产品新增" style="padding: 20px 70px 0px 70px;">
		<form id="form_edit" method="post" enctype="multipart/form-data">
			<input type="hidden" name="pid"/>
			<table>
				<tr>
					<td align="right">产品ID：</td>
					<td align="left">
						<input class="easyui-validatebox textbox"  name="ProductID" style="width: 200px;height: 20px;"   required></input>
					</td>
				</tr>
				<tr>
					<td align="right">产品名称：</td>
					<td align="left">
						<input class="easyui-validatebox textbox"  name="ProductName" style="width: 200px;height: 20px;"   required></input>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="2" height="50">
						<a class="easyui-linkbutton" href="javascript:void(0);" onclick="save();" style="width:60px">保存</a>
						<a class="easyui-linkbutton" href="javascript:void(0);" onclick="$('#product_edit').window('close');" style="width:60px">取消</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>