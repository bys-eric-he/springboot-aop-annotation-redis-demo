package com.example.aop.redis.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据传输模型
 */
@Getter
@Setter
@ToString
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -2424399419173607903L;
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdateDate;
    @NotNull(message = "Name should not be null or empty!")
    @NotEmpty(message = "Name should not be empty!")
    private String name;
    private String email;
    @Size(min = 11, message = "Mobile should have at least 11 characters")
    private String mobile;
    private String address;
}
