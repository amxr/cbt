package com.fip.cbt.mapper.modelassemblers;

import com.fip.cbt.controller.api.CandidateController;
import com.fip.cbt.model.Candidate;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CandidateAssembler implements RepresentationModelAssembler<Candidate, EntityModel<Candidate>> {

    @Override
    public EntityModel<Candidate> toModel(Candidate candidate) {

        return EntityModel.of(candidate,
                linkTo(methodOn(CandidateController.class).getOneCandidate(candidate.getUsername())).withSelfRel());
    }
}
