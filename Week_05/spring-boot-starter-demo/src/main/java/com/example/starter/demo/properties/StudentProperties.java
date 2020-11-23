package com.example.starter.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("demo.student")
public class StudentProperties {
    private int id = 0;
    private String name ="unkonow";
}
