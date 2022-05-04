package com.fip.cbt.service;

import com.fip.cbt.controller.request.CandidateRequest;
import com.fip.cbt.exception.ResourceAlreadyExistsException;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.mapper.CandidateMapper;
import com.fip.cbt.model.Candidate;
import com.fip.cbt.model.Role;
import com.fip.cbt.repository.CandidateRepository;
import com.fip.cbt.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateServiceImpl implements CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RoleRepository roleRepository;

    public Candidate addCandidate(CandidateRequest newCandidateRequest){
        Optional<Candidate> findCandidate = candidateRepository.findByUsername(newCandidateRequest.getUsername());
         if(findCandidate.isPresent()) {
            throw new ResourceAlreadyExistsException("Candidate with username " + newCandidateRequest.getUsername() + " exists");
        }
                //.orElseThrow( () ->
                //        new ResourceAlreadyExistsException("Candidate with username "+newCandidateRequest.getUsername()+" exists"
                //        ));
        Candidate newCandidate = CandidateMapper.toCandidate(newCandidateRequest);
        return candidateRepository.save(newCandidate);
        //return candidateRepository.save(CandidateMapper.toCandidate(newCandidateRequest));
        //TODO: verify fields (especially username uniqueness)
    }
    public Candidate getCandidate(String username){
        return candidateRepository.findByUsername(username)
                .orElseThrow( () ->
                        new ResourceNotFoundException("Could not find candidate with username" +username)
                );
    }
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    @Override
    public void deleteCandidate(String username) throws ResourceNotFoundException {
        Candidate findCandidate = candidateRepository.findByUsername(username)
                .orElseThrow( () -> (
                    new ResourceNotFoundException("Could not find candidate with username " +username)
                ));
        candidateRepository.delete(findCandidate);
    }

    @Override
    public Candidate updateCandidate(CandidateRequest updateCandidateRequest) throws ResourceNotFoundException {
        Candidate findCandidate = candidateRepository.findByUsername(updateCandidateRequest.getUsername())
                .orElseThrow(()-> (
                        new ResourceNotFoundException("Could not find candidate with username" +
                                updateCandidateRequest.getUsername())
                ));
        Candidate updatedCandidate = CandidateMapper.toCandidate(updateCandidateRequest);
        return candidateRepository.save(updatedCandidate);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Candidate addRoleToCandidate(String username, String roleName) {
        Candidate candidate = candidateRepository.findByUsername(username)
                .orElseThrow( () -> (
                        new ResourceNotFoundException("Could not find candidate with username" +username)
                        ));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow( () -> (
                        new ResourceNotFoundException("Could not find role " +roleName)
                ));
        candidate.getRoles().add(role);
        return candidateRepository.save(candidate);
    }
}
