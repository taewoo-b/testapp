<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="include/header.jsp" %>

<div class="container">
	<div class="row">
		<div class="col-md-10 col-md-offset-1">
			<div class="panel panel-default">
				<div class="panel-heading">
					${title}
				</div>
				<div class="panel-body">
					<p>${msg}</p>
					<a href="javascript:history.back()" class="btn btn-success">돌아가기</a>
				</div>
			</div>
		</div>
	</div>
</div>

<%@ include file="include/footer.jsp" %>
