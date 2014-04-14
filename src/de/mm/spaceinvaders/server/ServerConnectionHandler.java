package de.mm.spaceinvaders.server;

import de.mm.spaceinvaders.io.ConnectionHandler;
import de.mm.spaceinvaders.io.FrameDecoder;
import de.mm.spaceinvaders.io.FramePrepender;
import de.mm.spaceinvaders.io.SpaceDecoder;
import de.mm.spaceinvaders.io.SpaceEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServerConnectionHandler
{

	private final int port;

	private EventLoopGroup eventLoops;

	public void start() throws InterruptedException
	{
		try
		{
			eventLoops = new NioEventLoopGroup();

			ServerBootstrap b = new ServerBootstrap();
			b.group(eventLoops);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(new ChannelInitializer<SocketChannel>()
			{

				@Override
				protected void initChannel(SocketChannel channel) throws Exception
				{
					channel.pipeline().addLast(new FramePrepender());
					channel.pipeline().addLast(new FrameDecoder());
					channel.pipeline().addLast(new SpaceEncoder());
					channel.pipeline().addLast(new SpaceDecoder());
					channel.pipeline().addLast(new ConnectionHandler(channel, new ServerPacketHandler()));
				}
			});
			ChannelFuture f = b.bind(port).sync();

			f.channel().closeFuture().sync();
		}
		finally
		{
			eventLoops.shutdownGracefully();
		}
	}

}
