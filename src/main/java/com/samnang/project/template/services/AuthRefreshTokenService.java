package com.samnang.project.template.services;

import com.samnang.project.template.db.entity.AuthRefreshToken;
import com.samnang.project.template.utils.JwtUtil;
import com.samnang.project.template.utils.UserAuthUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthRefreshTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRefreshTokenService.class);

    @Autowired
    protected AuthTokenService authTokenService;


    @Autowired
    protected JwtUtil jwtUtil;


    public AuthRefreshToken checkToken(UserAuthUtil userAuthUtil, String reflushTokenString) {
        if (!this.jwtUtil.validateToken(reflushTokenString)) {
            LOGGER.warn("token verification failed");
            return null;
        }

        Claims claims = this.jwtUtil.getClaimsFromToken(reflushTokenString);
        if (claims == null) {
            LOGGER.warn("token parsing failed");
            return null;
        }
        String claimsTokenId = claims.get("tokenId", String.class);
        String claimsUserType = claims.get("userType", String.class);
        String claimsUsername = claims.get("username", String.class);
        if (!StringUtils.hasLength(claimsTokenId) || claimsUserType == null || !StringUtils.hasLength(claimsUsername)) {
            LOGGER.warn("token data extraction failed");
            return null;
        }


        AuthRefreshToken authRefreshToken = AuthRefreshToken.builder().build();
        if (authRefreshToken == null) {
            LOGGER.warn("token status has expired: {}", claimsTokenId);
            return null;
        }

        return AuthRefreshToken.builder()
                .tokenId(authRefreshToken.getTokenId())
                .build();
    }

}
