<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<%
	String loginname = "";
	Cookie[] cookies = request.getCookies();
	if (cookies != null) {
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals("loginname")) {
				loginname = cookies[i].getValue();
				request.setAttribute("loginname", loginname); //存用户名
			}
		}
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>欢迎登录抽样管理系统</title>
<link href="<%=basePath%>/css/style.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript" src="<%=basePath%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/cloud.js"></script>

<script type="text/javascript">
	$(function() {
		$('.loginbox').css({
			'position' : 'absolute',
			'left' : ($(window).width() - 692) / 2
		});
		$(window).resize(function() {
			$('.loginbox').css({
				'position' : 'absolute',
				'left' : ($(window).width() - 692) / 2
			});
		})
	});
</script>
<script type="text/javascript">
function login(){
	   var name = document.getElementById("username").value;
	   var pwd = document.getElementById("pasId").value;
	   if(name == "" || name == "请输入用户名" || pwd == "" || pwd == "请输入密码"){
			alert("用户名或密码不能为空");
	   }else{
		   $.ajax({
				url : '<%=basePath%>/sysCommon/loginCheck.do',
				data : {
					'loginname' : name,
					'loginpwd' : pwd
				},
				dataType:'json',
				success : function(data) {
					if (data.success) {
						window.location.href = '<%=basePath%>/sysCommon/main.do';
							} else {
								alert("用户名或密码错误");
							}
						}
					});
		}
	};
	
</script>

</head>

<body
	style="background-color: #df7611; background-image: url(images/light.png); background-repeat: no-repeat; background-position: center top; overflow: hidden;">

	<div id="mainBody">
		<div id="cloud1" class="cloud"></div>
		<div id="cloud2" class="cloud"></div>
	</div>

	<div class="logintop">
		<span>欢迎登录抽样检测系统</span>
		<ul>
			<li><a href="#">回首页</a></li>
			<li><a href="#">帮助</a></li>
			<li><a href="#">关于</a></li>
		</ul>
	</div>

	<div class="loginbody">

		<span class="systemlogo"></span>

		<div class="loginbox">

			<ul>
				<li><input type="text" class="loginuser" value="${loginname }"
					id="username" /></li>
				<li><input type="password" class="loginpwd" value="密码"
					id="pasId" /></li>
				<li><input type="button" class="loginbtn" value="登录"
					onclick="login();" /><label><input type="checkbox"
						checked="checked" />记住密码</label><label><a href="#">忘记密码？</a></label></li>
			</ul>


		</div>

	</div>



	<div class="loginbm">Copyright© 2016 中国标准化研究院 版权所有
		京ICP备05019125号-10</div>

</body>

</html>
