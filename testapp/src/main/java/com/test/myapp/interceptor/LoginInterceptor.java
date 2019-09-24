package com.test.myapp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
}

