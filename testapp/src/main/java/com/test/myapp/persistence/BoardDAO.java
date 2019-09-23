package com.test.myapp.persistence;

import java.util.List;

import com.test.myapp.domain.BoardVO;
import com.test.myapp.domain.Criteria;
import com.test.myapp.domain.SearchCriteria;

public interface BoardDAO {
	//글 작성하기
	public void create(BoardVO vo) throws Exception;
	//글 읽어오기
	public BoardVO read(int bno) throws Exception;
	//글 수정하기
	public void update(BoardVO vo) throws Exception;
	//글 삭제하기
	public void delete(int bno) throws Exception;
	//글 목록 가져오기
	//public List<BoardVO> listAll() throws Exception;
	public List<BoardVO> listPage(Criteria cri) throws Exception;
	//글 개수 가져오기
	public int countPaging() throws Exception;

	//글 목록 가져오기
	public List<BoardVO> listPage(SearchCriteria cri) throws Exception;
	//글 개수 가져오기
	public int countPaging(SearchCriteria cri) throws Exception;

	//첨부파일 저장하기
	public void addAttach(String fullName) throws Exception;

	//첨부파일 가져오기
	public List<String> getAttach(int bno) throws Exception;

}
