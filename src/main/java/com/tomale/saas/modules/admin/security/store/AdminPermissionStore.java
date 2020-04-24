package com.tomale.saas.modules.admin.security.store;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.tomale.saas.modules.admin.security.Permission;


public class AdminPermissionStore {

    private static final Logger log = LogManager.getLogger(AdminPermissionStore.class);

    private static final String SQL_PERMISSIONS_ALL = "{call iam.permissions_all()}";

    private JdbcTemplate jdbc;

    public AdminPermissionStore(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Permission> getPermissions() throws Exception {
        try {
            CallableStatement stmt = jdbc.getDataSource()
                .getConnection()
                .prepareCall(SQL_PERMISSIONS_ALL);
            stmt.execute();

            ResultSet rs = stmt.getResultSet();
            ArrayList<Permission> permissions = new ArrayList<Permission>();
            while(rs.next()) {
                UUID id = UUID.fromString(rs.getString(1));
                boolean active = rs.getBoolean(2);
                String name = rs.getString(4);

                permissions.add(new Permission(id, active, name));
            }
            return permissions;
        } catch(SQLException e) {
            log.error(e);
            throw new Exception("An error occured while trying to retrieve permissions");
        }
    }
}