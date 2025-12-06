package com.ra2.users.spring_jdbc_users.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ra2.users.spring_jdbc_users.model.User;
import com.ra2.users.spring_jdbc_users.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    // Obtenir tots els usuaris
    @Tag(name = "Get users")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUser() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }
    }

    // Obtenir un usari per ID
    @Tag(name = "Get users")
    @GetMapping("/users/{user_id}")
    public ResponseEntity<User> getUserId(@PathVariable Long user_id) throws IOException {
        User user = userService.getUserbyId(user_id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }

    // Upload massiu amb csv
    @Tag(name = "Post users")
    @PostMapping("/users/upload-csv")
    public ResponseEntity<String> addAllCsv(@RequestParam MultipartFile csvFile) throws IOException {
        int numReg = userService.uploadCsv(csvFile);
        if (numReg == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al afegir usuaris");
        } 
        return ResponseEntity.status(HttpStatus.CREATED).body(numReg + " usuaris afegit correctament");
    }

    @Tag(name = "Post users")
    @PostMapping("/users/upload-json")
    public ResponseEntity<String> addAlljson(@RequestParam MultipartFile jsonFile) throws IOException {
        int numreg = userService.uploadJson(jsonFile);
        if (numreg == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al afegit usuaris");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(numreg + " usuaris afegit correctament");
    }

    // Afegir un usuari 
    @Tag(name = "Post users") 
    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody User user) throws IOException {
        int numReg = userService.createUser(user);
        if (numReg == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en afegir l'usuari.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(numReg + " usuari afegit correctament.");
    }

    // Update Complet
    @Tag(name = "Put users")
    @PutMapping("/users/{user_id}")
    public ResponseEntity<String> updateUser(@PathVariable Long user_id, @RequestBody User user) throws IOException {
        user.setId(user_id);
        int numReg = userService.updateAllUser(user);
        if (numReg == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en actualitzar l'usuari");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Usuari amb id " + user_id + " actualitzat.");
    }


    // Update parcial
    @Tag(name = "Patch users")
    @PatchMapping("/users/{user_id}/name")
    public ResponseEntity<String> partialUpdateUser(@PathVariable Long user_id, @RequestParam String name) throws IOException {
        int numReg = userService.partialUpdateUser(user_id, name);
        if (numReg == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Usuari amb id " + user_id + " actualitzat.");
    }

    // Updaate parcial image
    @Tag(name = "Patch users")
    @PatchMapping("/users/{user_id}/image")
    public ResponseEntity<String> partialUpdateUserImage(@PathVariable Long user_id, @RequestParam MultipartFile imageFile) throws Exception {
        String imagePath = userService.updateImage(user_id, imageFile);
        if (imagePath == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Usuari amb id " + user_id + " actualitzat.");
    }

    // delete
    @Tag(name = "Delete users")
    @DeleteMapping("/users/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long user_id) throws IOException {
        int numReg = userService.deleteById(user_id);
        if (numReg == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en eliminar l'usuari.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Usuari amb la id " + user_id + " eliminat.");
    }
}
