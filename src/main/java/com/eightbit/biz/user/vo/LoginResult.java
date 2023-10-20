package com.eightbit.biz.user.vo;

import lombok.Data;

@Data
public class LoginResult {
    private String State;
    private UserVO user;
}
