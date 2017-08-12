package com.mdd.proxyip;

/**
 * 代理ip匿名度
 * @author xwl
 * @version 1.0
 * @date 2017.5.21
 */
public enum AnonymityEnum {

	TRANSPARENT(1,"透明"),
	ANONYMOUS(2,"高匿");
	
	private int code;
	private String anonymous;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getAnonymous() {
		return anonymous;
	}

	public void setAnonymous(String anonymous) {
		this.anonymous = anonymous;
	}

	private AnonymityEnum(int code, String anonymous) {
		this.code = code;
		this.anonymous = anonymous;
	}

}
