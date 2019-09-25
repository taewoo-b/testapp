package com.test.myapp.controller;

import java.net.URLEncoder;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.myapp.domain.BoardVO;
import com.test.myapp.domain.Criteria;
import com.test.myapp.domain.PageMaker;
import com.test.myapp.domain.SearchCriteria;
import com.test.myapp.domain.UserVO;
import com.test.myapp.exception.ArticleNotFound;
import com.test.myapp.exception.InvalidAccessException;
import com.test.myapp.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Inject
	private BoardService bs;
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public void registerGET() throws Exception{
		logger.info("글 등록 GET 요청입니다");
	}
	

	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String registerPOST(BoardVO board, HttpSession session, Model model, RedirectAttributes rttr) throws InvalidAccessException {
		logger.info("글 등록 POST 요청입니다");
		
		UserVO vo = (UserVO)session.getAttribute("login");
		if(!vo.getUid().equals(board.getWriter()) ){
			//세션에 로그인된 아이디와 게시글의 아이디가 다르다면
			throw new InvalidAccessException();
		}
		
		try {
			bs.regist(board);
		} catch(Exception e){
			e.printStackTrace();
			model.addAttribute("result", "fail");
			model.addAttribute("errorMsg", e.getMessage());
			return "/board/afterPost";
		}
		
		rttr.addFlashAttribute("result", "success");
		return "redirect:/board/list";
		
	}
	
	
	@RequestMapping(value="/read", method=RequestMethod.GET)
	public void read(@RequestParam("bno") int bno, Model model) throws ArticleNotFound{
		try {
			BoardVO bo = bs.read(bno);
			String content = bo.getContent();
			content = content.replaceAll("\r\n", "<br/>");
			bo.setContent(content);
			model.addAttribute(bo);
		} catch(Exception e){
			throw new ArticleNotFound();
		}
	}
//
//	@RequestMapping(value="/read/{bno}", method=RequestMethod.GET)
//	public String read(@PathVariable("bno") int bno, Model model) throws Exception{
//		model.addAttribute(bs.read(bno));
//		return "/board/read";
//	}
	
	@RequestMapping(value={"/list/{page}/{perPageNum}", "/list/{page}", "/list"}, method=RequestMethod.GET)
	public String listAll(@ModelAttribute("cri") SearchCriteria cri, Model model) throws Exception {
		logger.info("페이지 조건에 맞는 글 목록을 불러옵니다");
		model.addAttribute("list", bs.listPage(cri));
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(bs.countTotal());
		model.addAttribute("pageMaker", pageMaker);
		
		return "/board/list";
	}

	@RequestMapping(value={"/remove/{bno}", "/remove/{bno}/{page}/{perPageNum}", "/remove/{bno}/{page}" }, method=RequestMethod.GET)
	public String remove(@PathVariable("bno") int bno, SearchCriteria cri, RedirectAttributes rttr) throws Exception {
		bs.remove(bno);
		rttr.addFlashAttribute("result", "success");
		rttr.addFlashAttribute("msg", "성공적으로 삭제되었습니다.");
		return "redirect:/board/list/" + cri.getPage() + "/" + cri.getPerPageNum() + "?searchType=" + cri.getSearchType() + "&keyword=" + URLEncoder.encode(cri.getKeyword(), "UTF-8");
	}
	
	//userlogin삭제
	@RequestMapping(value="/remove", method=RequestMethod.POST)
	public String remove(@RequestParam("bno") int bno, HttpSession session, Criteria cri, RedirectAttributes rttr) throws Exception {
		UserVO user = (UserVO)session.getAttribute("login");
		
		try {
			BoardVO article = bs.read(bno); //글을 가져오고
			if(!article.getWriter().equals(user.getUid())){
				//현재 로그인된 사용자와 글쓴이가 다르다면
				logger.info("잘못된 접근");
				throw new InvalidAccessException();
			}
		}catch (Exception e){
			e.printStackTrace();
			throw new InvalidAccessException();
		}
		
		bs.remove(bno);
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addFlashAttribute("result", "success");
		return "redirect:/board/list";
	}


	
	@RequestMapping(value={"/modify/{bno}", "/modify/{bno}/{page}/{perPageNum}", "/modify/{bno}/{page}"}, method = RequestMethod.GET)
	public String modifyGET(@PathVariable("bno") int bno, @ModelAttribute("cri") SearchCriteria cri, Model model) throws Exception {
		model.addAttribute(bs.read(bno)); //해당 글번호의 글을 읽어와서 모델에 넣어줌
		return "/board/modify";
	}


	@RequestMapping(value={"/modify/{bno}", "/modify/{bno}/{page}/{perPageNum}", "/modify/{bno}/{page}"}, method=RequestMethod.POST)
	public String modifyPOST(BoardVO vo, SearchCriteria cri, HttpSession session, RedirectAttributes rttr) throws Exception {
		UserVO user = (UserVO)session.getAttribute("login");
		
		try { //try catch 안해도 됨.
			int bno = vo.getBno(); //글번호만 가지고
			BoardVO article = bs.read(bno); //글을 가져오고
			if(!article.getWriter().equals(user.getUid())){
				//현재 로그인된 사용자와 글쓴이가 다르다면
				logger.info("잘못된 접근");
				throw new InvalidAccessException();
			}
		}catch (Exception e){
			e.printStackTrace();
			throw new InvalidAccessException();
		}

		bs.modify(vo);
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addFlashAttribute("msg", "success");
		
		return "redirect:/board/list/" + cri.getPage() + "/" + cri.getPerPageNum() + "?searchType=" + cri.getSearchType() + "&keyword=" + URLEncoder.encode(cri.getKeyword(), "UTF-8");
	}	
	
	
	@RequestMapping(value={"/read/{bno}", "/read/{bno}/{page}/{perPageNum}", "/read/{bno}/{page}"}, method=RequestMethod.GET)
	public String read(@PathVariable("bno") int bno, @ModelAttribute("cri") SearchCriteria cri, Model model) throws ArticleNotFound, Exception{
		try {
			BoardVO bo = bs.read(bno);
			String content = bo.getContent();
			content = content.replaceAll("\r\n", "<br/>");
			bo.setContent(content);
			model.addAttribute(bo);
			model.addAttribute("cri", cri);
		} catch (Exception e){
			throw new ArticleNotFound();
		}
		return "/board/read";
	}
	
	


}

