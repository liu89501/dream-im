package com.dream.im.codec;

import com.dream.im.ImMessageBinary;
import com.dream.im.ImMessageText;
import com.dream.im.utils.ImBinaryBufUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class ImMessageDecoder extends MessageToMessageDecoder<WebSocketFrame> {

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame, List<Object> out) throws Exception {

        log.info("isFinalFragment: {}, {}", webSocketFrame.isFinalFragment(), this);

        ByteBuf byteBuf = webSocketFrame.content();

        if (webSocketFrame instanceof TextWebSocketFrame)
        {
            ObjectMapper objectMapper = new ObjectMapper();

            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);

            if (log.isInfoEnabled())
            {
                log.info("message: {}", new String(bytes, StandardCharsets.UTF_8));
            }

            ImMessageText imMessageText = objectMapper.readValue(bytes, ImMessageText.class);

            out.add(imMessageText);
        }
        else if (webSocketFrame instanceof BinaryWebSocketFrame)
        {
            log.info("file bytes: {}", byteBuf.readableBytes());

            // todo 需要增加一个数据是否传输完毕的标记

            /*if (log.isInfoEnabled())
            {
                ByteBuf duplicate = byteBuf.duplicate();
                byte[] bytes = new byte[duplicate.readableBytes()];
                duplicate.readBytes(bytes);
                log.info("bytes: {}", Arrays.toString(bytes));
            }*/

            ImMessageBinary imMessageBinary = new ImMessageBinary();
            String messageName = ImBinaryBufUtils.getMessageName(byteBuf);

            imMessageBinary.setName(messageName);
            imMessageBinary.setByteBuf(byteBuf.retain());
            out.add(imMessageBinary);
        }
    }
}
