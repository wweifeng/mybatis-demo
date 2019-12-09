package com.mframe.dubbo.loadbalance.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.mframe.dubbo.loadbalance.LoadBalance;

/**
 * 轮训
 * @author wwf
 *
 */
public class LoopLoadBalance implements LoadBalance {

	private AtomicInteger index = new AtomicInteger(0);
	
	public String chooseServer(List<String> serverList) {
		int i = index.getAndIncrement() % serverList.size();
		return serverList.get(i);
	}

}
