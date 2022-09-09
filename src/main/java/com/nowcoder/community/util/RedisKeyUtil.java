package com.nowcoder.community.util;

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
}
