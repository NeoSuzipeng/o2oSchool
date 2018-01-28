package com.imooc.o2o.enums;

public enum ShopStateEnum{
	CHECK(0, "审核中"), OFFLINE(-1, "非法店铺"), SUCCESS(1, "操作成功"), PASS(2, "通过审核"), 
	INNERT_ERROR(-1001,"内部错误"), NULL_SHOPID(-1002, "ShopId为空"), NULL_SHOP(-1003, "Shop为空");
	
	private int state;
	
	private String stateInfo;

	private ShopStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	
	/**
	 * 根据传入的状态值返回ShopStateEnum对象
	 * @param state
	 * @return
	 */
	public static ShopStateEnum stateOf(int state) {
		for(ShopStateEnum shopStateEnum: values()) {
			if(state == shopStateEnum.getState()) {
				return shopStateEnum;
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
