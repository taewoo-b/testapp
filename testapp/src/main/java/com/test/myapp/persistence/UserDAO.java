package com.test.myapp.persistence;

import java.util.Date;

import com.test.myapp.domain.UserVO;
import com.test.myapp.dto.LoginDTO;

public interface UserDAO {
	public String getTime();
	
	public void insertUser(UserVO vo);
	
    public UserVO readUser(String uid) throws Exception;
	
	public UserVO readWithPw(String uid, String upw) throws Exception;
	
	public UserVO login(LoginDTO dto) throws Exception;
	
	public void keepLogin(String uid, String sessionId, Date next);
	
	public UserVO checkUserWithSessionKey(String value);

}

