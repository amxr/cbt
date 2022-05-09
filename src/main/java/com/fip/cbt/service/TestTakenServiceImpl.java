package com.fip.cbt.service;

import com.fip.cbt.controller.request.TestTakenRequest;
import com.fip.cbt.controller.request.UpdateTestTakenRequest;
import com.fip.cbt.exception.ResourceAlreadyExistsException;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.mapper.TestTakenMapper;
import com.fip.cbt.model.Test;
import com.fip.cbt.model.TestTaken;
import com.fip.cbt.repository.QuestionResponseRepository;
import com.fip.cbt.repository.TestTakenRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class TestTakenServiceImpl implements TestTakenService{
    
    @Autowired
    private TestTakenRepository testTakenRepository;
    
    @Autowired
    private QuestionResponseRepository questionResponseRepository;
    
    @Override
    public TestTaken add(TestTakenRequest testTakenRequest) {
        //TODO: Search by ID
        if(testTakenRepository.findByTestId(testTakenRequest.getTestId()).isPresent())
            throw new ResourceAlreadyExistsException("Test with number " + testTakenRequest.getTestId()
                                                             + " has already been taken and saved.");
    
        TestTaken testTaken = TestTakenMapper.toTestTaken(testTakenRequest);
        testTakenRequest.setSubmissionDate(LocalDateTime.now());
    
        return testTakenRepository.save(testTaken);
    }
    
    @Override
    public TestTaken getOne(String testId) {
        return testTakenRepository.findByTestId(testId).orElseThrow(
                () -> new ResourceNotFoundException("Test with number " + testId + " not found.")
        );
    }
    
    @Override
    public void delete(String testId) {
        TestTaken testTaken = testTakenRepository.findByTestId(testId).orElseThrow(
                () -> new ResourceNotFoundException("Could not find test with id " +testId)
        );
        testTakenRepository.delete(testTaken);
    }
    
    @Override
    public TestTaken update(UpdateTestTakenRequest updateTestTakenRequest) {
        testTakenRepository.findById(updateTestTakenRequest.getId())
                      .orElseThrow(
                              () -> new ResourceNotFoundException("Could not find test with id "
                                                                          +updateTestTakenRequest.getId())
                      );
        TestTaken testTaken = TestTakenMapper.toTestTaken(updateTestTakenRequest);
        return testTakenRepository.save(testTaken);
    }
    
    @Override
    public List<TestTaken> getAll() {
        return testTakenRepository.findAll();
    }
}
