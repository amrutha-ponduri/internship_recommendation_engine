package com.example.reccomendation_system.mapper;
import com.example.reccomendation_system.dto.InternshipDTO;
import com.example.reccomendation_system.dto.InternshipDetailsDTO;
import com.example.reccomendation_system.dto.UserDTO;
import com.example.reccomendation_system.dto.ml_model_dtos.InternshipInfoDTO;
import com.example.reccomendation_system.dto.ml_model_dtos.UserInfoDTO;
import com.example.reccomendation_system.model.*;
import com.example.reccomendation_system.util.QualificationConstants;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;

@Component
public class Mapper {
    public UserDTO toDTO(User user) {
        String location = "";
        if (user.getDistrict() != null) {
            location += user.getDistrict();
        }
        if (user.getState() != null) {
            location += user.getState();
        }
        return new UserDTO(user.getName(), location, user.getGender());
    }

    public InternshipDetailsDTO toInternshipDetailsDTO(Internship internship, InternshipRequirements internshipRequirements) {

        String address = getAddress(internship);

        ArrayList<Skill> requiredSkills = new ArrayList<>();
        ArrayList<InternshipSkill> internshipSkills = new ArrayList<>(internshipRequirements.getSkills());
        for (InternshipSkill internshipSkill : internshipSkills) {
            requiredSkills.add(internshipSkill.getSkill());
        }
        HashMap<Integer, String> rankQualifiction = QualificationConstants.RANK_QUALIFICATIONS;
        String minimumQualification = rankQualifiction.get(internshipRequirements.getMinimumQualificationRank());
        return new InternshipDetailsDTO(internship.getTitle(), internship.getSector(), internship.getField(), internship.getTotalCount(), internship.getAppliedCount(), internship.getDescription(), address, minimumQualification, internshipRequirements.getStream(), internshipRequirements.getSpecialization(), requiredSkills, internshipRequirements.getDescription(), internship.getBenefits());

    }

    public InternshipDTO toInternshipDTO(Internship internship) {
        return new InternshipDTO(internship.getId(), internship.getTitle(), internship.getCompany(), internship.getDuration(), internship.getPostingTime(), internship.getSector(), internship.getDescription(), internship.getAppliedCount(), internship.getTotalCount(), internship.getBenefits(), internship.getState(), internship.getDistrict());
    }

    private static String getAddress(Internship internship) {
        String address = "";
        if (internship.getDistrict() != null) {
            address += internship.getDistrict() + ", ";
        }
        if (internship.getState() != null) {
            address += internship.getState();
        }
        return address;
    }

    public UserInfoDTO toUserInfoDTO(User user) {
        ArrayList<String> skillNames = new ArrayList<>();
        ArrayList<UserSkill> skills = new ArrayList<>(user.getSkills());
        for (UserSkill userSkill : skills) {
            skillNames.add(userSkill.getSkill().getSkillName());
        }
        return new UserInfoDTO(user.getAge(), user.getGender(), user.getExperience(), user.getStream(), user.getSpecialization(), user.getHighestQualificationRank(), user.getDistrict(), user.getState(), skillNames);
    }

    public InternshipInfoDTO toInternshipInfoDTO(Internship internship, InternshipRequirements internshipRequirements) {

        ArrayList<String> skillNames = new ArrayList<>();
        ArrayList<InternshipSkill> internshipSkills = new ArrayList<>(internshipRequirements.getSkills());

        for (InternshipSkill internshipSkill : internshipSkills) {
            skillNames.add(internshipSkill.getSkill().getSkillName());
        }
        String companyName = internship.getCompany().getCompanyName();
        return new InternshipInfoDTO(String.valueOf(internship.getId()), internship.getBenefits(), internship.getMaxStipend(), internship.getMinStipend(), internship.getField(), internship.getSector(), internship.getTotalCount(), skillNames, internshipRequirements.getMinExperience(), internshipRequirements.getMaxExperience(), companyName, internshipRequirements.getMinimumQualificationRank());
    }
}
