package com.example.aop.redis.demo;

import com.example.aop.redis.demo.dto.UserDTO;
import com.example.aop.redis.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class ComExampleAopRedisDemoApplication {

    public UserService userService;

    @Autowired
    public ComExampleAopRedisDemoApplication(UserService userService) {
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ComExampleAopRedisDemoApplication.class, args);
    }

    @PostConstruct
    public void init() {
        log.info("开始初始化数据.....");
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Eric.He");
        userDTO.setAddress("7/F, Block 12, SZ Software Park Phase 2, Nanshan District, Shenzhen, Guangdong, P.R. China");
        userDTO.setEmail("heyong@accela.com");
        userDTO.setMobile("13688789899");
        userDTO.setCreatedDate(LocalDateTime.now());
        userDTO.setLastUpdateDate(LocalDateTime.now());

        if (userService.count() == 0) {
            log.info("正在插入数据.......");
            userService.saveUsers(Arrays.asList(userDTO));
        }

        log.info("初始化数据完成.....");
    }
}
