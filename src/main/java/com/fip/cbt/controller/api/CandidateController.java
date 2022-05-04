package com.fip.cbt.controller.api;

import com.fip.cbt.controller.request.CandidateRequest;
import com.fip.cbt.mapper.CandidateMapper;
import com.fip.cbt.mapper.modelassemblers.CandidateAssembler;
import com.fip.cbt.model.Candidate;
import com.fip.cbt.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
public class CandidateController {

    @Autowired
    private final CandidateService candidateService;

    @Autowired
    private final CandidateAssembler candidateAssembler;

    @PostMapping("/add")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addCandidate(@Valid @RequestBody CandidateRequest newCandidateRequest){
        //return candidateService.addCandidate(newCandidateRequest);
        EntityModel<Candidate> candidateEntityModel = candidateAssembler.toModel(
                candidateService.addCandidate(newCandidateRequest));

        return ResponseEntity.created(candidateEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(candidateEntityModel);
    }

    @GetMapping("/get/all")
    public ResponseEntity<CollectionModel<EntityModel<Candidate>>> getAllCandidates(){
        List<EntityModel<Candidate>> films = candidateService.getAllCandidates().stream()
                .map(candidateAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(films, linkTo(methodOn(CandidateController.class)
                .getAllCandidates()).withSelfRel()));
        //return candidateService.getAllCandidates();
    }

    @GetMapping("/get/{username}")
    public ResponseEntity<EntityModel<Candidate>> getOneCandidate(@PathVariable String username){
        //return candidateService.getCandidate(username);
        return ResponseEntity.ok(candidateAssembler.toModel(candidateService.getCandidate(username)));
    }

    @PutMapping("{username}")
    public Candidate updateCandidate(@RequestBody CandidateRequest updateCandidateRequest){
        return candidateService.updateCandidate(updateCandidateRequest);
    }

    @DeleteMapping("{username}")
    public ResponseEntity<?> deleteCandidate(@PathVariable String username){
        candidateService.deleteCandidate(username);
        return ResponseEntity.noContent().build();
    }
}
