<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include/header.jsp" %>

<div class="container">
	<div class="row">
		<div class="page-header">
			<h1>글 등록 페이지<small>자신의 글은 자신의 얼굴입니다</small></h1>
		</div>
	</div>
	<div class="row">
		<div class="col-md-10 col-md-offset-1">
			<form class="form-horizontal" method="post"><!--form 태그 시작-->
				<div class="form-group">
					<label class="col-md-2 control-label" for="title">글제목</label>
					<div class="col-md-10">
						<input type="text" name="title" id="title" class="form-control"
							placeholder="글 제목을 입력하세요" required />
					</div>
				</div>
				<div class="form-group">
					<label for="info" class="control-label col-md-2">글내용</label>
					<div class="col-md-10 col-md-offset-2">
						<textarea class="form-control" id="content" name="content" rows="5"></textarea>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label" for="writer">글쓴이</label>
					<div class="col-md-4">
						<input type="text" name="writer" id="writer" class="form-control"
							value="${login.uid}" readonly="readonly" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-2 col-md-offset-2">
						<button type="submit" class="btn btn-primary">작성완료</button>
					</div>
				</div>
			</form><!-- form 태그 끝 -->
		</div>
	</div>
</div>


<%@ include file="../include/footer.jsp" %>
