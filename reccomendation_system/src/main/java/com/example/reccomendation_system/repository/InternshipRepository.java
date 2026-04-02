package com.example.reccomendation_system.repository;

import com.example.reccomendation_system.dto.InternshipDTO;
import com.example.reccomendation_system.dto.UserRequirementsAndProjectExperienceDescription;

import java.util.ArrayList;

public interface InternshipRepository {
    ArrayList<InternshipDTO> getInternships();

    ArrayList<InternshipDTO> getTopFiveInternships(int userId, UserRequirementsAndProjectExperienceDescription userRequirementsAndProjectExperienceDescription) throws InterruptedException;
}
