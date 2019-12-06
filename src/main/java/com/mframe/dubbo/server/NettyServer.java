package com.mframe.dubbo.server;

import java.util.HashMap;
import java.util.Map;

import com.mframe.dubbo.anotation.DubboRpc;
import com.mframe.dubbo.marshalling.MarshallingCodeCFactory;
import com.mframe.dubbo.regist.RegisterService;
import com.mframe.dubbo.regist.impl.RegisterServiceImpl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyServer {
	
	private static String HOST = "127.0.0.1";
	
	private static String DUBBO = "dubbo://";
	
	//private static int PORT = 8088;
	
	private static Map<String, Object> registerCache = new HashMap<>();
	
	private int port;
	
	private Object object;
	
	private RegisterService registerService;
	
	public NettyServer() {
		registerService = new RegisterServiceImpl();
	}
	
	public NettyServer bindPort(int port) {
		this.port = port;
		return this;
	}
	
	public NettyServer bindObject(Object object) {
		this.object = object;
		return this;
	}
	
	public void start() throws Exception {
		// 注册到ZK
		this.bind(object);
		// 启动Netty
		this.start(port);
	}
	
	private void bind(Object object) throws Exception {
		DubboRpc dubboRpc = object.getClass().getAnnotation(DubboRpc.class);
		if(dubboRpc == null) {
			return;
		}
		String serviceName = dubboRpc.value().getName();
		// 注册到ZK
		String serviceAddress = DUBBO + HOST + ":" + port;
		registerService.register(serviceName, serviceAddress);
		registerCache.put(serviceName, object);
	}
	
	private void start(int port) {
		// 用来接收进来的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(); 
        // 用来处理已经被接收的连接，一旦bossGroup接收到连接，就会把连接信息注册到workerGroup上
        EventLoopGroup workerGroup = new NioEventLoopGroup();
		
        try {
            // nio服务的启动类
            ServerBootstrap sbs = new ServerBootstrap();
            // 配置nio服务参数
            sbs.group(bossGroup, workerGroup)
               .channel(NioServerSocketChannel.class) // 说明一个新的Channel如何接收进来的连接
               .option(ChannelOption.SO_BACKLOG, 128) // tcp最大缓存链接个数
               .childOption(ChannelOption.SO_KEEPALIVE, true) //保持连接
               .handler(new LoggingHandler(LogLevel.INFO)) // 打印日志级别
               .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        
                        // marshalling 序列化对象的解码
                        socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                        // marshalling 序列化对象的编码
                        socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                        // 网络超时时间
                        //socketChannel.pipeline().addLast(new ReadTimeoutHandler(5));
                    	//ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                    	//socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                    	// 处理接收到的请求
                        socketChannel.pipeline().addLast(new NettyServerHandler(registerCache, port)); // 这里相当于过滤器，可以配置多个
                    }
               });
            
            System.err.println("server 开启--------------");
            // 绑定端口，开始接受链接
            ChannelFuture cf = sbs.bind(port).sync();
            
            // 开多个端口
//          ChannelFuture cf2 = sbs.bind(3333).sync();
//          cf2.channel().closeFuture().sync();
            
            // 等待服务端口的关闭；在这个例子中不会发生，但你可以优雅实现；关闭你的服务
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
			e.printStackTrace();
		} finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }           
	}

}
