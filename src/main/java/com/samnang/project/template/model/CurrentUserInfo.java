package com.samnang.project.template.model;

import com.samnang.project.template.utils.AesUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserInfo {
    private Long userId;
    private String username;
    private String deviceId;
    private Long roleId;
    private String jwtToken;
    private String tokenId;

    public CurrentUserInfo setUserId(String id) {
        try {
            this.userId = Long.parseLong(Objects.requireNonNull(AesUtil.Decrypt(id)));
        } catch (Exception ignored) {
        }
        return this;
    }

}