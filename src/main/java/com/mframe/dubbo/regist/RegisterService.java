package com.mframe.dubbo.regist;

public interface RegisterService {
	
	void register(String serviceName, String serviceAddress) throws Exception;

}
