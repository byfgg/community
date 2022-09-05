package com.nowcoder;

import com.nowcoder.community.CommunityApplication;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests {

    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private SensitiveFilter sensitiveFilter;

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
        for (DiscussPost post : discussPosts) {
            System.out.println(post);
        }
    }

    @Test
    void testStatus() {
        userMapper.updateStatus(152, 1);
    }

    @Test
    void testLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(100);
        loginTicket.setTicket("aa");
        loginTicket.setStatus(1);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    void testLoginTicket2() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("aa");
        System.out.println(loginTicket);
        loginTicketMapper.updateStatus("aa", 0);

    }

    @Test
    void testFilter() {
        String text = "你不能去嫖娼啊吗，赌博啊，吸毒啊，开票也不行";
        String filter = sensitiveFilter.filter(text);
        System.out.println(filter);
    }

}

