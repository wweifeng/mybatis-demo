package com.mframe.plugin;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;


@Intercepts({@Signature(type = StatementHandler.class, args = { Connection.class }, method = "prepare")})
public class MyPlugin implements Interceptor {

	public Object intercept(Invocation invocation) throws Throwable {
		
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		
		MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
		
		System.out.println(metaObject.getValue("delegate.boundSql.sql"));
		
	    //int i = 1/0; 
		
		return invocation.proceed();
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
		System.out.println(properties);
	}

}
