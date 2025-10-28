package com.ra2.users.spring_jdbc_users.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ra2.users.spring_jdbc_users.model.User;
import com.ra2.users.spring_jdbc_users.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    // Obtenir tots els usuaris
    @GetMapping("/user")
    public ResponseEntity<List<User>> getUser() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }
    }

    // Afegir un usuari  
    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        int numReg = userRepository.save(user);
        if (numReg > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(String.format("%d usuari afegit correctament.", numReg));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la creació de l'usuari.");
        }
    }
    

}
