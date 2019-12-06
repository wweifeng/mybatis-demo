package com.mframe.dubbo.server;

import java.lang.reflect.Method;
import java.util.Map;

import com.mframe.dubbo.req.RpcReq;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class NettyServerHandler extends ChannelHandlerAdapter {

	private Map<String, Object> registerCache;
	
	private int port;
	
	public NettyServerHandler(Map<String, Object> registerCache, int port) {
		this.registerCache = registerCache;
		this.port = port;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		RpcReq req = (RpcReq) msg;
		// 获取要执行的对象
		Object object = registerCache.get(req.getClassName());
		if(object == null) {
			return;
		}
		System.out.println("port : " + port + ",正在处理消息...");
		Method method =  object.getClass().getMethod(req.getMethodName(), req.getParameterTypes());
		Object result = method.invoke(object, req.getArgs());
		ctx.writeAndFlush(result);
	}

}
