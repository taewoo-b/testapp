<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="../include/header.jsp" %>

<div class="container">
	<div class="row">
		<div class="page-header">
			<h1>자유게시판<small>아무 글이나 적어도 됩니다</small></h1>
		</div>
	</div>
	<div class="row">
		<div class="row" style="margin-bottom:10px;">
			<div class="col-md-4 col-md-offset-8 text-right">
			
				<div class="row" style="margin-bottom:10px;">
					<div class="col-md-8">	
						<form class="form-inline">
							<div class="form-group">
								<label class="sr-only" for="searchType">검색종류</label>
								<select name="searchType" id="searchType" class="form-control">
									<option value="n" <c:out value="${cri.searchType == null ? 'selected' : ''}"/>>검색종류</option>
									<option value="t" <c:out value="${cri.searchType eq 't' ? 'selected' : ''}"/>>제목</option>
									<option value="c" <c:out value="${cri.searchType eq 'c' ? 'selected' : ''}"/>>내용</option>
									<option value="w" <c:out value="${cri.searchType eq 'w' ? 'selected' : ''}"/>>작성자</option>
									<option value="tc" <c:out value="${cri.searchType eq 'tc' ? 'selected' : ''}"/>>제목 + 내용</option>
									<option value="cw" <c:out value="${cri.searchType eq 'cw' ? 'selected' : ''}"/>>내용 + 작성자</option>
									<option value="tcw" <c:out value="${cri.searchType eq 'tcw' ? 'selected' : ''}"/>>제목 + 내용 + 작성자</option>
								</select>
							</div>
							
							
							
							<div class="form-group">
								<label class="sr-only" for="keywordInput">검색어</label>
								<input type="text" class="form-control" id="keywordInput" name="keywordInput" placeholder="검색어를 입력하세요" value="${cri.keyword}">
							<button type="button" id="searchBtn" class="btn btn-primary">검색</button>
														</div>
							
						</form>
					</div>
				<div class="btn-group">
					<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							게시글 개수 조정<span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a href="/board/list/1/10">10개씩보기</a></li>
						<li><a href="/board/list/1/15">15개씩보기</a></li>
						<li><a href="/board/list/1/20">20개씩보기</a></li>
					</ul>				
				</div>
			</div>
		</div>
		<table class="table table-striped table-hover">
			<tr>
				<th width="10%">번호</th>
				<th width="60%">제목</th>	
				<th>글쓴이 </th>
				<th>등록일</th><th>조회수</th>

			</tr>						
			
			
			<c:forEach items="${list}" var="boardVO">
				<tr>
					<td>${boardVO.bno}</td>
					<td><a href="/board/read/${boardVO.bno}/${pageMaker.makeSearch(pageMaker.cri.page)}">${boardVO.title}</a></td>
					<td>${boardVO.writer}</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${boardVO.regdate}" /></td>
					<td><span class="badge">${boardVO.viewcnt}</span></td>
				</tr>
			</c:forEach>


		</table>
	</div>
	<div class="row">
		<div class="col-md-2 col-md-offset-10 text-right">
			<a href="/board/register" class="btn btn-primary">새 글쓰기</a>
		</div>
	</div>
		
</div>

<div class="row">
	<div class="col-md-12 text-center">
		<ul class="pagination">
			<c:if test="${pageMaker.prev}">
				<li class="page-item"><a class="page-link" href="/board/list/${pageMaker.makeSearch(pageMaker.startPage-1)}">&laquo;</a></li>
			</c:if>
			
			<c:forEach begin="${pageMaker.startPage}" end="${pageMaker.endPage}" var="idx">
				<li <c:out value="${pageMaker.cri.page == idx ? 'class=active' : ''}" /> >
					<a class="page-link"  href="/board/list/${pageMaker.makeSearch(idx)}">${idx}</a>
				</li> 
			</c:forEach>
			
			<c:if test="${pageMaker.next}">
				<li class="page-item"><a class="page-link" href="/board/list/${pageMaker.makeSearch(pageMaker.endPage+1)}">&raquo;</a></li>
			</c:if>
		</ul>
	</div>
</div>

</div>

<%@ include file="../include/footer.jsp" %>

<script>
	var result = "${result}";
	
	if(result == "success"){
		alert("글이 성공적으로 등록되었습니다");
	}
</script>

<script>
	$(document).ready(function(){
		$("#searchBtn").on("click", function(e){
			self.location = "/board/list/${pageMaker.makeQuery(1)}" 
				+ "?searchType=" + $("select option:selected").val() 
				+ "&keyword=" + $("#keywordInput").val(); 	
		});
	});
</script>

