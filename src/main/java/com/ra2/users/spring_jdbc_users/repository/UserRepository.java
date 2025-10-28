package com.ra2.users.spring_jdbc_users.repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ra2.users.spring_jdbc_users.model.User;

@Repository
public class UserRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public User mapRow(ResultSet rs, int rowNum) throws Exception {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setDescription(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setDataCreated(LocalDateTime.now());
        user.setDataUpdated(LocalDateTime.now());
        user.setUltimAcces((rs.getTimestamp("ultimAcces") == null) ? null: rs.getTimestamp("ultimAcces").toLocalDateTime() );
        return user;
    }

    public int save(User user) {
        String sql = "Insert into Users(name,description,email,password) values (?,?,?,?)";
        return jdbcTemplate.update(sql,user.getName(),user.getDescription(),user.getEmail(),user.getPassword());
    }
}
