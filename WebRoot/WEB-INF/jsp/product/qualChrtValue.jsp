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
		url : '<%=basePath%>/qualChrtValue/queryList.do',
		idField : 'QVid',
		pageSize:'20',
		title:'质量特性限定列表',
		pagination : true,
		fit : true,
		rownumbers:true,
		toolbar : [
					{
						text : '添加',
						iconCls : 'icon-add',
						handler : function() {
							$('#qualChrtValueForm').form('clear');
							$('#qualChrtValueEdit').window('open');
							$('#qualChrtValueEdit').window('move',{
								left:300,top:10});
						}
					},
					'-',
					{						
						text : '编辑',
						iconCls : 'icon-edit',
						handler : function() {
							var rows = $('#datagrid').datagrid('getSelections');
							alert(rows);
							if (rows.length < 1) {
								$.messager.alert('提示', '请选择一行','error');
							} else if (rows.length > 1) {
								$.messager.alert('提示','只能选择一行进行修改', 'error');
							} else {
								$('#qualChrtValueForm').form('clear');
								$('#qualChrtValueForm').form('load',{
									QVid:rows[0].QVid,
									qid:rows[0].qualChrt.qid,
									QualChrtValSymbol:rows[0].qualChrtValSymbol,
									QualChrtValue:rows[0].qualChrtValue,
									QualChrtValUnit:rows[0].qualChrtValUnit
								});
								$('#qualChrtValueEdit').window('open');
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
											ids.push(rows[i].QVid);
										}
										$.ajax({
												url : '<%=basePath%>/qualChrtValue/delete.do',
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
			field : 'QVid',
			title : '序号',
			sortable : true,
			checkbox : true 
		},{
			field : 'qualChrt0',
			title : '质量特性ID',
			width : 200,
			sortable : true,
			formatter:function(value,row,index){
				var qualChrt=row.qualChrt;
				return qualChrt.product.productName;
			}
		},{
			field : 'qualChrt1',
			title : '质量特性ID',
			width : 200,
			sortable : true,
			formatter:function(value,row,index){
				var qualChrt=row.qualChrt;
				return qualChrt.qualChrtName;
			}
		},{
			field : 'qualChrtValSymbol',
			title : '质量特性限定符号',
			width : 200,
			sortable : true,
			formatter:function(value,row,index){ 
			    if(value == "01"){ 
			        return "="; 
			    }
			    if(value == "02"){
			    	return "<";
			    }
			    if(value == "03"){
			    	return ">";
			    }
			    if(value == "04"){
			    	return "<=";
			    }
			    if(value == "05"){
			    	return ">=";
			    }
			}
		},{
			field : 'qualChrtValue',
			title : '质量特性限定值',
			width : 200,
			sortable : true
		},{
			field : 'qualChrtValUnit',
			title : '质量特性限定单位',
			width : 200,
			sortable : true
		}]]
	});
});

//文本框自动添加
var counter = 1;
$(document).ready(function(){
    var InputsWrapper = $("#TextBoxesGroup");
    $("#addButton").click(function () {
		if(counter>10){
            alert("Only 10 textboxes allow");
            return false;
		}   
		$(InputsWrapper).append('<tr>'+'<td align="right">特性限定符号'+counter+':</td>'+'<td align="left">'+
				'<select class="easyui-combobox" id="QualChrtValSymbol'+counter+'" name="QualChrtValSymbol'+counter+'" style="width: 100px;height: 20px;" data-options="panelHeight:"auto"" required="required">'
				+'<option value="01" selected="selected">'+'='+'</option>'+'<option value="02">'+'<'+'</option>'
				+'<option value="03">'+'>'+'</option>'+'<option value="04">'+'<='+'</option>'
				+'<option value="05">'+'>='+'</option>'+'</select>'+
				'</td>'+ '</tr>'
				+'<tr>'+'<td align="right">特性限定值'+counter+':</td>'+'<td align="left">'+
				'<input class="easyui-validatebox textbox" style="width: 200px;height: 20px;" id="QualChrtValue'+counter+'"name="QualChrtValue'+counter+'"/>'+'</td>'+'</tr>'
				+'<tr>'+'<td align="right">特性单位'+counter+':</td>'+'<td align="left">'+
				'<input class="easyui-validatebox textbox" style="width: 200px;height: 20px;" id="QualChrtValUnit'+counter+'"name="QualChrtValUnit'+counter+'"/>'+'</td>'+'</tr>'); 
		counter++;
     });
});

/* 存储质量特性的值 */
function saveQualChrt(){
	var flag = $('#qualChrtValueForm').form('validate');
	if(!flag){
		return false;
	}
	/* alert(counter); */
	
	for(i=0;i<counter;i++){
		var obj=new Object();
		obj.QVid=$('#QVid').val();
		obj.qid=$('#qid').combogrid('grid').datagrid('getSelected').qid;
		obj.QualChrtValSymbol=$('#QualChrtValSymbol'+i).combobox('getValue');
		obj.QualChrtValue=$('#QualChrtValue'+i).val();
		obj.QualChrtValUnit=$('#QualChrtValUnit'+i).val();
		var myString = $.param(obj);
		/* alert(myString); */
		$.ajax({
			url : '<%=basePath%>/qualChrtValue/save.do',
			data : myString,
			type: 'post',
			dataType:'json' ,
			success : function(data) {
				if (data.success) {
					$('#datagrid').datagrid('reload');
					$('#datagrid').datagrid('unselectAll');
					$('#qualChrtValueEdit').window('close');
					showSuccess();
				} else {
					showError();
				}
			} 
		});
		
	}
	
	
};
</script>
</head>
<body class="easyui-layout">
<div data-options="region:'center'" style="border: 0px;height: 100%;">	
	<div style="width: 100%;height: 100%;">
		<table id="datagrid"></table>
	</div>
</div>
	<div id="qualChrtValueEdit" class="easyui-window" data-options="modal:true, closed:true, collapsible:true, minimizable:false, maximizable:false, closable:true" title="新增质量特性限定" 
		style="padding: 20px 70px 0px 70px;">
		<form id="qualChrtValueForm" method="post" enctype="multipart/form-data">
			<input type="hidden" name="QVid" id="QVid"/>
 			<table id="TextBoxesGroup">
				<tr>
					<td align="right">质量特性信息:</td>
					<td align="left">
						<select class="easyui-combogrid" name="qid" id="qid" style="width:200px" required data-options="
									url: '<%=basePath%>/qualChrt/queryList.do',
									panelWidth: 420,
									idField: 'qid',
									textField: 'qualChrtName',
									columns : [[{field : 'product',title : '产品信息',width :200 ,sortable : true,formatter:function(value){return value.productName;}},
												{field : 'qualChrtID',title : '质量特性ID',width : 150,sortable : true},
												{field : 'qualChrtName',title : '质量特性名称',width : 200,sortable : true},
												{field : 'inspectionType',title : '检验类型',width : 200,sortable : true,formatter:function(value,row,index){ 
			    											if(value == '0'){return '计件检验';}
			    											if(value == '1'){return '计点检验';}
													}
												},
												{field : 'inspectionDescription',title : '检验说明',sortable : true}]],
									fitColumns: true,
									rownumbers:true,
									pageSize:10,
									pagination:true
							">
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">特性限定符号0:</td>
					<td align="left">
						<select class="easyui-combobox" name="QualChrtValSymbol" id="QualChrtValSymbol0"style="width: 100px;height: 20px;" data-options="panelHeight:'auto'" required="required">
        					<option value="01" selected="selected">=</option>
        					<option value="02"><</option>
        					<option value="03">></option>
        					<option value="04"><=</option>
        					<option value="05">>=</option>
      					</select>
					</td>
				</tr>
				<tr>
					<td align="right">特性限定值0:</td>
					<td align="left">
						<input class="easyui-validatebox textbox"  name="QualChrtValue" id="QualChrtValue0" style="width: 200px;height: 20px;"  required></input>
					</td>
				</tr>
				<tr>
					<td align="right">特性单位0:</td>
					<td align="left">
						<input class="easyui-validatebox textbox"  name="QualChrtValUnit" id="QualChrtValUnit0" style="width: 200px;height: 20px;"  required></input>
					</td>
				</tr>
			</table>
				<div align="center" style="colspan:'2'; height:'50'">
						<input type='button' value='Add Button' id='addButton'>
				</div>
				<div align="center" style="colspan:'2'; height:'50'">
						<a class="easyui-linkbutton" href="javascript:void(0);" onclick="saveQualChrt();" style="width:60px">保存</a>
						<a class="easyui-linkbutton" href="javascript:void(0);" onclick="$('#qualChrtValueEdit').window('close');" style="width:60px">取消</a>
				</div>
		</form>
	</div>
</body>
</html>	