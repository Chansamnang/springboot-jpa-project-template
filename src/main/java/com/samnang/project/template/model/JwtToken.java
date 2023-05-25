package com.samnang.project.template.model;

import com.samnang.project.template.utils.AesUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {
    private static final Logger log = LoggerFactory.getLogger(JwtToken.class);
    private String id;
    private String username;
    private String deviceId;
    private String tokenId;
    private String jwtToken;
    private String toleId;

    public Map<String, Object> backMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id);
        map.put("username", this.username);
        map.put("deviceId", this.deviceId);
        map.put("tokenId", this.tokenId);
        map.put("toleId", this.toleId);
        map.put("jwtToken", this.jwtToken);
        return map;
    }

    public String decodeID() {
        try {
            return AesUtil.Decrypt(this.id);
        } catch (Exception e) {
            return "";
        }
    }

    public Long getUserId() {
        try {
            return Long.parseLong(decodeID());
        } catch (Exception e) {
            return 0L;
        }
    }

    public JwtToken encodeID(String id) {
        if (StringUtils.isEmpty(id)) {
            return this;
        }
        try {
            this.id = AesUtil.Encrypt(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}
