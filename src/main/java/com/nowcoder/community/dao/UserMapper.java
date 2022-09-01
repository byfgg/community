package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author byfgg
 * @create 2022-08-31 15:00
 */
@Mapper
public interface UserMapper {
    //根据id查用户
    User selectById(int id);

    //根据name查用户
    User selectByName(String username);

    //根据email查用户
    User selectByEmail(String email);

    //插入user
    int insertUser(User user);

    //更新状态
    int updateStatus(int id, int status);

    //更新url
    int updateUrl(int id, String headerUrl);

    //更新密码
    int updatePassword(int id, String password);
}
