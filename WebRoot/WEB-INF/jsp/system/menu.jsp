<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>菜单管理</title>
<jsp:include page="../inc/incJs.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		$('#ul_menu_tree').tree({/*菜单功能树菜单*/
			animate : true,
			url : '<%=basePath%>/menu/menutreeAll.do',
			checkbox : false,
			onClick : function(node) {
				$('#menu_edit [name=pid]').val(node.id);
				queryMenu(node.id);
			}
		});
		
		queryMenu("");
	});
	$(function(){
        $(".tree").treemenu({delay:300}).openActive();
    });s
function queryMenu(id){
	$('#datagrid').datagrid({
		url : '<%=basePath%>/menu/queryList.do',
		pagination : true,
		idField : 'id',
		striped : true,
		pageSize:'20',
		queryParams : {
			id:id
		},
		fit : true,
		rownumbers:true,
		title : '菜单列表',
		toolbar : [
					{
					text : '添加',
					iconCls : 'icon-add',
					handler : function() {
						$('#form_edit').form('load',{
							id:'',
							menuName:'',
							menuCode:'',
							url:'',
							des:''
						});
						$('#menu_edit').window('open');
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
							$('#form_edit').form('load',{
								id:rows[0].id,
								menuName:rows[0].menuName,
								menuCode:rows[0].menuCode,
								url:rows[0].url,
								des:rows[0].des
							});
							$('#menu_edit').window('open');
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
																url : '<%=basePath%>/menu/delete.do',
																data : {
																	'ids' : ids.join(',')
																},
																type: 'post',
																dataType:'json',
																success : function(data) {
																	if(data.success){
																		showSuccess();
																		$('#datagrid').datagrid('reload');
																		$('#datagrid').datagrid('unselectAll');
																		$('#ul_menu_tree').tree('reload');
																	} else {
																		$.messager.alert('提示',data.msg, 'error');
																	}
																	
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
					text : '刷新',
					iconCls : 'icon-reload',
					handler : function() {
						$('#ul_menu_tree').tree('reload');
						queryMenu("");
						$('#menu_edit [name=pid]').val("");
					}
				}],
		columns : [ [ {
			field : 'id',
			title : '序号',
			sortable : true,
			checkbox : true
		}, {
			field : 'menuName',
			title : '菜单名称',
			sortable : true,
			width : 150
		},{
			field : 'menuCode',
			title : '菜单编码',
			sortable : true,
			width : 150
		} ,
		 {
			field : 'url',
			title : 'url',
			sortable : true,
			width : 300
		}, {
			field : 'des',
			title : '描述',
			sortable : true,
			width : 320
		} ] ]
	});
}

function save(){
	var flag = $('#form_edit').form('validate');
	if(!flag){
		return false;
	}
   $.ajax({
		url : '<%=basePath%>/menu/save.do',
		data : $('#form_edit').serialize(),
		type: 'post',
		dataType:'json',
		success : function(data) {
			if (data.success) {
				$('#datagrid').datagrid('reload');
				$('#datagrid').datagrid('unselectAll');
				$('#ul_menu_tree').tree('reload');;
				$('#menu_edit').window('close');
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
<div data-options="region:'west',split:true" title="菜单树" style="width: 200px;">
	<ul id="ul_menu_tree"></ul>
</div>
<div data-options="region:'center'">
	<div title="菜单管理"  class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center', border:false">
			<table id="datagrid"></table>
		</div>
	</div>
	<div id="menu_edit" class="easyui-window" data-options="modal:true, closed:true, collapsible:true, minimizable:false, maximizable:false, closable:true" title="菜单维护" style="padding: 20px 100px 0px 100px;">
	<form id="form_edit" method="post" enctype="multipart/form-data">
		<input type="hidden" name="id" />
		<input type="hidden" name="pid" />
		<table>
			<tr>
				<td align="right">菜单名称：</td>
				<td align="left">
					<input class="easyui-validatebox textbox"  name="menuName" style="width: 200px;height: 20px;"  maxlength="32" data-options="required:true">
				</td>
			</tr>
			<tr>
				<td align="right">菜单编码：</td>
				<td align="left">
					<input class="easyui-validatebox textbox"  name="menuCode" style="width: 200px;height: 20px;"  maxlength="16" data-options="required:true">
				</td>
			</tr>
			<tr>
				<td align="right">URL：</td>
				<td align="left">
					<input class="easyui-validatebox textbox"  name="url" style="width: 200px;height: 20px;"  maxlength="150">
				</td>
			</tr>
			<tr>
				<td align="right">描述：</td>
				<td align="left">
					<input class="easyui-textbox" name="des" data-options="multiline:true" style="width: 200px;height:60px"></input>
				</td>
			</tr>
			<tr>
				<td align="center" colspan="2" height="50">
				<a style="width:60px" class="easyui-linkbutton" href="javascript:void(0);" onclick="save();">保存</a>
				<a style="width:60px" class="easyui-linkbutton" href="javascript:void(0);" onclick="$('#menu_edit').window('close');">取消</a></td>
			</tr>
		</table>
	</form>
	</div>
</div>
</body>
</html>