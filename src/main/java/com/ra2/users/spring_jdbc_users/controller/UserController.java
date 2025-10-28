package com.ra2.users.spring_jdbc_users.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/user/post")
    public String addUser(@RequestBody User user) {
        int numReg = userRepository.save(user);
        return String.format("Has afegit %d registre", numReg);
    }
    

}
