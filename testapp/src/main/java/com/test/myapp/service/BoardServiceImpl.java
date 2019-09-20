package com.test.myapp.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.test.myapp.domain.BoardVO;
import com.test.myapp.domain.Criteria;
import com.test.myapp.domain.SearchCriteria;
import com.test.myapp.persistence.BoardDAO;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Inject
	private BoardDAO dao;
	
	@Override
	public void regist(BoardVO vo) throws Exception {
		dao.create(vo);
	}
	
	@Override
	public BoardVO read(int bno) throws Exception {
		return dao.read(bno);
	}
	
	@Override
	public void modify(BoardVO vo) throws Exception {
		dao.update(vo);
	}
	
	@Override
	public void remove(int bno) throws Exception {
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



	
	
}
