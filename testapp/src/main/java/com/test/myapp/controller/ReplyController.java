package com.test.myapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.test.myapp.domain.Criteria;
import com.test.myapp.domain.PageMaker;
import com.test.myapp.domain.ReplyVO;
import com.test.myapp.domain.UserVO;
import com.test.myapp.service.ReplyService;

@RestController
@RequestMapping("/replies")
public class ReplyController {
	@Inject
	private ReplyService service;
	
	@RequestMapping(value="", method= RequestMethod.POST, produces ="text/json;charset=UTF-8")
	public ResponseEntity<String> register(@RequestBody ReplyVO vo, HttpSession session){
		ResponseEntity<String> entity = null;
		
		UserVO loginUser = (UserVO)session.getAttribute("login");
		
		if(loginUser == null){
			//로그인 하지 않은 유저는 메시지를 보내줌.
			entity = new ResponseEntity<String>("{result:'fail', msg:'로그인 한 후에 댓글을 작성하실 수 있습니다'", HttpStatus.OK);
			return entity;
		}
		
		vo.setReplyer(loginUser.getUid()); //로그인한 사용자로 작성자를 변경함.
		
		try {
			service.addReply(vo);
			entity = new ResponseEntity<String>("{result:'success', msg:'댓글이 성공적으로 작성되었습니다'}", HttpStatus.OK);
		} catch (Exception e){
			e.printStackTrace();
			entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
	
	@RequestMapping(value="/all/{bno}", method= RequestMethod.GET)
	public ResponseEntity<List<ReplyVO>> list(@PathVariable("bno") int bno ){
		ResponseEntity<List<ReplyVO>> entity = null;
		
		try{
			entity = new ResponseEntity<List<ReplyVO>>(service.listReply(bno), HttpStatus.OK);
		}catch (Exception e){
			e.printStackTrace();
			entity=  new ResponseEntity<List<ReplyVO>>(HttpStatus.BAD_REQUEST);
		}
		
		return entity;
	}
	
	@RequestMapping(value= "/{rno}", method=RequestMethod.PUT , produces ="text/json;charset=UTF-8")
	public ResponseEntity<String> update(@PathVariable("rno") int rno, @RequestBody ReplyVO vo, HttpSession session){
		ResponseEntity<String> entity = null;
		//권한 검사
		UserVO loginUser = (UserVO)session.getAttribute("login");
		
		if(loginUser == null){
			entity = new ResponseEntity<String>("{result:'fail', 'msg':'로그인 후에 시도하세요'}", HttpStatus.OK );
			return entity;
		}
		
		try {
			ReplyVO tempReply = service.getReply(rno); // 해당 댓글을 가져와서.
			if(tempReply.getReplyer().equals(loginUser.getUid())){
				vo.setRno(rno); //들어온 정보에 rno값을 설정하고.
				service.modifyReply(vo);
				entity = new ResponseEntity<String>("{result:'success', 'msg':'성공적으로 댓글이 작성되었습니다'}", HttpStatus.OK );
			} else {
				entity = new ResponseEntity<String>("{result:'fail', 'msg':'해당 댓글을 수정하기 위한 권한이 없습니다.'}", HttpStatus.OK );
			}
		} catch(Exception e){
			e.printStackTrace();
			entity = new ResponseEntity<String>("{result:'fail', 'msg':'댓글 작성중 오류가 발생했습니다.'}", HttpStatus.BAD_REQUEST);
		}
		
		return entity;
	}
	
	@RequestMapping(value="/{rno}", method = RequestMethod.DELETE , produces ="text/json;charset=UTF-8")
	public ResponseEntity<String> remove(@PathVariable("rno")int rno, HttpSession session){
		
		ResponseEntity<String> entity = null;
		
		UserVO loginUser = (UserVO)session.getAttribute("login");
		
		if(loginUser == null){
			entity = new ResponseEntity<String>("{result:'fail', 'msg':'로그인 후에 시도하세요'}", HttpStatus.OK );
			return entity;
		}
		
		try {
			ReplyVO tempReply = service.getReply(rno); // 해당 댓글을 가져와서.
			if(tempReply.getReplyer().equals(loginUser.getUid())){
				service.removeReply(rno);
				entity = new ResponseEntity<String>("{result:'success', 'msg':'댓글이 성공적으로 삭제되었습니다'}", HttpStatus.OK);
			} else {
				entity = new ResponseEntity<String>("{result:'fail', 'msg':'댓글을 수정하기 위한 권한이 없습니다'}", HttpStatus.OK);
			}
		} catch (Exception e){
			e.printStackTrace();
			entity = new ResponseEntity<String>("{result:'fail', 'msg':'댓글 삭제중 오류 발생'}", HttpStatus.BAD_REQUEST);
		}
		
		return entity;
	}
	
	@RequestMapping(value="/{bno}/{page}", method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> listPage(@PathVariable("bno") int bno, @PathVariable("page") int page){
		
		ResponseEntity<Map<String, Object>> entity = null;
		
		try {
			Criteria cri = new Criteria();
			cri.setPage(page);
			
			PageMaker pageMaker = new PageMaker();
			pageMaker.setCri(cri);
			
			Map<String, Object> map = new HashMap<String, Object>();
			List<ReplyVO> list = service.listReplyPage(bno, cri); //해당 페이지에 해당하는 리스트 가져오고.
			
			map.put("list", list); //반환값에 list라는 이름으로 리스트를 반환함.
			
			int replyCount = service.count(bno); //전체 답글의 수를 가져오고.
			pageMaker.setTotalCount(replyCount); //그 정보로 페이지 메이커를 셋팅해준다. 
			//페이지 메이커의 기본 perPageNum은 10으로 고정된 채로 간다.
			map.put("pageMaker", pageMaker);
			
			entity = new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
		} catch (Exception e){
			e.printStackTrace();
			entity = new ResponseEntity<Map<String,Object>>(HttpStatus.BAD_REQUEST);
		}
		
		return entity;
	}


	
}
