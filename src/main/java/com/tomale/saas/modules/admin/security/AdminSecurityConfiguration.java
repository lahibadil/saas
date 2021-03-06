package com.tomale.saas.modules.admin.security;

import com.tomale.saas.modules.admin.security.store.AdminPermissionStore;
import com.tomale.saas.modules.admin.security.store.AdminRoleStore;
import com.tomale.saas.modules.admin.security.store.AdminUsersStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;


@Configuration
public class AdminSecurityConfiguration {

    @Autowired
    private JdbcTemplate jdbc;

    @Bean
    public AdminPermissionStore adminPermissionStore() {
        return new AdminPermissionStore(jdbc);
    }

    @Bean
    public AdminRoleStore adminRoleStore() {
        return new AdminRoleStore(jdbc);
    }

    @Bean
    public AdminUsersStore adminUserStore() {
        return new AdminUsersStore(jdbc);
    }
}