package com.test.myapp.service;

import java.util.List;

import com.test.myapp.domain.Criteria;
import com.test.myapp.domain.ReplyVO;

public interface ReplyService {
	public void addReply(ReplyVO vo) throws Exception;
	
	public List<ReplyVO> listReply(int bno) throws Exception;
	
	public void modifyReply(ReplyVO vo) throws Exception;
	
	public void removeReply(int rno) throws Exception;
	
	public ReplyVO getReply(int rno) throws Exception;
	
	public List<ReplyVO> listReplyPage(int bno, Criteria cri) throws Exception;

	public int count(int bno) throws Exception;

}

