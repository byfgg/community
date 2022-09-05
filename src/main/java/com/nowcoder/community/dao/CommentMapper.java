package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author byfgg
 * @create 2022-09-05 20:07
 */
@Mapper
public interface CommentMapper {
    List<Comment> selectCommentsByEntity(@Param("entityType") int entityType, @Param("entityId") int entityId, @Param("offset")int offset, @Param("limit")int limit);

    int selectCountByEntity(@Param("entityType")int entityType, @Param("entityId")int entityId);


}
