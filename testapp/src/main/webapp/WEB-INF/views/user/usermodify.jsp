<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../include/header.jsp" %>
<div class="container">
	<div class="row">
		<div class="page-header">
			<h1>회원정보 페이지<small>자신의 글은 자신의 얼굴입니다</small></h1>
		</div>
	</div>
	<div class="row">
		<div class="col-md-10 col-md-offset-1">
			<form id="usermodifyForm" role="form" class="form-horizontal" method="post"><!--form 태그 시작-->

				
				<div class="form-group">
					<label class="col-md-2 control-label" for="bno">아이디</label>
					<div class="col-md-10">
						<input type="text" name="uid" id="uid" class="form-control" value="${login.uid}" readonly="readonly" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label" for="title">비밀번호</label>
					<div class="col-md-10">
						<input type="password" name="upw" id="upw" class="form-control"value="${login.upw }"readonly="readonly"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">이름</label>
					<div class="col-md-10">
						<input type="text" name="uname"id="uname"class="form-control"value="${login.uname}"readonly="readonly">
					</div>
				</div>

				<div class="form-group">
					<div class="col-md-4 col-md-offset-2"id="test_div"style="display:none;">
						<button type="button" class="btn btn-primary" id="save">작성완료</button>
						<button type="button" class="btn btn-warning" id="cancel">취소</button>
					</div>
					<div class="col-md-4 col-md-offset-2" id="test">
						<button type="button" class="btn btn-primary" id="v"onclick="div_show();">수정하기</button>
 						<!--  <button type="button" id="h"onclick="div_hide();">숨기기</button>-->
 					</div>
				</div>
			</form><!-- form 태그 끝 -->
		</div>
	</div>
</div>

<%@ include file="../include/footer.jsp" %>
<script type="text/javascript">
 //보이기
 function div_show() {
	document.getElementById("test_div").style.display = "block";
	document.all.uname.readOnly=false
	document.all.upw.readOnly=false
	document.getElementById("test").style.display = "none";
 }
  //숨기기
//	function div_hide() {
//	document.getElementById("test_div").style.display = "none";
//	}
</script>
<script>
	$(document).ready(function(){
		var formObj = $("form[role='form']");
	
		$("#save").on("click", function(){
			formObj.submit();
			

		});
		
		$("#cancel").on("click", function(){
			self.location = "/";
		});
		
		$("#usermodifyForm").on("submit", function(e){
			e.preventDefault();
			
			var usermodifyForm = $(this);
			var str = "";
			
			usermodifyForm.append(str);
			
			usermodifyForm.get(0).submit();
			
		});
	})
	
</script>