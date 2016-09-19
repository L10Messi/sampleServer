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
$(function() {
	$('#datagrid').datagrid({
		url : '<%=basePath%>/webService/queryList.do',
		idField :'webServiceID',
		pageSize:'20',
		title:'网络配置列表',
		pagination : true,
		fit : true,
		rownumbers:true,
		striped:true,
		toolbar : [
					{
						text : '更新URL信息',
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
									WebServiceID:rows[0].webServiceID,
									WebServiceURL:rows[0].webServiceURL
								});
								$('#ws_edit').window('open');
							}
						},
					}
				],
	    columns : [[{
			field : 'webServiceID',
			title : 'ID',
			width : 200,
			align:'center',
			sortable : true
		}, {
			field : 'webServiceURL',
			title : 'Name',
			width : 400,
			align:'center',
			sortable : true
		}]]
	});
});

function update(){
	var flag = $('#form_edit').form('validate');
	if(!flag){
		return false;
	}
   $.ajax({
		url : '<%=basePath%>/webService/save.do',
		data : $('#form_edit').serialize(),
		type: 'post',
		dataType:'json',
		success : function(data) {
			if (data.success) {
				$('#datagrid').datagrid('reload');
				$('#datagrid').datagrid('unselectAll');
				$('#ws_edit').window('close');
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
	<div style="width: 100%;height: 100%;">
		<table id="datagrid"></table>
	</div>
</div>

	<div id="ws_edit" class="easyui-window" data-options="modal:true, closed:true, collapsible:true, minimizable:false, maximizable:false, closable:true" title="产品新增" style="padding: 20px 70px 0px 70px;">
		<form id="form_edit" method="post" enctype="multipart/form-data">
			<table>
				<tr>
					<td align="right">ID:</td>
					<td align="left">
						<input class="easyui-validatebox textbox" name="WebServiceID" style="width: 200px;height: 20px;" data-options="required:true" readonly="readonly"></input>
					</td>
				</tr>
				<tr>
					<td align="right">URL:</td>
					<td align="left">
						<input class="easyui-validatebox textbox"  name="WebServiceURL" style="width: 200px;height: 20px;" data-options="required:true"></input>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="2" height="50">
						<a class="easyui-linkbutton" href="javascript:void(0);" onclick="update();" style="width:60px">更新</a>
						<a class="easyui-linkbutton" href="javascript:void(0);" onclick="$('#ws_edit').window('close');" style="width:60px">取消</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>