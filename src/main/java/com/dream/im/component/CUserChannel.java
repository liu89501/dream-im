package com.dream.im.component;

import com.dream.container.anno.Component;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.HashMap;
import java.util.Map;

@Component(proxy = false)
public class CUserChannel {

    private final Map<String, Channel> channelMap = new HashMap<>();

    public void saveChannel(Channel channel, String userId)
    {
        channelMap.put(userId, channel);
    }

    public <T> void sendTalkTo(String userId, TextWebSocketFrame frame)
    {
        Channel channel = channelMap.get(userId);
        if (channel != null)
        {
            channel.writeAndFlush(frame);
        }
    }

    public void sendBinaryTo(String userId, BinaryWebSocketFrame frame)
    {
        Channel channel = channelMap.get(userId);
        if (channel != null)
        {
            channel.writeAndFlush(frame);
        }
    }
}
