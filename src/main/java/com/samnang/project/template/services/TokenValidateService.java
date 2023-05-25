package com.samnang.project.template.services;


import com.samnang.project.template.common.exception.TokenErrorException;
import com.samnang.project.template.db.entity.AuthToken;
import com.samnang.project.template.db.entity.User;
import com.samnang.project.template.db.repository.AuthTokenRepo;
import com.samnang.project.template.db.repository.UserRepository;
import com.samnang.project.template.model.CurrentUserInfo;
import com.samnang.project.template.model.TokenResponse;
import com.samnang.project.template.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Service
public class TokenValidateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenValidateService.class);

    @Autowired
    protected AuthTokenRepo authTokenRepo;

    @Autowired
    protected JwtUtil jwtUtil;

    @Autowired
    protected UserRepository userRepository;

    public CurrentUserInfo checkToken(HttpServletRequest request) {
        if (!StringUtils.hasLength(request.getHeader("DeviceId"))) {
            LOGGER.warn("DeviceId is required");
            throw new TokenErrorException("DeviceId not find");
        }

        if (!StringUtils.hasLength(request.getHeader("Authorization"))) {
            LOGGER.warn("Authorization AND UserType is required");
            throw new TokenErrorException("Authorization not find");
        }

        String[] tokenStringArr = request.getHeader("Authorization").split(TokenResponse.builder().build().getTokenType());
        if (tokenStringArr.length == 1) {
            LOGGER.warn("Token verification failed 0 {}", Arrays.toString(tokenStringArr));
            return null;
        }

        String jwtToken = tokenStringArr[1];
        if (!this.jwtUtil.validateToken(jwtToken)) {
            LOGGER.warn("token verification failed 1");
            return null;
        }

        Claims claims = this.jwtUtil.getClaimsFromToken(jwtToken);
        if (claims == null) {
            LOGGER.warn("token parsing failed 3");
            return null;
        }

        String claimsTokenId = claims.get("tokenId", String.class);
        if (org.apache.commons.lang3.StringUtils.isBlank(claimsTokenId)) {
            LOGGER.warn("token data extraction failed");
            return null;
        }

        AuthToken authToken = this.authTokenRepo.getByTokenId(claimsTokenId);
        if (authToken == null) {
            LOGGER.warn("token status has expired");
            return null;
        }

        String userName = claims.get("username", String.class);
        User user = this.userRepository.findByUsername(userName);
        if (user == null) {
            LOGGER.warn("User not exist!");
            return null;
        } else if (user.getStatus() == 0) {
            LOGGER.warn("Suspended!");
            return null;
        }

        return CurrentUserInfo.builder()
                .tokenId(claimsTokenId)
                .jwtToken(jwtToken)
                .deviceId(claims.get("deviceId", String.class))
                .username(userName)
                .build().setUserId(claims.get("id", String.class));
    }

}