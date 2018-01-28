package com.imooc.o2o;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
    
	@RequestMapping(value="/hello", method = RequestMethod.GET)
	public String hello() {
		return "Hello o2o For SpringBoot";
	}
}
