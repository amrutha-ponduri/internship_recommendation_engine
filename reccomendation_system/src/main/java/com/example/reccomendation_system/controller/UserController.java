package com.example.reccomendation_system.controller;

import com.example.reccomendation_system.dto.UserDTO;
import com.example.reccomendation_system.dto.UserDropdownDTO;
import com.example.reccomendation_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public UserDTO getUserDetails(@PathVariable("userId") int userId) {
        return userService.getUserDetails(userId);
    }

    @GetMapping("/")
    public List<UserDropdownDTO> getUserList() {
        return userService.getUserList();
    }

}
