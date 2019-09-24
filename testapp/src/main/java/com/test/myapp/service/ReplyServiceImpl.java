package com.test.myapp.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.test.myapp.persistence.ReplyDAO;
import com.test.myapp.domain.Criteria;
import com.test.myapp.domain.ReplyVO;

@Service
public class ReplyServiceImpl implements ReplyService{
	@Inject
	private ReplyDAO dao;
	
	@Override
	public void addReply(ReplyVO vo) throws Exception {
		dao.create(vo);
	}
	
	@Override
	public List<ReplyVO> listReply(int bno) throws Exception {
		return dao.list(bno);
	}
	
	@Override
	public void modifyReply(ReplyVO vo) throws Exception{
		dao.update(vo);
	}
	
	@Override
	public void removeReply(int rno) throws Exception {
		dao.delete(rno);
	}
	@Override
	public ReplyVO getReply(int rno) throws Exception {
		return dao.getReply(rno);
	}
	
	@Override
	public List<ReplyVO> listReplyPage(int bno, Criteria cri) throws Exception {
		return dao.listPage(bno, cri);
	}

	@Override
	public int count(int bno) throws Exception {
		return dao.count(bno);
	}


}

