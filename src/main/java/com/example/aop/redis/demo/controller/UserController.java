package com.example.aop.redis.demo.controller;

import com.example.aop.redis.demo.dto.UserDTO;
import com.example.aop.redis.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Api(value = "/api/v1", description = "用户信息操作接口")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/find/{id}")
    @ApiOperation(value = "根据ID查找用户", httpMethod = "GET")
    public UserDTO findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @DeleteMapping("/user/delete/{id}")
    @ApiOperation(value = "根据ID删除用户", httpMethod = "DELETE")
    public void delUserById(@PathVariable Long id) {
        userService.delUserById(id);
    }

    @PutMapping("/user/update")
    @ApiOperation(value = "更新用户信息", httpMethod = "PUT")
    public void updateUser(@Validated UserDTO userDTO) {
        userService.updateUser(userDTO);
    }

    @GetMapping("/user/emails")
    @ApiOperation(value = "获取用户E-Mail列表", httpMethod = "GET")
    public List<String> userEmails() {
        return userService.userEmails();
    }

    @GetMapping("/user/names-emails")
    @ApiOperation(value = "获取用户名及对应的E-Mail列表", httpMethod = "GET")
    public List<Map<String, String>> userNamesEmails() {
        return userService.userNamesEmails();
    }

    @PostMapping("/user/save")
    @ApiOperation(value = "保存用户", httpMethod = "POST")
    public void saveUser(@Validated UserDTO userDTO) {
        userService.saveUser(userDTO);
    }
}
