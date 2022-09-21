package com.dream.im;

import com.dream.im.entity.User;
import lombok.Data;

@Data
public class ImAuthenticatedInfo
{
    private User user;

    public ImAuthenticatedInfo(User user)
    {
        this.user = user;
    }
}
