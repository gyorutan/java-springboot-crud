package com.tpi.springboot.crud.demo.mapper;

import com.tpi.springboot.crud.demo.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserMapper {
    void insertUser(User user);

    User selectUserById(Long id);

    User selectUserByUsername(String username);

    List<User> selectAllUsers();

    void updateUser(User user);

    void deleteUser(Long id);
}
