package com.imooc.o2o.enums;

public enum ShopAuthMapEnum {
    
	SUCCESS(1, "操作成功"), FAIL(-1001, "操作失败"), NULL_AUTH(-1002,"无用户");
    
    private int state;

	private String stateInfo;

	private ShopAuthMapEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}
    
	public static ShopAuthMapEnum stateOf(int index) {
		for(ShopAuthMapEnum shopAuthMapEnum : values()) {
			if(shopAuthMapEnum.getState() == index)
				return shopAuthMapEnum;
		}
		return null;
	}
}
