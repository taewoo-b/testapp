package com.test.myapp.service;

import java.util.Date;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.test.myapp.domain.UserVO;
import com.test.myapp.dto.LoginDTO;
import com.test.myapp.exception.InvalidRegisterException;
import com.test.myapp.persistence.UserDAO;

@Service
public class UserServiceImpl implements UserService{
	@Inject
	private UserDAO dao;
	
	@Override
	public UserVO login(LoginDTO dto) throws Exception {
		return dao.login(dto);
	}
	
	@Override
	public void keepLogin(String uid, String sessionId, Date next) throws Exception {
		dao.keepLogin(uid, sessionId, next);
	}

	@Override
	public UserVO checkLoginBefore(String value){
		return dao.checkUserWithSessionKey(value);
	}

	
	@Override
	public void register(UserVO vo) throws Exception, InvalidRegisterException {
		UserVO tempUser = dao.readUser(vo.getUid()); //해당 아이디로 이미 유저가 존재하는지 확인
		
		if(tempUser != null){
			throw new InvalidRegisterException("해당 아이디는 이미 사용중입니다.");
		}
		dao.insertUser(vo);
	}
	
	



}
