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
		
		
			<c:if test="${not empty login}">
				<div class="well" style="margin-top:20px;">
					<form class="form-horizontal">
					<div class="form-group">
						<label for="reply" class="col-md-2 control-label">${login.uname}</label>
						<div class="col-md-8">
							<textarea rows="5" maxlength="200" style="resize:none;" class="form-control" id="content" placeholder="댓글을 입력하세요."></textarea>
						</div>
						<div class="col-md-2">
							<button type="button" class="btn btn-success btn-lg btn-block" id="replyBtn">작성하기</button>
						</div>
					</div>
					</form>
				</div>	
			</c:if>
			
			<ul class="timeline" style="margin-top:20px;">
				<li class="time-label" id="repliesDiv"><span class="bg-green">댓글 리스트 보기</span></li>
			</ul>
			
			<div class="text-center">
				<ul id="pagination" class="pagination pagination-sm no-margin">
				
				</ul>
			</div>
						
			<div id="modifyModal" class="modal modal-primary fade" role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">&times;</button>
							<h4 class="modal-title"></h4>
						</div>
						<div class="modal-body" data-rno>
							<p><textarea id="replytext" class="form-control" rows="5" maxlength="200" style="resize:none;"></textarea></p>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-info" id="replyModBtn">수정</button>
							<button type="button" class="btn btn-danger" id="replyDelBtn">삭제</button>
							<button type="button" class="btn btn=default" data-dismiss="modal">닫기</button>
						</div>
					</div>
				</div>
			</div>
									
		
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
	
	$(".timeline").on("click", ".replyLi", function(e){
		var reply = $(this);
		console.log(reply.find(".timeline-body").text());
		$("#replytext").val(reply.find(".timeline-body").text());
		$(".modal-title").html(reply.attr("data-rno"));
	});

	
	$("#replyBtn").on("click", function(){
		var nowReplyer = "${login.uid}";
		var replyTextObj = $("#content");
		<%-- 댓글 등록 오브젝트 --%>
		var replyText = replyTextObj.val();
		console.log(replyText);
		$.ajax({
			type:'post',
			url:'/replies',
			headers:{
				"Content-Type":"application/json",
				"X-HTTP-Method-Override":"POST" },
			dataType:'text',
			data:JSON.stringify({bno:bno, replyer:nowReplyer, replytext:replyText}),
			success:function(returnMsg){
				console.log(returnMsg);
			
				<%-- 성공적으로 댓글이 등록되었다면 1페이지로 이동. --%>
				var returnObj = eval("(" + returnMsg + ")");
				alert(returnObj.msg);
				if(returnObj.result == "success"){
					<%-- 입력창 비워주고 --%>
					replyTextObj.val("");
					getPage("/replies/" + bno + "/1");
				}
			}
			
		});
	});

	
	$(".pagination").on("click", "li a", function(e){
		e.preventDefault();
		
		replyPage = $(this).attr("href");
		
		getPage("/replies/" + bno + "/" + replyPage);
	});
	
	
	$("#repliesDiv").on("click", function(){
		if($(".timeline li").length > 1){
			return;
		}
		getPage("/replies/all/" + bno);
	});

	function getPage(pageInfo){
		var replyPage = 1;

		$("#repliesDiv").on("click", function(){
			if($(".timeline li").length > 1){
				return;
			}
			getPage("/replies/" + bno + "/1");
		});

		function getPage(pageInfo){
			$.getJSON(pageInfo, function(data){
				printData(data.list, $("#repliesDiv"), $("#replyTemplate"));
				printPaging(data.pageMaker, $(".pagination"));
			});
		}

		
			
	$.getJSON("/board/getAttach/" + bno, function(list){
		$(list).each(function(){
			var fileInfo = getFileInfo(this);
			var html = template(fileInfo);
			
			$(".file-attachments").append(html);
		})
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

$("#del").on("click", function(e){
	e.preventDefault();
	var href = $(this).attr("href"); //href 저장후
	var arr = []; //첨부파일 배열
	$(".file-attachments li").each(function(index) {
		arr.push($(this).attr("data-src"));
	});
	
	if(arr.length > 0){
		$.post("/deleteAllFiles", {files:arr}, function(){
			location.href = href;
		});
	}

});
}

Handlebars.registerHelper("prettifyDate", function (timeValue){
	var dateObj = new Date(timeValue);
	var year = dateObj.getFullYear();
	var month = dateObj.getMonth() + 1;
	var date = dateObj.getDate();
	return year + "/" + month + "/" + date;
});

Handlebars.registerHelper("eqReplyer", function(replyer, block){
	var result = '';
	if(replyer == '${login.uid}'){
		result += block.fn();
	}
	return result;
});



var printData = function(replyArr, target, templateObject){
	var template = Handlebars.compile(templateObject.html());
	
	var html = template(replyArr);
	$(".replyLi").remove();
	target.after(html);
}

var printPaging = function(pageMaker, target){
	var str = "";
	if(pageMaker.prev){
		str += "<li><a href='" + (pageMaker.startPage - 1) + "'> << </a></li>";
	}
	
	for(var i = pageMaker.startPage, len = pageMaker.endPage; i <= len; i++){
		var strClass = pageMaker.cri.page == i ? 'class=active' : '';
		str += "<li " + stClass + "><a href='" + i + "'>" + i + "</a></li>";
	}
	
	if(pageMaker.next){
		str += "<li><a href='" + (pageMaker.endPage + 1) + "'> >> </a></li>";
	}
	
	target.html(str);
}

$("#replyModBtn").on("click", function(){
	var rno = $(".modal-title").html();
	var replytextVal = $("#replytext").val();
	
	$.ajax({
		type:'put',
		url:'/replies/' + rno,
		headers:{
			"Content-Type":"application/json",
			"X-HTTP-Method-Override":"PUT"
		},
		data:JSON.stringify({replytext:replytextVal}),
		dataType:'text',
		success:function(returnMsg){
			var returnObj = eval("(" + returnMsg + ")");
			alert(returnObj.msg);
			if(returnObj.result == "success"){
				<%-- 수정이 성공했다면 해당 댓글 페이지를 다시 로드. --%>
				getPage("/replies/" + bno + "/" + replyPage);
				$("#modifyModal").modal('hide');
			}
		}
		
	});
});

$("#replyDelBtn").on("click", function(){
	var rno = $(".modal-title").html();
	
	$.ajax({
		type:'delete',
		url:'/replies/' + rno,
		headers:{
			"Content-Type":"application/json",
			"X-HTTP-Method-Override":"DELETE"
		},
		dataType:'text',
		success:function(returnMsg){
			var returnObj = eval("(" + returnMsg + ")");
			alert(returnObj.msg);
			if(returnObj.result == "success"){
				<%-- 삭제가 성공했다면 해당 댓글 페이지를 다시 로드. --%>
				getPage("/replies/" + bno + "/" + replyPage);
				$("#modifyModal").modal('hide');
			}
		}
	});
})





</script>

<script id="replyTemplate" type="text/x-handlebars-template">
{{#each .}}
	<li class="replyLi" data-rno={{rno}}>
		<i class="fa fa-comments bg-blue"></i>
		<div class="timeline-item">
			<span class="time">
				<i class="fa fa-clock-o"></i>{{prettifyDate regdate}}
			</span>
			<h3 class="timeline-header"><strong>{{rno}}</strong> -{{replyer}} </h3>
			<div class="timeline-body">{{replytext}}</div>
			{{#eqReplyer replyer}}
			<div class="timeline-footer">
				<a class="btn btn-primary btn-xs" data-toggle="modal" data-target="#modifyModal">수정</a>
			</div>
			{{/eqReplyer}}
		</div>
	</li>
{{/each}}
</script>



