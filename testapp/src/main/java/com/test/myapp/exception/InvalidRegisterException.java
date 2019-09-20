package com.test.myapp.exception;

public class InvalidRegisterException extends Exception{
	private String msg;
	@Override
	public String getMessage(){
		return msg;
	}
	
	public void setMessage(String msg){
		this.msg = msg;
	}
	
	public InvalidRegisterException(String msg){
		this.msg = msg;
	}
}

