<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../include/header.jsp" %>

<div class="container">
	<div class="row">
		<form role="form" method="post">
			<input type="hidden" name="bno" value="${boardVO.bno}"/>
		</form>
	</div>
	
	<div class="row">
		<div class="panel panel-success">
			<div class="panel-heading">
				<span class="label label-primary">글번호 : ${boardVO.bno}</span>&nbsp;&nbsp;${boardVO.title}
				<span class="label label-success pull-right" id="writer">${boardVO.writer}</span>
			</div>
			<div class="panel-body">
				${boardVO.content}			
			</div>
		</div>
	</div>

	<div class="row">
		<c:if test="${login.uid == boardVO.writer}">
			<a href="/board/modify/${boardVO.bno}/${cri.page}/${cri.perPageNum}?searchType=${cri.searchType}&keyword=${cri.keyword}" class="btn btn-warning">수정</a>
			<a href="/board/remove/${boardVO.bno}/${cri.page}/${cri.perPageNum}}?searchType=${cri.searchType}&keyword=${cri.keyword}" class="btn btn-danger" id="del">삭제</a>
		</c:if>
		<a href="/board/list/${cri.page}/${cri.perPageNum}?searchType=${cri.searchType}&keyword=${cri.keyword}" class="btn btn-primary" id="list">글목록</a>
	</div>
</div>

<%@ include file="../include/footer.jsp" %>

