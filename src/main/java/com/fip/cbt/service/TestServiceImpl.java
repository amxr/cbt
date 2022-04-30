package com.fip.cbt.service;

import com.fip.cbt.controller.request.TestRequest;
import com.fip.cbt.controller.request.UpdateTestRequest;
import com.fip.cbt.exception.ResourceAlreadyExistsException;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.mapper.TestMapper;
import com.fip.cbt.model.Test;
import com.fip.cbt.repository.TestRepository;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TestServiceImpl implements TestService{
    @Autowired
    TestRepository testRepository;

    @Override
    public Test add(TestRequest testRequest) {
        if(testRepository.findTestByTestNumber(testRequest.getTestNumber()).isPresent())
            throw new ResourceAlreadyExistsException("Test with number " + testRequest.getTestNumber() + " already exists.");

        Test test = TestMapper.toTest(testRequest);
        test.setCreated(LocalDateTime.now());

        return testRepository.save(test);
    }

    @Override
    public Test getOne(String testNumber) {
        return testRepository.findTestByTestNumber(testNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Test with number " + testNumber + " not found.")
                );
    }

    @Override
    public void delete(String testNumber) {
        Test test = testRepository.findTestByTestNumber(testNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Test with number " + testNumber + " not found.")
                );
        testRepository.delete(test);
    }

    @Override
    public void update(UpdateTestRequest updateTestRequest) {
        testRepository.findById(updateTestRequest.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Test not found.")
                );
        Test test = TestMapper.toTest(updateTestRequest);
        testRepository.save(test);
    }
}
