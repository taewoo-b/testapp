<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>양영디지털 고등학교</title>

<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
<!-- <link href="/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="/resources/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
<script src="/resources/js/jquery-3.4.1.min.js"></script>
<script src="/resources/js/bootstrap.min.js"></script> -->

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>

</head>
<body>
<div id="head">
	<nav class="navbar navbar-default">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar-col"
					aria-expanded="false">
					<span class="sr-only">Toggle navigation</span> 
					<span class="icon-bar"></span> 
					<span class="icon-bar"></span> 
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">Y-Y</a>
			</div>
	
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse" id="navbar-col">
				<ul class="nav navbar-nav">
					<li><a href="/">메인화면 </a></li>
					<li><a href="/board/list">게시판</a></li>
				</ul>
				
				<ul class="nav navbar-nav navbar-right">
					<c:if test="${empty login}">
						<li class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button">로그인<span class="caret"></span></a>
							<div class="dropdown-menu login-form">
								<form method="post" accept-charset="UTF-8" style="margin:0px;padding:10px;">
									<input type="text" class="form-control" placeholder="아이디" id="uid" name="uid" style="margin-bottom:5px;" >
									<input type="password" class="form-control" placeholder="비밀번호" id="upw" name="upw" style="margin-bottom:5px;" >
									<input type="checkbox" name="useAutoLogin"> 자동로그인
									<button class="btn btn-default btn-block" type="button" id="sign-in">로그인</button>
								</form>
							</div>
						</li>
						<li><a href="/user/register">회원가입</a></li>
					</c:if>

					<c:if test="${not empty login}">
						<li class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button">${login.uname}님(${login.uid})<span class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="#">회원정보</a></li>
								<li><a href="/user/logout">로그아웃</a></li>
							</ul>
						</li>
					</c:if>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
	<!-- /.container-fluid --> 
	</nav>
</div>



<script>
$("#sign-in").on("click", function(){
	var userid = $("#uid").val();
	var userpw = $("#upw").val();
	var autoLogin = $("#useAutoLogin").is(":checked");
	
	$.ajax({
		type:'post',
		url:'/user/loginAjax',
		headers :{
			"Content-Type" : "application/json",
			"X-HTTP-Method-Override" : "POST"
		},
		contentType: 'application/json',
		dataType : 'json',
		data : JSON.stringify({
			uid:userid,
			upw:userpw,
			useAutoLogin:autoLogin
		}),
		success:function(result){
			alert(result.msg);
			if(result.success){
				location.reload();
			}
		}
	})
});
</script>

<c:if test="${!empty result}">
<div class="alert alert-${result eq 'success' ? 'success' : 'danger' } alert-dismissible" role="alert">
	<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	<strong>${result eq 'success' ? '성공' : '실패' }</strong> ${msg}
</div>
</c:if>




