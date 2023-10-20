package com.eightbit.biz.user.vo;

import lombok.Data;

@Data
public class UserVO {
    private String email;
    private String password;
    private String nickname;
    private String role;
    private String profileImgPath;
    private int point;
}
