package com.dream.im.component;

import com.dream.container.anno.Config;
import com.dream.im.entity.User;
import lombok.Data;

import java.util.List;

@Data
@Config(classpath = "test-users.json")
public class TestUsers {

    private List<User> users;
}
