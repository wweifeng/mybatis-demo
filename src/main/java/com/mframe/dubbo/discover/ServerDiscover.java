package com.mframe.dubbo.discover;

import java.util.List;

public interface ServerDiscover {
	
	List<String> getServer(String serviceName);

}
