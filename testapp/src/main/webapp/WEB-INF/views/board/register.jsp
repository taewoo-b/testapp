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
			<form id="registerForm" class="form-horizontal" method="post"><!--form 태그 시작-->
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
				
				<!-- AJAX 파일 전송 -->
				<div class="form-group">
					<div class="col-md-10 col-md-offset-2">
						<label for="fileDrop">업로드할 파일을 드래그 & 드롭 하세요</label>
					</div>
					<div class="col-md-10 col-md-offset-2">
						<div class="fileDrop">

						</div>	
					</div>
				</div>
				
				<ul class="file-attachments clearfix">
				</ul>
				<!-- AJAX 파일 전송 -->
				
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
<script src="/resources/js/upload.js"></script>
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
				<i class="fa fa-fw fa-remove"></i></button>
			</a>
		</div>
	</li>
</script>
<script>
	var template = Handlebars.compile($("#fileTemplate").html()); //핸들바 템플릿 
	
	function checkImageType(fileFormat) {
		var pattern = /jpg|gif|png|jpeg/i;
		return fileFormat.match(pattern); //패턴과 일치하는지 t,f
	}
	
	function getFileName(fileLink){
		var idx = fileLink.indexOf("_") + 1;
		return fileLink.substring(idx, fileLink.length);
	}

	function getImgLink(fileLink){
		var front = fileLink.substr(0, 12); // '/20XX/XX/XX/'  0부터 12개를 잘라냄.
		var end = fileLink.substr(14); // 's_' 를 제거한 나머지.
		
		return front + end; //중간에 s_ 만 제거된 나머지가 전송됨
	}
	
	function getFileInfo(fullName){
		var fileName, imgsrc, getLink;
		
		var ext = fullName.substring(fullName.lastIndexOf('.') + 1, fullName.length).toLowerCase();
		
		if(checkImageType(ext)) {
			imgsrc = "/displayFile?fileName=" + fullName; //썸네일 링크 
			fileLink = fullName.substr(14); //파일 이름만 뽑아내기.
			getLink = "/displayFile?fileName=" + getImgLink(fullName); //원본 이미지 링크
		}else {
			imgsrc = "/resources/imgs/file.png";
			fileLink = fullName.substr(12); //썸네일이 없기 때문에 12번째부터 잘라내면 됨.
			getLink = "/displayFile?fileName=" + fullName; //파일 다운로드 링크
		}
		fileName = fileLink.substr(fileLink.indexOf("_") + 1); //UUID 제거한 파일이름.
		
		return {fileName:fileName, imgsrc:imgsrc, getLink:getLink, fullName:fullName};
	}
	
	$(".fileDrop").on("dragenter dragover", function(e){
		e.preventDefault();
	});
	
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


</script>
<script>
$("#registerForm").on("submit", function(e){
	e.preventDefault();
	
	var registerForm = $(this);
	var str = "";
	
	$(".file-attachments .delbtn").each(function(index){
		str += "<input type='hidden' name='files[" + index + "]' value='" + $(this).attr('data-src') + "'/>";
	});
	registerForm.append(str);
	
	registerForm.get(0).submit();
});
</script>





