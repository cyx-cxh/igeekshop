<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head></head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>会员注册</title>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<!-- 引入自定义css文件 style.css -->
<link rel="stylesheet" href="css/style.css" type="text/css" />
<script src="js/jquery.validate.min.js" type="text/javascript" charset="utf-8"></script>
	<!-- 中文包 -->
	<script src="js/messages_zh.js" type="text/javascript" charset="utf-8"></script>
<style>
body {
	margin-top: 20px;
	margin: 0 auto;
}

.carousel-inner .item img {
	width: 100%;
	height: 300px;
}

font {
	color: #3164af;
	font-size: 18px;
	font-weight: normal;
	padding: 0 10px;
}
</style>
<script type="text/javascript">
$(function(){
	$("#registForm").validate({
		rules:{
			"username":{
				"required":true
			},
			"password":{
				"required":true,
				"rangelength":[6,12]
			},
			"confirmpwd":{
				"required":true,
				"rangelength":[6,12],
				"equalTo":"[name='password']"
			},
			"email":{
				"required":true,
				"email":true
			},
			"telephone":{
				"required":true
			},
			"name":{
				"required":true
			},
			"birthday":{
				"required":true,
				"date":true
			},
			"sex":{
				"required":true
			}
			
		},
		messages:{
			"username":{
				"required":"用户名不能为空"
			},
			"password":{
				"required":"密码不能为空",
				"rangelength":"密码长度在6-12位"
			},
			"confirmpwd":{
				"required":"确认密码不能为空",
				"rangelength":"确认密码长度在6-12位",
				"equalTo":"两次密码不一致"
			}, 
			"email":{
				"required":"邮箱不能为空",
				"email":"邮箱格式不正确"
			},
			"telephone":{
				"required":"电话不能为空"
			},
			"name":{
				"required":"真实姓名不能为空"
			},
			"birthday":{
				"required":"生日不能为空",
				"date":"日期格式不正确"
			},
			"sex":{
				"required":"性别必须选择"
			}
		},
		errorPlacement:function(error,element){
			if (element.is(':radio')||element.is(':checkbox')) {
				error.appendTo(element.parent().parent());
			} else{
				error.insertAfter(element);
			}
		}
	
		
	});

	$.validator.addMethod(
			"checkUsername",//自定义校验规则的名称
			function(value,element,params){//自定义校验规则的实现
				//value)表单元素值
				//element)校验的元素对象
				//params)校验规则输入的参数
				var flag=true;//发送一个Ajax，到服务器进行验证用户存在
				$.ajax({
					"async":false,//同步操作
					"url":"${pageContext.request.contextPath}/checkUsername",
					"type":"POST",
					"data":{"username":value},
					"dataType":"json",
					"success":function(data){
						flag=data.isExist;//true--存在 false--不存在
					}
				});
				return !flag;
				
			}
			)

})
	
</script>
</head>
<body>

	<!-- 引入header.jsp -->
	<jsp:include page="/header.jsp"></jsp:include>

	<div class="container"
		style="width: 100%; background: url('image/regist_bg.jpg');">
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8"
				style="background: #fff; padding: 40px 80px; margin: 30px; border: 7px solid #ccc;">
				<font>会员注册</font>USER REGISTER
				<form class="form-horizontal" id="registForm" style="margin-top: 5px;" action="${pageContext.request.contextPath }/user?method=regist" method="post">
					<div class="form-group">
						<label for="username" class="col-sm-2 control-label">用户名</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="username" name="username"
								placeholder="请输入用户名">
						</div>
					</div>
					<div class="form-group">
						<label for="inputPassword3" class="col-sm-2 control-label" >密码</label>
						<div class="col-sm-6">
							<input type="password" class="form-control" id="password" name="password" 
								placeholder="请输入密码">
						</div>
					</div>
					<div class="form-group">
						<label for="confirmpwd" class="col-sm-2 control-label">确认密码</label>
						<div class="col-sm-6">
							<input type="password" class="form-control" id="confirmpwd" name="confirmpwd"
								placeholder="请输入确认密码">
						</div>
					</div>
					<div class="form-group">
						<label for="inputEmail3" class="col-sm-2 control-label">Email</label>
						<div class="col-sm-6">
							<input type="email" class="form-control" id="email" name="email"
								placeholder="Email">
						</div>
					</div>
					<div class="form-group">
					<label for="inputEmail3" class="col-sm-2 control-label">电话</label>
						<div class="col-sm-6">
							<input type="tel" class="form-control" id="telephone" name="telephone"
								placeholder="Telephone">
					</div>
					</div>
					<div class="form-group">
						<label for="usercaption" class="col-sm-2 control-label">姓名</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="name" name="name"
								placeholder="请输入姓名">
						</div>
					</div>
					<div class="form-group opt">
						<label for="inlineRadio1" class="col-sm-2 control-label">性别</label>
						<div class="col-sm-6">
							<label class="radio-inline"> <input type="radio"
								name="sex" id="sex1" value="male">
								男
							</label> <label class="radio-inline"> <input type="radio"
								name="sex" id="sex2" value="female">
								女
							</label>
						</div>
					</div>
					<div class="form-group">
						<label for="date" class="col-sm-2 control-label">出生日期</label>
						<div class="col-sm-6">
							<input type="date" class="form-control" name="birthday">
						</div>
					</div>

					<div class="form-group">
						<label for="date" class="col-sm-2 control-label">验证码</label>
						<div class="col-sm-3">
							<input type="text" class="form-control">

						</div>
						<div class="col-sm-2">
							<img src="./image/captcha.jhtml" />
						</div>

					</div>

					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<input type="submit" width="100" value="注册" name="submit"
								style="background: url('./images/register.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0); height: 35px; width: 100px; color: white;">
						</div>
					</div>
				</form>
			</div>

			<div class="col-md-2"></div>

		</div>
	</div>

	<!-- 引入footer.jsp -->
	<jsp:include page="/footer.jsp"></jsp:include>

</body>
</html>




