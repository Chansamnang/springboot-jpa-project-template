package com.samnang.project.template.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    @Builder.Default
    String tokenType = "Bearer ";

    String token;

    int expireTime;

    String reflushToken;

}
