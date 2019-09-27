package com.test.myapp.controller;

import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import com.test.myapp.domain.AjaxResVO;
import com.test.myapp.domain.BoardVO;
import com.test.myapp.domain.UserVO;
import com.test.myapp.dto.LoginDTO;
import com.test.myapp.exception.InvalidAccessException;
import com.test.myapp.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Inject
	private UserService service;
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public void loginGET(){
		
	}
	
	@RequestMapping(value="/loginPost", method=RequestMethod.POST)
	public String loginPOST(LoginDTO dto, HttpServletResponse res, HttpSession session, RedirectAttributes rttr) throws Exception {
		UserVO vo = makeLogin(res,session,dto);
		if(vo == null){
			//로그인 실패
			rttr.addFlashAttribute("result", "danger");
			rttr.addFlashAttribute("msg", "아이디나 비밀번호가 올바르지 않습니다");
			return "redirect:/user/login";
		}else{
			//로그인 성공
			Object dest = session.getAttribute("dest");
			return "redirect:" + (dest != null ? (String)dest : "/");
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/loginAjax", method=RequestMethod.POST)
	public AjaxResVO loginAjax(@RequestBody LoginDTO dto, HttpServletResponse res, HttpSession session) throws Exception {
		UserVO vo = makeLogin(res, session, dto);
		AjaxResVO retJSON = new AjaxResVO();
		
		if(vo != null){
			retJSON.setSuccess(true);
			retJSON.setMsg("로그인 성공");
		}else{
			retJSON.setSuccess(false);
			retJSON.setMsg("로그인 실패, 아이디와 비밀번호를 확인하세요");
		}
		
		return retJSON;
	}	
	
	private UserVO makeLogin(HttpServletResponse res, HttpSession session, LoginDTO dto){
		UserVO vo = null;
		try {
			vo = service.login(dto);
			if(vo == null){
				return null;
			}
			if(dto.isUseAutoLogin()){
				//쿠키 파괴날짜를 시스템 날짜로부터 7일후로 잡음.
				int amount = 60 * 60 * 24 * 7; 
				
				//쿠키를 DB에 저장
				Date sessionLimit = new Date(System.currentTimeMillis() + (1000 * amount));
				service.keepLogin(vo.getUid(), session.getId(), sessionLimit);
				
				//쿠키를 등록
				Cookie loginCookie = new Cookie("loginCookie", session.getId());
				loginCookie.setPath("/");
				//7일간 지속
				loginCookie.setMaxAge(60 * 60 * 24 * 7);
				res.addCookie(loginCookie);
			}
			//세션저장
			session.setAttribute("login", vo);
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return vo;
	}

	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logout(HttpServletRequest req, HttpServletResponse res, HttpSession session, RedirectAttributes rttr) throws Exception{
		
		Object obj= session.getAttribute("login");
		
		//로그인되어있는 상태였다면
		if(obj != null) {
			UserVO vo = (UserVO) obj;
			session.invalidate();
			
			//쿠키 검사
			Cookie loginCookie = WebUtils.getCookie(req, "loginCookie");
			
			if(loginCookie != null){
				loginCookie.setPath("/");
				loginCookie.setMaxAge(0);
				res.addCookie(loginCookie);
				service.keepLogin(vo.getUid(), session.getId(), new Date());//오늘날짜로 지정
			}
		}
		return "redirect:/";
		
	}

	@RequestMapping(value="/register", method = RequestMethod.GET)
	public void registerGET(){
		
	}
	@RequestMapping(value="/register", method = RequestMethod.POST)
	public String registerPost(UserVO vo, RedirectAttributes rttr) throws Exception {
		service.register(vo);
		
		rttr.addFlashAttribute("result", "success");
		rttr.addFlashAttribute("msg", "성공적으로 회원가입 되었습니다.");
		return "redirect:/";
	}
	
	@RequestMapping(value="/usermodify", method = RequestMethod.GET)
	public void modifyGET(){
	
	}
	@RequestMapping(value="/usermodify", method = RequestMethod.POST)
	public String modifyPost(UserVO vo, RedirectAttributes rttr, HttpSession session) throws Exception {
		
		service.usermodify(vo);
		
		rttr.addFlashAttribute("result", "success");
		rttr.addFlashAttribute("msg", "성공적으로 수정되었습니다.");
		
		
		
		
		
		
		
		Object obj= session.getAttribute("login");
		
		//로그인되어있는 상태였다면
		if(obj != null) {
			vo = (UserVO) obj;
			session.invalidate();
			
//			//쿠키검사
//			Cookie loginCookie = WebUtils.getCookie(req, "loginCookie");
//			
//			if(loginCookie != null){
//				loginCookie.setPath("/");
//				loginCookie.setMaxAge(0);
//				res.addCookie(loginCookie);
//				service.keepLogin(vo.getUid(), session.getId(), new Date());//오늘날짜로 지정
//			}
		}
		
		return "redirect:/";
	}

}