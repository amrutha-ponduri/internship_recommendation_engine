package com.example.reccomendation_system.repository;

import com.example.reccomendation_system.dto.ml_model_dtos.CompleteInfoDTO;
import com.example.reccomendation_system.dto.ml_model_dtos.ScoredInternshipDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface MlModelRepository {
    public HashMap<Integer, Double> getMLScores(int userId, List<Integer> eligibleInternshipIds);
    public ArrayList<ScoredInternshipDTO> getScoredInternships(CompleteInfoDTO completeInfo);
}
