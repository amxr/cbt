package com.fip.cbt.controller.api;

import com.fip.cbt.controller.request.CandidateRequest;
import com.fip.cbt.model.Candidate;
import com.fip.cbt.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
public class CandidateController {

    @Autowired
    private final CandidateService candidateService;
    

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Candidate addCandidate(@Valid @RequestBody CandidateRequest newCandidateRequest){
        return candidateService.addCandidate(newCandidateRequest);
    }

    @GetMapping("/get/all")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Candidate> getAllCandidates(){
        return candidateService.getAllCandidates();
    }

    @GetMapping("/get/{username}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Candidate getOneCandidate(@PathVariable String username){
        return candidateService.getCandidate(username);
    }

    @PutMapping("{username}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Candidate updateCandidate(@RequestBody CandidateRequest updateCandidateRequest){
        return candidateService.updateCandidate(updateCandidateRequest);
    }

    @DeleteMapping("{username}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteCandidate(@PathVariable String username){
        candidateService.deleteCandidate(username);
    }
}
