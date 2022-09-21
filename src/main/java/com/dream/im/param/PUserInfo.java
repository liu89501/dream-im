package com.dream.im.param;

import com.dream.im.entity.User;
import lombok.Data;

@Data
public class PUserInfo
{
    private String username;

    public PUserInfo(User user)
    {
        this.username = user.getNickName();
    }
}
