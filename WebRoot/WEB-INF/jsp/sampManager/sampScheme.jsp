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
		url : '<%=basePath%>/sampscheme/queryList.do',
		idField : 'SSid',
		pageSize:'20',
		title:'抽样计划列表',
		pagination : true,
		fit : true,
		rownumbers:true,
		toolbar : [
					{
						text : '添加',
						iconCls : 'icon-add',
						handler : function() {
							$('#SampSchemeForm').form('clear');
							$('#SampSchemeEdit').window('open');
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
								$('#SampSchemeForm').form('clear');
								$('#SampSchemeForm').form('load',{
									SSid:rows[0].SSid,
									pid:rows[0].product.pid,
									qid:rows[0].qualChrt.qid,
									SampSchemeID:rows[0].sampSchemeID,
									IL:rows[0].IL,
									AQL:rows[0].AQL,
									LotSize:rows[0].lotSize,
									SeverityLevel:rows[0].severityLevel,
									NPlanType:rows[0].NPlanType,
									TPlanType:rows[0].TPlanType,
									RPlanType:rows[0].RPlanType,
									initStringency:rows[0].initStringency,
									needReduced:rows[0].needReduced,
									Remark:rows[0].remark
								});
								$('#SampSchemeEdit').window('open');
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
											ids.push(rows[i].SSid);
										}
										$.ajax({
												url : '<%=basePath%>/sampscheme/delete.do',
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
					}],
	    columns : [[{
			field : 'SSid',
			title : '序号',
			sortable : true,
			checkbox : true
		},{
			field : 'product',
			title : '产品',
			width : 100,
			align:'center',
			sortable : true,
			formatter:function(value,rows,index){
				return value.productName;
			}
		},{
			field : 'qualChrt',
			title : '质量特性',
			width : 100,
			align:'center',
			sortable : true,
			formatter:function(value,rows,index){
				return value.qualChrtName;
			}
		},{
			field : 'sampSchemeID',
			title : '抽样计划ID',
			width : 200,
			align:'center',
			sortable : true
		},{
			field : 'lotSize',
			title : '批量',
			width : 60,
			align:'center',
			sortable : true
		},{
			field : 'IL',
			title : '检验水平',
			width : 100,
			sortable : true,
			align:'center',
			formatter:function(value,row,index){ 
			    if(value == 0){ 
			        return "特殊S_1"; 
			    }
			    if(value == 1){
			    	return "特殊S_2";
			    }
			    if(value == 2){
			    	return "特殊S_3";
			    }
			    if(value == 3){
			    	return "特殊S_4";
			    }
			    if(value == 4){
			    	return "一般I";
			    }
			    if(value == 5){
			    	return "一般II";
			    }
			    if(value == 6){
			    	return "一般III";
			    }
			}
		},{
			field : 'AQL',
			title : '接收质量限',
			width : 100,
			align:'center',
			sortable : true
		},{
			field : 'severityLevel',
			title : '严重程度',
			width : 100,
			align:'center',
			sortable : true,
			formatter:function(value,row,index){ 
			    if(value == 0){ 
			        return "A"; 
			    }
			    if(value == 1){
			    	return "B";
			    }
			    if(value == 2){
			    	return "C";
			    }
			}
		},{
			field : 'NPlanType',
			title : '正常检验类型',
			width : 80,
			align:'center',
			sortable : true,
			formatter:function(value,row,index){ 
			    if(value == 0){ 
			        return "一次"; 
			    }
			    if(value == 1){
			    	return "二次";
			    }
			    if(value == 2){
			    	return "多次";
			    }
			    if(value == 3){
			    	return "全检验";
			    }
			}
		},/* {
			field : 'TPlanType',
			title : '加严检验类型',
			width : 80,
			align:'center',
			sortable : true,
			formatter:function(value,row,index){ 
			    if(value == 0){ 
			        return "一次"; 
			    }
			    if(value == 1){
			    	return "二次";
			    }
			    if(value == 2){
			    	return "多次";
			    }
			    if(value == 3){
			    	return "全检验";
			    }
			}
		},{
			field : 'RPlanType',
			title : '放宽检验类型',
			width : 80,
			align:'center',
			sortable : true,
			formatter:function(value,row,index){ 
			    if(value == 0){ 
			        return "一次"; 
			    }
			    if(value == 1){
			    	return "二次";
			    }
			    if(value == 2){
			    	return "多次";
			    }
			    if(value == 3){
			    	return "全检验";
			    }
			}
		}, */{
			field : 'needReduced',
			title : '放宽检验',
			width : 80,
			align:'center',
			sortable : true,
			formatter:function(value,row,index){ 
			    if(value == 0){ 
			        return "否"; 
			    }
			    if(value == 1){
			    	return "是";
			    }
			}
		},{
			field : 'initStringency',
			title : '初始严格性',
			width : 80,
			align:'center',
			sortable : true,
			formatter:function(value,row,index){ 
			    if(value == 0){ 
			        return "正常"; 
			    }
			    if(value == 1){
			    	return "加严";
			    }
			    if(value == 2){
			    	return "放宽";
			    }
			}
		},{
			field : 'remark',
			title : '抽样计划说明',
			width : 200,
			sortable : true
		}]]
	});
	
});

function save(){
	var flag = $('#SampSchemeForm').form('validate');
	if(!flag){
		return false;
	}
	alert($('#SampSchemeForm').serialize());
    $.ajax({
		url : '<%=basePath%>/sampscheme/save.do',
		data : $('#SampSchemeForm').serialize(),
		type: 'post',
		dataType:'json',
		success : function(data) {
			if (data.success) {
				$('#datagrid').datagrid('reload');
				$('#datagrid').datagrid('unselectAll');
				$('#SampSchemeEdit').window('close');
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
	<div id="SampSchemeEdit" class="easyui-window" data-options="modal:true, closed:true, collapsible:true, minimizable:false, maximizable:false, closable:true" title="抽样计划创建" style="padding: 20px 70px 0px 70px;">
		<form id="SampSchemeForm" method="post" enctype="multipart/form-data">
			<input type="hidden" name="SSid"/>
			<input type="hidden" name="SampSchemeID"/>
			<table>
			<tr>
					<td align="right">产品信息：</td>
					<td align="left">
						<select class="easyui-combogrid" name="pid" id="pid" style="width:200px" required data-options="
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
									pagination:true,
									onHidePanel:function(){
										var record = $('#pid').combobox('getValue'); 
										alert(record);
										$.ajax({
											type : 'POST',
											url:'<%=basePath%>/qualChrt/getQualChrtList.do?pid='+ record,
											cache: false,  
											dataType : 'json', 
											success : function(data) {
												$('#qid').combogrid('grid').datagrid('loadData', data);
											 } 
										});
									}
							" >
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">质量特性信息：</td>
					<td align="left">
						<select class="easyui-combogrid" name="qid" id="qid" style="width:200px" required data-options="
									panelWidth: 420,
									idField: 'qid',
									textField: 'qualChrtName',
									columns : [[
									{field : 'product',title : '产品信息',width : 150,sortable : true,formatter:function(value){return value.productName;}},
									{field : 'qualChrtID',title : '质量特性ID',width : 150,sortable : true},
									{field : 'qualChrtName',title : '质量特性名称',width : 200,sortable : true},
									{field : 'inspectionType',title : '检验类型',width : 200,sortable : true,formatter:function(value,row,index){ 
			    											if(value == '0'){return '计件检验';}
			    											if(value == '1'){return '计点检验';}
													}
									}
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
					<td align="right">批量:</td>
					<td align="left">
						<input class="easyui-validatebox textbox"  name="LotSize" id="LotSize" style="width: 200px;height: 20px;" required="required" ></input>
					</td>
				 <tr>
    				<td align="right">检验水平(IL):</td>
   					<td>
      					<select class="easyui-combobox" name="IL" id="IL" style="width: 100px;height: 20px;" data-options="panelHeight:'auto'">
        					<option value="0">特殊S_1</option>
        					<option value="1">特殊S_2</option>
        					<option value="2">特殊S_3</option>
        					<option value="3">特殊S_4</option>
        					<option value="4">一般|</option>
        					<option value="5">一般||</option>
        					<option value="6">一般|||</option>
      					</select>
    				</td>
  				</tr>
  				<tr>
    				<td align="right">接收质量限(AQL):</td>
    				<td>
      					<select class="easyui-combobox" name="AQL" id="AQL" style="width: 100px;height: 20px;" >
       						<option value="0.01">0.01%</option>
       						<option value="0.025">0.025%</option>
        					<option value="0.04">0.04%</option>
        					<option value="0.065">0.065%</option>
       						<option value="0.1">0.1%</option>
        					<option value="0.15">0.15%</option>
        					<option value="0.25">0.25%</option>
        					<option value="0.4">0.4%</option>
        					<option value="0.65">0.65%</option>
        					<option value="1">1%</option>
       	 					<option value="1.5">1.5%</option>
        					<option value="2.5">2.5%</option>
        					<option value="4">4%</option>
        					<option value="6.5">6.5%</option>
        					<option value="10">10%</option>
      					</select>
   	 				</td>
  				</tr>
  				<tr>
    				<td align="right">严重程度:</td>
    				<td>
      					<select class="easyui-combobox" name="SeverityLevel" id="SeverityLevel" style="width: 100px;height: 20px;" data-options="panelHeight:'auto'">
        					<option value="0" >A</option>
        					<option value="1">B </option>
        					<option value="2">C </option>
      					</select>
    				</td>
			  	</tr>
  				<tr>
    				<td align="right">正常检验:</td>
					<td>      
						<select class="easyui-combobox" name="NPlanType" id="NPlanType" style="width: 100px;height: 20px;" data-options="panelHeight:'auto'">
        					<option value="0">一次检验</option>
        					<option value="1">二次检验</option>
        					<option value="2">多次检验</option>
        					<option value="3">全检验</option>
      					</select>
     				</td>
     			</tr>
     			<tr>
     				<td align="right">加严检验:</td>
					<td> 
      					<select class="easyui-combobox" name="TPlanType" id="TPlanType" style="width: 100px;height: 20px;" data-options="panelHeight:'auto'">
       						<option value="0">一次检验</option>
        					<option value="1">二次检验</option>
        					<option value="2">多次检验</option>
        					<option value="3">全检验</option>
      					</select>
      				</td>
      			</tr>	
      			<tr> 
      				<td align="right">放宽检验:</td>
					<td> 
      					<select class="easyui-combobox" name="RPlanType" id="RPlanType" style="width: 100px;height: 20px;" data-options="panelHeight:'auto'">
       						<option value="0">一次检验</option>
        					<option value="1">二次检验</option>
        					<option value="2">多次检验</option>
        					<option value="3">全检验</option>
      					</select>
    				</td>   
 				</tr>
  				<tr>
    				<td align="right">初始严格性:</td>
    				<td colspan="2">
      					<select class="easyui-combobox" name="initStringency" id="initStringency"  style="width: 100px;height: 20px;" data-options="panelHeight:'auto'">
        					<option value="0">正常检验</option>
        					<option value="1">加严检验</option>
        					<option value="2">放宽检验</option>
        				</select>
   					 <input type="checkbox" name="needReduced" id="needReduced" value="1" > 
							需要放宽检验<!-- 0否，1是 -->
     				</td>    
  				</tr>
  				<tr>
					<td align="right">抽样计划说明:</td>
					<td align="left">
						<input class="easyui-textbox"  name="Remark" id="Remark" data-options="multiline:true" style="width: 200px;height: 60px;" ></input>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="2" height="50">
						<a class="easyui-linkbutton" href="javascript:void(0);" onclick="save();" style="width:60px">保存</a>
						<a class="easyui-linkbutton" href="javascript:void(0);" onclick="$('#SampSchemeEdit').window('close');" style="width:60px">取消</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>