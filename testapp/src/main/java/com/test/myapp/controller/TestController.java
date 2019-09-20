package com.test.myapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.test.myapp.domain.UserVO;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/test/")
public class TestController {
private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@RequestMapping("doA")
	public void doA(){
		logger.info("doA가 호출되었습니다");
	}
	
	@RequestMapping("doB")
	public String doB(@ModelAttribute("msg") String msg, Model model){
		logger.info("doB가 호출되었습니다");
        
		return "bbb";
	}
	@RequestMapping("getBean")
	public void getBean(Model model){
		logger.info("getBean 이 출력되었습니다");
		UserVO temp = new UserVO();
		temp.setUid("gondr");
		temp.setUpw("1234");
		temp.setUname("abc");
		model.addAttribute("user", temp);
		
	}
	
}
