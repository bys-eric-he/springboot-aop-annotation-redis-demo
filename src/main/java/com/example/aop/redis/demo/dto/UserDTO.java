package com.example.aop.redis.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

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
    /**
     * #@JsonFormat将LocalDateTime以指定格式化后返回给前端
     * #@DateTimeFormat对前端传入的日期进行格式化
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
    /**
     * #@JsonFormat将LocalDateTime以指定格式化后返回给前端
     * #@DateTimeFormat对前端传入的日期进行格式化
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateDate;
    @NotNull(message = "Name should not be null or empty!")
    @NotEmpty(message = "Name should not be empty!")
    private String name;
    private String email;
    @Size(min = 11, message = "Mobile should have at least 11 characters")
    private String mobile;
    private String address;
}
