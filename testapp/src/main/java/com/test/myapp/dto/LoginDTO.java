package com.test.myapp.dto;

public class LoginDTO {
	private String uid;
	private String upw;
	private boolean useAutoLogin;
	
	@Override
	public String toString(){
		return "로긴DTO[uid=" + uid + ", upw=" + upw + ", useAutoLogin=" + useAutoLogin + "]";
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUpw() {
		return upw;
	}

	public void setUpw(String upw) {
		this.upw = upw;
	}

	public boolean isUseAutoLogin() {
		return useAutoLogin;
	}

	public void setUseAutoLogin(boolean useAutoLogin) {
		this.useAutoLogin = useAutoLogin;
	}
}
