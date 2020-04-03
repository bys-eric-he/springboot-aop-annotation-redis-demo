package com.example.aop.redis.demo.service;

import com.example.aop.redis.demo.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserService {
    /**
     * 查询指定ID的用户
     *
     * @param id
     * @return
     */
    UserDTO findUserById(Long id);

    /**
     * 删除指定ID的用户
     *
     * @param id
     */
    void deleteUserById(Long id);

    /**
     * 更新用户
     *
     * @param userDTO
     */
    void updateUser(UserDTO userDTO);

    /**
     * 获取用户所有E-mail地址
     *
     * @return
     */
    List<String> userEmails();

    /**
     * 获取用户名及E-Mail地址
     *
     * @return
     */
    List<Map<String, String>> userNamesEmails();

    /**
     * 保存用户
     *
     * @param userDTO
     */
    void saveUser(UserDTO userDTO);

    /**
     * 批量保存用户
     *
     * @param userDTOS
     */
    void saveUsers(List<UserDTO> userDTOS);

    /**
     * 获取表记录数
     *
     * @return
     */
    long count();
}
