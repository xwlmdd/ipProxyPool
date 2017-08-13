package com.mdd.proxyip.model;

public enum ProxyIpTypeEnum {
	HTTP(1,"HTTP"),
	HTTPS(2,"HTTPS");
	
	private int typeNo;
	private String typeName;
	
	private ProxyIpTypeEnum(int typeNo, String typeName) {
		this.typeNo = typeNo;
		this.typeName = typeName;
	}
	public int getTypeNo() {
		return typeNo;
	}
	public void setTypeNo(int typeNo) {
		this.typeNo = typeNo;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
	
	
}
