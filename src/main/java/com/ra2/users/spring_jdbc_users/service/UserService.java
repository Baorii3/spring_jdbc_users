package com.ra2.users.spring_jdbc_users.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.ra2.users.spring_jdbc_users.model.User;

import com.ra2.users.spring_jdbc_users.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id);
    }

    public int save(User user) {
        return userRepository.save(user);
    }

    public int update(User user) {
        return userRepository.update(user);
    }

    public int partialUpdateUser(Long id, String name) {
        User user = userRepository.findById(id);
        if (user != null) {
            user.setName(name);
            return userRepository.update(user);
        }
        return 0;
    }

    public int deleteById(Long id) {
        return userRepository.deleteById(id);
    }

    
    public String updateImage(Long id, MultipartFile imageFile) throws Exception {
        String imagen_path = "src/main/resources/static/images/";
        User user = userRepository.findById(id);
        if (user == null) {
            return null;
        }

        File dir = new File(imagen_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = imageFile.getOriginalFilename();
        Path filePath = Path.of(imagen_path, fileName);
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        String imagePath = "/images/" + fileName;
        userRepository.updateImage(id, imagePath);
        return imagePath;

    }
}
