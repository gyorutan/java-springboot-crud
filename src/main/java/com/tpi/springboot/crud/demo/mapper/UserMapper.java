package com.tpi.springboot.crud.demo.mapper;

import com.tpi.springboot.crud.demo.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectUserByUsername(String username);
}
