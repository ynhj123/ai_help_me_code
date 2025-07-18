package com.admin.modules.auth.service;

import com.admin.common.exception.BadRequestException;
import com.admin.common.security.config.BruteForceProtectionService;
import com.admin.common.security.jwt.JwtUtils;
import com.admin.modules.auth.dto.LoginRequest;
import com.admin.modules.auth.dto.LoginResponse;
import com.admin.modules.auth.dto.SignupRequest;
import com.admin.modules.auth.dto.SignupResponse;
import com.admin.modules.auth.entity.Role;
import com.admin.modules.auth.entity.User;
import com.admin.modules.auth.enums.RoleName;
import com.admin.modules.auth.repository.RoleRepository;
import com.admin.modules.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private BruteForceProtectionService bruteForceProtectionService;

    public LoginResponse authenticateUser(LoginRequest loginRequest, String ip) {
        String username = loginRequest.getUsername();
        
        // 检查是否被锁定
        if (bruteForceProtectionService.isLocked(username, ip)) {
            throw new BadRequestException("账户已被锁定，请30分钟后再试");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken((UserDetails) authentication.getPrincipal());

            // 登录成功，清除失败记录
            bruteForceProtectionService.loginSucceeded(username, ip);

            return new LoginResponse(jwt, "Bearer", jwtUtils.getJwtExpirationMs());
        } catch (Exception e) {
            // 登录失败，记录失败次数
            bruteForceProtectionService.loginFailed(username, ip);
            int remainingAttempts = bruteForceProtectionService.getRemainingAttempts(username, ip);
            
            throw new BadRequestException("用户名或密码错误，剩余尝试次数：" + remainingAttempts);
        }
    }

    public SignupResponse registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new BadRequestException("用户名已存在！");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("邮箱已被注册！");
        }

        // 创建新用户
        User user = new User(signUpRequest.getUsername(), 
                           signUpRequest.getEmail(),
                           encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByCode("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("用户角色未找到"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByCode("ROLE_ADMIN")
                                .orElseThrow(() -> new RuntimeException("管理员角色未找到"));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByCode("ROLE_MODERATOR")
                                .orElseThrow(() -> new RuntimeException("版主角色未找到"));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByCode("ROLE_USER")
                                .orElseThrow(() -> new RuntimeException("用户角色未找到"));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        SignupResponse response = new SignupResponse();
        response.setMessage("用户注册成功！");
        response.setUserId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        return response;
    }
}