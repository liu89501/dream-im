package com.dream.im;

import com.dream.im.constant.ImConst;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ImParameterBindText implements ImParameterBind<ImMessageText> {

    @Override
    public Object[] binding(ChannelHandlerContext ctx, Parameter[] parameters, ImMessageText message) throws Exception
    {
        return bindParameters(ctx, parameters, message.getData());
    }

    public static Object[] bindParameters(ChannelHandlerContext ctx, Parameter[] parameters, String json) throws JsonProcessingException {
        ImAuthenticatedInfo authenticatedInfo = ctx.channel().attr(ImConst.AUTHORIZATION_KEY).get();

        Object[] args = new Object[parameters.length];

        JsonMapper jsonMapper = new JsonMapper();

        ConstraintViolation<Object> constraintViolation = null;

        for (int i = 0; i < parameters.length; i++)
        {
            Class<?> parameterType = parameters[i].getType();

            Object arg;

            if (parameterType == ImAuthenticatedInfo.class)
            {
                arg = authenticatedInfo;
            }
            else if (parameterType == Channel.class)
            {
                arg = ctx.channel();
            }
            else if (Map.class.isAssignableFrom(parameterType))
            {
                arg = jsonMapper.readValue(json, Map.class);
            }
            else
            {
                arg = jsonMapper.readValue(json, parameterType);
                Set<ConstraintViolation<Object>> validate = Validation.buildDefaultValidatorFactory().getValidator().validate(arg);

                if (validate != null && validate.size() > 0)
                {
                    Optional<ConstraintViolation<Object>> first = validate.stream().findFirst();
                    constraintViolation = first.get();
                    break;
                }
            }

            args[i] = arg;
        }

        if (constraintViolation != null)
        {
            throw new IllegalArgumentException(constraintViolation.getMessage());
        }

        return args;
    }
}
