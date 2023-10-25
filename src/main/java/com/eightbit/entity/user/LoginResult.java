package com.eightbit.entity.user;

import lombok.Data;

@Data
public class LoginResult {
    private String State;
    private User user;
}
