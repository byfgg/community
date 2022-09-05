package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author byfgg
 * @create 2022-09-03 16:18
 */
@Data
public class LoginTicket {
    private int id;
    private int userId;
    private String ticket;
    private int status;
    private Date expired;
}
