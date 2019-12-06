package com.mframe.dubbo.server;

import com.mframe.dubbo.service.impl.DemoServiceImpl;

public class Test {
	
	public static void main(String[] args) throws Exception {
		
		NettyServer nettyServer = new NettyServer().bindPort(8090)
				                                   .bindObject(new DemoServiceImpl());
		nettyServer.start();
	}
}
