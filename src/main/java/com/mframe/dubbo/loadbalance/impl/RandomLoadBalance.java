package com.mframe.dubbo.loadbalance.impl;

import java.util.List;
import java.util.Random;

import com.mframe.dubbo.loadbalance.LoadBalance;

public class RandomLoadBalance implements LoadBalance {

	@Override
	public String chooseServer(List<String> serverList) {
	    int index = new Random().nextInt(serverList.size());
	    return serverList.get(index);
	}

}
