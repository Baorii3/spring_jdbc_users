package com.ra2.users.spring_jdbc_users.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

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
            user.setImagePath(rs.getString("image_path") != null ? rs.getString("image_path") : null);
            user.setUltimAcces(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            user.setDataCreated(rs.getTimestamp("dataCreated").toLocalDateTime().truncatedTo(ChronoUnit.SECONDS));
            user.setDataUpdated(rs.getTimestamp("dataUpdated") != null ? rs.getTimestamp("dataUpdated").toLocalDateTime().truncatedTo(ChronoUnit.SECONDS) : null);
            return user;
        }
    }

    public List<User> findAll() {
        String sql = "Select * from User";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    public User findById(Long id) {
        String sql = "SELECT * FROM User WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), id);
        return users.isEmpty() ? null : users.get(0);
    }

    public int save(User user) {
        String sql = "Insert into User(name,description,email,password,image_path) values (?,?,?,?,?)";
        return jdbcTemplate.update(sql,user.getName(),user.getDescription(),user.getEmail(),user.getPassword(),user.getImagePath());
    }

    public int update(User user) {
        String sql = "UPDATE User SET name = ?, description = ?, email = ?, password = ?, image_path = ?, dataUpdated = ? WHERE id = ?";
        user.onUpdate();
        return jdbcTemplate.update(sql, user.getName(), user.getDescription(), user.getEmail(), user.getPassword(), user.getImagePath(), user.getDataUpdated(), user.getId());
    }

    public int updateImage(Long id, String imageFile) {
        String sql = "UPDATE User SET image_path = ?, dataUpdated = ? WHERE id = ?";
        LocalDateTime dataUpdated = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(dataUpdated); 
        return jdbcTemplate.update(sql, imageFile, timestamp, id);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM User WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
