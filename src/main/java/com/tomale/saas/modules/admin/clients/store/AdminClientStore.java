package com.tomale.saas.modules.admin.clients.store;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.tomale.saas.base.models.Client;
import com.tomale.saas.base.models.User;


public class AdminClientStore {

    private static final Logger log = LogManager.getLogger(AdminClientStore.class);

    private static final String SQL_CLIENTS_ALL = "{call clients.clients_all()}";
    private static final String SQL_CLIENT_ADD = "{? = call clients.client_add(?,?)}";
    private static final String SQL_CLIENT_USERS = "{call iam.client_users_all(?)}";
    private static final String SQL_CLIENT_ADD_USER = "{call iam.client_user_add(?,?)}";
    private static final String SQL_CLIENT_REMOVE_USER = "{call iam.client_user_remove(?,?)}";

    private final JdbcTemplate jdbc;

    public AdminClientStore(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Client> getAll() throws Exception {
        try {
            CallableStatement stmt = jdbc.getDataSource()
                .getConnection()
                .prepareCall(SQL_CLIENTS_ALL);
            stmt.execute();

            ResultSet rs = stmt.getResultSet();
            ArrayList<Client> clients = new ArrayList<Client>();
            while(rs.next()) {
                UUID id = UUID.fromString(rs.getString(1));
                boolean active = rs.getBoolean(2);
                String name = rs.getString(3);
                String address = rs.getString(4);

                clients.add(new Client(id, name, address));
            }
            return clients;
        } catch(SQLException e) {
            log.error(e);
            throw new Exception("An error occured while trying to retrieve clients");
        }
    }

    public UUID add(String name, String address) throws Exception {
        try {
            CallableStatement stmt = jdbc.getDataSource()
                .getConnection()
                .prepareCall(SQL_CLIENT_ADD);
            stmt.registerOutParameter(1, java.sql.Types.OTHER);
            stmt.setString(2, name);
            stmt.setString(3, address);
            stmt.execute();

            UUID clientId = (UUID) stmt.getObject(1);
            return clientId;
        } catch(Exception e) {
            log.error(e);
            throw new Exception("An error occured while trying to add client");
        }
    }

    public List<User> getAllUsers(UUID clientId) throws Exception {
        try {
            CallableStatement stmt = jdbc.getDataSource()
                .getConnection()
                .prepareCall(SQL_CLIENT_USERS);
            stmt.setObject(1, clientId);
            stmt.execute();
            ResultSet rs = stmt.getResultSet();

            ArrayList<User> users = new ArrayList<User>();
            while(rs.next()) {
                UUID id = UUID.fromString(rs.getString(1));
                boolean active = rs.getBoolean(2);
                String email = rs.getString(3);
                String name = rs.getString(4);

                users.add(new User(id, name, email, active));
            }
            return users;
        } catch(SQLException e) {
            log.error(e);
            throw new Exception("An error occured while trying to retrieve all users for client");
        }
    }

    public void addUsertoClient(UUID clientId, UUID userId) throws Exception {
        try {
            CallableStatement stmt = jdbc.getDataSource()
                .getConnection()
                .prepareCall(SQL_CLIENT_ADD_USER);
            stmt.setObject(1, clientId);
            stmt.setObject(2, userId);
            stmt.execute();
        } catch(SQLException e) {
            log.error(e);
            throw new Exception("An error occured while trying to add user to client");
        }


    }

    public void removeUserFromClient(UUID clientId, UUID userId) throws Exception {
        try {
            CallableStatement stmt = jdbc.getDataSource()
                .getConnection()
                .prepareCall(SQL_CLIENT_REMOVE_USER);
            stmt.setObject(1, clientId);
            stmt.setObject(2, userId);
            stmt.execute();
        } catch(SQLException e) {
            log.error(e);
            throw new Exception("An error occured while trying to add user to client");
        }


    }
}