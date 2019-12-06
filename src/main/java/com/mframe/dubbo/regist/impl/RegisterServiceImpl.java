package com.mframe.dubbo.regist.impl;

import java.net.URLEncoder;

import org.I0Itec.zkclient.ZkClient;

import com.mframe.dubbo.regist.RegisterService;
import com.mframe.mapper.UserMapper;

public class RegisterServiceImpl implements RegisterService {

	private final static String zkServer = "127.0.0.1:2181";
	
	private final static int connectionTimeOut = 5000;
	
	private final static String rootPath = "/dubbo";
	
	private final static String provider = "/providers";
	
	private ZkClient zkClient;
	
	public RegisterServiceImpl() {
		zkClient = new ZkClient(zkServer, connectionTimeOut);
	}
	
	@Override
	public void register(String serviceName, String serviceAddress) throws Exception {
		// 创建根节点
		if(!zkClient.exists(rootPath)) {
			zkClient.createPersistent(rootPath);
		}
		// 创建服务节点
		String serviceNodePath = rootPath + "/" + serviceName;
		if(!zkClient.exists(serviceNodePath)) {
			zkClient.createPersistent(serviceNodePath);
		}
		// 创建Providers节点
		String providerNodePath = serviceNodePath + provider;
		if(!zkClient.exists(providerNodePath)) {
			zkClient.createPersistent(providerNodePath);
		}
		// 创建服务地址节点
		String serviceAddressNodePath = providerNodePath + "/" + URLEncoder.encode(serviceAddress,"UTF-8");
		if(zkClient.exists(serviceAddressNodePath)) {
			zkClient.delete(providerNodePath);
		}
		zkClient.createEphemeral(serviceAddressNodePath);
	}
	
	public static void main(String[] args) throws Exception {
		new RegisterServiceImpl().register(UserMapper.class.getName(), "selectByUserId");
	}

}
