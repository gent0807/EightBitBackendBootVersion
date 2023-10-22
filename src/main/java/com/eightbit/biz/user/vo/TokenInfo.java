package com.eightbit.biz.user.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenInfo {
    private String loginState;
    private String role;
    private String nickName;
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Integer point;
}