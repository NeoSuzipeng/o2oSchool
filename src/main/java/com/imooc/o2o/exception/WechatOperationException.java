package com.imooc.o2o.exception;

public class WechatOperationException extends RuntimeException {

	
	private static final long serialVersionUID = 3301777973282698064L;
    
	public WechatOperationException(String msg) {
		super(msg);
	}
}
