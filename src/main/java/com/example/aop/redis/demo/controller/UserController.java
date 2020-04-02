package com.example.aop.redis.demo.controller;

import com.example.aop.redis.demo.dto.UserDTO;
import com.example.aop.redis.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/emails")
    public List<String> userEmails() {
        return userService.userEmails();
    }

    @GetMapping("/user/names-emails")
    public List<Map<String, String>> userNamesEmails() {
        return userService.userNamesEmails();
    }

    /**
     * 保存用户
     *
     * @param userDTO
     */
    @PostMapping("/user/save")
    public void saveUser(@Validated UserDTO userDTO) {
        userService.saveUser(userDTO);
    }
}
