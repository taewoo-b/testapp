package com.test.myapp.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.myapp.domain.BoardVO;
import com.test.myapp.domain.Criteria;
import com.test.myapp.domain.SearchCriteria;
import com.test.myapp.persistence.BoardDAO;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Inject
	private BoardDAO dao;
	
	@Transactional
	@Override
	public void regist(BoardVO vo) throws Exception {
		dao.create(vo);
		
		String[] files = vo.getFiles();
		
		if (files == null) { return ;}
		
		for (String fileName : files){
			dao.addAttach(fileName);
		}
	}
	
	@Override
	public BoardVO read(int bno) throws Exception {
		return dao.read(bno);
	}
		
	@Transactional
	@Override
	public void remove(int bno) throws Exception {
		dao.deleteAttach(bno);
		dao.delete(bno);
	}

	
//	@Override
//	public List<BoardVO> listAll() throws Exception {
//		return dao.listAll();
//	}
	
	@Override
	public List<BoardVO> listPage(Criteria cri) throws Exception {
		return dao.listPage(cri);
	}

	@Override
	public BoardVO readbr(int bnoo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countTotal() throws Exception {
		return dao.countPaging();
	}

	@Override
	public List<BoardVO> listPage(SearchCriteria cri) throws Exception {
		return dao.listPage(cri);
	}

	@Override
	public int countTotal(SearchCriteria cri) throws Exception {
		return dao.countPaging(cri);
	}
	
	@Override
	public List<String> getAttach(int bno) throws Exception {
		return dao.getAttach(bno);
	}

	@Transactional
	@Override
	public void modify(BoardVO vo) throws Exception {
		dao.update(vo); //글정보 업데이트 이후
		
		int bno = vo.getBno();
		
		dao.deleteAttach(bno); //모든 첨부파일 정보 삭제.
		
		String[] files = vo.getFiles();
		
		if(files == null) { return; }
		
		for(String fileName : files ) { //첨부파일이 존재한다면 전부 다시 입력
			dao.replaceAttach(fileName, bno);
		}
	}
	
	@Override
	public String getWriter(String fullName) throws Exception {
		return dao.getWriter(fullName);
	}



	
	
}
