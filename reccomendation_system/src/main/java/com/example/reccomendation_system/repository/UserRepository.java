package com.example.reccomendation_system.repository;

import com.example.reccomendation_system.dto.UserDTO;
import com.example.reccomendation_system.dto.UserDropdownDTO;

import java.util.List;

public interface UserRepository {

    public UserDTO getUserDetails(int userId);

    List<UserDropdownDTO> getUserList();
}
