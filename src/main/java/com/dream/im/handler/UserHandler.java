package com.dream.im.handler;

import com.dream.container.anno.Assign;
import com.dream.im.ImAuthenticatedInfo;
import com.dream.im.ImResponse;
import com.dream.im.ImResult;
import com.dream.im.ImResults;
import com.dream.im.anno.HandleMethod;
import com.dream.im.anno.Handler;
import com.dream.im.component.CUserChannel;
import com.dream.im.constant.ImConst;
import com.dream.im.entity.User;
import com.dream.im.param.*;
import com.dream.im.utils.ImBinaryBufUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.nio.charset.StandardCharsets;

@Handler
public class UserHandler
{

    @Assign
    private CUserChannel userChannel;

    @HandleMethod("user.info")
    public ImResult<User> info(Channel channel)
    {
        ImAuthenticatedInfo authenticatedInfo = channel.attr(ImConst.AUTHORIZATION_KEY).get();
        return ImResults.success(authenticatedInfo.getUser());
    }

    /**
     * 发送聊天消息
     */
    @HandleMethod("user.sendTalkMessage")
    public ImResult<?> sendTalkMessage(Channel channel, InTalkMessage message) throws JsonProcessingException {
        PTalkMsg talkMsg = new PTalkMsg();
        talkMsg.setMessage(message.getMessage());
        talkMsg.setTimestamp(System.currentTimeMillis());

        String nickName = channel.attr(ImConst.AUTHORIZATION_KEY).get().getUser().getNickName();
        talkMsg.setSenderName(nickName);

        ObjectMapper objectMapper = new ObjectMapper();
        String textMsg = objectMapper.writeValueAsString(new ImResponse(ImConst.RECEIVE_TALK_MESSAGE, talkMsg));
        userChannel.sendTalkTo(message.getUserId(), new TextWebSocketFrame(textMsg));
        return ImResults.success();
    }

    /**
     * 发送聊天消息
     */
    @HandleMethod("user.sendFile")
    public void sendFile(Channel channel, ByteBuf byteBuf) throws JsonProcessingException {

        PBinaryPacket packet = ImBinaryBufUtils.readBasePacket(byteBuf);
        ObjectMapper objectMapper = new ObjectMapper();
        InTalkMessageFile talkMessage = objectMapper.readValue(packet.getParams(), InTalkMessageFile.class);

        ByteBuf buffer = channel.alloc().buffer();

        PTalkFileMsg talkMsg = new PTalkFileMsg();
        talkMsg.setMessage(talkMessage.getMessage());
        talkMsg.setTimestamp(System.currentTimeMillis());
        talkMsg.setFileName(talkMessage.getFileName());
        talkMsg.setFileSize(talkMessage.getFileSize());
        talkMsg.setFileType(talkMessage.getFileType());

        String nickName = channel.attr(ImConst.AUTHORIZATION_KEY).get().getUser().getNickName();
        talkMsg.setSenderName(nickName);

        byte[] nameBytes = ImConst.RECEIVE_FILE.getBytes(StandardCharsets.UTF_8);
        byte[] jsonParams = objectMapper.writeValueAsBytes(talkMsg);

        buffer.writeByte(byteBuf.getByte(0));
        buffer.writeShortLE(nameBytes.length);
        buffer.writeShortLE(jsonParams.length);
        buffer.writeBytes(nameBytes);
        buffer.writeBytes(jsonParams);
        buffer.writeBytes(byteBuf);

        userChannel.sendBinaryTo(talkMessage.getUserId(), new BinaryWebSocketFrame(buffer));
    }
}
