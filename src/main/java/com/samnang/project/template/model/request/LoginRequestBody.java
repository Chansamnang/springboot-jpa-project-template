package com.samnang.project.template.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class LoginRequestBody {
    @NotBlank
    @ApiModelProperty(required = true)
    private String username;
    @NotBlank
    @ApiModelProperty(required = true)
    private String password;
}
