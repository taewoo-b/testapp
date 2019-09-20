package com.test.myapp.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

public class ArticleNotFound extends Exception{
	@Override
	public String getMessage(){
		return "지정하신 게시물이 없습니다.글 번호가 올바르지 않거나 이미 삭제된 게시물입니다.";
	}

	@ExceptionHandler(ArticleNotFound.class)
	public ModelAndView articleNF(ArticleNotFound e){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/errorPage");
		mav.addObject("title", "글읽기 에러");
		mav.addObject("msg", "메시지 에러 : " + e.getMessage());
		return mav;
	}
}
