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
		<ul class="file-attachments clearfix">
		</ul>
	</div>

	<div class="row">
		<c:if test="${login.uid == boardVO.writer}">
			<a href="/board/modify/${boardVO.bno}/${cri.page}/${cri.perPageNum}?searchType=${cri.searchType}&keyword=${cri.keyword}" class="btn btn-warning">수정</a>
			<a href="/board/remove/${boardVO.bno}/${cri.page}/${cri.perPageNum}}?searchType=${cri.searchType}&keyword=${cri.keyword}" class="btn btn-danger" id="del">삭제</a>
		</c:if>
		<a href="/board/list/${cri.page}/${cri.perPageNum}?searchType=${cri.searchType}&keyword=${cri.keyword}" class="btn btn-primary" id="list">글목록</a>
	</div>
</div>
<div class="popup back" style="display:none;"></div>
<div class="popup front" style="display:none;">
	<img id="popupImg"/>
</div>


<%@ include file="../include/footer.jsp" %>

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js"></script>
<script id="fileTemplate" type="text/x-handlebars-template">
	<li data-src="{{fullName}}">
		<span class="file-attachment-icon has-img">
			<img src="{{imgsrc}}">
		</span>
		<div class="file-attachment-info">
			<a href="{{getLink}}" class="file-attachment-name">{{fileName}}</a>
		</div>
	</li>
</script>
<script src="/resources/js/upload.js"></script>
<script>
$(document).ready(function(){	
	var bno = ${boardVO.bno};
	var template = Handlebars.compile($("#fileTemplate").html());
	
		$.getJSON("/board/getAttach/" + bno, function(list){
			$(list).each(function(){
				var fileInfo = getFileInfo(this);
				var html = template(fileInfo);
				
				$(".file-attachments").append(html);
			})
		});
	});
	
	$(".file-attachments").on("click", ".file-attachment-info a", function(e){
		var fileLink = $(this).attr("href");
		var ext = fileLink.substring(fileLink.lastIndexOf('.') + 1, fileLink.length).toLowerCase();
		
		if(checkImageType(ext)){
			//그림파일이면 다운로드가 아니라 라이트 박스
			e.preventDefault();
			console.log(ext);
			var imgTag = $("#popupImg");
			imgTag.attr("src", fileLink);
			
			$(".popup").show("slow");
			imgTag.addClass("show");
		}
	});
	
	$("#popupImg").on("click", function(){
		$(".popup").hide("slow");
	});

</script>


