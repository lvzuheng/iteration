package com.lzh.iteration.bean.http;

public class IterationRequest {

	private String conditionKey;
	private String packName;
	
	public IterationRequest(){}
	public IterationRequest(String conditionKey,String packName){
		this.conditionKey = conditionKey;
		this.packName = packName;
	}
	
	public String getConditionKey() {
		return conditionKey;
	}
	public void setConditionKey(String conditionKey) {
		this.conditionKey = conditionKey;
	}
	public String getPackName() {
		return packName;
	}
	public void setPackName(String packName) {
		this.packName = packName;
	}
}
