package com.mframe.dubbo.loadbalance;

import java.util.List;

public interface LoadBalance {
	
	String chooseServer(List<String> serverList);

}
