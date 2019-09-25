package com.test.myapp.persistence;

import java.util.List;

import com.test.myapp.domain.Criteria;
import com.test.myapp.domain.ReplyVO;

public interface ReplyDAO {
	public List<ReplyVO> list(int bno) throws Exception;
	
	public void create(ReplyVO vo) throws Exception;
	
	public void update(ReplyVO vo) throws Exception;
	
	public void delete(int rno) throws Exception;
	
	public ReplyVO getReply(int rno) throws Exception;
	
	public List<ReplyVO> listPage(int bno, Criteria cri) throws Exception;

	public int count(int bno) throws Exception;

	
}

