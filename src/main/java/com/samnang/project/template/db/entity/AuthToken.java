package com.samnang.project.template.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {
    private Integer id;
    private String username;
    private String reflushTokenId;
    private String tokenId;
    private String deviceId;
    private Boolean status;
    private Date createdAt;
    private Date expiredAt;

}