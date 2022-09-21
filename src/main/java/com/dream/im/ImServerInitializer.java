package com.dream.im;

import com.dream.container.anno.Assign;
import com.dream.container.anno.Component;
import com.dream.im.codec.ImHttpServerHandler;
import com.dream.im.codec.ImMessageBindingHandler;
import com.dream.im.codec.ImMessageDecoder;
import com.dream.im.constant.ImConst;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

@Component(proxy = false)
public class ImServerInitializer extends ChannelInitializer<SocketChannel>
{

    @Assign
    private ImMessageBindingHandler bindingHandler;

    @Assign
    private ImHttpServerHandler httpServerHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception
    {
        ChannelPipeline pipeline = socketChannel.pipeline();

        //pipeline.addLast(authenticateHandler);

        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(httpServerHandler);

        pipeline.addLast(new WebSocketServerCompressionHandler());

        WebSocketServerProtocolConfig serverProtocolConfig = WebSocketServerProtocolConfig.newBuilder()
                .checkStartsWith(true)
                .websocketPath(ImConst.WEBSOCKET_PATH)
                .allowExtensions(true)
                .maxFramePayloadLength(1024 * 1024)
                .build();

        pipeline.addLast(new WebSocketServerProtocolHandler(serverProtocolConfig));

        pipeline.addLast(new ImMessageDecoder());
        pipeline.addLast(bindingHandler);
    }
}
