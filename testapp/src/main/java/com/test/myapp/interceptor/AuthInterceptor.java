package com.test.myapp.interceptor;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.test.myapp.domain.UserVO;
import com.test.myapp.service.UserService;


public class AuthInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
	
	@Inject
	private UserService us;

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
	
		HttpSession session = req.getSession();
		
		if(session.getAttribute("login") == null){
			logger.info("현재 유저는 로그인되어 있지 않습니다");
			saveDest(req);
			
			//쿠키가 존재할경우
			Cookie loginCookie = WebUtils.getCookie(req, "loginCookie");
			
			if(loginCookie != null){
				//쿠키값으로 DB검색
				UserVO userVO = us.checkLoginBefore(loginCookie.getValue());
				
				if(userVO != null){
					//기간내에 자동로그인 정보가 존재한다면
					session.setAttribute("login", userVO);
					return true;
				}
			}
			
			res.sendRedirect("/user/login");
			return false;
		}
		return true;
	}
	
	private void saveDest(HttpServletRequest req){
		String uri = req.getRequestURI();
		String query = req.getQueryString();
		
		if(query == null || query.equals("null")){
			query = "";
		}else {
			query = "?" + query;
		}
		
		if(req.getMethod().equals("GET")){
			logger.info("목적URI : " + (uri + query));
			//세션에 목적지 정보를 저장함.
			req.getSession().setAttribute("dest", uri+query);
		}
	}

}

