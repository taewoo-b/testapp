<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../include/header.jsp" %>

<div class="container">
	<div class="row">
		<div class="panel panel-success">
			<div class="panel-heading">
    			<h3 class="panel-title">로그인 입력</h3>
			</div>
			<div class="panel-body">
				<p>${msg }</p>
				<a href="/user/login" class="btn btn-warning">다시 시도</a>
			</div>
		</div>
	</div>
	
</div>
<%@ include file="../include/footer.jsp" %>
