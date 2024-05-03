package com.flowcanvas.auth.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Users { 

    private int userId;
    private String email;
    private String password;
    private String nickName;
    private LocalDateTime createAt;
}