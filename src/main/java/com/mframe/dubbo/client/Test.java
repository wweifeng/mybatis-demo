package com.mframe.dubbo.client;

import com.mframe.dubbo.proxy.NettyProxy;
import com.mframe.dubbo.service.DemoService;

public class Test {
	
	public static void main(String[] args) {
		
		DemoService demoService = new NettyProxy().createProxy(DemoService.class);
		for(int i=0;i<5;i++) {
			String result = demoService.sayHello("wangwf", "ni zhen shuai");
			System.out.println(result);
		}
	}
}
