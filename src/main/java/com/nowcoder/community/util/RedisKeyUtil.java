package com.nowcoder.community.util;

import javafx.scene.transform.Scale;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author byfgg
 * @create 2022-09-07 15:20
 */
public class RedisKeyUtil {

    public static final String SPLIT = ":";
    public static final String PREFIX_ENTITY_LIKE = "like:entity";

    public static final String PREFIX_USER_LIKE = "like:user";

    //粉丝
    public static final String PREFIX_FOLLOWER = "follower";
    //关注者
    public static final String PREFIX_FOLLOWEE = "followee";

    //验证码
    public static final String PREFIX_KAPTCHA = "kaptcha";

    //登录凭证
    public static final String PREFIX_TICKET = "ticket";

    //用户
    public static final String PREFIX_USER = "user";

    //unique visitor
    public static final String PREFIX_UV = "uv";

    //daily active user
    public static final String PREFIX_DAU = "dau";

    public static final String PREFIX_POST = "post";

    //某个实体的赞

    /**
     * 得到redis中的键值
     * 一共有三处点赞，帖子，帖子的评论，评论的回复
     * 每个post和comment都需要一个key来记录赞数
     *
     * @param entityType 实体类型 post comment reply
     * @param entityId   id
     * @return
     */
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }


    //KEY:  like:user:userId -> int
    public static String getUserLike(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    //KEY: follower:entityType:entityId ->zset(userId,now)
    //粉丝只可能是人，人、帖子、评论都能被关注
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }


    //KEY: followee:userId:entityType ->zset(entityId,now)
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }

    //单日UV
    public static String getUVKey(String date) {
        return PREFIX_UV + SPLIT + date;
    }

    //区间UV
    public static String getUVKey(String startDate, String endDate) {
        return PREFIX_UV + SPLIT + startDate + SPLIT + endDate;
    }

    //单日DAU
    public static String getDAUKey(String date) {
        return PREFIX_DAU + SPLIT + date;
    }

    //区间DAU
    public static String getDAUKey(String startDate, String endDate) {
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDate;
    }

    public static String getPostScoreKey() {
        return PREFIX_POST + SPLIT + "score";
    }

}



