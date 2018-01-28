package com.imooc.o2o.config.dao;

import java.io.IOException;

import javax.sql.DataSource;


import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;


@Configuration
public class SqlSessionFactoryConfiguration {
	
	@Autowired
	private DataSource dataSource;
	
	private static String mybatisConfigFile;
	
	private static String mapperPath;
	
	
	@Value("${type_alias_package}")
	private String typeAliasPackage;
	
	
	@Value("${mybatis_config_file}")
	public void setMybatisConfigFile(String mybatisConfigFile) {
		SqlSessionFactoryConfiguration.mybatisConfigFile = mybatisConfigFile;
	}


	@Value("${mybatis_mapper_path}")
	public void setMapperPath(String mapperPath) {
		SqlSessionFactoryConfiguration.mapperPath = mapperPath;
	}


	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactoryBean createSqlSessionFactoryBean() throws IOException {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		//设置mybatis-config的扫描路径
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisConfigFile));
		//设置mapper扫描路径
		//获取类加载路径
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + mapperPath;
		sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(packageSearchPath));
		
		//设置DateSoucre即数据源
		sqlSessionFactoryBean.setDataSource(dataSource);
		//设置别名包扫描路径
		sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasPackage);
		return sqlSessionFactoryBean;
	}
}
