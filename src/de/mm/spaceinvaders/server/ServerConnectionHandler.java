package de.mm.spaceinvaders.server;

import de.mm.spaceinvaders.io.ConnectionHandler;
import de.mm.spaceinvaders.io.PipelineInitiator;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServerConnectionHandler implements Runnable
{

	private final int port;

	private EventLoopGroup eventLoops;

	@Override
	public void run()
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
				PipelineInitiator.initPipeline(channel.pipeline());
				channel.pipeline().addLast(
						new ConnectionHandler(channel, new ServerPacketHandler()));
			}
		});
		ChannelFuture f;
		try
		{
			f = b.bind(port).sync();

			f.addListener(new ChannelFutureListener()
			{

				@Override
				public void operationComplete(ChannelFuture future) throws Exception
				{
					if (!future.isSuccess())
					{
						System.out.println("Fail :(");
						System.exit(0);
					}
				}
			});

			f.channel().closeFuture().sync();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

}
