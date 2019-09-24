package com.test.myapp.service;

import java.util.List;

import com.test.myapp.domain.BoardVO;
import com.test.myapp.domain.Criteria;
import com.test.myapp.domain.SearchCriteria;

public interface BoardService {
	// 글 등록
	public void regist(BoardVO vo) throws Exception;

	// 글 읽기
	public BoardVO read(int bno) throws Exception;

	// 글 수정
	public void modify(BoardVO vo) throws Exception;

	// 글삭제
	public void remove(int bno) throws Exception;

	// 리스트 보기
	public List<BoardVO> listPage(Criteria cri) throws Exception;

	// br처리
	public BoardVO readbr(int bnoo) throws Exception;
	
	//글 개수 가져오기
	public int countTotal() throws Exception;

	//리스트 보기
	public List<BoardVO> listPage(SearchCriteria cri) throws Exception;
	//글 개수 가져오기
	public int countTotal(SearchCriteria cri) throws Exception;
	
	public List<String> getAttach(int bno) throws Exception;
	
	//첨부파일의 게시자 가져오기
	public String getWriter(String fullName) throws Exception;

	
	

}
