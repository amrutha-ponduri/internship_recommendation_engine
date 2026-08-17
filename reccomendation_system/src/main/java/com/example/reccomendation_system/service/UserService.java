package com.example.reccomendation_system.service;

import com.example.reccomendation_system.dto.UserDTO;
import com.example.reccomendation_system.dto.UserDropdownDTO;
import com.example.reccomendation_system.mapper.Mapper;
import com.example.reccomendation_system.model.User;
import com.example.reccomendation_system.repository.UserJpaRepository;
import com.example.reccomendation_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final Mapper mapper;

    @Autowired
    public UserService(UserJpaRepository userJpaRepository, Mapper mapper) {
        this.userJpaRepository = userJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public UserDTO getUserDetails(int userId) {
        try {
            User user = userJpaRepository.findById(userId).get();
            return mapper.toDTO(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<UserDropdownDTO> getUserList() {
        return new ArrayList<>(userJpaRepository.findUserDropdownItems());
    }

}
