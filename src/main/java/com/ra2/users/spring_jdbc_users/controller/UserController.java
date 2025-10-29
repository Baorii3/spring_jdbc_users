package com.ra2.users.spring_jdbc_users.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @GetMapping("/user")
    public ResponseEntity<List<User>> getUser() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }
    }

    // Obtenir un usari per ID
    @GetMapping("/user/{user_id}")
    public ResponseEntity<User> getUserId(@PathVariable Long user_id) {
        User user = userRepository.findById(user_id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }

    // Afegir un usuari  
    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        try {
            int numReg = userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(String.format("%d usuari afegit correctament.", numReg));
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("L'usuari amb aquest email ja existeix: " + user.getEmail());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falten dades obligatòries o valor invalids: nom, email o contrasenya.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en afegir l'usuari: " + e.getMessage());
        }
    }   
    

}
