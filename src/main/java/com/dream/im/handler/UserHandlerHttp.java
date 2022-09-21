package com.dream.im.handler;

import com.dream.container.anno.Assign;
import com.dream.im.ImResult;
import com.dream.im.ImResults;
import com.dream.im.anno.Http;
import com.dream.im.anno.Post;
import com.dream.im.component.CUserTool;
import com.dream.im.entity.User;
import com.dream.im.param.InLogin;
import com.dream.im.param.PUserInfo;

@Http("/im")
public class UserHandlerHttp {

    @Assign
    private CUserTool userTool;

    @Post(value = "/login", authenticate = false)
    public ImResult<PUserInfo> login(InLogin param)
    {
        ImResult<User> result = userTool.check(param.getUserId(), param.getSign());
        if (result.isSuccess())
        {
            User user = result.getData();
            return ImResults.success(new PUserInfo(user));
        }

        return ImResults.fail(result.getMsg());
    }
}
