package com.samnang.project.template.services;

import com.samnang.project.template.db.entity.AuthToken;
import com.samnang.project.template.db.repository.AuthTokenRepo;
import com.samnang.project.template.model.JwtToken;
import com.samnang.project.template.model.TokenResponse;
import com.samnang.project.template.utils.DateTimeUtil;
import com.samnang.project.template.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class AuthTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenService.class);

    @Value("${jwt.token-expire-seconds}")
    protected int tokenExpireSeconds;

    @Value("${jwt.reflush-token-expire-seconds}")
    protected int reflushTokenExpireSeconds;

//    @Autowired
////    protected AuthRefreshTokenService authRefreshTokenService;

    @Autowired
    protected JwtUtil jwtUtil;

    @Autowired
    protected AuthTokenRepo authTokenRepo;

    public TokenResponse generate(Long userId, Long roleId, String userName, String deviceId) {
        String token = UUID.randomUUID().toString();
        String reflushToken = UUID.randomUUID().toString();
        Date currDate = new Date();
        this.authTokenRepo.insert(AuthToken.builder()
                .createdAt(currDate)
                .deviceId(deviceId)
                .username(userName)
                .tokenId(token)
                .reflushTokenId(reflushToken)
                .status(Boolean.TRUE)
                .expiredAt(DateTimeUtil.addSeconds(currDate, this.tokenExpireSeconds))
                .build());

        String jwtToken = this.jwtUtil.generateToken(JwtToken.builder()
                .tokenId(token)
                .username(userName)
                .deviceId(deviceId)
                .build()
                .encodeID(String.valueOf(userId))
                .backMap(), this.tokenExpireSeconds);

        String reflushJwtToken = this.jwtUtil.generateToken(JwtToken.builder()
                .tokenId(reflushToken)
                .username(userName)
                .deviceId(deviceId)
                .build()
                .encodeID(String.valueOf(userId))
                .backMap(), this.reflushTokenExpireSeconds);

        return TokenResponse.builder()
                .expireTime(this.tokenExpireSeconds)
                .token(jwtToken)
                .reflushToken(reflushJwtToken)
                .build();
    }

}