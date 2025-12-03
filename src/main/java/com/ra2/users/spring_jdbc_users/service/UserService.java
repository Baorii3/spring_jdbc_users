package com.ra2.users.spring_jdbc_users.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra2.users.spring_jdbc_users.model.User;
import com.ra2.users.spring_jdbc_users.logging.customLogging;
import com.ra2.users.spring_jdbc_users.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    customLogging customLogging;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User getUserbyId(Long id) throws IOException {
        String className = this.getClass().getSimpleName();
        User user = userRepository.findById(id);
        customLogging.info(className, "getUserbyId", "Consultant l'user amb id: " + id);
        if (user == null) customLogging.error(className, "getUserbyId", "L'user amb id: " + id + " no existeix");
        return user;
    }

    public int postUser(User user) throws IOException {
        String className = this.getClass().getSimpleName();
        customLogging.info(className, "postUser", "Afegint un nou user: " + user.getName());
        int numReg = userRepository.save(user);
        if (numReg == 0) customLogging.error(className, "postUser", "Error en afegir l'user: " + user.getName());

        return numReg;
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
        String imagenPath = "upload/images";
        User user = userRepository.findById(id);
        if (user == null) {
            return null;
        }

        File dir = new File(imagenPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = imageFile.getOriginalFilename();
        Path filePath = Path.of(imagenPath, fileName);
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        String imagePath = "/images/" + fileName;
        userRepository.updateImage(id, imagePath);
        return imagePath;
    }

    public int uploadCsv(MultipartFile csv) throws IOException {
        int numReg = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(csv.getInputStream()))) {
            String row;
            int numRow = 0;

            while ((row = br.readLine()) != null) {
                numRow++;
                if (numRow == 1) continue;
                
                String[] rowSplit = row.split(";");
                User user = new User();
                if (rowSplit.length < 4) continue;

                user.setName(rowSplit[0]);
                user.setDescription(rowSplit[1]);
                user.setEmail(rowSplit[2]);
                user.setPassword(rowSplit[3]);
                
                if (rowSplit.length > 4) {
                    user.setImagePath(rowSplit[4]);
                }
                
                userRepository.save(user);
                numReg++;
            }
        }
        return numReg;
    }

    public int uploadJson(MultipartFile json) {
        int numreg = 0;
        String ruta = "json_processed/"+json.getOriginalFilename();
        try {
            byte[] contenido = json.getBytes();
            JsonNode arrel = mapper.readTree(contenido);
            JsonNode data = arrel.path("data");
            String control = data.path("control").asText();
            if (!control.toLowerCase().equals("ok")) {
                return 0;
            }
            
            int count = data.path("count").asInt();
            JsonNode users = data.path("users");

            if (count != users.size()) {
                return 0;
            }

            for (JsonNode userJson: users) {
                User user = new User();
                user.setName(userJson.path("name").asText());
                user.setDescription(userJson.path("description").asText());
                user.setEmail(userJson.path("email").asText());
                user.setPassword(userJson.path("password").asText());
                userRepository.save(user);
                numreg++;
            }
            Files.write(Paths.get(ruta), contenido);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return numreg;
    }
}
