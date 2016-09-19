<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户端管理</title>
<jsp:include page="../inc/incJs.jsp"></jsp:include>
<script type="text/javascript">
//客户端列表
$(function() {
	$('#datagrid').datagrid({
		url : '<%=basePath%>/client/queryList.do',
		idField :'cid',
		pageSize:'10',
		title:'客户端列表',
		pagination : true,
		fit : true,
		rownumbers:true,
		striped:true,
		toolbar : [
					{
						text : '添加',
						iconCls : 'icon-add',
						handler : function() {
							$('#form_edit').form('clear');
							$('#clientedit').window('open');
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
								$('#form_edit').form('clear');
								$('#form_edit').form('load',{
									cid:rows[0].cid,
 									ClientID:rows[0].clientID,
 									MacAddress:rows[0].macAddress,
									ClientType:rows[0].clientType,
									Remark:rows[0].remark 
								});
								$('#clientedit').window('open');
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
								$.messager.confirm('请确认','您要删除当前所选项目？',function(r) {
									if (r) {
										for ( var i = 0; i < rows.length; i++) {
											ids.push(rows[i].cid);
										}
										$.ajax({
												url : '<%=basePath%>/client/delete.do',
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
					}
				],
	    columns : [[{
			field : 'cid',
			title : 'Id',
			sortable : true,
			checkbox : true
		}, {
			field : 'clientID',
			title : 'ClientID',
			width : 200,
			align:'center',
			sortable : true/* ,
			formatter:function(value,row,index){ 
				return "<a href='http://www.baidu.com'>"+value+"</a>";
			} */
		},{
			field : 'clientType',
			title : 'ClientType',
			align:'center',
			width : 200,
			sortable : true
		},{
			field : 'macAddress',
			title : 'MacAddress',
			align:'center',
			width : 200,
			sortable : true
		},{
			field : 'remark',
			title : 'Remark',
			align:'center',
			width : 200,
			sortable : true
		}]]
	});
});

function query(){
	var clientID = $('#queryForm [name=clientID]');
	var queryParams = $('#datagrid').datagrid('options').queryParams;

	queryParams.clientID = clientID.val();
	$('#datagrid').datagrid('options').queryParams = queryParams;
	$("#datagrid").datagrid('load');
}

function reset(){
	$('#queryForm').form('clear');
}

function save(){
	var flag = $('#form_edit').form('validate');
	if(!flag){
		return false;
	}
   $.ajax({
		url : '<%=basePath%>/client/save.do',
		data : $('#form_edit').serialize(),
		type: 'post',
		dataType:'json',
		success : function(data) {
			if (data.success) {
				$('#datagrid').datagrid('reload');
				$('#datagrid').datagrid('unselectAll');
				$('#clientedit').window('close');
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
	<div  class="easyui-panel" title='查询条件' style="height:15%; collapsible:true">
	<div style="height: 15px;"></div>
		<form name="queryForm" id="queryForm" method="post" >
			<table style="width:100%"  >
				<tr>
					<td align="right">客户端ID：</td>
					<td align="left"><input class="easyui-validatebox textbox" style="width: 200px;height: 20px;"  name="clientID"  maxlength="32"></input></td>
					<td align="center"><a href="javascript:void(0)" onclick="query();" class="easyui-linkbutton"  style="width:80px;iconCls:'icon-search'">查询</a></td>
					<td align="center"><a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset();" style="width:80px">重置</a></td>
				</tr>
			</table>
			
		</form>
	</div>
	<div style="width: 100%;height: 85%;">
		<table id="datagrid"></table>
	</div>
<!-- 添加客户端窗口 -->
</div>
	<div id="clientedit" class="easyui-window" data-options="modal:true, closed:true, collapsible:true, minimizable:false, maximizable:false, closable:true" title="产品新增" style="padding: 20px 70px 0px 70px;">
		<form id="form_edit" method="post" enctype="multipart/form-data">
			<input type="hidden" name="cid" />
			<table>
				<tr>
					<td align="right">客户端ID:</td>
					<td align="left">
						<input class="easyui-validatebox textbox" name="ClientID" style="width: 200px;height: 20px;" data-options="required:true"></input>
					</td>
				</tr>
				<tr>
					<td align="right">客户端类型:</td>
					<td align="left">
						<input class="easyui-validatebox textbox"  name="ClientType" style="width: 200px;height: 20px;" data-options="required:true"></input>
					</td>
				</tr>
				<tr>
					<td align="right">MAC地址:</td>
					<td align="left">
						<input class="easyui-validatebox textbox"  name="MacAddress" style="width: 200px;height: 20px;" required="required"></input>
					</td>
				</tr>
				<tr>
					<td align="right">客户端说明:</td>
					<td align="left">
						<input class="easyui-textbox"  name="Remark" data-options="multiline:true" style="width: 200px;height: 60px;" ></input>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="2" height="50">
						<a class="easyui-linkbutton" href="javascript:void(0);" onclick="save();" style="width:60px">保存</a>
						<a class="easyui-linkbutton" href="javascript:void(0);" onclick="$('#clientedit').window('close');" style="width:60px">取消</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>