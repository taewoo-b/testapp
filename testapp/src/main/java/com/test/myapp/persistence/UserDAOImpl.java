package com.test.myapp.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.test.myapp.domain.UserVO;
import com.test.myapp.dto.LoginDTO;

@Repository
public class UserDAOImpl implements UserDAO {
	@Inject
	private SqlSession sqlSession;
	
	private static final String namespace = "com.test.myapp.mapper.UserMapper";
	
	@Override
	public String getTime(){
		return sqlSession.selectOne(namespace + ".getTime");
	}
	
	@Override
	public void insertUser(UserVO vo){
		sqlSession.insert(namespace + ".insertUser", vo);
	}

	@Override
	public UserVO readUser(String uid) throws Exception {
		return (UserVO) sqlSession.selectOne(namespace + ".selectUser", uid);
	}
	
	@Override
	public UserVO readWithPw(String uid, String pw) throws Exception {
		Map<String, Object> paramMap = new HashMap<String,Object>();
		
		paramMap.put("uid", uid);
		paramMap.put("upw", pw);
		
		return sqlSession.selectOne(namespace + ".readWithPW", paramMap);
	}
	
	@Override
	public UserVO login(LoginDTO dto) throws Exception {
		return sqlSession.selectOne(namespace + ".login", dto);
	}

	@Override
	public UserVO checkUserWithSessionKey(String value){
		return sqlSession.selectOne(namespace + ".checkUserWithSessionKey", value);
	}

	
	@Override
	public void keepLogin(String uid, String sessionId, java.util.Date next){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("uid",  uid);
		paramMap.put("sessionId", sessionId);
		paramMap.put("next", next);
		
		sqlSession.update(namespace + ".keepLogin", paramMap);
	}





}
