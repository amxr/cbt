package com.fip.cbt.repository;

import com.fip.cbt.model.QuestionResponse;
import com.fip.cbt.model.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QuestionResponseRepository extends MongoRepository<QuestionResponse, String> {
}
