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
				<form class="form-horizontal" method="post" action="/user/loginPost">
					<div class="form-group">
						<label for="uid" class="col-sm-2 control-label">아이디</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="uid" name="uid" placeholder="ID를 입력하세요">
						</div>
					</div>
					<div class="form-group">
						<label for="upw" class="col-sm-2 control-label">비밀번호</label>
						<div class="col-sm-10">
							<input type="password" class="form-control" id="upw" name="upw" placeholder="비밀번호를 입력하세요">
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<div class="checkbox">
								<label> 
									<input type="checkbox" name="useAutoLogin"> 자동로그인
								</label>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="submit" class="btn btn-success">로그인</button>
						</div>
					</div>
				</form>
			</div>
			<div class="panel-footer">
				자동 로그인은 신뢰할 수 있는 곳에서만 사용하세요
			</div>
		</div>
	</div>
	
</div>
<%@ include file="../include/footer.jsp" %>
