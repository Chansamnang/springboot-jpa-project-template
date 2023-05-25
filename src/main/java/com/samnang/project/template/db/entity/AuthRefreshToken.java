package com.samnang.project.template.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRefreshToken {

    @Id
    private Integer id;

    @Column(name = "token_id")
    private String tokenId;

    @Column(name = "reflush_token_id")
    private String reflushTokenId;

    private Boolean status;

    @Column(name = "expired_at")
    private Date expiredAt;

    @Column(name = "created_at")
    private Date createdAt;

}