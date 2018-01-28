package com.imooc.o2o.enums;

public enum AwardStateEnum {
   
	SUCCESS(1,"操作成功"), FAIL(-1,"操作失败"), NULL_ERROR(-1000,"参数为空");
	
	private int state;
	
	private String stateInfo;

	private AwardStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	
	/**
	 * 根据传入的状态值返回AwardStateEnum对象
	 * @param state
	 * @return
	 */
	public static AwardStateEnum stateOf(int state) {
		for(AwardStateEnum AwardStateEnum: values()) {
			if(state == AwardStateEnum.getState()) {
				return AwardStateEnum;
			}
		}
		return null;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	} 
}
