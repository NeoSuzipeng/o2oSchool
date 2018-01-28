package com.imooc.o2o.enums;

public enum ProductCategoryStateEnum{
	SUCCESS(1, "创建成功"), INNER_ERROR(-1001, "操作失败"), EMPTY_LIST(-1002, "添加数少于1");
    
	private String stateInfo;
	
	private int state;
	
	private ProductCategoryStateEnum(int state, String stateInfo) {
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
	public static ProductCategoryStateEnum stateOf(int index) {
		for(ProductCategoryStateEnum productCategoryStateEnum : values()) {
			if(productCategoryStateEnum.getState() == index){
				return productCategoryStateEnum;
			}
		} 
		return null;
	}
	
}
