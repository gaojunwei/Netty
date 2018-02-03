package com.gjw.netty.server.handler;

import com.gjw.netty.client.handler.ClientHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		channel.pipeline().addLast(new ClientHandler());
	}

}
