package com.fip.cbt.repository;

import com.fip.cbt.model.QuestionResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionResponseRepository extends MongoRepository<QuestionResponse, String> {
}
