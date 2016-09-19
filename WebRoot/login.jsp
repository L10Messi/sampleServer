<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>中国标准化研究院检测系统</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript">
	$(function() {
		$('.loginbox0').css({
			'position' : 'absolute',
			'left' : ($(window).width() - 810) / 2
		});
		$(window).resize(function() {
			$('.loginbox0').css({
				'position' : 'absolute',
				'left' : ($(window).width() - 810) / 2
			});
		})
	});
</script>

</head>

<body
	style="background-color: #df7611; background-image: url(images/light1.png); background-repeat: no-repeat; background-position: center top; overflow: hidden;">


	<div class="logintop">
		<span>欢迎进入检测管理系统</span>
		<ul>
			<li><a href="#">回首页</a></li>
			<li><a href="#">帮助</a></li>
			<li><a href="#">关于</a></li>
		</ul>
	</div>

	<div class="loginbody">

		<span class="systemlogo" style="font-size:40px; color:#fff;">质量统计分析云服务系统</span> 

		<div class="loginbox0">

			<ul class="loginlist">
				<li><a href="samp_1.jsp"><img src="images/l01.png" alt="GB/T 2828.1 按接收质量限检索的逐批检验" />
						<span>按接收质量限检索的<br/>逐批检验
						</span>
					</a>
				</li>
				<li><a href="login2.html"><img src="images/l02.png" alt="GB/T 2828.4 声称质量水平的评定" />
					<span>声称质量水平的评定
					</span>
					</a>
				</li>
				
				<li><a href="login3.html"><img src="images/l03.png"alt="JJF 1059.1 测量不确定度计算分析系统" />
						<span>测量不确定度<br/>计算分析系统
						</span>
					</a></li>
			</ul>
		</div>

	</div>

	<div class="loginbm">Copyright© 2016 中国标准化研究院 版权所有
		京ICP备05019125号-10</div>


</body>

</html>