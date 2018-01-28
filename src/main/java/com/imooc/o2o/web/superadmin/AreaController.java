package com.imooc.o2o.web.superadmin;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.entity.Area;
import com.imooc.o2o.service.AreaService;

import ch.qos.logback.classic.Logger;

@Controller
@RequestMapping("/superadmin")
public class AreaController {
	
	Logger logger = (Logger) LoggerFactory.getLogger(AreaController.class);
	
	@Autowired
	private AreaService areaService;
	
	/**
	 * @ResponseBody 
     * 功能：将Map等数据转化为JSON对象发送给前端
	 * @return Map
	 */
	@RequestMapping(value="/listarea", method=RequestMethod.GET)
	@ResponseBody
    public Map<String, Object> listArea(){
		logger.info("---start---");
		Long startTime = System.currentTimeMillis();
    	Map<String, Object> modelMap = new HashMap<String, Object>();
        List<Area> areas = new ArrayList<Area>();
        try{
            areas =  areaService.getQueryArea();
            modelMap.put("rows", areas);
            modelMap.put("total", areas.size());
        }catch(Exception e) {
        	modelMap.put("success", false);
        	modelMap.put("errMsg", e.toString());
        }
        Long endTime = System.currentTimeMillis();
        logger.debug("costTime:[{}ms]",endTime - startTime);
        logger.info("---end---");
        return modelMap;
    }
    
}
