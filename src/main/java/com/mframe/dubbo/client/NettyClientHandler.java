package com.mframe.dubbo.client;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class NettyClientHandler extends ChannelHandlerAdapter {

	private Object response;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		this.response = msg;
		ctx.channel().close();
	}

	public Object getResponse() {
		return response;
	}
	
}
