package com.fip.cbt.mapper;

import com.fip.cbt.controller.request.CandidateRequest;
import com.fip.cbt.controller.request.TestRequest;
import com.fip.cbt.model.Candidate;
import com.fip.cbt.model.Test;

public class CandidateMapper {
    public static Candidate toCandidate(CandidateRequest candidateRequest){
        return new Candidate()
                .setFirstName(candidateRequest.getFirstName())
                .setLastName(candidateRequest.getLastName())
                .setUsername(candidateRequest.getUsername())
                .setPassword(candidateRequest.getPassword())
                .setRoles(candidateRequest.getRoles())
                .setTestsTaken(candidateRequest.getTestsTaken());
    }
}
