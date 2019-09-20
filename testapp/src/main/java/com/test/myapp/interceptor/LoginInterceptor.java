package com.test.myapp.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter{
	private static final String LOGIN = "login";
	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object Handler) throws Exception {
		HttpSession session = req.getSession();
		//이미 로그인된 경우는 이전 로그인 데이터 클리어
		if(session.getAttribute(LOGIN) != null){
			logger.info("이전 로그인 정보를 삭제합니다");
			session.removeAttribute(LOGIN);
		}
		
		return true;
	}
	

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res, Object Handler, ModelAndView modelAndView) throws Exception {
		HttpSession session = req.getSession();
		ModelMap modelMap = modelAndView.getModelMap();
		//만약 모델 맵에 userVO가 들어가있다면 로그인이 성공했다는 뜻
		Object userVO = modelMap.get("UserVO");
		if(userVO != null){
			logger.info("로그인 성공");
			session.setAttribute(LOGIN, userVO); //유저 객체를 세션에 넣어줌
			
			if(req.getParameter("useAutoLogin") != null){
				//자동로그인 체크시 쿠키 생성
				logger.info("쿠키를 생성합니다");
				Cookie loginCookie = new Cookie("loginCookie", session.getId());
				loginCookie.setPath("/");
				//7일간 지속
				loginCookie.setMaxAge(60 * 60 * 24 * 7);
				res.addCookie(loginCookie);
			}
			
			//res.sendRedirect("/"); //루트 폴더로 돌려보냄.
			Object dest = session.getAttribute("dest");
			res.sendRedirect(dest != null ? (String)dest : "/");
		}else {
			//로그인 실패시 다시 로그인 창으로 리다이렉트
			logger.info("로그인 실패");
			modelMap.addAttribute("msg", "아이디나 비밀번호가 올바르지 않습니다");
		}
	}

	
	
	
	
	
}
