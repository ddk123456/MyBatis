package cn.edu.guet.mapper;

import cn.edu.guet.bean.User;

import java.util.List;

public interface UserMapper {
    int saveUser(User user);
    List<User> getAllUser();
    User getUserByName(String name);
    // 根据name获取用户
    int deleteUserById(String id);
    int updateUser(User user);
    User getUserById(String id);
}
