package com.mframe.dubbo.client;

import com.mframe.dubbo.marshalling.MarshallingCodeCFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
	
	// 要请求的服务器的ip地址
    private String ip;
    // 服务器的端口
    private int port;
    
    public NettyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    
    public Object sendMsg(Object msg) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        NettyClientHandler handler = new NettyClientHandler();
        
        Bootstrap bs = new Bootstrap();
        bs.group(bossGroup)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.SO_KEEPALIVE, true)
          .handler(new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel socketChannel) throws Exception {              
            	    // marshalling 序列化对象的解码
                    socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                    // marshalling 序列化对象的编码
                    socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
            	    //ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
              	    //socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                    // 处理来自服务端的响应信息
                    socketChannel.pipeline().addLast(handler);
              }
         });
        
        // 客户端开启
        ChannelFuture cf = bs.connect(ip, port).sync();
        cf.channel().writeAndFlush(msg);
        // 等待获取到响应之后连接中断
        cf.channel().closeFuture().sync(); 
        return handler.getResponse();
    }

}
