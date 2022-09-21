package com.dream.im.component;

import com.dream.container.anno.Assign;
import com.dream.container.anno.Component;
import com.dream.im.ImAuthenticatedInfo;
import com.dream.im.ImResult;
import com.dream.im.ImResults;
import com.dream.im.constant.ImConst;
import com.dream.im.entity.User;
import com.dream.im.utils.SecretUtils;

import java.util.Optional;

@Component
public class CUserTool {

    @Assign
    private TestUsers testUsers;

    public User queryUser(String userId)
    {
        Optional<User> optional = testUsers.getUsers().stream()
                .filter(i -> i.getUserId().equals(userId))
                .findFirst();

        return optional.orElse(null);
    }


    public ImResult<User> check(String userId, String sign)
    {
        User user = queryUser(userId);
        if (user == null)
        {
            return ImResults.fail("用户不存在");
        }

        String encryptSign = SecretUtils.encrypt(user.getSecretKey(), userId);

        if (!encryptSign.equals(sign))
        {
            return ImResults.fail("签名错误");
        }
        return ImResults.success(user);
    }
}
