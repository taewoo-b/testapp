package com.test.myapp.controller;

import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import com.test.myapp.domain.UserVO;
import com.test.myapp.dto.LoginDTO;
import com.test.myapp.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Inject
	private UserService service;
	private HttpSession session;
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public void loginGET(){
		
	}
	
	@RequestMapping(value="/loginPost", method=RequestMethod.POST)
	public void loginPOST(LoginDTO dto, Model model) throws Exception {
		UserVO vo = service.login(dto);
		
		if(vo == null){
			return; //로그인 실패
		}	
		model.addAttribute("UserVO", vo);
		
		if(dto.isUseAutoLogin()){
			int amount = 60 * 60 * 24 * 7;
			//쿠키 파괴날짜를 시스템 날짜로부터 7일후로 잡음.
			Date sessionLimit = new Date(System.currentTimeMillis() + (1000 * amount));
			
			service.keepLogin(vo.getUid(), session.getId(), sessionLimit);
		}

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

	
}

