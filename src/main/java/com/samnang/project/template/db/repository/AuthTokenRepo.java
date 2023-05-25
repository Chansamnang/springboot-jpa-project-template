package com.samnang.project.template.db.repository;

import com.samnang.project.template.db.entity.AuthToken;
import com.samnang.project.template.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenRepo.class);

    @Value("${tsca.redis-token-pre:AUTH_TOKEN_}")
    protected String redisPrefix = "AUTH_TOKEN_";

    @Autowired
    protected RedisUtil redisUtil;

    public AuthToken getByTokenId(String tokenId) {
        String redisKey = this.redisPrefix + tokenId;
        return this.redisUtil.getValue(redisKey, AuthToken.class);
    }

    public void insert(AuthToken authToken) {
        String redisKey = this.redisPrefix + authToken.getTokenId();
        this.redisUtil.setValue(redisKey, authToken);
    }

    public void clear(AuthToken authToken) {
        String redisKey = this.redisPrefix + authToken.getTokenId();
        this.redisUtil.del(redisKey);
    }

}
