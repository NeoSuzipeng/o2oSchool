package com.imooc.o2o.enums;

public enum UserProductMapStateEnum {
	
    SUCCESS(1,"操作成功"), FAIL(-1,"操作失败"), NULL_ERROR(-1000,"参数为空");
	
	private int state;
	
	private String stateInfo;

	private UserProductMapStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	
	/**
	 * 根据传入的状态值返回UserProductMapStateEnum对象
	 * @param state
	 * @return
	 */
	public static UserProductMapStateEnum stateOf(int state) {
		for(UserProductMapStateEnum UserProductMapStateEnum: values()) {
			if(state == UserProductMapStateEnum.getState()) {
				return UserProductMapStateEnum;
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
