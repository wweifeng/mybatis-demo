package com.mframe.dubbo.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URLDecoder;
import java.util.List;

import com.mframe.dubbo.client.NettyClient;
import com.mframe.dubbo.discover.ServerDiscover;
import com.mframe.dubbo.discover.impl.ServerDiscoverImpl;
import com.mframe.dubbo.loadbalance.LoadBalance;
import com.mframe.dubbo.loadbalance.impl.LoopLoadBalance;
import com.mframe.dubbo.req.RpcReq;

public class NettyProxy {
	
	private ServerDiscover serverDiscover;
	
	private LoadBalance loadBalance;
	
	public NettyProxy() {
		serverDiscover = new ServerDiscoverImpl();
		loadBalance = new LoopLoadBalance();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T createProxy(Class<?> object) {
		return (T) Proxy.newProxyInstance(object.getClassLoader(), new Class[] {object}, new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				RpcReq req = new RpcReq();
				req.setClassName(object.getName());
				req.setMethodName(method.getName());
				req.setParameterTypes(method.getParameterTypes());
				req.setArgs(args);
				// 获取服务器
				List<String> serverList = serverDiscover.getServer(object.getName());
				// 负载均衡
				String server = loadBalance.chooseServer(serverList);
				server = URLDecoder.decode(server, "UTF-8");
				String[] addressArr = server.split(":");
				String host = addressArr[1].replace("//", "");
				String port = addressArr[2];
				return new NettyClient(host, Integer.valueOf(port)).sendMsg(req);
			}
		});
	}

}
