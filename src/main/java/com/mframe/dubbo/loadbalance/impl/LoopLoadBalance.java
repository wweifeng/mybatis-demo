package com.mframe.dubbo.loadbalance.impl;

import java.util.List;

import com.mframe.dubbo.loadbalance.LoadBalance;

/**
 * 轮训
 * @author wwf
 *
 */
public class LoopLoadBalance implements LoadBalance {

	private static int index;
	
	@Override
	public String chooseServer(List<String> serverList) {
		if(index >= serverList.size()) {
			index = 0;
		}
		return serverList.get(index++);
	}

}
