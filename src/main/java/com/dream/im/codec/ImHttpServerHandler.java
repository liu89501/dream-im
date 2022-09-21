package com.dream.im.codec;

import com.dream.container.anno.Assign;
import com.dream.container.anno.Component;
import com.dream.im.*;
import com.dream.im.component.CUserChannel;
import com.dream.im.component.CUserTool;
import com.dream.im.constant.ImConst;
import com.dream.im.entity.User;
import com.dream.im.utils.ImByteBufUtils;
import com.dream.im.utils.ParamUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Parameter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.*;

@Slf4j
@Component(proxy = false)
public class ImHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Assign
    private ImHandlerContainerHttp httpHandlers;

    @Assign
    private CUserTool ut;

    @Assign
    private CUserChannel userChannel;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception
    {
        if (!request.decoderResult().isSuccess())
        {
            sendHttpResponse(ctx, request, BAD_REQUEST, ctx.alloc().buffer(0));
            return;
        }

        Channel channel = ctx.channel();

        if (request.uri().startsWith(ImConst.WEBSOCKET_PATH))
        {
            log.info("upgrade websocket [{}]", request.uri());

            Map<String, String> params = ParamUtils.parseParams(request.uri());

            String userId = params.get(ImConst.PK_USER_ID);
            String sign = URLDecoder.decode(params.get(ImConst.PK_SIGN), "UTF-8");

            if (StringUtils.isBlank(userId) || StringUtils.isBlank(sign))
            {
                responseUnauthorized(ctx, request);
                return;
            }

            ImResult<User> result = ut.check(userId, sign);
            if (!result.isSuccess())
            {
                responseUnauthorized(ctx, request);
                return;
            }

            ImAuthenticatedInfo info = new ImAuthenticatedInfo(result.getData());
            channel.attr(ImConst.AUTHORIZATION_KEY).setIfAbsent(info);
            userChannel.saveChannel(channel, info.getUser().getUserId());

            ctx.fireChannelRead(request.retain());
            return;
        }

        if (log.isInfoEnabled())
        {
            log.info("request path[{}]", request.uri());
        }

        ImHandlerDescHttp handler = httpHandlers.getHandler(request.uri());
        if (handler == null)
        {
            log.info("请求了不存在的资源: {}", request.uri());
            sendHttpResponse(ctx, request, NOT_FOUND, ImResults.fail("资源不存在"));
            return;
        }

        if (handler.isAuthenticate())
        {
            ImAuthenticatedInfo authenticatedInfo = channel.attr(ImConst.AUTHORIZATION_KEY).get();
            if (authenticatedInfo == null)
            {
                log.info("客户端未授权");
                sendHttpResponse(ctx, request, UNAUTHORIZED, ImResults.fail("客户端未授权"));
                return;
            }
        }

        Parameter[] parameters = handler.getClassMethod().getParameters();

        ByteBuf content = request.content();
        byte[] bytes = new byte[content.readableBytes()];
        content.readBytes(bytes);

        Object[] objects = ImParameterBindText.bindParameters(ctx, parameters, new String(bytes, StandardCharsets.UTF_8));

        Object returnValue;

        try
        {
            returnValue = handler.getClassMethod().invoke(handler.getInstance(), objects);
        }
        catch (Throwable e)
        {
            returnValue = ImResults.fail(e.getMessage());
        }

        ByteBuf byteBuf = ImByteBufUtils.fromObject(returnValue, ctx.alloc().buffer());
        FullHttpResponse res = new DefaultFullHttpResponse(request.protocolVersion(), OK, byteBuf);
        HttpUtil.setContentLength(res, byteBuf.readableBytes());

        sendHttpResponse(ctx, request, res);
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, HttpResponseStatus status, Object content) throws JsonProcessingException
    {
        ByteBuf buf = ImByteBufUtils.fromObject(content, ctx.alloc().buffer());
        sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), status, buf));
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res)
    {
        res.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        HttpResponseStatus responseStatus = res.status();
        boolean keepAlive = HttpUtil.isKeepAlive(req) && responseStatus.code() == OK.code();
        HttpUtil.setKeepAlive(res, keepAlive);
        ChannelFuture future = ctx.write(res);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static void responseUnauthorized(ChannelHandlerContext ctx, FullHttpRequest req)
    {
        sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), UNAUTHORIZED, ctx.alloc().buffer(0)));
    }
}
