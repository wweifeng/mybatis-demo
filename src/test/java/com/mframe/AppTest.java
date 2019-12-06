package com.mframe;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.FloatTypeHandler;
import org.apache.ibatis.type.StringTypeHandler;

import com.mframe.mapper.UserMapper;
import com.mframe.model.User;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    
	public static void main(String[] args) throws Exception {
		selectByUserId();
		
		//select();
	}

	public static void select() throws Exception {
		// 配置数据源
		PooledDataSource dataSource = new PooledDataSource();
		dataSource.setDriver("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://47.107.224.17:3306/testdb");
		dataSource.setUsername("root");
		dataSource.setPassword("123456");
		// 配置事务
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
        // 创建数据库运行环境
		Environment environment = new Environment("dev", transactionFactory, dataSource);
		// 构建Configuration对象
		Configuration configuration = new Configuration(environment);
		configuration.addMapper(UserMapper.class);
		configuration.addLoadedResource("mapper/UserMapper.xml");
		// 加入映射
		//configuration.addMappers("mapper/UserMapper.xml");
		// 创建SqlSessionFactory
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		sqlSession.close();
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User user = userMapper.selectByUserId(1);
		System.out.println(user.getUserName());
	}
	
	public static void selectByUserId() throws Exception {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User user = userMapper.selectByUserId(1);
		System.out.println(user.getUserName());
	}
	
}
