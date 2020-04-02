package com.example.aop.redis.demo.service.impl;

import com.example.aop.redis.demo.annotation.ClearCacheKey;
import com.example.aop.redis.demo.annotation.QueryCache;
import com.example.aop.redis.demo.annotation.QueryCacheKey;
import com.example.aop.redis.demo.common.CacheNameSpace;
import com.example.aop.redis.demo.convert.UserConvert;
import com.example.aop.redis.demo.dto.UserDTO;
import com.example.aop.redis.demo.entity.QUser;
import com.example.aop.redis.demo.entity.User;
import com.example.aop.redis.demo.repository.UserRepository;
import com.example.aop.redis.demo.service.UserService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserConvert userConvert;
    private UserRepository userRepository;
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    public UserServiceImpl(UserConvert userConvert, UserRepository userRepository, JPAQueryFactory jpaQueryFactory) {
        this.userConvert = userConvert;
        this.userRepository = userRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    /**
     * 查询指定ID的用户
     *
     * @param id
     * @return
     */
    @Override
    @QueryCache(nameSpace = CacheNameSpace.USER)
    public UserDTO findUserById(@QueryCacheKey(keyName = "userDTO") Long id) {
        User user = userRepository.getOne(id);
        return userConvert.convertToDto(user);
    }

    /**
     * 删除指定ID的用户
     *
     * @param id
     */
    @Override
    public void delUserById(Long id) {
        userRepository.deleteById(id);
        log.info(String.format("----->用户id:{%s}的用户已经删除!<------", id));
    }

    /**
     * 更新用户
     *
     * @param userDTO
     */
    @Override
    @QueryCache(nameSpace = CacheNameSpace.USER)
    public void updateUser(@ClearCacheKey(keyName = "userDTO") UserDTO userDTO) {
        if (userDTO != null && userDTO.getId() != null) {
            Optional<User> optionalUser = userRepository.findById(userDTO.getId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setAddress(userDTO.getAddress());
                user.setEmail(userDTO.getEmail());
                user.setMobile(userDTO.getMobile());
                user.setName(userDTO.getName());
                user.setLastUpdateDate(LocalDateTime.now());

                userRepository.save(user);

                log.info(String.format("----->用户id:{%s}的用户已经更新!<------", userDTO.getId()));
            }
        }
    }

    /**
     * 获取用户所有E-mail地址
     *
     * @return
     */
    @Override
    public List<String> userEmails() {
        QUser user = QUser.user;
        return jpaQueryFactory.selectFrom(user)
                .select(user.email)
                .fetch();
    }

    /**
     * 获取用户名及E-Mail地址
     *
     * @return
     */
    @Override
    public List<Map<String, String>> userNamesEmails() {
        QUser user = QUser.user;
        return jpaQueryFactory.selectFrom(user)
                .select(user.email, user.name)
                .fetch()
                .stream()
                .map(tuple -> {
                    Map<String, String> map = new LinkedHashMap<>();
                    map.put("name", tuple.get(user.name));
                    map.put("email", tuple.get(user.email));
                    return map;
                }).collect(Collectors.toList());
    }

    /**
     * 保存用户
     *
     * @param userDTO
     */
    @Override
    public void saveUser(UserDTO userDTO) {
        User user = userConvert.convertToEntity(userDTO);
        userRepository.save(user);
        log.info("----->用户信息保存成功!<------");
    }

    /**
     * 批量保存用户
     *
     * @param userDTOS
     */
    @Override
    public void saveUsers(List<UserDTO> userDTOS) {
        List<User> users = new ArrayList<>();
        if (userDTOS != null) {
            userDTOS.forEach(o -> users.add(userConvert.convertToEntity(o)));
        }
        if (users.size() > 0) {
            userRepository.saveAll(users);
        }
        log.info("----->用户信息批量保存成功!<------");
    }

    /**
     * 获取表记录数
     *
     * @return
     */
    @Override
    public long count() {
        return userRepository.count();
    }
}
