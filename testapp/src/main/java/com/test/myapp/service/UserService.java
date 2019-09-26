package com.test.myapp.service;

import java.util.Date;

import com.test.myapp.domain.UserVO;
import com.test.myapp.dto.LoginDTO;
import com.test.myapp.exception.InvalidRegisterException;


public interface UserService {
	public UserVO login(LoginDTO dtd) throws Exception;
	
	public void keepLogin(String uid, String sessionId, Date next) throws Exception;
	
	public UserVO checkLoginBefore(String value);
	
	public void register(UserVO vo) throws Exception, InvalidRegisterException;

	public void usermodify(UserVO vo) throws Exception;

	
}
