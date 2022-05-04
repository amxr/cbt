package com.fip.cbt.repository;

import com.fip.cbt.model.Candidate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface CandidateRepository extends MongoRepository<Candidate, String> {

    @Query("{username:'?0'}")
    Optional<Candidate> findByUsername(String username);

    //@Query("")
    //List<Candidate> findByRole(String rolename);
}
