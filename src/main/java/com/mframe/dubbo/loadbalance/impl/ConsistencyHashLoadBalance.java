package com.mframe.dubbo.loadbalance.impl;

import java.util.List;

/**
 * 一致性Hash
 */
import com.mframe.dubbo.loadbalance.LoadBalance;

public class ConsistencyHashLoadBalance implements LoadBalance {

	@Override
	public String chooseServer(List<String> serverList) {
		return null;
	}

	public String chooseHashServer(List<String> serverList, String address) {
		int index = address.hashCode() % serverList.size();
		return serverList.get(index);
	}
	
}
