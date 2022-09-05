package com.nowcoder;

import com.nowcoder.community.CommunityApplication;
import com.nowcoder.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;


/**
 * @author byfgg
 * @create 2022-09-01 21:08
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {

    @Autowired
    private MailClient mailClient;

    @Test
    public void sendMail() {
        mailClient.sendMail("1209952905@qq.com","我用java给你写一封信","我爱你");
    }
}
