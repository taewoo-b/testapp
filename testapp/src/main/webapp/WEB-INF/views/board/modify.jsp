<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../include/header.jsp" %>
<div class="container">
	<div class="row">
		<div class="page-header">
			<h1>글 수정 페이지<small>자신의 글은 자신의 얼굴입니다</small></h1>
		</div>
	</div>
	<div class="row">
		<div class="col-md-10 col-md-offset-1">
			<form role="form" class="form-horizontal" method="post" action="/board/modify"><!--form 태그 시작-->
				<input type="hidden" name="page" value="${cri.page}"/>
				<input type="hidden" name="perPageNum" value="${cri.perPageNum}"/>
				<div class="form-group">
					<label class="col-md-2 control-label" for="bno">글번호</label>
					<div class="col-md-10">
						<input type="text" name="bno" id="bno" class="form-control" value="${boardVO.bno}" readonly="readonly" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label" for="title">글제목</label>
					<div class="col-md-10">
						<input type="text" name="title" id="title" class="form-control"
							placeholder="글 제목을 입력하세요" required value="${boardVO.title}"/>
					</div>
				</div>
				<div class="form-group">
					<label for="info" class="control-label col-md-2">글내용</label>
					<div class="col-md-10 col-md-offset-2">
						<textarea class="form-control" id="content" name="content" rows="5">${boardVO.content}</textarea>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label" for="writer">글쓴이</label>
					<div class="col-md-4">
						<input type="text" name="writer" id="writer" class="form-control"
							readonly="readonly" value="${boardVO.writer}" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-4 col-md-offset-2">
						<button type="button" class="btn btn-primary" id="save">작성완료</button>
						<button type="button" class="btn btn-warning" id="cancel">취소</button>
					</div>
				</div>
			</form><!-- form 태그 끝 -->
		</div>
	</div>
</div>
<input type="hidden" name="searchType" value="${cri.searchType}"/>
<input type="hidden" name="keyword" value="${cri.keyword}" />

<%@ include file="../include/footer.jsp" %>
<script>
$(document).ready(function(){
	var formObj = $("form[role='form']");
	
	$("#save").on("click", function(){
		formObj.submit();
	});
	
	$("#cancel").on("click", function(){
		self.location = "/board/list/${cri.page}/${cri.perPageNum}";
	})
});
</script>

