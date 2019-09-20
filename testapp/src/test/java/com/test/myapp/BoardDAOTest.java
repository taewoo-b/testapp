package com.test.myapp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scripting.bsh.BshScriptUtils.BshExecutionException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.test.myapp.domain.BoardVO;
import com.test.myapp.domain.Criteria;
import com.test.myapp.persistence.BoardDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ={"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class BoardDAOTest {
	@Inject
	private BoardDAO dao;
	
	private static Logger logger = LoggerFactory.getLogger(BoardDAOTest.class);
	
	@Test
	public void testCreate() throws Exception {
		BoardVO vo = new BoardVO();
		vo.setTitle("새로운 글의 제목입니다");
		vo.setContent("글 내용입니다..글 내용입니다. 내용이에요.");
		vo.setWriter("gondr");
		dao.create(vo);
	}
	
	@Test
	public void testRead() throws Exception {
		logger.info(dao.read(11).toString());
	}
	
	@Test
	public void testListPage() throws Exception {
		Criteria cri = new Criteria();
		cri.setPage(2);
		cri.setPerPageNum(20);
		
		List<BoardVO> list = dao.listPage(cri);
		//리스트의 각각의 boardVO에 대해서 출력해줌.
		for(BoardVO boardVO : list){
			logger.info(boardVO.getBno() + " : " + boardVO.getTitle());
		}
	}

	public void testGetList() throws Exception {
		List<BoardVO> voList = new ArrayList<BoardVO>();
		Criteria cri = new Criteria();
		cri.setPage(2);
		cri.setPerPageNum(10);
		voList = bs.listPage(cri);
		
		for(BoardVO vo : voList){
			logger.info(vo.toString());
		}
	}



	
	
	@Test
	public void testUpdate() throws Exception {
		BoardVO vo = new BoardVO();
		vo.setBno(1);
		vo.setTitle("수정된 제목입니다");
		vo.setContent("수정된 내용입니다. 이 내용은 수정되었습니다");
		dao.update(vo);
	}
	
	@Test
	public void testDelete() throws Exception {
		dao.delete(1);
	}
}
