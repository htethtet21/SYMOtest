package com.example.authjwtdemo.service;

import lombok.Getter;

@Getter
public class Login {
    private final Jwt accessToken;
    private final Jwt refreshToken;

    private static  final Long Access_Token_Validity=3L;
    private static  final Long Refresh_Token_Validity=1440L;

    private Login(Jwt accessToken, Jwt refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static Login of(Long userId,String accessSecret,String refreshSecret){
        return new Login(
                Jwt.of(userId,Access_Token_Validity,accessSecret),
                Jwt.of(userId,Refresh_Token_Validity,refreshSecret)
        );
    }

    public static Login of(Long userId, String accessSecret, Jwt refreshToken){
        return new Login(
                Jwt.of(userId,10L,accessSecret),
               refreshToken
        );
    }

}
