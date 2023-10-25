package com.eightbit.entity.user;

import lombok.Data;

@Data
public class User {
    private String email;
    private String password;
    private String nickname;
    private String role;
    private String profileImgPath;
    private int point;
}
