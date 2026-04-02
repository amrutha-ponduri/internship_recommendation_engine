package com.example.reccomendation_system.controller;

import com.example.reccomendation_system.dto.InternshipDTO;
import com.example.reccomendation_system.dto.UserRequirements;
import com.example.reccomendation_system.dto.UserRequirementsAndProjectExperienceDescription;
import com.example.reccomendation_system.service.InternshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/internships")
public class InternshipController {

    private final InternshipService internshipService;

    @Autowired
    public InternshipController(InternshipService internshipService) {
        this.internshipService = internshipService;
    }

    @PostMapping("")
    public ArrayList<InternshipDTO> getInternships(@RequestBody UserRequirements userRequirements) {
        return internshipService.getInternships();
    }

    @PostMapping("/filtered/{userId}")
    public ArrayList<InternshipDTO> getTopFiveInternships(@PathVariable("userId") int userId, @RequestBody UserRequirementsAndProjectExperienceDescription userRequirementsAndProjectExperienceDescription) throws InterruptedException {
        return internshipService.getTopFiveInternships(userId, userRequirementsAndProjectExperienceDescription);
    }

}
