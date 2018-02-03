package com.gjw.netty.keepalive;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class KeepAliveServer {
    
    private int port ;

    public KeepAliveServer(int port) {
        this.port = port;
    }
    
    ChannelFuture f ;
    
    ServerBootstrap b ;
    
    public void startServer() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {
            b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new KeepAliveServerInitializer());
            // �������󶨶˿ڼ���
            f = b.bind(port).sync();
            // �����������رռ������˷���������
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        
    }
    
    public static void main(String[] args) {
    	KeepAliveServer kServer = new KeepAliveServer(8088);
    	kServer.startServer();
	}
}