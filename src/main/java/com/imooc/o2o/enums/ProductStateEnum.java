package com.imooc.o2o.enums;

public enum ProductStateEnum {
	SUCCESS(1, "创建成功"), INNER_ERROR(-1001, "添加失败"), NULL_PRODUCT(-1002,"添加商品少于1");
	
	private String stateInfo;
	
	private int state;
	
	private ProductStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
    
	/**
	 * 根据状态值返回对应的枚举对象
	 * @param index
	 * @return
	 */
	public static ProductStateEnum stateOf(int index) {
		for(ProductStateEnum ProductStateEnum : values()) {
			if(ProductStateEnum.getState() == index){
				return ProductStateEnum;
			}
		} 
		return null;
	}
}
