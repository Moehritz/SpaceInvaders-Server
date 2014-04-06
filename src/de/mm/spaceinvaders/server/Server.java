package de.mm.spaceinvaders.server;

import de.mm.spaceinvaders.protocol.Protocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

	private int port = 8765;

	public void run() throws InterruptedException {
		new Protocol();
		
		EventLoopGroup eventLoops = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.channel(NioServerSocketChannel.class)
					.group(eventLoops)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							ch.pipeline().addLast(new SpaceDecoder());
							ch.pipeline().addLast(new SpaceEncoder());
							ch.pipeline().addLast(new SpaceServerHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture f = b.bind(port).sync();

			f.channel().closeFuture().sync();
		} finally {
			eventLoops.shutdownGracefully();
		}
	}
}
