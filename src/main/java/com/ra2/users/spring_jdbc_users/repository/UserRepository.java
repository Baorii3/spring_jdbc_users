package com.ra2.users.spring_jdbc_users.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ra2.users.spring_jdbc_users.model.User;

@Repository
public class UserRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setDescription(rs.getString("description"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setUltimAcces((rs.getTimestamp("ultimAcces") == null) ? null: rs.getTimestamp("ultimAcces").toLocalDateTime() );
            user.setDataCreated(rs.getTimestamp("dataCreated").toLocalDateTime());
            user.setDataUpdated(rs.getTimestamp("dataUpdated").toLocalDateTime());
            return user;
        }
    }

    public List<User> findAll() {
        String sql = "Select * from User";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    public int save(User user) {
        String sql = "Insert into User(name,description,email,password) values (?,?,?,?)";
        return jdbcTemplate.update(sql,user.getName(),user.getDescription(),user.getEmail(),user.getPassword());
    }
}
