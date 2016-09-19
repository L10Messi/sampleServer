<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
		url : '<%=basePath%>/qualChrt/queryList.do',
		idField : 'qid',
		pageSize:'20',
		title:'质量特性列表',
		pagination : true,
		fit : true,
		rownumbers:true,
		toolbar : [
					{
						text : '添加',
						iconCls : 'icon-add',
						handler : function() {
							$('#qualChrtForm').form('clear');
							$('#qualChrtEdit').window('open');
							$('input[name=InspectionType]').get(0).checked = true; 
							$('#qualChrtEdit').window('move',{
								left:300,
								top:10
								});
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
								$('#qualChrtForm').form('clear');
								$('#qualChrtForm').form('load',{
									qid:rows[0].qid,
									pid:rows[0].product.pid,
									QualChrtID:rows[0].qualChrtID,
									QualChrtName:rows[0].qualChrtName,
									InspectionType:rows[0].inspectionType,
									InspectionDescription:rows[0].inspectionDescription
								});
								$('#qualChrtEdit').window('open');
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
											ids.push(rows[i].qid);
										}
										$.ajax({
												url : '<%=basePath%>/qualChrt/delete.do',
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
			field : 'qid',
			title : '序号',
			sortable : true/* ,
			checkbox : true */
		}, {
			field : 'product',
			title : '产品',
			align:'center',
			width : 100,
			sortable : true,
			formatter:function(value){ 
			    return value.productName;
			}
			
		},{
			field : 'qualChrtID',
			title : '质量特性ID',
			align:'center',
			width : 150,
			sortable : true
		},{
			field : 'qualChrtName',
			title : '质量特性名称',
			align:'center',
			width : 200,
			sortable : true
		},{
			field : 'inspectionType',
			title : '检验类型',
			align:'center',
			width : 200,
			sortable : true,
			formatter:function(value,row,index){ 
			    if(value == "0"){ 
			        return "计件检验"; 
			    }
			    if(value == "1"){
			    	return "计点检验";
			    }
			}
		},{
			field : 'inspectionDescription',
			title : '检验说明',
			align:'center',
			sortable : true
		}]]
	});
});

//对质量特性ID进行处理,实现添加时质量特性自动生成
function calQualChrtId(){
	var pid=$('#pid').combogrid('grid').datagrid('getSelected').pid;
	$.ajax({
		url : '<%=basePath%>/qualChrt/qualchrtId.do',
		data : {'ids': pid},
		type: 'post',
		dataType:'text',
		success : function(response) {
			$('#QualChrtID').val(response);
			},
		error:function() {
			$.messager.alert('提示',response.msg, 'error');
        	}
	})
}

//文本框自动添加
var counter = 1;
$(document).ready(function(){
    var InputsWrapper = $("#TextBoxesGroup");
    $("#addButton").click(function () {
		if(counter>1){
            alert("请到质量特性限定管理界面批量添加");
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

/* 存储质量特性 */
function saveQualChrt(){
	var flag = $('#qualChrtForm').form('validate'); 
	var pid=$('#pid').combogrid('grid').datagrid('getSelected').pid;
	if(!flag){
		return false;
	}else{
	   $.ajax({
			url : '<%=basePath%>/qualChrt/save.do',
			data : $('#qualChrtForm').serialize(),
			type: 'post',
			dataType:'json',
			success : function(data) {
				if (data.success) {
						var flagOne = $('#qualChrtValForm').form('validate'); 
					   	if(flagOne){
					   		alert(counter);
					   		for(i=0;i<counter;i++){
								var obj=new Object();
								obj.QVid=$('#QVid').val();
								/* obj.QualChrtID=$('#QualChrtID').combogrid('grid').datagrid('getSelected').qualChrtID; 
								+i+':selected').val();*/
								/* obj.qVid=$('#QualChrtID').val(); */
								obj.QualChrtID=$('#QualChrtID').val();
								alert(QualChrtID);
								obj.QualChrtValSymbol=$('#QualChrtValSymbol'+i).combobox('getValue')
								obj.QualChrtValue=$('#QualChrtValue'+i).val();
								obj.QualChrtValUnit=$('#QualChrtValUnit'+i).val();
								var myString = $.param(obj);
								alert(myString); 
								
								$.ajax({
									url : '<%=basePath%>/qualChrtValue/savebyQC.do',
									data : myString,
									type: 'post',
									dataType:'json',
									success : function(data) {
										if (data.success) {
											/* $('#datagrid').datagrid('reload');
											$('#datagrid').datagrid('unselectAll');
											$('#qualChrtEdit').window('close'); */
											showSuccess();
										} else {
											showError();
										}
									}
								});
					   		}
					   		$('#datagrid').datagrid('reload');
							$('#datagrid').datagrid('unselectAll');
							$('#qualChrtEdit').window('close');
							showSuccess(); 	
				} else {
					showError();
				}
			}
			}
		});
	}
	<%-- var flagOne = $('#qualChrtValForm').form('validate'); 
	alert(flagOne);
   	if(flagOne){
   		for(i=0;i<counter;i++){
			var obj=new Object();
			obj.QVid=$('#QVid').val();
			/* obj.QualChrtID=$('#QualChrtID').combogrid('grid').datagrid('getSelected').qualChrtID; 
			+i+':selected').val();*/
			/* obj.qVid=$('#QualChrtID').val(); */
			obj.QualChrtID=$('#QualChrtID').val();
			obj.QualChrtValSymbol=$('#QualChrtValSymbol'+i).combobox('getValue')
			obj.QualChrtValue=$('#QualChrtValue'+i).val();
			obj.QualChrtValUnit=$('#QualChrtValUnit'+i).val();
			var myString = $.param(obj);
			alert(myString); 
			
			$.ajax({
				url : '<%=basePath%>/qualChrtValue/save.do',
				data : myString,
				type: 'post',
				dataType:'json',
				success : function(data) {
					if (data.success) {
						$('#datagrid').datagrid('reload');
						$('#datagrid').datagrid('unselectAll');
						$('#qualChrtEdit').window('close');
						showSuccess();
					} else {
						showError();
					}
				}
			});
		}
   	}else{
   		return flase;
   	}
	 --%>
}
	
</script>
</head>
<body class="easyui-layout">
	<div data-options="region:'center'" style="border: 0px; height: 100%;">
		<div style="width: 100%; height: 100%;">
			<table id="datagrid"></table>
		</div>
	</div>
	<div id="qualChrtEdit" class="easyui-window"
		data-options="modal:true, closed:true, collapsible:true, minimizable:false, maximizable:false, closable:true"
		title="新增质量特性" style="padding: 20px 70px 0px 70px;">
		<!-- 质量特性表单 -->
		<form id="qualChrtForm" method="post" enctype="multipart/form-data">
			<input type="hidden" name="qid" />
			<table>
				<tr>
					<td align="right">产品信息：</td>
					<td align="left"><select class="easyui-combogrid"
						name="pid" id="pid" style="width: 200px" required
						data-options="
									panelWidth: 420,
									idField: 'pid',
									textField: 'productName',
									url: '<%=basePath%>/product/queryList.do',
									columns : [[{field : 'productID',title : '产品ID',width : 200,sortable : true},
												{field : 'productName',title : '产品名称',width : 200,sortable : true}
												]],
									fitColumns: true,
									rownumbers:true,
									pageSize:10,
									pagination:true
							">
					</select></td>
				</tr>
				<tr>
					<td align="right">质量特性ID：</td>
					<td align="left"><input class="easyui-validatebox textbox"
						name="QualChrtID" id="QualChrtID"style="width: 200px; height: 20px;" required onfocus="calQualChrtId()"></input>
					</td>
				</tr>
				<tr>
					<td align="right">质量特性名称：</td>
					<td align="left"><input class="easyui-validatebox textbox"
						name="QualChrtName" id="QualChrtName" style="width: 200px; height: 20px;" required></input>
					</td>
				</tr>
				<tr>
					<td align="right">检验方式：</td>
					<td align="left">
						<input type="radio" name="InspectionType" id="InspectionType" value="0" ><span>计件检验</span> 
						<input type="radio" name="InspectionType" id="InspectionType" value="1" ><span>计点检验</span>
					</td>
				</tr>
				<tr>
					<td align="right">检验方法说明：</td>
					<td align="left"><input class="easyui-textbox"
						name="InspectionDescription" data-options="multiline:true"
						style="width: 200px; height: 60px"></input><br></td>
				</tr>
				<tr>
					<td><br /></td>
					<td><br /></td>
				</tr>
			</table>
			<!-- <div align="center" style="colspan: '2'; height: '50'">
				<input type='button' value="增加质量特性限定" id='addQualChrtValue'>
			</div> 
		</form>-->
		<!-- 质量特性的值的表单 -->
		<!--  <form id="qualChrtValForm" method="post" enctype="multipart/form-data">
			<input type="hidden" name="QVid" id="QVid" />
			<table id="TextBoxesGroup">
				<tr>
					<td align="right">特性限定符号0:</td>
					<td align="left">
						<select class="easyui-combobox" name="QualChrtValSymbol0" id="QualChrtValSymbol0"style="width: 100px;height: 20px;" data-options="panelHeight:'auto'">
        					<option></option>
        					<option value="01">=</option>
        					<option value="02"><</option>
        					<option value="03">></option>
        					<option value="04"><=</option>
        					<option value="05">>=</option>
      					</select>
					</td>
				</tr>
				<tr>
					<td align="right">特性限定值0:</td>
					<td align="left"><input class="easyui-validatebox textbox"
						name="QualChrtValue" id="QualChrtValue0"
						style="width: 200px; height: 20px;"></input></td>
				</tr>
				<tr>
					<td align="right">特性单位0:</td>
					<td align="left"><input class="easyui-validatebox textbox"
						name="QualChrtValUnit" id="QualChrtValUnit0"
						style="width: 200px; height: 20px;"></input></td>
				</tr>
			</table>
			<div align="center" style="colspan: '2'; height: '50'">
				<input type='button' value='增加质量特性限定' id='addButton'>
			</div>-->
			<div align="center" style="colspan: '2'; height: '50'">
				<a class="easyui-linkbutton" href="javascript:void(0);"
					onclick="saveQualChrt();" style="width: 60px">保存</a> <a
					class="easyui-linkbutton" href="javascript:void(0);"
					onclick="$('#qualChrtEdit').window('close');" style="width: 60px">取消</a>
			</div>
		</form>  
	</div>
</body>
</html>
