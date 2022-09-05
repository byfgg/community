package com.nowcoder.community.util;

/**
 * @author byfgg
 * @create 2022-09-03 13:45
 */
public interface CommunityConstant {

    /*
    激活成功
     */
    int ACTIVATION_SUCCESS = 0;
    /*
    重复激活
    */
    int ACTIVATION_REPEAT = 1;
    /*
   激活失败
    */
    int ACTIVATION_FAILURE = 2;


    //默认登录凭证的超时时间
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    //记住我状态下的超时时间
    int REMEMBER_EXPIRED_SECONDS = 3600 * 12 * 100;

    //回复贴子
    int ENTITY_TYPE_POST=1;

    //回复评论
    int ENTITY_TYPE_REPLY=2;

}
