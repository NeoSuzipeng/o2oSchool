package com.imooc.o2o.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.o2o.entity.Area;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaServiceTest{
    
	@Autowired
	private AreaService service;
	
	
	@Test
	public void testgetQueryArea() {
		List<Area> areas = service.getQueryArea();
		assertEquals(2, areas.size());
	}
}
