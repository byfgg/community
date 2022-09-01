package com.nowcoder;

import com.nowcoder.community.CommunityApplication;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostService discussPostService;

    @Test
    void contextLoads() {
    }


    @Test
    void testUserMapper() {
        User user = userMapper.selectById(1);
        System.out.println(user);
    }


    @Test
    void testdiscussPostService() {
        List<DiscussPost> discussPosts = discussPostService.findDiscussPosts(0, 0, 10);
        for (DiscussPost post:discussPosts){
            System.out.println(post);
        }
    }
}

