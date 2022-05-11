package com.fip.cbt.service;

import com.fip.cbt.controller.request.CandidateRequest;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.model.Candidate;
import com.fip.cbt.model.Role;

import java.util.List;

public interface CandidateService {
    Candidate addCandidate(CandidateRequest newCandidateRequest);
    Candidate getCandidate(String username);
    List<Candidate> getAllCandidates();
    void deleteCandidate(String username) throws ResourceNotFoundException;
    Candidate updateCandidate(CandidateRequest updateCandidate) throws ResourceNotFoundException;
    Role saveRole(Role role);
    Candidate addRoleToCandidate(String username, String roleName);
}
