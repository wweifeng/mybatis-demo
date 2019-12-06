package com.mframe.dubbo.service.impl;

import com.mframe.dubbo.anotation.DubboRpc;
import com.mframe.dubbo.service.DemoService;

@DubboRpc(DemoService.class)
public class DemoServiceImpl implements DemoService {

	@Override
	public String sayHello(String name, String content) {
		return "Hello, " + name + ", bu zhi dao ni shuo de :" + content + " shi sha yi si";
	}

}
