package com.dream.im.codec;

import com.dream.container.anno.Assign;
import com.dream.container.anno.Component;
import com.dream.im.*;
import com.dream.im.constant.ImConst;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Parameter;

@Slf4j
@ChannelHandler.Sharable
@Component(proxy = false)
public class ImMessageBindingHandler extends SimpleChannelInboundHandler<ImMessage> {

    @Assign
    private ImHandlerContainer handlerContainer;

    @Assign
    private ImParameterBinders binders;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ImMessage message) throws Exception {

        ImHandlerDesc handlerDesc = handlerContainer.getHandler(message.getName());

        if (handlerDesc == null)
        {
            log.error("请求了一个不存在的服务, name: {}", message.getName());
            return;
        }

        if (handlerDesc.isMustAuthenticate())
        {
            ImAuthenticatedInfo authenticatedInfo = ctx.channel().attr(ImConst.AUTHORIZATION_KEY).get();

            if (authenticatedInfo == null)
            {
                log.info("客户端未授权");
                ctx.close();
                return;
            }
        }

        JsonMapper jsonMapper = new JsonMapper();

        Object[] boundArgs;

        try
        {
            ImParameterBind<ImMessage> binder = binders.getBinder(message.getParameterBinderClass());
            Parameter[] parameters = handlerDesc.getClassMethod().getParameters();
            boundArgs = binder.binding(ctx, parameters, message);
        }
        catch (Exception e)
        {
            String msg = jsonMapper.writeValueAsString(
                    new ImResponse(message.getName(), ImResults.fail(e.getMessage())));

            ctx.writeAndFlush(new TextWebSocketFrame(msg));
            return;
        }

        Object returnValue;
        try
        {
            returnValue = handlerDesc.getClassMethod().invoke(handlerDesc.getInstance(), boundArgs);
        }
        catch (Throwable e)
        {
            log.error("handler error", e);
            returnValue = ImResults.fail();
        }

        if (returnValue == null)
        {
            return;
        }

        // ctx.channel().write 会从头部的handle开始
        if (returnValue instanceof ByteBuf)
        {
            ctx.writeAndFlush(new BinaryWebSocketFrame((ByteBuf) returnValue));
        }
        else
        {
            String json = jsonMapper.writeValueAsString(new ImResponse(message.getName(), returnValue));
            ctx.writeAndFlush(new TextWebSocketFrame(json));
        }
    }
}
