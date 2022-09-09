package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author byfgg
 * @create 2022-09-06 13:44
 */
@Data
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String conversationId;
    private String content;
    private int status;
    private Date createTime;
}
