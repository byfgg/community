package com.nowcoder.community.controller;

import com.google.code.kaptcha.impl.FishEyeGimpy;
import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.*;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.sun.org.apache.xml.internal.dtm.DTMDOMException;
import jdk.internal.org.objectweb.asm.Handle;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.context.Theme;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author byfgg
 * @create 2022-09-04 15:47
 */
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;
    @Autowired
    private FollowService followService;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private CommentService commentService;

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${qiniu.key.access}")
    private String accessKey;

    @Value("${qiniu.key.secret}")
    private String secretKey;

    @Value("${qiniu.bucket.header.name}")
    private String headerBucketName;

    @Value("${qiniu.bucket.header.url}")
    private String headerBucketUrl;

    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage(Model model) {
        //上传文件名称
        String fileName = CommunityUtil.generateUUID();
        //设置响应信息
        StringMap policy = new StringMap();
        policy.put("returnBody", CommunityUtil.getJSONString(0));
        //生成上传凭证
        Auth auth = Auth.create(accessKey, secretKey);
        String uploadToken = auth.uploadToken(headerBucketName, fileName, 3600, policy);

        model.addAttribute("uploadToken", uploadToken);
        model.addAttribute("fileName", fileName);

        return "/site/setting";
    }

    @PostMapping("/header/url")
    @ResponseBody
    public String updateHeaderUrl(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return CommunityUtil.getJSONString(1, "文件名不能为空");
        }
        String url = headerBucketUrl + "/" + fileName;
        userService.updateHeaderUrl(hostHolder.getUser().getId(), url);
        return CommunityUtil.getJSONString(0);
    }

    //废弃
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model) {
        //判断文件是否为空
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片");
            return "/site/setting";
        }
        //取出后缀，判断后缀是否符合要求
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确");
            return "/site/setting";
        }
        //生成随机文件名，将文件保存到本地
        fileName = CommunityUtil.generateUUID() + "." + suffix;
        File dest = new File(uploadPath + "/" + fileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常" + e);
        }

        //更新当前用户的头像的路径（web访问本地）
        //http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeaderUrl(user.getId(), headerUrl);
        return "redirect:/index";
    }

    //废弃
    @LoginRequired
    @GetMapping("/header/{fileName}")
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        //根据web路径，重新换写成本地路径
        fileName = uploadPath + "/" + fileName;
        //取出后缀，设置响应图片的类型
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        response.setContentType("image/" + suffix);
        //输入输出流
        try (FileInputStream fis = new FileInputStream(fileName); ServletOutputStream os = response.getOutputStream();) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败" + e.getMessage());
        }

    }

    @GetMapping("/profile/{userId}")
    public String getProfile(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user", user);
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        //关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        //粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        //是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }

    @GetMapping("/mypost/{userId}")
    public String getMyPost(@PathVariable("userId") int userId, Model model, Page page) {
        page.setRows(discussPostService.findDiscussPostsRows(userId));
        page.setPath("/user/mypost/" + userId);
        List<DiscussPost> list = discussPostService.findDiscussPosts(userId, page.getOffset(), page.getLimit(), 0);
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("post", post);
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);
                discussPosts.add(map);
            }
        }
        int postCount = discussPostService.findDiscussPostsRows(userId);
        model.addAttribute("postCount", postCount);
        model.addAttribute("posts", discussPosts);

        return "/site/my-post";
    }

    @GetMapping("/myreply/{userId}")
    public String getMyReply(@PathVariable("userId") int userId, Model model, Page page) {
        page.setRows(commentService.findCommentCountByUser(ENTITY_TYPE_POST, userId));
        page.setPath("/user/myreply/" + userId);
        List<Comment> list = commentService.findCommentsByUser(ENTITY_TYPE_POST, userId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> comments = new ArrayList<>();
        if (list != null) {
            for (Comment comment : list) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("comment", comment);
                DiscussPost post = discussPostService.findDiscussPostById(comment.getEntityId());
                map.put("post", post);
                comments.add(map);
            }
        }
        int rows = commentService.findCommentCountByUser(ENTITY_TYPE_POST, userId);
        model.addAttribute("replyCount", rows);
        model.addAttribute("comments", comments);
        return "/site/my-reply";
    }


}




















