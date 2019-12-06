package com.mframe.netty;

import java.io.UnsupportedEncodingException;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

public class NettyClient {
	
	  // 要请求的服务器的ip地址
    private String ip;
    // 服务器的端口
    private int port;
    
    public NettyClient(String ip, int port){
        this.ip = ip;
        this.port = port;
    }
    
    // 请求端主题
    private void action() throws InterruptedException, UnsupportedEncodingException {
        
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        
        Bootstrap bs = new Bootstrap();
        
        bs.group(bossGroup)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.SO_KEEPALIVE, true)
          .handler(new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel socketChannel) throws Exception {              
                    // marshalling 序列化对象的解码
//                  socketChannel.pipeline().addLast(MarshallingCodefactory.buildDecoder());
                    // marshalling 序列化对象的编码
//                  socketChannel.pipeline().addLast(MarshallingCodefactory.buildEncoder());
            	    ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
              	    socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                    // 处理来自服务端的响应信息
                    socketChannel.pipeline().addLast(new ClientHandler());
              }
         });
        
        // 客户端开启
        ChannelFuture cf = bs.connect(ip, port).sync();
        
        String reqStr = "我是客户端请求1$_我是客户端请求2$_我是客户端请求3$_";
        
        // 发送客户端的请求
        cf.channel().writeAndFlush(Unpooled.copiedBuffer(reqStr.getBytes("UTF-8")));
//      Thread.sleep(300);
//      cf.channel().writeAndFlush(Unpooled.copiedBuffer("我是客户端请求2$_---".getBytes(Constant.charset)));
//      Thread.sleep(300);
//      cf.channel().writeAndFlush(Unpooled.copiedBuffer("我是客户端请求3$_".getBytes(Constant.charset)));
        
//      Student student = new Student();
//      student.setId(3);
//      student.setName("张三");
//      cf.channel().writeAndFlush(student);
        
        // 等待直到连接中断
        cf.channel().closeFuture().sync();      
    }
            
    public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {
        new NettyClient("127.0.0.1", 8081).action();
    }

}
