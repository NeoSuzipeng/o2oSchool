package com.imooc.o2o.config.service;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
/**
 * 由于事务管理无法使用使用Bean的形式
 * <tx:annotation-driven transaction-manager="transactionManager"/>
 * 则使用继承的形式
 */
@Configuration
@EnableTransactionManagement   //使用注解启动事务，而service层的使用则为@Transactional
public class TransactionManagementConfig implements TransactionManagementConfigurer{
    
	@Autowired
	private DataSource dataSource;  //注入需要事务管理的数据源
	
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

}
