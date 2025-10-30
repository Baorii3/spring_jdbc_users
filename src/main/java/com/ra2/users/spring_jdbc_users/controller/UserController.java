package com.ra2.users.spring_jdbc_users.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import com.ra2.users.spring_jdbc_users.model.User;
import com.ra2.users.spring_jdbc_users.repository.UserRepository;

/* 
200	OK	                    Todo correcto.
201	Created	                Se ha creado el recurso correctamente.
400	Bad Request	            El cliente envió datos incorrectos.
401	Unauthorized	        Falta autenticación.
403	Forbidden	            No tienes permiso.
404	Not Found	            No se encontró el recurso.
409	Conflict	            El recurso ya existe o hay un conflicto.
500	Internal Server Error	Error inesperado del servidor.
*/

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    // Obtenir tots els usuaris
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUser() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }
    }

    // Obtenir un usari per ID
    @GetMapping("/users/{user_id}")
    public ResponseEntity<User> getUserId(@PathVariable Long user_id) {
        User user = userRepository.findById(user_id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }

    // Afegir un usuari  
    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        int numReg = userRepository.save(user);
        if (numReg == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en afegir l'usuari.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(numReg + " usuari afegit correctament.");
    }

    // Update Complet
    @PutMapping("/users/{user_id}")
    public ResponseEntity<String> updateUser(@PathVariable Long user_id, @RequestBody User user) {
        user.setId(user_id);
        int numReg = userRepository.update(user);
        if (numReg == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en actualitzar l'usuari");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Usuari amb id " + user_id + " actualitzat.");
    }

    // Update parcial
    @PatchMapping("/users/{user_id}")
    public ResponseEntity<User> partialUpdateUser(@PathVariable Long user_id, @RequestParam String name) {
        User user = userRepository.findById(user_id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        user.setName(name);
        int numReg = userRepository.update(user);
        if (numReg == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    // delete
    @DeleteMapping("/users/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long user_id) {
        int numReg = userRepository.deleteById(user_id);
        if (numReg == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en eliminar l'usuari.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Usuari amb la id " + user_id + " eliminat.");
    }
}
