package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author byfgg
 * @create 2022-08-31 15:32
 * 帖子 实体类
 */
@Data
public class DiscussPost {
    private int id;
    private int userId;
    private String title;
    private String content;
    private int type;
    private int status;
    private Date createTime;
    private int commentCount;

}
