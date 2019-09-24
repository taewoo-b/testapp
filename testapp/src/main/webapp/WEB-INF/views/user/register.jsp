<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="../include/header.jsp" %>

<div class="container">
	<div class="row">
		<div class="page-header">
			<h1>회원가입<small>환영합니다.</small></h1>
		</div>
	</div>
	
	<div class="row">
		<div class="col-md-10 col-md-offset-1">
			<form method="post">
				<div class="form-group">
					<label for="ruid">아이디</label>
					<input id="ruid" name="uid" class="form-control" type="text" 
						length="10" placeholder="아이디를 입력하세요 (10글자 이내)" required/>
				</div>
				<div class="form-group">
					<label for="rupw">비밀번호</label>
					<input id="rupw" name="upw" class="form-control" type="password" 
						length="20" placeholder="비밀번호를 입력하세요 (20글자 이내)" required/>
				</div>
				
				<div class="form-group">
					<label for="rupwc">비밀번호 확인</label>
					<input id="rupwc" name="upwc" class="form-control" type="password" 
						length="20" placeholder="비밀번호를 한번 더 입력하세요 (20글자 이내)" required/>
				</div>
				
				<div class="form-group">
					<label for="runame">사용자 이름</label>
					<input id="runame" name="uname" class="form-control" type="text" 
						length="10" placeholder="아이디를 입력하세요 (10글자 이내)" required/>
				</div>
				
				<button type="submit" class="btn btn-default">가입하기</button>
			</form>
		</div>
	</div>
</div>

<%@ include file="../include/footer.jsp" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js"></script>

<script>
function clearMsg(){
	$(".alert").fadeOut();
}

$(document).ready(function(){
	$("form").on("submit", function(e){
		var uid = $("#ruid").val();
		var upw = $("#rupw").val();
		var upwc = $("#rupwc").val();
		var uname = $("#runame").val();
		var success = true;
		var target;
		
		var source = "<div class='alert alert-warning' role='alert'><strong>{{cat}}</strong> {{msg}}</div>";
		var template = Handlebars.compile(source);
		var data = {cat:"주의", msg:""};
		
		if(uid.length <= 0 || uid.length > 10) {
			data.msg = "아이디 입력이 올바르지 않습니다";
			success = false;
			target = $("#ruid");
		}
		if(upw != upwc || upw.length < 5){
			data.msg = "비밀번호가 너무 짧거나 일치하지 않습니다";
			success = false;
			target = $("#rupw");
		}
		if(uname.length < 3) {
			data.msg = "사용자 이름은 최소 3글자 이상입니다"
			success = false;
			target = $("#runame");
		}
			
		if(!success){
			var warningHTML = $( template(data) );
			target.parent().after(warningHTML);
			setTimeout("clearMsg()", 2000);
			e.preventDefault();
			return;
			}
		});
	});
	
	$(".file-attachments").on("click", ".delbtn", function(){
		var parent = $(this).parent().parent(); //li 태그
	
		$.ajax({
			url:"/deleteFile",
			type:"post",
			data:{fileName:$(this).attr('data-src')},
			dataType:'text',
			success:function(data){
				if(data == 'deleted'){
					parent.fadeOut(300, function(){parent.remove();});
				}else {
					alert(data);
				}
			}
		
		});
	});


</script>

<script src="/resources/js/upload.js"></script>
