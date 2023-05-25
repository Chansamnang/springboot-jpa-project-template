package com.samnang.project.template.services;

import com.samnang.project.template.common.exception.ApiException;
import com.samnang.project.template.db.entity.AuthRefreshToken;
import com.samnang.project.template.db.entity.AuthToken;
import com.samnang.project.template.db.entity.User;
import com.samnang.project.template.db.repository.AuthTokenRepo;
import com.samnang.project.template.db.repository.UserRepository;
import com.samnang.project.template.model.CurrentUserInfo;
import com.samnang.project.template.model.TokenResponse;
import com.samnang.project.template.model.UserLoginRequest;
import com.samnang.project.template.utils.ApiResult;
import com.samnang.project.template.utils.RegexUtil;
import com.samnang.project.template.utils.UserAuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Value("${tsca.login-sign-device:false}")
    protected boolean loginSignDevice = false;

    @Autowired
    protected AuthTokenRepo authTokenRepo;

    @Autowired
    protected AuthRefreshTokenService authRefreshTokenService;

    @Autowired
    protected UserAuthUtil userAuthUtil;

    @Autowired
    protected AuthTokenService authTokenService;

    @Autowired
    protected UserLoginLogService userLoginLogService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ApiResult login(UserLoginRequest request) {
        if (!RegexUtil.checkUsername(request.getUsername())) {
            ApiResult.error("Username format error");
        }
        ApiResult result = this.userLoginLogService.checkErrorNums(request);
        if (result.isFail()) {
            return result;
        }

        ApiResult<User> resultUser = getUserByUsername(request.getUsername());
        if (resultUser.isFail()) {
            throw new ApiException(resultUser.getMessage());
        }
        User user = resultUser.getData();
        if (!this.passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ApiResult.error("Username or password is incorrect");
        }
        return ApiResult.success(execLogin(user.getId(), user.getRoleId(), user.getUsername(), request.getDeviceId()));
    }

    public TokenResponse execLogin(Long userId, Long roleId, String userName, String deviceId) {
        return this.authTokenService.generate(userId, roleId, userName, deviceId);
    }

    public ApiResult loginOut(UserAuthUtil userTokenRequest) {
        this.authTokenRepo.clear(AuthToken.builder()
                .tokenId(userTokenRequest.getTokenUser().getTokenId())
                .deviceId(userTokenRequest.getDeviceId())
                .build());
        return ApiResult.success();
    }

    public ApiResult reflushToken(UserAuthUtil userTokenRequest, String reflushToken) {
        AuthRefreshToken authReflushToken = this.authRefreshTokenService.checkToken(userTokenRequest, reflushToken);
        if (authReflushToken == null) {
            return ApiResult.error("Token expired", 401);
        }

        CurrentUserInfo jwtToken = this.userAuthUtil.getTokenUser();
        if (!jwtToken.getTokenId().equals(authReflushToken.getTokenId())) {
            return ApiResult.error("refresh token mismatch, jwtToken-token_id:{}  authReflushToken-token_id: {}", 401);
        }

        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .deviceId(userTokenRequest.getDeviceId())
                .username(jwtToken.getUsername())
                .build();

        this.authTokenRepo.clear(AuthToken.builder()
                .tokenId(userTokenRequest.getTokenUser().getTokenId())
                .tokenId(authReflushToken.getTokenId())
                .build());

        TokenResponse tokenResponse = this.authTokenService.generate(jwtToken.getUserId(), jwtToken.getRoleId(), jwtToken.getUsername(), userTokenRequest.getDeviceId());
        return ApiResult.success(tokenResponse);
    }

    public ApiResult changePassword(String oldPassword, String password) {
        CurrentUserInfo tokenUser = this.userAuthUtil.getTokenUser();
        userAuthUtil.setTokenUser(tokenUser);
        loginOut(userAuthUtil);

        return ApiResult.success(execLogin(tokenUser.getUserId(), tokenUser.getRoleId(), tokenUser.getUsername(), tokenUser.getDeviceId()));
    }

    public ApiResult resetPassword(String username, String userType, String password) {
        return ApiResult.todo("Not yet implement: resetPassword");
    }

    public ApiResult<User> getUserByUsername(String username) throws SecurityException {
        User currUser = userRepository.findByUsername(username);
        if (currUser == null) {
            LOGGER.error("Login user information does not exist: " + username);
            return ApiResult.error("Login user information does not exist: " + username);
        } else if (currUser.getStatus() == 0) {
            LOGGER.warn("Suspended!");
            return ApiResult.error("Suspended! " + username);
        }
        return ApiResult.success(currUser);
    }

    public User getUserById(Long userId) throws SecurityException {
        return userRepository.findById(userId).orElse(null);
    }


    public ApiResult<User> createUser(String username, String password) {
        User savedUser = userRepository.save(User.builder()
                .status(1)
                .password(this.passwordEncoder.encode(password))
                .createdAt(new Date())
                .username(username).build());
        return ApiResult.success(savedUser);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
