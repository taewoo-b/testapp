package com.test.myapp.exception;

public class InvalidAccessException extends Exception{
	@Override
	public String getMessage(){
		return "잘못된 접근입니다. 값이 임의 변조되지 않았는지 확인하세요";
	}
}
