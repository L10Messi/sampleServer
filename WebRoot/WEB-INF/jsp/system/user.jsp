<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<jsp:include page="../inc/incJs.jsp"></jsp:include>
<script type="text/javascript">
$(function() {
	$('#datagrid').datagrid({
		url : '<%=basePath%>/user/queryList.do',
		idField : 'id',
		pageSize:'10',
		title:'用户列表',
		pagination : true,
		fit : true,
		rownumbers:true,
		toolbar : [
					{
						text : '添加',
						iconCls : 'icon-add',
						handler : function() {
							$('#form_edit').form('clear');
							$('#form_edit').form('load',{
								state:'0'
							});
							$('#user_edit').window('open');
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
									id:rows[0].id,
									loginname:rows[0].loginname,
									name:rows[0].name,
									state:rows[0].state
								});
								$('#user_edit').window('open');
							}
						}
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
											ids.push(rows[i].id);
										}
										$.ajax({
												url : '<%=basePath%>/user/delete.do',
												data : {
													'ids' : ids.join(',')
												},
												type: 'post',
												dataType:'json',
												success : function(response) {
													if (response.success) {
														showSuccess();
													} else {
														showError();
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
					},'-',
					{
						text : '角色分配',
						iconCls : 'icon-edit',
						handler : function() {
							var rows = $('#datagrid').datagrid('getSelections');
							if (rows.length < 1) {
								$.messager.alert('提示', '请选择一个用户','error');
							} else if (rows.length > 1) {
								$.messager.alert('提示','只能选择一个用户分配权限', 'error');
							} else {
								$('#deftree').tree({
									url : '<%=basePath%>/user/userRoleSelect.do?userid='+rows[0].id,
									checkbox : true,
									onLoadSuccess : function() {
										$('#window_auth').window('open');
									},
									animate : true,
									cascadeCheck : true
								});
							}
						}
					}
				  ],
	    columns : [ [{
			field : 'id',
			title : '序号',
			sortable : true,
			checkbox : true
		},{
			field : 'loginname',
			title : '登录名',
			width : 200,
			sortable : true
		},{
			field : 'name',
			title : '姓名',
			width : 200,
			sortable : true
		},{
			field : 'state',
			title : '状态',
			width : 200,
			sortable : true,
			formatter:function(value,row,index){ 
			    if(value == "0"){ 
			        return "启用"; 
			    }
			    if(value == "1"){
			    	return "禁用";
			    }
			},
			styler:cellStyler
		}] ]
	});
});

function cellStyler(value,row,index){
	if(value == '0'){
		return 'color:#00CC00';
	}
	if(value == '1'){
		return 'color:#FF0000';
	}
}

function save(){
	var flag = $('#form_edit').form('validate');
	if(flag){
		var name = $('#user_edit [name=loginname]').val();
		var reg = /^[a-zA-Z0-9_]{1,}$/;
		if (!name.match(reg)) {
			$.messager.alert('提示',"用户名只允许输入字母数字下划线", 'error');
			return false;
		}
		
		$.ajax({
			url : '<%=basePath%>/user/save.do',
			data : $('#form_edit').serialize(),
			type: 'post',
			dataType:'json',
			success : function(data) {
				if (data.success) {
					$('#datagrid').datagrid('reload');
					$('#datagrid').datagrid('unselectAll');
					$('#user_edit').window('close');
					showSuccess();
				} else {
					showError();
				}
			}
		});
	}
};

function query(){
	var name = $('#queryForm [name=name]');
	var loginname = $('#queryForm [name=loginname]');
	var state = $('#queryForm [name=state]');

	var queryParams = $('#datagrid').datagrid('options').queryParams;
	queryParams.name = name.val();
	queryParams.loginname = loginname.val();
	queryParams.state = state.val();
	$('#datagrid').datagrid('options').queryParams = queryParams;
	$("#datagrid").datagrid('load');
}
function reset(){
	$('#queryForm').form('clear');
	$('#queryForm').form('load',{
		state:''
	});
}


function saveRoleSelect() {
	var nodes = $('#deftree').tree('getChecked');
	var ids = '';
	var rows = $('#datagrid').datagrid('getSelections');
	for ( var i = 0; i < nodes.length; i++) {
		if (ids != '') {
			ids += ',';
		}
		ids += nodes[i].id;
	}
	$.ajax({
		url : '<%=basePath%>/user/saveRoleSelect.do',
		data : {
			'userid' : rows[0].id,
			'ids' : ids
		},
		type: 'post',
		dataType:'json',
		success : function(data) {
			if (data.success) {
				$('#window_auth').window('close');
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
		<div id="p" class="easyui-panel" title='查询条件' style="height:20%;" data-options="collapsible:true">
			<form name="queryForm" id="queryForm" method="post" >
			<div style="height: 20px;"></div>
				<table style="wigth:100%" >
					<tr>
						<td align="right">姓名：</td>
						<td align="left"><input class="easyui-validatebox textbox" style="width: 200px;height: 20px;"  name="name"  maxlength="32"></input></td>
						<td align="right">登录名：</td>
						<td align="left"><input class="easyui-validatebox textbox" style="width: 200px;height: 20px;"  name="loginname"  maxlength="32" style="width: 180px;"></input></td>
						<td align="right">状态</td>
						<td align="left">
							<select class="easyui-combobox" name="state">
								<option value="">全部</option>
								<option value="0">启用</option>
								<option value="1">禁用</option>
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

	<div id="user_edit" class="easyui-window" data-options="modal:true, closed:true, collapsible:true, minimizable:false, maximizable:false, closable:true" title="用户维护" style="padding: 20px 70px 0px 70px;">
		<form id="form_edit" method="post" enctype="multipart/form-data">
			<input type="hidden" name="id"/>
			<table>
				<tr>
					<td align="right">登录名：</td>
					<td align="left">
						<input class="easyui-validatebox textbox"  name="loginname" style="width: 200px;height: 20px;"  maxlength="32" data-options="required:true"></input>
					</td>
				</tr>
				<tr>
					<td align="right">姓名：</td>
					<td align="left">
						<input class="easyui-validatebox textbox"  name="name" style="width: 200px;height: 20px;"  maxlength="32" data-options="required:true"></input>
					</td>
				</tr>
				<tr>
					<td align="right">状态：</td>
					<td align="left">
						<select class="easyui-combobox" name="state">
							<option value="0">启用</option>
							<option value="1">禁用</option>
						</select>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="2" height="50">
						<a class="easyui-linkbutton" href="javascript:void(0);" onclick="save();" style="width:60px">保存</a>
						<a class="easyui-linkbutton" href="javascript:void(0);" onclick="$('#user_edit').window('close');" style="width:60px">取消</a>
					</td>
				</tr>
			</table>
		</form>
	</div>

	
	
	
	<div id="window_auth" class="easyui-window" data-options="modal:true, closed:true, collapsible:true, minimizable:false, maximizable:false, closable:true"
		title="角色分配" style="width: 600px; height: 450px; padding: 5px; background: #fafafa;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center', border:true" >
				<ul id="deftree"></ul>
			</div>
			<div data-options="region:'south', border:false"  style="text-align: right; height: 50px; line-height: 50px; border:false; region:south">
				<p align="right">
					<a class="easyui-linkbutton" href="javascript:void(0);"
						onclick="javascript:saveRoleSelect();">分配</a>
				</p>
			</div>
		</div>
	</div>
	
	
</body>
</html>