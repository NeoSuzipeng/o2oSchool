package com.imooc.o2o.dao.split;

import java.util.Locale;
import java.util.Properties;

import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Mybatis拦截器
 * 写操作时使用主库
 * 读操作使用从库
 * @author 10353
 *
 */
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
	@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
			RowBounds.class, ResultHandler.class }) })
public class DynamicDataSourceInterceptor implements Interceptor{
    
	private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceInterceptor.class);
	
	private static final String REGEX = ".*insert\\u0020.*|.*update\\\\u0020.*|.*delete\\\\u0020.*";
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		//判断操作是否是使用事务进行管理,是返回true否返回false
		boolean synchronizationActive = TransactionSynchronizationManager.isActualTransactionActive();
		Object[] objects = invocation.getArgs();
		MappedStatement ms = (MappedStatement)objects[0];
		String lookUpKey = DynamicDataSourceHolder.DB_MASTER;  //默认主库
		if(synchronizationActive != true) {
			//读方法处理
			if(ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
				//selectKey为自增id查询主键方法（SELECT LAST_INSERT_ID()）,使用主库
				if(ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
					lookUpKey = DynamicDataSourceHolder.DB_MASTER;
				}else {
					BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);
					String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
					if(sql.matches(REGEX)) {
						lookUpKey = DynamicDataSourceHolder.DB_MASTER;
					}else {
						lookUpKey = DynamicDataSourceHolder.DB_SLAVE;
					}
				}
			}
		}else {
			lookUpKey = DynamicDataSourceHolder.DB_MASTER;
		}
		logger.debug("设置方法[{}] use [{}] Strategy, SqlCommanType[{}]", ms.getId(), lookUpKey, ms.getSqlCommandType().name());
		DynamicDataSourceHolder.setDbType(lookUpKey);
		return invocation.proceed();
	}
    
	
	@Override
	public Object plugin(Object target) {
		//如果拦截的对象是mybatis执行器则将其包装
		//执行器作用：完成读写
		if(target instanceof Executor){  
		    return Plugin.wrap(target, this);
		}else {
			//否则直接返回
			return target;
		}
	}

	@Override
	public void setProperties(Properties arg0) {
		
	}

}
