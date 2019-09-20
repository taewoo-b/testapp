package com.test.myapp.persistence;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.test.myapp.domain.BoardVO;
import com.test.myapp.domain.Criteria;
import com.test.myapp.domain.SearchCriteria;

@Repository
public class BoardDAOImpl implements BoardDAO {
	@Inject
	private SqlSession session;
	
	private static String namespace = "com.test.myapp.mapper.BoardMapper";
	
	@Override
	public void create(BoardVO vo) throws Exception {
		session.insert(namespace + ".create", vo);
	}
	
	@Override
	public BoardVO read(int bno) throws Exception{
		return session.selectOne(namespace + ".read", bno);
	}
	
	@Override
	public void update(BoardVO vo) throws Exception {
		session.update(namespace + ".update", vo);
	}
	
	@Override
	public void delete(int bno) throws Exception {
		session.delete(namespace + ".delete", bno);
	}
	
//	@Override
//	public List<BoardVO> listPage(Criteria cri) throws Exception {
//		return session.selectList(namespace + ".listPage", cri);
//	}
//	
//	@Override
//	public int countPaging() throws Exception {
//		return session.selectOne(namespace + ".countPaging");
//	}
	
	@Override
	public List<BoardVO> listPage(SearchCriteria cri) throws Exception {
		return session.selectList(namespace + ".listPage", cri);
	}

	@Override
	public int countPaging(SearchCriteria cri) throws Exception {
		return session.selectOne(namespace + ".countPaging", cri);
	}

	@Override
	public List<BoardVO> listPage(Criteria cri) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countPaging() throws Exception {
		return session.selectOne(namespace + ".countPaging");
	}


	
}

