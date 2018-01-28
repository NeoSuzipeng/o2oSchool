package com.imooc.o2o.enums;

public enum UserAwardMapStateEnum {
   SUCCESS(1,"操作成功"), FAIL(-1,"操作失败"), NULL_ERROR(-1000,"参数为空");
	
	private int state;
	
	private String stateInfo;

	private UserAwardMapStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	
	/**
	 * 根据传入的状态值返回UserAwardMapStateEnum对象
	 * @param state
	 * @return
	 */
	public static UserAwardMapStateEnum stateOf(int state) {
		for(UserAwardMapStateEnum UserAwardMapStateEnum: values()) {
			if(state == UserAwardMapStateEnum.getState()) {
				return UserAwardMapStateEnum;
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
