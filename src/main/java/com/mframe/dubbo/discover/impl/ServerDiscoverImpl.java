package com.mframe.dubbo.discover.impl;

import java.util.List;

import org.I0Itec.zkclient.ZkClient;

import com.mframe.dubbo.discover.ServerDiscover;

public class ServerDiscoverImpl implements ServerDiscover {

	private ZkClient zkClient;
	
	private final static String zkServer = "127.0.0.1:2181";
	
	private final static int connectionTimeOut = 5000;
	
	private final static String rootPath = "/dubbo";
	
	private final static String provider = "/providers";
	
	public ServerDiscoverImpl() {
		zkClient = new ZkClient(zkServer, connectionTimeOut);
	}
	
	@Override
	public List<String> getServer(String serviceName) {
		String serviceNamePath = rootPath + "/" + serviceName + provider;
		return zkClient.getChildren(serviceNamePath);
	}

}
