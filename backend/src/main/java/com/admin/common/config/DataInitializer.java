package com.admin.common.config;

import com.admin.modules.auth.entity.Permission;
import com.admin.modules.auth.entity.Role;
import com.admin.modules.auth.enums.UserStatus;
import com.admin.modules.auth.enums.UserType;
import com.admin.modules.auth.entity.User;
import com.admin.modules.auth.repository.PermissionRepository;
import com.admin.modules.auth.repository.RoleRepository;
import com.admin.modules.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            // 初始化权限
            initPermissions();
            
            // 初始化角色
            initRoles();
            
            // 初始化超级管理员
            initSuperAdmin();
        };
    }

    private void initPermissions() {
        if (permissionRepository.count() == 0) {
            // 用户管理权限
            permissionRepository.save(new Permission("用户查看", "user:read", "user", "read", "查看用户列表和详情"));
            permissionRepository.save(new Permission("用户创建", "user:create", "user", "create", "创建新用户"));
            permissionRepository.save(new Permission("用户更新", "user:update", "user", "update", "更新用户信息"));
            permissionRepository.save(new Permission("用户删除", "user:delete", "user", "delete", "删除用户"));

            // 角色管理权限
            permissionRepository.save(new Permission("角色查看", "role:read", "role", "read", "查看角色列表和详情"));
            permissionRepository.save(new Permission("角色创建", "role:create", "role", "create", "创建新角色"));
            permissionRepository.save(new Permission("角色更新", "role:update", "role", "update", "更新角色信息"));
            permissionRepository.save(new Permission("角色删除", "role:delete", "role", "delete", "删除角色"));

            // 商品管理权限
            permissionRepository.save(new Permission("商品查看", "product:read", "product", "read", "查看商品列表和详情"));
            permissionRepository.save(new Permission("商品创建", "product:create", "product", "create", "创建新商品"));
            permissionRepository.save(new Permission("商品更新", "product:update", "product", "update", "更新商品信息"));
            permissionRepository.save(new Permission("商品删除", "product:delete", "product", "delete", "删除商品"));

            // 订单管理权限
            permissionRepository.save(new Permission("订单查看", "order:read", "order", "read", "查看订单列表和详情"));
            permissionRepository.save(new Permission("订单创建", "order:create", "order", "create", "创建新订单"));
            permissionRepository.save(new Permission("订单更新", "order:update", "order", "update", "更新订单信息"));
            permissionRepository.save(new Permission("订单删除", "order:delete", "order", "delete", "删除订单"));

            System.out.println("Permissions initialized successfully!");
        }
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {
            // 获取所有权限
            Set<Permission> allPermissions = new HashSet<>(permissionRepository.findAll());

            // 超级管理员角色
            Role superAdminRole = new Role();
            superAdminRole.setName("超级管理员");
            superAdminRole.setCode("SUPER_ADMIN");
            superAdminRole.setDescription("系统超级管理员，拥有所有权限");
            superAdminRole.setSystem(true);
            superAdminRole.setPermissions(allPermissions);
            roleRepository.save(superAdminRole);

            // 管理员角色
            Role adminRole = new Role();
            adminRole.setName("管理员");
            adminRole.setCode("ADMIN");
            adminRole.setDescription("系统管理员，拥有大部分管理权限");
            adminRole.setSystem(true);
            
            // 管理员拥有除删除外的所有权限
            Set<Permission> adminPermissions = new HashSet<>();
            for (Permission permission : allPermissions) {
                if (!permission.getAction().equals("delete")) {
                    adminPermissions.add(permission);
                }
            }
            adminRole.setPermissions(adminPermissions);
            roleRepository.save(adminRole);

            // 普通用户角色
            Role userRole = new Role();
            userRole.setName("普通用户");
            userRole.setCode("USER");
            userRole.setDescription("普通用户，只有查看权限");
            userRole.setSystem(true);
            
            Set<Permission> userPermissions = new HashSet<>();
            for (Permission permission : allPermissions) {
                if (permission.getAction().equals("read")) {
                    userPermissions.add(permission);
                }
            }
            userRole.setPermissions(userPermissions);
            roleRepository.save(userRole);

            System.out.println("Roles initialized successfully!");
        }
    }

    private void initSuperAdmin() {
        if (userRepository.count() == 0) {
            User superAdmin = new User();
            superAdmin.setUsername("admin");
            superAdmin.setEmail("admin@admin.com");
            superAdmin.setPasswordHash(passwordEncoder.encode("admin123"));
            superAdmin.setStatus(UserStatus.ACTIVE);
            superAdmin.setUserType(UserType.SUPER_ADMIN);
            
            Role superAdminRole = roleRepository.findByCode("SUPER_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Super Admin role not found"));
            superAdmin.setRoles(new HashSet<>(Arrays.asList(superAdminRole)));
            
            userRepository.save(superAdmin);
            
            System.out.println("Super admin initialized successfully!");
            System.out.println("Username: admin");
            System.out.println("Password: admin123");
        }
    }
}