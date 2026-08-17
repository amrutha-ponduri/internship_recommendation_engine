package com.example.reccomendation_system.repository;

import com.example.reccomendation_system.dto.UserDropdownDTO;
import com.example.reccomendation_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Integer> {
    @Query("""
            SELECT new com.example.reccomendation_system.dto.UserDropdownDTO(u.id, u.name)
            FROM User u""")
    public List<UserDropdownDTO> findUserDropdownItems();
}
