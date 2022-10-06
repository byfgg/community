package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author byfgg
 * @create 2022-09-06 14:46
 */
@Mapper
public interface MessageMapper {

    //查询当前会话列表，正对每一个会话只返回最新的一条私信 conversation
    public List<Message> selectConversations(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    //查询会话数量
    public int selectConversationCount(int userId);

    //查询当前会话的私信
    public List<Message> selectLetters(@Param("conversationId") String conversationId, @Param("offset") int offset, @Param("limit") int limit);

    //查询当前会话的私信数量
    public int selectLetterCount(String conversationId);

    //查询未读私信的数量
    int selectLetterUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    int insertMessage(Message message);

    int updateStatus(@Param("ids") List<Integer> ids, @Param("status") int status);

    //TOPIC_COMMENT = "comment";
    //TOPIC_LIKE = "like";
    //TOPIC_FOLLOW = "follow";
    //查询某个主题下最新的通知
    Message selectLatestNotice(@Param("userId")int userId, @Param("topic")String topic);

    //查询某个主题下包含的通知数量
    int selectNoticeCount(@Param("userId")int userId, @Param("topic")String topic);

    //查询未读的通知的数量
    int selectUnreadNoticeCount(@Param("userId")int userId, @Param("topic")String topic);

    //查询某个主题所包含的通知列表
    List<Message> selectNotices(@Param("userId")int userId, @Param("topic")String topic, @Param("offset")int offset, @Param("limit")int limit);
}
