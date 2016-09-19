<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>验收抽样检验复合性判定</title>
<jsp:include page="inc/incJs.jsp"></jsp:include>

<script type="text/javascript">
	$(function() {
		$('#ul_menu_tree').tree({/*初始化功能树菜单*/
			animate : true,
			url : '<%=basePath%>/sysCommon/menutree.do',
			checkbox : false,
			onClick : function(node) {
				if (node.url != null) {
					addTab({
						jqObj : $('#tabs_index'),
						url :  '<%=basePath%>' + node.url,
						title : node.text
					});
				}
			}
		});
		showindex();
		
		var reportid = '${reportid}';
		if(reportid != ""){
			addTab({
				jqObj : $('#tabs_index'),
				url :  '<%=basePath%>/report/queryReportByUser.do?id=${reportid}',
				title : '${title}'
			}); 
		}
	});
	
	function showindex(){
		$.messager.show({
			title:'消息',
			timeout:1000,
			msg:"欢迎登录",
			showType:'show'
		});
	}
	
	function relogin(){
		$.messager.confirm('请确认','您要退出系统？',function(r) {
			if(r){
				$.ajax({
					url : '<%=basePath%>/sysCommon/logout.do',
					dataType:'text',
					success : function() {
						window.location.href = "<%=basePath%>/sysCommon/login.do";//跳到登陆页面重新登陆
					}
				});
			}
		}); 
	}

function updatePassword(){
	$('#form_edit').form('clear');
	$('#password_edit').window('open');
}

function save(){
	var flag = $('#form_edit').form('validate');
	if(flag){
		var password = $('#form_edit [name=password]').val();
		var password_new1 = $('#form_edit [name=password_new1]').val();
		var password_new2 = $('#form_edit [name=password_new2]').val();
		$.ajax({
			url : '<%=basePath%>/sysCommon/updatePassword.do',
			data : {
				'password':password,
				'password_new1':password_new1,
				'password_new2':password_new2
			},
			type: 'post',
			dataType:'json',
			success : function(data) {
				if (data.success) {
					alert("修改成功，请重新登陆！");
					window.location.href = "<%=basePath%>/sysCommon/login.do";//跳到登陆页面重新登陆
				} else {
					$.messager.alert('提示',data.msg, 'error');
				}
			}
		});
	}
}
</script>
</head>
<body class="easyui-layout">
	<div data-options="region:'north'" style="height:90px;background-image: url('<%=basePath%>/images/top2.png');background-repeat:repeat-x;">
		<div style="float: right;margin:15px 40px;">
	 		<div style="float: left;"><%-- <img src="<%=basePath%>/images/yonghu.png"> --%></div><div style="float: left;font-size: 14px;margin-top: 2px;color: white;">欢迎您：${sessionScope.user.name}</div>
	 		<div style="float: left;margin-left: 30px;"><div style="float: left;margin-top:2px;font-size: 14px;cursor: pointer;color: white;" onclick="updatePassword();">修改密码</div></div>
	 		<div style="float: left;margin-left: 30px;"><%-- <img src="<%=basePath%>/images/tuichu.png"> --%></div><div style="float: right;font-size: 14px;margin-top:2px;cursor: pointer;color: white;"  onclick="javascript:relogin();">退出</div>
	 	</div>
	</div>
	
	<div data-options="region:'west',split:true" title="菜单" style="width:180px;">
		<ul id="ul_menu_tree"></ul>
	</div>
	
	<div data-options="region:'center'" style="background-color: #f0f0f0;">
		<div class="easyui-tabs" id="tabs_index" data-options="fit:true,border:false" >
			<div title="首页" style="padding: 5px;text-align:center;background-color: #f0f0f0;margin-top: 15px">
				<img alt="首页" src="<%=basePath%>/images/292-11031406360670.jpg">
				
				<!-- <span style="font-size: 20px;">欢迎使用GB/T 2828.1 按接收质量限检索的 逐批检验 分析系统
				</span>background-image: url('<%=basePath%>/images/292-11031406360670.jpg'); -->
			</div>
		</div>
	</div>
	
	
	
	<div id="password_edit" class="easyui-window" data-options="modal:true, closed:true, collapsible:true, minimizable:false, maximizable:false, closable:true" title="修改密码" style="padding: 20px 70px 0px 70px;">
		<form id="form_edit" method="post" enctype="multipart/form-data">
			<table>
				<tr>
					<td align="right">原密码：</td>
					<td align="left">
						<input class="easyui-validatebox textbox" type="password"  name="password" style="width: 200px;height: 20px;"  maxlength="32" required></input>
					</td>
				</tr>
				<tr>
					<td align="right">新密码：</td>
					<td align="left">
						<input class="easyui-validatebox textbox" type="password"  name="password_new1" style="width: 200px;height: 20px;"  maxlength="32" required></input>
					</td>
				</tr>
				<tr>
					<td align="right">确认新密码：</td>
					<td align="left">
						<input class="easyui-validatebox textbox" type="password"  name="password_new2" style="width: 200px;height: 20px;"  maxlength="32" required></input>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="2" height="50">
						<a class="easyui-linkbutton" href="javascript:void(0);" onclick="save();" style="width:60px">保存</a>
						<a class="easyui-linkbutton" href="javascript:void(0);" onclick="$('#password_edit').window('close');" style="width:60px">取消</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>