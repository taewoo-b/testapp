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
			<form id="modifyForm" role="form" class="form-horizontal" method="post"><!--form 태그 시작-->
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
					<div class="col-md-10 col-md-offset-2">
						<label for="fileDrop">업로드할 파일을 드래그 드롭 하세요</label>
					</div>
					<div class="col-md-10 col-md-offset-2">
						<div class="fileDrop"></div>
					</div>
				</div>
				
				<ul class="file-attachments clearfix">
				</ul>
				
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

<%@ include file="../include/footer.jsp" %>
<!-- 핸들바 추가 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js"></script>
<script id="fileTemplate" type="text/x-handlebars-template">
	<li>
		<span class="file-attachment-icon has-img">
			<img src="{{imgsrc}}">
		</span>
		<div class="file-attachment-info">
			<a href="{{getLink}}" class="file-attachment-name">{{fileName}}</a>
			<button type='button' data-src="{{fullName}}" class="btn btn-default btn-xs pull-right delbtn">
				<i class="fa fa-fw fa-remove"></i>
			</a>
		</div>
	</li>
</script>
<script src="/resources/js/upload.js"></script>
<script>
$(document).ready(function(){
	var formObj = $("form[role='form']");
	
	$("#save").on("click", function(){
		formObj.submit();
	});
	
	$("#cancel").on("click", function(){
		self.location = "/board/list?page=${cri.page}&perPageNum=${cri.perPageNum}";
	})
	
	$("#modifyForm").on("submit", function(e){
		e.preventDefault();
		
		var modifyForm = $(this);
		var str = "";
		
		$(".file-attachments .delbtn").each(function(index){
			str += "<input type='hidden' name='files[" + index + "]' value='" + $(this).attr('data-src') + "'/>";
		});
		modifyForm.append(str);
		
		modifyForm.get(0).submit();
	});
	var template = Handlebars.compile($("#fileTemplate").html()); //핸들바 템플릿 
	
	$(".fileDrop").on("drop", function(e){
		console.log("drop");
		e.preventDefault(); //브라우저의 기본 실행을 차단.
		
		var files = e.originalEvent.dataTransfer.files; //드래그앤 드롭 이벤트에서 파일명을 받아냄(배열)
		var file = files[0]; //만약 여러개를 드래그했다 하더라도 그중에 하나만 
		var formData = new FormData(); //익스 10이상부터 지원됨(HTML5)
		formData.append("file", file);
		
		$.ajax({
			url:"/uploadAjax",
			data:formData,
			dataType:"text",
			processData:false,
			contentType:false,
			type:"POST",
			success:function(data){
				if(data == "failed"){
					alert("파일 업로드에 실패했습니다. 권한을 확인하세요");
					return;
				}
				var fileInfo = getFileInfo(data);
				var html = template(fileInfo);
				
				$(".file-attachments").append(html);
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
	var bno = ${boardVO.bno};
	$.getJSON("/board/getAttach/" + bno, function(list){
		$(list).each(function(){
			var fileInfo = getFileInfo(this);
			var html = template(fileInfo);
			
			$(".file-attachments").append(html);
		})
	});
});
</script>
