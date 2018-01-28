package com.imooc.o2o.config.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
/**
 * Quartz调度配置类
 * @author 10353
 *
 */
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.imooc.o2o.service.ProductSellDailyService;
@Configuration
public class QuartzConfiguration {
    
	@Autowired
	private ProductSellDailyService productSellDailyJob;
	
	@Autowired
	private MethodInvokingJobDetailFactoryBean productSellDailyDetailFactory;
	
	@Autowired
	private CronTriggerFactoryBean productSellDailyTriggerFactory;
	
	/**
	 * 任务细节工厂
	 * @return
	 */
	@Bean
	public MethodInvokingJobDetailFactoryBean createJobDetail() {
		MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
		jobDetailFactoryBean.setName("product_sell_daily_job");
		jobDetailFactoryBean.setGroup("job_product_sell_daily_group");
		//禁止任务并行执行，即一个任务完成以后另一个任务开始
		jobDetailFactoryBean.setConcurrent(false);
		jobDetailFactoryBean.setTargetObject(productSellDailyJob);
		jobDetailFactoryBean.setTargetMethod("dailyCalculate");
		return jobDetailFactoryBean;
	}
	
	
	/**
	 * 时间触发器工厂
	 * @return
	 */
	@Bean
	public CronTriggerFactoryBean createJobTrigger() {
		CronTriggerFactoryBean jobTrigger = new CronTriggerFactoryBean();
		jobTrigger.setName("product_sell_daily_trigger");
		jobTrigger.setGroup("job_product_sell_daily_group");
		jobTrigger.setJobDetail(productSellDailyDetailFactory.getObject());
		jobTrigger.setCronExpression("0 0 0 * * ? ");
		return jobTrigger;
	}
	
	/**
	 * 创建调度工厂
	 * @return
	 */
	@Bean
	public SchedulerFactoryBean createScheduler() {
		SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
		scheduler.setTriggers(productSellDailyTriggerFactory.getObject());
		return scheduler;
	}
	
}
