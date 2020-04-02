package com.example.aop.redis.demo.convert;

import com.example.aop.redis.demo.dto.UserDTO;
import com.example.aop.redis.demo.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserConvert implements DTOEntityConvert<User, UserDTO> {
    /**
     * 将entity转换成dto
     *
     * @param user
     * @return
     */
    @Override
    public UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    /**
     * 将dto转换成entity
     *
     * @param userDTO
     * @return
     */
    @Override
    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }
}

